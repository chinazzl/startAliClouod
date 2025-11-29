package com.alicloud.common.model;

import lombok.Data;

/**
 * @author: zhaolin
 * @Date: 2025/11/26
 * @Description: 注册返回VO
 **/
@Data
public class RegisterResponse {

    /**
     * 注册是否成功
     */
    private boolean success;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 注册时间
     */
    private String registerTime;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 是否需要邮箱验证
     */
    private boolean needEmailVerification;

    /**
     * 验证邮箱
     */
    private String verificationEmail;
}
