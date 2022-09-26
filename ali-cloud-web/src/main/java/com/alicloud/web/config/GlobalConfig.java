package com.alicloud.web.config;

import com.alicloud.service.UserService;
import com.alicloud.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 16:13
 * @Description:
 */
@Configuration
public class GlobalConfig {

    @Bean("userService")
    public UserService userService() {
        return new UserServiceImpl();
    }
}
