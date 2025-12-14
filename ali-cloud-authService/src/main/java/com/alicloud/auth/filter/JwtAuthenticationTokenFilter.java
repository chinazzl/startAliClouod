package com.alicloud.auth.filter;

import com.alicloud.auth.config.LoginUser;
import com.alicloud.common.constant.RedisConstant;
import com.alicloud.common.exception.TokenExpiredException;
import com.alicloud.common.exception.UserException;
import com.alicloud.common.handler.TokenManager;
import com.alicloud.common.model.auth.LoginUserVO;
import com.alicloud.common.utils.RedisUtils;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: zhaolin
 * @Date: 2024/12/9
 **/
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisUtils<LoginUser> redisUtils;
    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {
        try {
            // 1. 从request的header中获取token，并判断是否存在
            String token = extractToken(httpServletRequest);
            // 2. 如果不存在，则直接放行，让后续其他拦截器进行校验
            if (StringUtils.isBlank(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            // 3. 如果存在，则进行解析token，然后从subject中获取id
            JWTInfo infoFromToken = validateToken(token);
            String id = infoFromToken.getId();
            // 4. 根据id，从redis中尝试获取LoginUser
            LoginUser loginUser = redisUtils.get(RedisConstant.REDIS_KEY_USER_LOGIN + id);
            if (Objects.isNull(loginUser)) {
                throw new UserException(HttpStatus.SC_UNAUTHORIZED, "用户未登录");
            }
            // 5. 验证会话版本
            if (infoFromToken.getSessionVersion() == null || !infoFromToken.getSessionVersion().equals(loginUser.getUserVo().getSessionVersion())) {
                throw new UserException(HttpStatus.SC_UNAUTHORIZED, "会话已过期，请重新登录");
            }
            // 6. 构建UserPassword Authentic
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            // 6. 将authentic放入SecurityContextHolder中，便于随时使用
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 7. 放行dofilter
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (TokenExpiredException e) {
            log.error("Token过期异常: {}", e.getMessage());
            handleException(httpServletResponse, HttpStatus.SC_UNAUTHORIZED, e.getMessage());
        } catch (UserException e) {
            log.error("用户认证异常: {}", e.getMessage());
            handleException(httpServletResponse, e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            log.error("其他运行时异常: {}", e.getMessage());
            handleException(httpServletResponse, HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统内部错误");
        }
    }

    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(com.alicloud.common.model.Result.error(status, message)));
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private JWTInfo validateToken(String token) {
        return tokenManager.parseToken(token);
    }
}
