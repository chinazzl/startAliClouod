package com.alicloud.service.collect;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据采集服务接口
 */
public interface DataCollectService {

    /**
     * 通过HTTP接口采集数据
     * @param apiUrl API地址
     * @param headers 请求头
     * @param params 请求参数
     * @return 采集结果
     */
    CollectResult collectData(String apiUrl, Map<String, String> headers, Map<String, Object> params);

    /**
     * 批量采集数据
     * @param collectRequests 批量采集请求
     * @return 批量采集结果
     */
    List<CollectResult> batchCollectData(List<CollectRequest> collectRequests);

    /**
     * 采集数据并保存到数据库
     * @param collectRequest 采集请求
     * @return 采集结果
     */
    CollectResult collectAndSave(CollectRequest collectRequest);

    /**
     * 采集请求
     */
    @Data
    class CollectRequest {
        /**
         * 采集ID
         */
        private String collectId;

        /**
         * API地址
         */
        private String apiUrl;

        /**
         * 请求头
         */
        private Map<String, String> headers;

        /**
         * 请求参数
         */
        private Map<String, Object> params;

        /**
         * 请求方法 (GET/POST)
         */
        private String method = "GET";

        /**
         * 超时时间(毫秒)
         */
        private Integer timeout = 30000;

        /**
         * 重试次数
         */
        private Integer retryCount = 3;
    }

    /**
     * 采集结果
     */
    @Data
    class CollectResult {
        /**
         * 采集ID
         */
        private String collectId;

        /**
         * 是否成功
         */
        private Boolean success;

        /**
         * 响应数据
         */
        private String responseData;

        /**
         * 错误信息
         */
        private String errorMessage;

        /**
         * 耗时(毫秒)
         */
        private Long costTime;

        /**
         * HTTP状态码
         */
        private Integer httpStatus;

        /**
         * 采集时间
         */
        private Long collectTime;
    }
}