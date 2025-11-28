package com.alicloud.api.feign.web;

import com.alibaba.fastjson.JSONObject;
import com.alicloud.common.config.feign.OpenFeignOkHttpConfiguration;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: zhaolin
 * @Date: 2025/11/27
 * @Description:
 **/
@FeignClient(
    name = "github-client",
    url = "https://api.github.com",
    configuration = OpenFeignOkHttpConfiguration.class
)
public interface GithubFeign {

    @RequestLine("GET /search/repositories?q={q}")
    JSONObject getRepositories(@Param("q") String q);
}
