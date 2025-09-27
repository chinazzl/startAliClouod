package com.alicloud.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.api.service.user.UserService;
import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.model.AuthResponse;
import com.alicloud.model.UserVo;
import com.alicloud.constant.RedisConstant;
import com.alicloud.mapper.UserMapper;
import com.alicloud.model.BaseModelVo;
import com.alicloud.bean.User;
import com.alicloud.config.security.LoginUser;
import com.alicloud.utils.RedisUtils;
import com.alicloud.utils.jwt.JWTInfo;
import com.alicloud.utils.jwt.JwtTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:53
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils<LoginUser> redisUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    AuthenticationManager authenticationManager;


    // public List<User> getUserList() {
    //     List<User> userList;
    //     if (Boolean.TRUE.equals(redisUtils.existsKey(RedisConstant.REDIS_KEY_USER))) {
    //         userList = redisUtils.range(RedisConstant.REDIS_KEY_USER);
    //     } else {
    //         userList = userMapper.getUserList();
    //         redisUtils.leftPush(RedisConstant.REDIS_KEY_USER, userList);
    //     }
    //     return userList;
    // }

    @Deprecated
    @Override
    public ModelVo login(String username, String password, int loginType, String loginSystem) {
        ModelVo modelVo = new ModelVo();
        User user = new User();
        user.setUserName(username);
        user.setLoginType(loginType);
        user.setMobile(username);
        user.setPassword(password);
        // 1. 根据用户录入信息进行查询密码数据。
        User userInfo = userMapper.getUserToLogin(user);
        // 2. 如果查询的密码和录入密码相同，则生成token传入前台，如果没有则直接提示错误信息
        if (Objects.nonNull(userInfo)) {
            if (loginType == 0 && !passwordEncoder.matches(userInfo.getPassword(), password)) {
                modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "密码不正确");
            } else {
                try {
                    JWTInfo jwtInfo = JWTInfo.of(userInfo);
                    String token = jwtTokenUtil.generateToken(jwtInfo);
                    String refreshToken = jwtTokenUtil.generateRefreshToken(jwtInfo);
                    modelVo.setCodeEnum(BaseModelVo.Code.SUCCESS);
                    modelVo.getResult().put("token", token);
                    modelVo.getResult().put("refreshToken", refreshToken);
                    // 3. 将用户数据存入redis中
//                    redisUtils.opsValueForString(RedisConstant.REDIS_KEY_USER_LOGIN, token);
                } catch (Exception e) {
                    modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "获取token失败：" + e.getMessage());
                }

            }
        } else {
            modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "用户不存在。");

        }
        return modelVo;
    }

    @Override
    public AuthResponse login(UserLoginDto userDto) {
        AuthResponse authResponse = new AuthResponse();
        // 将前端传入的参数进行验证权限，获取权限
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 如果没有获得权限抛出异常
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登陆异常，无法找到用户");
        }
        // 如果存在权限，则生成jwt 存入redis中 key token。value userId
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        UserVo user = loginUser.getUserVo();
        if (!passwordEncoder.matches(userDto.getPassword(),user.getPassword())) {
            throw new RuntimeException("用户密码不正确，请重试");
        }
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setId(String.valueOf(user.getId()));
        jwtInfo.setUsername(user.getUserName());
        jwtInfo.setPassword(user.getPassword());
        String token,refreshToken;
        try {
            token = jwtTokenUtil.generateToken(jwtInfo);
            refreshToken = jwtTokenUtil.generateRefreshToken(jwtInfo);
        } catch (Exception e) {
            throw new RuntimeException("生成jwt异常", e);
        }
        //返回存入redis中的Map
        redisUtils.set(RedisConstant.REDIS_KEY_USER_LOGIN + user.getId(), loginUser,-1, TimeUnit.DAYS);
        authResponse.setAccessToken(token);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUserData(loginUser);
        return authResponse;
    }

    @Override
    public List<UserVo> getUserTotalList() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        List<User> userList = userMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(userList, UserVo.class);
    }

    @Override
    public ModelVo getJwtUserPubKey() {
        ModelVo vo = new ModelVo();
        vo.setCodeEnum(BaseModelVo.Code.SUCCESS);
        vo.getResult().put("pubKey", jwtTokenUtil.getUserPubKey());
        return vo;
    }

    @Override
    public Boolean logout() {
        // 1. 从SecurityContextHolder中获取用户登陆信息
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserVo().getId();
        // 2. 从redis中删除用户信息
        boolean delete = redisUtils.delete(RedisConstant.REDIS_KEY_USER_LOGIN + id);
        if (delete) {
            // 3.SecurityContextHolder的Authentic为null
            SecurityContextHolder.getContext().setAuthentication(null);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
