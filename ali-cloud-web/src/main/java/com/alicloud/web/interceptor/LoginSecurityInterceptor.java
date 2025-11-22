package com.alicloud.web.interceptor;

import com.alicloud.service.LoginFailService;
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
    private LoginFailService loginFailService;

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

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 只对POST登录请求进行检查
        if (isLoginRequest(requestURI, method)) {
            String username = extractUsername(request);

            if (username != null && loginFailService.isAccountLocked(username)) {
                log.warn("尝试登录被锁定的账户: {}, IP: {}",
                    username, getClientIP(request));

                // 返回账户锁定响应
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");

                Map<String, Object> result = new HashMap<>();
                result.put("code", 403);
                result.put("message", "账户已被锁定，请稍后再试");

                response.getWriter().write(objectMapper.writeValueAsString(result));
                return false;
            }
        }

        return true;
    }

    /**
     * 判断是否为登录请求
     */
    private boolean isLoginRequest(String requestURI, String method) {
        return "POST".equalsIgnoreCase(method) &&
               LOGIN_PATHS.stream().anyMatch(requestURI::contains);
    }

    /**
     * 从请求中提取用户名
     */
    private String extractUsername(HttpServletRequest request) {
        // 尝试从请求参数中获取
        String username = request.getParameter("username");
        if (username != null && !username.trim().isEmpty()) {
            return username.trim();
        }

        // 对于JSON请求，这里需要更复杂的处理
        // 实际项目中可能需要通过HttpServletRequestWrapper来读取请求体
        return null;
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}