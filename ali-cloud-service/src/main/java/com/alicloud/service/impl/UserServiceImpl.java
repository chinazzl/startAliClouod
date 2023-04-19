package com.alicloud.service.impl;

import com.alicloud.api.service.user.UserService;
import com.alicloud.api.vo.ModelVo;
import com.alicloud.constant.RedisConstant;
import com.alicloud.mapper.UserMapper;
import com.alicloud.model.BaseModelVo;
import com.alicloud.model.User;
import com.alicloud.utils.RedisUtils;
import com.alicloud.utils.jwt.JWTInfo;
import com.alicloud.utils.jwt.JwtTokenUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:53
 * @Description:
 */
@DubboService
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils<String> redisUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


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

    @Override
    public ModelVo login(String username, String password, int loginType, String loginSystem) {
        ModelVo modelVo = new ModelVo();
        User user = new User();
        user.setUserName(username);
        user.setLoginType(loginType);
        user.setMobile(username);
        // 1. 根据用户录入信息进行查询密码数据。
        User userInfo = userMapper.getUserToLogin(user);
        // 2. 如果查询的密码和录入密码相同，则生成token传入前台，如果没有则直接提示错误信息
        if (Optional.of(userInfo).isPresent()) {
            if (loginType == 0 && !passwordEncoder.matches(userInfo.getPassWord(), password)) {
                modelVo.setCodeEnum(BaseModelVo.Code.ERROR, "密码不正确");
            } else {
                try {
                    JWTInfo jwtInfo = JWTInfo.of(userInfo);
                    String token = jwtTokenUtil.generateToken(jwtInfo);
                    modelVo.setCodeEnum(BaseModelVo.Code.SUCCESS);
                    modelVo.getResult().put("token",token);
                    // 3. 将用户数据存入redis中
                    redisUtils.opsValueForString(RedisConstant.REDIS_KEY_USER_LOGIN, token);
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
    public ModelVo getJwtUserPubKey() {
        ModelVo vo = new ModelVo();
        vo.setCodeEnum(BaseModelVo.Code.SUCCESS);
        vo.getResult().put("pubKey", jwtTokenUtil.getUserPubKey());
        return vo;
    }
}
