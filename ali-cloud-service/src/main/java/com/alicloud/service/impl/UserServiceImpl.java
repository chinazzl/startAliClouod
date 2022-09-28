package com.alicloud.service.impl;

import com.alicloud.constant.RedisConstant;
import com.alicloud.mapper.UserMapper;
import com.alicloud.model.User;
import com.alicloud.service.UserService;
import com.alicloud.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:53
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils<User> redisUtils;

    @Override
    public List<User> getUserList() {
        List<User> userList;
        if (Boolean.TRUE.equals(redisUtils.existsKey(RedisConstant.REDIS_KEY_USER))) {
            userList = redisUtils.range(RedisConstant.REDIS_KEY_USER);
        }else {
            userList = userMapper.getUserList();
            redisUtils.leftPush(RedisConstant.REDIS_KEY_USER, userList);
        }
        return userList;
    }
}
