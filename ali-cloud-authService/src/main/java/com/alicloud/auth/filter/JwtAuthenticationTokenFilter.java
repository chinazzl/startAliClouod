package com.alicloud.auth.filter;

import com.alicloud.auth.config.LoginUser;
import com.alicloud.common.constant.RedisConstant;
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

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {
        // 1. 从request的header中获取token，并判断是否存在
        String token = extractToken(httpServletRequest);
        // 2. 如果不存在，则直接放行，让后续其他拦截器进行校验
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 3. 如果存在，则进行解析token，然后从subject中获取id
        String id;
        JWTInfo infoFromToken = validateToken(token);
        id = infoFromToken.getId();
        // 4. 根据id，从redis中尝试获取LoginUser
        LoginUser loginUser = redisUtils.get(RedisConstant.REDIS_KEY_USER_LOGIN + id);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        // 5. 构建UserPassword Authentic
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // 6. 将authentic放入SecurityContextHolder中，便于随时使用
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 7. 放行dofilter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private JWTInfo validateToken(String token) {
        try {
            return jwtTokenUtil.getInfoFromToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Token格式错误或已损坏",e);
        }
    }
}
