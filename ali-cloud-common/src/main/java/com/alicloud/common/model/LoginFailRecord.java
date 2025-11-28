package com.alicloud.common.model;

import lombok.Data;

import java.util.Date;

/**
 * 登录失败记录DTO
 * @author Claude
 * @date 2025-11-18
 */
@Data
public class LoginFailRecord {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 失败次数
     */
    private Integer failCount;

    /**
     * 最后失败时间
     */
    private Date lastFailTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 是否锁定
     */
    private Boolean isLocked;

    /**
     * 锁定时间
     */
    private Date lockTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;
}