package com.alicloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**********************************
 * @author zhang zhao lin
 * @date 2022年09月28日 21:54
 * @Description: service启动类
 **********************************/
@SpringBootApplication(scanBasePackages = "com.alicloud")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = "com.alicloud.mapper")
public class ALCloudService9001 {
    public static void main(String[] args) {
        SpringApplication.run(ALCloudService9001.class, args);
    }
}
