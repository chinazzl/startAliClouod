package com.alicloud.service.job;

import com.alibaba.fastjson.JSON;
import com.alicloud.common.job.BaseXxlJob;
import com.alicloud.service.collect.DataCollectService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * HTTP数据采集定时任务
 * 支持分片模式，通过HTTP接口采集数据
 */
@Slf4j
@Component
public class HttpDataCollectJob extends BaseXxlJob {

    @Autowired
    private DataCollectService dataCollectService;

    @Override
    protected String getTaskName() {
        return "HTTP数据采集任务";
    }

    @Override
    protected String getJobName() {
        return "httpDataCollectJob";
    }

    /**
     * XXL-Job任务执行入口
     * @param param 任务参数
     */
    @XxlJob("httpDataCollectJob")
    public void httpDataCollectJob(String param) {
        execute(param);
    }

    @Override
    protected void doExecute(String param) throws Exception {
        log.info("开始执行HTTP数据采集任务，参数: {}", param);

        // 解析任务参数
        CollectTaskConfig config = parseTaskParam(param);
        if (config == null) {
            throw new IllegalArgumentException("任务参数解析失败");
        }

        // 获取当前分片要处理的数据列表
        List<String> dataIds = getDataIdsForCurrentShard(config);
        if (dataIds.isEmpty()) {
            log.info("当前分片无数据需要处理");
            return;
        }

        log.info("当前分片({}/{})需要处理的数据量: {}",
            XxlJobHelper.getShardIndex(),
            XxlJobHelper.getShardTotal(),
            dataIds.size());

        // 构建采集请求列表
        List<DataCollectService.CollectRequest> collectRequests = buildCollectRequests(config, dataIds);

        // 批量采集数据
        List<DataCollectService.CollectResult> results = dataCollectService.batchCollectData(collectRequests);

        // 统计采集结果
        int successCount = 0;
        int failedCount = 0;

        for (DataCollectService.CollectResult result : results) {
            if (result.getSuccess()) {
                successCount++;
                // 处理采集成功的数据
                processCollectResult(config, result);
            } else {
                failedCount++;
                log.error("数据采集失败: {}, 错误: {}", result.getCollectId(), result.getErrorMessage());
            }
        }

        // 记录采集统计
        recordCollectStats(results.size(), successCount, failedCount);

        // 如果有失败的采集，记录到任务日志
        if (failedCount > 0) {
            String errorMsg = String.format("本次采集失败 %d 条数据", failedCount);
            XxlJobHelper.log(errorMsg);
        }

        log.info("HTTP数据采集任务完成，总计: {}, 成功: {}, 失败: {}",
            results.size(), successCount, failedCount);
    }

    /**
     * 解析任务参数
     * @param param JSON格式的任务参数
     * @return 解析后的任务配置
     */
    private CollectTaskConfig parseTaskParam(String param) {
        if (!StringUtils.hasText(param)) {
            log.error("任务参数为空");
            return null;
        }

        try {
            return JSON.parseObject(param, CollectTaskConfig.class);
        } catch (Exception e) {
            log.error("解析任务参数失败: {}, 参数: {}", e.getMessage(), param, e);
            return null;
        }
    }

    /**
     * 获取当前分片需要处理的数据ID列表
     * @param config 任务配置
     * @return 当前分片的数据ID列表
     */
    private List<String> getDataIdsForCurrentShard(CollectTaskConfig config) {
        // 这里需要根据实际业务实现数据ID获取逻辑
        // 示例：从数据库查询需要采集的数据ID
        List<String> allDataIds = getAllDataIdsFromDatabase(config);

        // 根据分片信息筛选当前分片要处理的数据
        List<String> currentShardDataIds = new ArrayList<>();

        for (String dataId : allDataIds) {
            if (belongsToCurrentShard(dataId)) {
                currentShardDataIds.add(dataId);
            }
        }

        return currentShardDataIds;
    }

    /**
     * 从数据库获取所有需要采集的数据ID
     * @param config 任务配置
     * @return 数据ID列表
     */
    private List<String> getAllDataIdsFromDatabase(CollectTaskConfig config) {
        // TODO: 实现具体的数据库查询逻辑
        // 示例代码，需要根据实际业务调整

        List<String> dataIds = new ArrayList<>();

        // 示例：根据配置条件查询数据库
        // String sql = "SELECT id FROM target_table WHERE collect_status = 0 AND last_update_time < ?";
        // List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, new Date());
        // for (Map<String, Object> row : results) {
        //     dataIds.add(row.get("id").toString());
        // }

        // 为了演示，这里返回一些模拟数据
        for (int i = 1; i <= 100; i++) {
            dataIds.add("DATA_" + String.format("%06d", i));
        }

        return dataIds;
    }

    /**
     * 构建采集请求列表
     * @param config 任务配置
     * @param dataIds 数据ID列表
     * @return 采集请求列表
     */
    private List<DataCollectService.CollectRequest> buildCollectRequests(CollectTaskConfig config, List<String> dataIds) {
        List<DataCollectService.CollectRequest> requests = new ArrayList<>();

        for (String dataId : dataIds) {
            DataCollectService.CollectRequest request = new DataCollectService.CollectRequest();
            request.setCollectId(dataId);

            // 构建API URL，支持参数替换
            String apiUrl = config.getApiUrl();
            if (apiUrl.contains("{dataId}")) {
                apiUrl = apiUrl.replace("{dataId}", dataId);
            }
            request.setApiUrl(apiUrl);

            // 设置请求头
            request.setHeaders(config.getHeaders());

            // 设置请求参数
            Map<String, Object> params = new HashMap<>();
            if (config.getParams() != null) {
                params.putAll(config.getParams());
            }
            params.put("dataId", dataId);
            request.setParams(params);

            // 设置其他配置
            request.setMethod(config.getMethod());
            request.setTimeout(config.getTimeout());
            request.setRetryCount(config.getRetryCount());

            requests.add(request);
        }

        return requests;
    }

    /**
     * 处理采集成功的结果
     * @param config 任务配置
     * @param result 采集结果
     */
    private void processCollectResult(CollectTaskConfig config, DataCollectService.CollectResult result) {
        try {
            log.debug("处理采集结果: {}", result.getCollectId());

            // TODO: 实现具体的数据处理逻辑
            // 1. 解析响应数据
            // 2. 数据转换和校验
            // 3. 保存到目标数据库
            // 4. 更新数据状态

            // 示例：更新数据库中的数据采集状态
            // String updateSql = "UPDATE target_table SET collect_status = 1, collect_time = ?, response_data = ? WHERE id = ?";
            // jdbcTemplate.update(updateSql, new Date(), result.getResponseData(), result.getCollectId());

        } catch (Exception e) {
            log.error("处理采集结果失败: {}, 异常: {}", result.getCollectId(), e.getMessage(), e);
            throw new RuntimeException("处理采集结果失败", e);
        }
    }

    /**
     * 采集任务配置
     */
    public static class CollectTaskConfig {
        /**
         * API地址模板，支持{dataId}占位符
         */
        private String apiUrl;

        /**
         * 请求方法
         */
        private String method = "GET";

        /**
         * 请求头
         */
        private Map<String, String> headers;

        /**
         * 请求参数
         */
        private Map<String, Object> params;

        /**
         * 超时时间(毫秒)
         */
        private Integer timeout = 30000;

        /**
         * 重试次数
         */
        private Integer retryCount = 3;

        /**
         * 批处理大小
         */
        private Integer batchSize = 50;

        // getters and setters
        public String getApiUrl() { return apiUrl; }
        public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public Integer getTimeout() { return timeout; }
        public void setTimeout(Integer timeout) { this.timeout = timeout; }
        public Integer getRetryCount() { return retryCount; }
        public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
        public Integer getBatchSize() { return batchSize; }
        public void setBatchSize(Integer batchSize) { this.batchSize = batchSize; }
    }
}