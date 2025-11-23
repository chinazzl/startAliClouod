package com.alicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSecurity
public class AliCloudAuthServiceRunner {
    public static void main(String[] args) {
        SpringApplication.run(AliCloudAuthServiceRunner.class, args);
    }
}
