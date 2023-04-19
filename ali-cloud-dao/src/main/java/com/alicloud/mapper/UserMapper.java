package com.alicloud.mapper;

import com.alicloud.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:54
 * @Description: 用户模块
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    User getUserToLogin(User user);
    List<User> getUserList();
}
