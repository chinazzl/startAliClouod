package com.alicloud.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.service.UserService;
import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.UserVo;
import com.alicloud.dao.mapper.UserMapper;
import com.alicloud.common.model.BaseModelVo;
import com.alicloud.dao.bean.User;
import com.alicloud.common.utils.jwt.JWTInfo;
import com.alicloud.common.utils.jwt.JwtTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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


    //@Resource
    //private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;





   /* @Deprecated
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
    }*/


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
        return false;
    }
}
