package com.alicloud.filter;

import com.alicloud.config.security.LoginUser;
import com.alicloud.constant.RedisConstant;
import com.alicloud.utils.RedisUtils;
import com.alicloud.utils.jwt.JWTInfo;
import com.alicloud.utils.jwt.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
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
    RedisUtils<LoginUser> redisUtils;


    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest httpServletRequest, jakarta.servlet.http.HttpServletResponse httpServletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {
        // 1. 从request的header中获取token，并判断是否存在
        String token = httpServletRequest.getHeader("token");
        // 2. 如果不存在，则直接放行，让后续其他拦截器进行校验
        if (StringUtils.isAllBlank(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 3. 如果存在，则进行解析token，然后从subject中获取id
        String id;
        try {
            JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
            id = infoFromToken.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
