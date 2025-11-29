package com.alicloud.web.filter;

import com.alicloud.common.model.UserVo;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import com.alicloud.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录安全过滤器 - 在过滤器层面处理账户锁定检查
 *
 * @author Claude
 * @date 2025-11-18
 */
@Slf4j
@Component
public class LoginSecurityFilter extends OncePerRequestFilter {

    @Resource
    AuthService authService;

    @Resource
    JwtTokenUtil jwtTokenUtil;

    // 需要进行登录安全检查的路径
    private static final List<String> LOGIN_PATHS = Arrays.asList(
            "/v1/users/login",
            "/login",
            "/auth/login",
            "/register",
            "/v1/users/register"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (isSkipRequest(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 只对POST登录请求进行检查
        if (isLoginRequest(requestURI, method)) {
            String token = extractUsername(request);

            if (StringUtils.isEmpty(token) && !isValidateToken(token)) {
                // 返回账户锁定响应
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        "{\"code\":403,\"message\":\"Token无效或已过期\"}"
                );
                return;
            }
            setUserInfoRequest(request, token);
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    private void setUserInfoRequest(HttpServletRequest request, String token) {
        // TODO 修改为从authService中获取userInfo
        try {
            JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
            UserVo user = new UserVo();
            user.setId(Long.valueOf(infoFromToken.getId()));
            user.setUserName(infoFromToken.getUsername());
            user.setPassword(infoFromToken.getPassword());
            request.setAttribute("currentUser", user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否为登录请求
     */
    private boolean isLoginRequest(String requestURI, String method) {
        return "POST".equalsIgnoreCase(method);
    }

    private boolean isSkipRequest(String requestURI) {
        return LOGIN_PATHS.stream().anyMatch(requestURI::contains);
    }

    private boolean isValidateToken(String token) {
        try {
            JWTInfo jwtInfo = authService.validateToken(token);
            return jwtInfo != null;
        } catch (Exception e) {
            log.error("校验token失败,token=> {}", token, e);
            throw new RuntimeException("校验token失败", e);
        }
    }

    /**
     * 从请求中提取token
     */
    private String extractUsername(HttpServletRequest request) {
        // 尝试从请求参数中获取
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
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