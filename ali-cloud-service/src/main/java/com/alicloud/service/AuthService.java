package com.alicloud.service;

import com.alicloud.common.model.RegisterResponse;
import com.alicloud.common.model.dto.UserLoginDto;
import com.alicloud.common.model.dto.UserRegisterDto;
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
     * 获取刷新token
     * @param refreshToken
     * @return
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * 验证Token
     */
    JWTInfo validateToken(String token);

    /**
     * 注册
     * @param userRegisterDto
     * @return
     */
    RegisterResponse register(UserRegisterDto userRegisterDto);

    void logout(String token);

}
