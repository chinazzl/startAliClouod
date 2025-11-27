package com.alicloud.api.feign;

import com.alibaba.fastjson.JSONObject;
import com.alicloud.api.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: zhaolin
 * @Date: 2025/11/27
 * @Description:
 **/
@FeignClient(name = "github-client",url = "https://api.github.com",configuration = FeignConfig.class)
public interface GithubFeign {

    @RequestMapping(value = "/search/repositories",method = RequestMethod.GET)
    JSONObject getRepositories(@RequestParam("q") String q);
}
