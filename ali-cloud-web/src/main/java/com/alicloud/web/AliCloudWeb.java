package com.alicloud.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 12:33
 * @Description:
 */
@SpringBootApplication(scanBasePackages = "com.alicloud")
public class AliCloudWeb {
    public static void main(String[] args) {
        SpringApplication.run(AliCloudWeb.class, args);
    }
}

