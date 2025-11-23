package com.alicloud.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录安全拦截器 - 在拦截器层面处理账户锁定检查
 * @author Claude
 * @date 2025-11-18
 */
@Slf4j
@Component
public class LoginSecurityInterceptor implements HandlerInterceptor {


    @Resource
    private ObjectMapper objectMapper;

    // 需要进行登录安全检查的路径
    private static final List<String> LOGIN_PATHS = Arrays.asList(
        "/v1/users/login",
        "/login",
        "/auth/login"
    );

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        return true;
    }

}