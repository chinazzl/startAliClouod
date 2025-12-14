package com.alicloud.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.alicloud.common.exception.UnauthorizedException;
import com.alicloud.common.exception.UserException;
import com.alicloud.common.model.RegisterResponse;
import com.alicloud.common.model.dto.UserLoginDto;
import com.alicloud.common.model.dto.UserRegisterDto;
import com.alicloud.auth.config.LoginUser;
import com.alicloud.common.constant.RedisConstant;
import com.alicloud.common.exception.AccountLockedException;
import com.alicloud.common.handler.TokenManager;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.UserVo;
import com.alicloud.common.model.auth.LoginUserVO;
import com.alicloud.common.utils.CommonUtil;
import com.alicloud.common.utils.RedisUtils;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import com.alicloud.dao.bean.User;
import com.alicloud.dao.enums.DelFlag;
import com.alicloud.dao.enums.Sex;
import com.alicloud.dao.enums.UserStatus;
import com.alicloud.dao.enums.UserType;
import com.alicloud.dao.mapper.UserMapper;
import com.alicloud.service.AuthService;
import com.alicloud.service.LoginFailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.commons.CommonUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    @Resource
    private PasswordEncoder passwordEncoder;


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
    public AuthResponse refreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new RuntimeException("Refresh token is empty");
        }
        try {
            JWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(refreshToken);
            String userId = infoFromToken.getId();
            // 检查token是否在黑名单中
            if (tokenManager.isTokenBlacklisted(refreshToken)) {
                throw new UnauthorizedException("Token已经失效");
            }
            // 检查用户是否有效
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getId, userId);
            queryWrapper.eq(User::isEnabled,true)
                    .eq(User::isAccountLocked,false)
                    .eq(User::getDelFlag,false);
            User user = userMapper.selectOne(queryWrapper);
            if (Objects.isNull(user)) {
                throw new UserException("用户不存在或者已禁用");
            }
            JWTInfo jwtInfo = new JWTInfo();
            jwtInfo.setId(String.valueOf(user.getId()));
            jwtInfo.setUsername(user.getUserName());
            jwtInfo.setPassword(user.getPassword());
            String newAccessToken = jwtTokenUtil.generateToken(jwtInfo);
            String  newRefreshToken = jwtTokenUtil.generateRefreshToken(jwtInfo);

            LoginUser loginUser = redisUtils.get(RedisConstant.REDIS_KEY_USER_LOGIN + user.getId());
            if (!Objects.isNull(loginUser)) {
                redisUtils.set(RedisConstant.REDIS_KEY_USER_LOGIN + user.getId(), loginUser, -1, TimeUnit.DAYS);
            }
            // 构建响应
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(newAccessToken);
            authResponse.setRefreshToken(newRefreshToken);
            tokenManager.blacklistToken(refreshToken);
            if (!Objects.isNull(loginUser)) {
                LoginUserVO loginUserVO = BeanUtil.copyProperties(loginUser, LoginUserVO.class);
                authResponse.setUserData(loginUserVO);
            }
            log.info("用户{},成功刷新", userId);
            return authResponse;
        }catch (Exception e) {
            log.error("refreshToken error", e);
            throw new RuntimeException("刷新Refresh Token",e);
        }
    }

    @Override
    public JWTInfo validateToken(String token) {
        try {
            JWTInfo infoFromToken = null;
            if (!tokenManager.isTokenBlacklisted(token)
                    && tokenManager.parseToken(token) != null) {
                infoFromToken = tokenManager.parseToken(token);
            }
            return infoFromToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RegisterResponse register(UserRegisterDto userRegisterDto) {

        //1. 校验输入密码和二次提交的密码是否符合
        validateRegisterParams(userRegisterDto);
        //2. 校验用户名、电话是否被注册过
        validateUserExists(userRegisterDto);
        //4. 创建用户
        User user = createUser(userRegisterDto);
        //5. 入库
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new UserException("注册失败，请联系工作人员");
        }
        //6. 构建返回结果
        RegisterResponse response = RegisterResponse.builder()
                .success(true)
                .userId(user.getId())
                .username(user.getUserName())
                .registerTime(new Date().toString())
                .message("注册成功")
                .needEmailVerification(false)
                .verificationEmail(user.getEmail())
                .build();

        log.info("用户注册成功: userId={}, username={}", user.getId(), user.getUserName());
        return response;
    }

    private User createUser(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setUserName(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setAccountLocked(false);
        user.setNickName(userRegisterDto.getNickname());
        String encodePassword = passwordEncoder.encode(userRegisterDto.getPassword());
        user.setPassword(encodePassword);
        user.setUserType(UserType.NORMAL);
        user.setUserStatus(UserStatus.NORMAL);
        user.setSex(Sex.UNKNOWN);
        user.setDelFlag(DelFlag.NO_DEL);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setPasswordSalt(CommonUtil.randomString(32));
        // 登录失败相关
        user.setLoginFailCount(0);
        user.setLockTime(null);
        user.setUnlockTime(null);
        user.setLastLoginTime(null);
        user.setLastLoginIp(null);

        // 创建信息
        user.setCreateBy(0L); // 系统创建
        user.setUpdateBy(0L);

        return user;
    }

    private void validateUserExists(UserRegisterDto userRegisterDto) {
        LambdaQueryWrapper<User>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userRegisterDto.getUsername());
        queryWrapper.eq(User::getDelFlag, DelFlag.NO_DEL);
        queryWrapper.eq(User::getUserStatus, UserStatus.NORMAL);
        boolean exists = userMapper.exists(queryWrapper);
        if (exists) {
            throw new UserException("用户已存在");
        }
    }

    private void validateRegisterParams(UserRegisterDto userRegisterDto) {
        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new UserException("输入密码不匹配，请重试");
        }
    }



    @Override
    public void logout(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }
        try {
            // 1. 获取Token信息
            JWTInfo jwtInfo = jwtTokenUtil.getInfoFromToken(token);
            if (jwtInfo != null) {
                String userId = jwtInfo.getId();
                // 2. 清除Redis中的登录状态
                redisUtils.delete(RedisConstant.REDIS_KEY_USER_LOGIN + userId);
                log.info("用户退出登录: userId={}, username={}", userId, jwtInfo.getUsername());
            }
            // 3. 将Token加入黑名单
            tokenManager.blacklistToken(token);
        } catch (Exception e) {
            log.warn("退出登录异常: token={}", token, e);
        }
    }
}
