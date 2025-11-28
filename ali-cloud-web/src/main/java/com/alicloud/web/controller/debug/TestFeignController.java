package com.alicloud.web.controller.debug;

import com.alibaba.fastjson.JSONObject;
import com.alicloud.api.feign.web.GithubFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhaolin
 * @Date: 2025/11/27
 * @Description:
 **/
@RestController
@RequestMapping("/debug/feign")
public class TestFeignController {

    @Resource
    private GithubFeign githubFeign;

    @RequestMapping(value = "/github/getRepositories",method = RequestMethod.GET)
    public JSONObject getRepositories(@RequestParam("q") String q) {
        return githubFeign.getRepositories(q);
    }
}
