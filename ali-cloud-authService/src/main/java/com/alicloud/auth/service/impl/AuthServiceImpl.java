package com.alicloud.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.auth.config.LoginUser;
import com.alicloud.auth.service.AuthService;
import com.alicloud.auth.service.LoginFailService;
import com.alicloud.common.constant.RedisConstant;
import com.alicloud.common.exception.AccountLockedException;
import com.alicloud.common.handler.TokenManager;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.UserVo;
import com.alicloud.common.model.auth.LoginUserVO;
import com.alicloud.common.utils.RedisUtils;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import com.alicloud.dao.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Component
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private LoginFailService loginFailService;
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisUtils<LoginUser> redisUtils;
    @Resource
    private TokenManager tokenManager;


    @Override
    public AuthResponse login(UserLoginDto userDto) {
        AuthResponse authResponse = new AuthResponse();
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        // 1. 检查账户是否被锁定
        if (loginFailService.isAccountLocked(username)) {
            throw new AccountLockedException("账户已被锁定，请稍后再试");
        }

        try {
            // 2. 将前端传入的参数进行验证权限，获取权限
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            // 如果没有获得权限抛出异常
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("登陆异常，无法找到用户");
            }

            // 如果存在权限，则生成jwt 存入redis中 key token。value userId
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            UserVo user = loginUser.getUserVo();

            // 3. 登录成功，清除登录失败记录
            loginFailService.clearLoginFail(username);

            JWTInfo jwtInfo = new JWTInfo();
            jwtInfo.setId(String.valueOf(user.getId()));
            jwtInfo.setUsername(user.getUserName());
            jwtInfo.setPassword(user.getPassword());
            String token, refreshToken;
            try {
                token = jwtTokenUtil.generateToken(jwtInfo);
                refreshToken = jwtTokenUtil.generateRefreshToken(jwtInfo);
            } catch (Exception e) {
                throw new RuntimeException("生成jwt异常", e);
            }
            LoginUserVO loginUserVO = BeanUtil.copyProperties(loginUser, LoginUserVO.class);
            // 返回存入redis中的Map
            redisUtils.set(RedisConstant.REDIS_KEY_USER_LOGIN + user.getId(), loginUser, -1, TimeUnit.DAYS);
            authResponse.setAccessToken(token);
            authResponse.setRefreshToken(refreshToken);
            authResponse.setUserData(loginUserVO);
            return authResponse;

        } catch (AccountLockedException e) {
            // 账户锁定异常直接抛出
            throw e;
        } catch (Exception e) {
            // 4. 登录失败，记录失败信息
            log.warn("用户登录失败: {}", username, e);
            loginFailService.recordLoginFail(username, "unknown", "unknown");
            throw e;
        }
    }

    @Override
    public JWTInfo validateToken(String token) {
        try {
            JWTInfo infoFromToken = null;
            if (!tokenManager.isTokenBlacklisted(token)
                    && tokenManager.parseToken(token) != null) {
                infoFromToken = jwtTokenUtil.getInfoFromToken(token);
            }
            return infoFromToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout() {

    }
}
