package com.alicloud.service.job;

import com.alibaba.fastjson.JSON;
import com.alicloud.common.enums.DataStatus;
import com.alicloud.common.job.BaseXxlJob;
import com.alicloud.service.collect.DataExpireService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 数据过期处理定时任务
 * 负责标记过期数据并清理待删除数据
 */
@Slf4j
@Component
public class DataExpireJob extends BaseXxlJob {

    @Autowired
    private DataExpireService dataExpireService;

    @Override
    protected String getTaskName() {
        return "数据过期处理任务";
    }

    @Override
    protected String getJobName() {
        return "dataExpireJob";
    }

    /**
     * XXL-Job任务执行入口
     * @param param 任务参数
     */
    @XxlJob("dataExpireJob")
    public void dataExpireJob(String param) {
        execute(param);
    }

    @Override
    protected void doExecute(String param) throws Exception {
        log.info("开始执行数据过期处理任务，参数: {}", param);

        // 解析任务参数
        ExpireTaskConfig config = parseTaskParam(param);
        if (config == null) {
            throw new IllegalArgumentException("任务参数解析失败");
        }

        // 获取过期数据统计
        DataExpireService.ExpiredDataStatistics statistics = dataExpireService.getExpiredDataStatistics();
        log.info("当前数据状态统计 - 正常: {}, 已过期: {}, 待删除: {}",
            statistics.getNormalCount(), statistics.getExpiredCount(), statistics.getToDeleteCount());

        // 第一步：标记过期数据
        if (config.getMarkExpiredEnabled()) {
            markExpiredData(config);
        }

        // 第二步：将部分过期数据标记为待删除
        if (config.getMarkToDeleteEnabled()) {
            markDataToDelete(config);
        }

        // 第三步：删除待删除数据
        if (config.getDeleteEnabled()) {
            deleteExpiredData(config);
        }

        // 输出最终统计
        DataExpireService.ExpiredDataStatistics finalStatistics = dataExpireService.getExpiredDataStatistics();
        log.info("最终数据状态统计 - 正常: {}, 已过期: {}, 待删除: {}",
            finalStatistics.getNormalCount(), finalStatistics.getExpiredCount(), finalStatistics.getToDeleteCount());

        log.info("数据过期处理任务完成");
    }

    /**
     * 解析任务参数
     * @param param JSON格式的任务参数
     * @return 解析后的任务配置
     */
    private ExpireTaskConfig parseTaskParam(String param) {
        if (!StringUtils.hasText(param)) {
            log.warn("任务参数为空，使用默认配置");
            return createDefaultConfig();
        }

        try {
            ExpireTaskConfig config = JSON.parseObject(param, ExpireTaskConfig.class);
            // 设置默认值
            if (config.getExpireThreshold() == null) {
                config.setExpireThreshold(30); // 默认30天过期
            }
            if (config.getBatchSize() == null) {
                config.setBatchSize(1000); // 默认批次大小
            }
            return config;
        } catch (Exception e) {
            log.error("解析任务参数失败: {}, 参数: {}, 使用默认配置", e.getMessage(), param, e);
            return createDefaultConfig();
        }
    }

    /**
     * 创建默认配置
     * @return 默认任务配置
     */
    private ExpireTaskConfig createDefaultConfig() {
        ExpireTaskConfig config = new ExpireTaskConfig();
        config.setExpireThreshold(30); // 30天过期
        config.setBatchSize(1000);     // 批次大小1000
        config.setMarkExpiredEnabled(true);  // 启用标记过期
        config.setMarkToDeleteEnabled(true); // 启用标记待删除
        config.setDeleteEnabled(false);      // 默认不启用物理删除
        config.setMarkToDeleteRatio(0.1);    // 10%的过期数据标记为待删除
        return config;
    }

    /**
     * 标记过期数据
     * @param config 任务配置
     */
    private void markExpiredData(ExpireTaskConfig config) {
        log.info("开始标记过期数据，过期阈值: {} 天", config.getExpireThreshold());

        try {
            int markedCount = dataExpireService.markExpiredData(config.getExpireThreshold());
            log.info("标记过期数据完成，数量: {}", markedCount);

            if (markedCount > 0) {
                XxlJobHelper.log(String.format("标记了 %d 条过期数据", markedCount));
            }

        } catch (Exception e) {
            log.error("标记过期数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("标记过期数据失败", e);
        }
    }

    /**
     * 标记数据为待删除状态
     * @param config 任务配置
     */
    private void markDataToDelete(ExpireTaskConfig config) {
        log.info("开始标记数据为待删除状态，比例: {}", config.getMarkToDeleteRatio());

        try {
            // 这里简化实现，实际应用中可能需要更复杂的逻辑
            // 例如：根据数据重要性、业务规则等决定哪些数据可以删除

            // 获取一部分过期数据标记为待删除
            String sql = String.format(
                "SELECT id FROM target_table " +
                "WHERE data_status = %d " +
                "ORDER BY last_update_time ASC " +
                "LIMIT %d",
                DataStatus.EXPIRED.getCode(),
                config.getBatchSize()
            );

            // 注意：这里需要根据实际使用的ORM框架调整
            // 示例使用JdbcTemplate，如果使用MyBatis Plus需要相应调整

            log.info("标记 {} 条数据为待删除状态", config.getBatchSize());

            // 实际实现需要查询数据并更新状态
            // List<String> dataIds = jdbcTemplate.queryForList(sql, String.class);
            // if (!dataIds.isEmpty()) {
            //     int updatedCount = dataExpireService.updateDataStatus(dataIds, DataStatus.EXPIRED, DataStatus.TO_DELETE);
            //     log.info("标记数据为待删除状态完成，数量: {}", updatedCount);
            // }

        } catch (Exception e) {
            log.error("标记数据为待删除状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("标记数据为待删除状态失败", e);
        }
    }

    /**
     * 删除过期数据
     * @param config 任务配置
     */
    private void deleteExpiredData(ExpireTaskConfig config) {
        log.info("开始删除过期数据，批次大小: {}", config.getBatchSize());

        try {
            int deletedCount = dataExpireService.deleteExpiredData(config.getBatchSize());
            log.info("删除过期数据完成，数量: {}", deletedCount);

            if (deletedCount > 0) {
                XxlJobHelper.log(String.format("删除了 %d 条过期数据", deletedCount));
            }

        } catch (Exception e) {
            log.error("删除过期数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除过期数据失败", e);
        }
    }

    /**
     * 过期任务配置
     */
    public static class ExpireTaskConfig {
        /**
         * 过期阈值（天数）
         */
        private Integer expireThreshold;

        /**
         * 批次大小
         */
        private Integer batchSize;

        /**
         * 是否启用标记过期数据
         */
        private Boolean markExpiredEnabled = true;

        /**
         * 是否启用标记为待删除
         */
        private Boolean markToDeleteEnabled = true;

        /**
         * 是否启用物理删除
         */
        private Boolean deleteEnabled = false;

        /**
         * 标记为待删除的比例（0-1）
         */
        private Double markToDeleteRatio = 0.1;

        // getters and setters
        public Integer getExpireThreshold() { return expireThreshold; }
        public void setExpireThreshold(Integer expireThreshold) { this.expireThreshold = expireThreshold; }
        public Integer getBatchSize() { return batchSize; }
        public void setBatchSize(Integer batchSize) { this.batchSize = batchSize; }
        public Boolean getMarkExpiredEnabled() { return markExpiredEnabled; }
        public void setMarkExpiredEnabled(Boolean markExpiredEnabled) { this.markExpiredEnabled = markExpiredEnabled; }
        public Boolean getMarkToDeleteEnabled() { return markToDeleteEnabled; }
        public void setMarkToDeleteEnabled(Boolean markToDeleteEnabled) { this.markToDeleteEnabled = markToDeleteEnabled; }
        public Boolean getDeleteEnabled() { return deleteEnabled; }
        public void setDeleteEnabled(Boolean deleteEnabled) { this.deleteEnabled = deleteEnabled; }
        public Double getMarkToDeleteRatio() { return markToDeleteRatio; }
        public void setMarkToDeleteRatio(Double markToDeleteRatio) { this.markToDeleteRatio = markToDeleteRatio; }
    }
}