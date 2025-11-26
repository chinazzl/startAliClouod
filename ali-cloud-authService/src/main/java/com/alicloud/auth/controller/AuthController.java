package com.alicloud.auth.controller;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.api.bean.dto.UserRegisterDto;
import com.alicloud.auth.service.AuthService;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.utils.jwt.JWTInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResponse<AuthResponse> login(@RequestBody UserLoginDto userLoginDto) {
        AuthResponse authResponse = authService.login(userLoginDto);
        return CommonResponse.<AuthResponse>builder().data(authResponse).build();
    }

    /**
     * 注册
     * @param userRegisterDto
     * @param request
     * @return
     */
    @PostMapping("/register")
    public CommonResponse<AuthResponse> register(@RequestBody @Valid UserRegisterDto userRegisterDto, HttpServletRequest request) {

        return null;
    }


    /**
     * 验证Token
     */
    @PostMapping("/validateToken")
    public CommonResponse<JWTInfo> validateToken(String token) {
        JWTInfo jwtInfo = authService.validateToken(token);
        return CommonResponse.<JWTInfo>builder().data(jwtInfo).build();
    }

}
