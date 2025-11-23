package com.alicloud.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 12:33
 * @Description:
 */
@SpringBootApplication(scanBasePackages = "com.alicloud")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.alicloud.api.feign")
@MapperScan(basePackages = {"com.alicloud.dao.mapper"})
public class AliCloudWeb {
    public static void main(String[] args) {
        SpringApplication.run(AliCloudWeb.class, args);
    }
}

