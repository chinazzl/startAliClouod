package com.alicloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhaolin
 * @Date: 2025/5/31
 * @Description:
 **/
@RestController
public class GatewayDebug {

    @Autowired
    private RouteLocator routeLocator;

    @GetMapping("/gateway/routes")
    public Mono<List<Map<String, Object>>> getAllRoutes() {
        return routeLocator.getRoutes()
                .map(route -> {
                    Map<String, Object> routeInfo = new HashMap<>();
                    routeInfo.put("id", route.getId());
                    routeInfo.put("uri", route.getUri().toString());
                    routeInfo.put("predicates", route.getPredicate().toString());
                    routeInfo.put("filters", route.getFilters().toString());
                    return routeInfo;
                })
                .collectList();
    }

}
