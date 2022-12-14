package com.alicloud.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 12:33
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = "com.alicloud")
public class ALCloudWeb9000 {
    public static void main(String[] args) {
        SpringApplication.run(ALCloudWeb9000.class, args);
    }
}

