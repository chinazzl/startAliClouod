package com.alicloud.auth.service;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.utils.jwt.JWTInfo;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
public interface AuthService {


    /**
     * 用户登录（新版本）
     */
    AuthResponse login(UserLoginDto userDto);

    /**
     * 验证Token
     */
    JWTInfo validateToken(String token);

    void logout();

}
