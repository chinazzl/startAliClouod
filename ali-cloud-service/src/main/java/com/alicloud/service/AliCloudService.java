package com.alicloud.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2023/4/17 15:51
 * @Description:
 */
@SpringBootApplication(scanBasePackages = "com.alicloud")
@EnableDiscoveryClient
@MapperScan("com.alicloud.mapper")
public class AliCloudService {
    public static void main(String[] args) {
        SpringApplication.run(AliCloudService.class, args);
    }
}
