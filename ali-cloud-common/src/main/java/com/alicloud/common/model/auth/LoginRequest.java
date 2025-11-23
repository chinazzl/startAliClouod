package com.alicloud.common.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String loginType;

    private String captcha;

    private String captchaKey;

    private String clientIp;

    private String userAgent;
}
