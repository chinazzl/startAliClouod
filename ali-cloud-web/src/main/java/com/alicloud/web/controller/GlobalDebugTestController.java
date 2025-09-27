package com.alicloud.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaolin
 * @Date: 2025/6/1
 * @Description:
 **/
@RestController
public class GlobalDebugTestController {

    @RequestMapping("/**")
    public ResponseEntity<Map<String, Object>> catchAll(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        info.put("actualPath", request.getRequestURI());
        info.put("method", request.getMethod());
        info.put("headers", Collections.list(request.getHeaderNames()));

        System.out.println("实际接收到的路径: " + request.getRequestURI());
        return ResponseEntity.ok(info);
    }
}
