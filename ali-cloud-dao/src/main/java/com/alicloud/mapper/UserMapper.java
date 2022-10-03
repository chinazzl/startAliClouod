package com.alicloud.mapper;

import com.alicloud.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:54
 * @Description: 用户模块
 */
@Repository
public interface UserMapper {

    Integer getUserToLogin(User user);
    List<User> getUserList();
}
