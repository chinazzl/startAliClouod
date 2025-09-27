package com.alicloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: zhaolin
 * @Date: 2025/5/18
 * @Description:
 **/
@Component
public class GateWayConfigration {

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/jd")
//                        .uri("http://jd.com:80/")
//                        .id("jd_route"))
//                .build();
//    }


    @Bean
    public RouteLocator afterRouteLocator(RouteLocatorBuilder builder) {
        String format = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(format);
        ZonedDateTime zonedDateTime = LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault());
        return builder.routes()
                .route("after-route", r -> r.path("/testAfterRoute").and().after(zonedDateTime)
                        .uri("http://www.baidu.com"))
                .build();

    }

    @Bean
    public RouteLocator requestHandlerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("add_request_header", r -> r.path("/testRequestHeader")
                        .filters(f -> f.addRequestHeader("X-Request_Acme", "ValueB")
                                .stripPrefix(1)
                                .prefixPath("/test/gateway/addRequestHeader"))
                        .uri("http://localhost:8010"))
                .build();

    }


}
