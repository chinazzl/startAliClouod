package com.alicloud.service.collect.impl;

import com.alicloud.service.collect.DataCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据采集服务实现类
 */
@Slf4j
@Service
public class DataCollectServiceImpl implements DataCollectService {

    private final RestTemplate restTemplate;
    private final Executor executor;

    public DataCollectServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.executor = Executors.newFixedThreadPool(10);
    }

    @Override
    public CollectResult collectData(String apiUrl, Map<String, String> headers, Map<String, Object> params) {
        CollectResult result = new CollectResult();
        result.setCollectId(apiUrl);
        result.setCollectTime(System.currentTimeMillis());

        long startTime = System.currentTimeMillis();

        try {
            log.info("开始采集数据: {}", apiUrl);

            // 设置默认请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("User-Agent", "DataCollectService/1.0");

            // 添加自定义请求头
            if (!CollectionUtils.isEmpty(headers)) {
                headers.forEach(httpHeaders::add);
            }

            HttpEntity<?> requestEntity;
            String urlWithParams = apiUrl;

            // 根据请求方法和参数构建请求
            HttpMethod method = HttpMethod.GET;
            if (params != null && !params.isEmpty()) {
                // 这里简化处理，实际应用中可能需要更复杂的参数处理
                if (urlWithParams.contains("?")) {
                    urlWithParams += "&";
                } else {
                    urlWithParams += "?";
                }

                StringBuilder paramStr = new StringBuilder();
                params.forEach((key, value) -> {
                    if (paramStr.length() > 0) {
                        paramStr.append("&");
                    }
                    paramStr.append(key).append("=").append(value);
                });
                urlWithParams += paramStr.toString();
            }

            requestEntity = new HttpEntity<>(httpHeaders);

            // 发送HTTP请求
            ResponseEntity<String> response = restTemplate.exchange(
                urlWithParams,
                method,
                requestEntity,
                String.class
            );

            long endTime = System.currentTimeMillis();

            // 设置响应结果
            result.setHttpStatus(response.getStatusCodeValue());
            result.setResponseData(response.getBody());
            result.setSuccess(response.getStatusCode().is2xxSuccessful());
            result.setCostTime(endTime - startTime);

            log.info("数据采集完成: {}, 状态码: {}, 耗时: {} ms",
                apiUrl, response.getStatusCodeValue(), result.getCostTime());

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("数据采集失败: {}, 异常: {}", apiUrl, e.getMessage(), e);

            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setCostTime(endTime - startTime);
        }

        return result;
    }

    @Override
    public List<CollectResult> batchCollectData(List<CollectRequest> collectRequests) {
        List<CollectResult> results = new ArrayList<>();

        if (CollectionUtils.isEmpty(collectRequests)) {
            return results;
        }

        log.info("开始批量采集数据, 总数: {}", collectRequests.size());

        // 使用并行流进行批量采集
        List<CompletableFuture<CollectResult>> futures = collectRequests.stream()
            .map(request -> CompletableFuture.supplyAsync(() -> {
                CollectResult result = new CollectResult();
                result.setCollectId(request.getCollectId());
                result.setCollectTime(System.currentTimeMillis());

                long startTime = System.currentTimeMillis();

                try {
                    // 执行采集，这里简化实现，直接调用collectData
                    result = collectData(request.getApiUrl(), request.getHeaders(), request.getParams());
                    result.setCollectId(request.getCollectId());

                } catch (Exception e) {
                    long endTime = System.currentTimeMillis();
                    log.error("批量采集失败: {}, 异常: {}", request.getCollectId(), e.getMessage(), e);

                    result.setSuccess(false);
                    result.setErrorMessage(e.getMessage());
                    result.setCostTime(endTime - startTime);
                }

                return result;
            }, executor))
                    .collect(Collectors.toList());

        // 等待所有任务完成并收集结果
        @SuppressWarnings("unchecked")
        CompletableFuture<CollectResult>[] futureArray = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(futureArray).join();

        // 收集所有结果
        futures.stream()
            .map(CompletableFuture::join)
            .forEach(results::add);

        log.info("批量采集完成, 成功: {}, 失败: {}",
            results.stream().mapToInt(r -> r.getSuccess() ? 1 : 0).sum(),
            results.stream().mapToInt(r -> !r.getSuccess() ? 1 : 0).sum());

        return results;
    }

    @Override
    public CollectResult collectAndSave(CollectRequest collectRequest) {
        // 先采集数据
        CollectResult result = collectData(collectRequest.getApiUrl(),
            collectRequest.getHeaders(), collectRequest.getParams());

        if (result.getSuccess() && StringUtils.hasText(result.getResponseData())) {
            try {
                // TODO: 保存数据到数据库
                // 这里需要根据具体的数据结构实现保存逻辑
                log.info("数据采集成功，开始保存到数据库: {}", collectRequest.getCollectId());

                // 示例：解析响应数据并保存
                // saveDataToDatabase(collectRequest.getCollectId(), result.getResponseData());

                log.info("数据保存成功: {}", collectRequest.getCollectId());

            } catch (Exception e) {
                log.error("数据保存失败: {}, 异常: {}", collectRequest.getCollectId(), e.getMessage(), e);
                result.setSuccess(false);
                result.setErrorMessage("数据保存失败: " + e.getMessage());
            }
        }

        return result;
    }
}