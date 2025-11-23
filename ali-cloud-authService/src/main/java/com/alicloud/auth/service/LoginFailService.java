package com.alicloud.auth.service;

import com.alicloud.common.model.LoginFailRecord;

/**
 * 登录失败处理服务
 * @author Claude
 * @date 2025-11-18
 */
public interface LoginFailService {

    /**
     * 记录登录失败
     * @param userName 用户名
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 登录失败记录
     */
    LoginFailRecord recordLoginFail(String userName, String ipAddress, String userAgent);

    /**
     * 清除登录失败记录
     * @param userName 用户名
     */
    void clearLoginFail(String userName);

    /**
     * 检查账户是否被锁定
     * @param userName 用户名
     * @return 是否锁定
     */
    boolean isAccountLocked(String userName);

    /**
     * 获取登录失败记录
     * @param userName 用户名
     * @return 登录失败记录
     */
    LoginFailRecord getLoginFailRecord(String userName);

    /**
     * 解锁账户
     * @param userName 用户名
     */
    void unlockAccount(String userName);
}