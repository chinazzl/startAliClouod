package com.alicloud.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录安全配置
 * @author Claude
 * @date 2025-11-18
 */
@Data
@Component
@ConfigurationProperties(prefix = "login.security")
public class LoginSecurityConfig {

    /**
     * 最大登录失败次数
     */
    private int maxFailCount = 5;

    /**
     * 账户锁定时间（分钟）
     */
    private long lockDurationMinutes = 30;

    /**
     * 登录失败记录过期时间（小时）
     */
    private long recordExpireHours = 24;

    /**
     * 是否启用登录失败限制
     */
    private boolean enableLoginFailLimit = true;
}