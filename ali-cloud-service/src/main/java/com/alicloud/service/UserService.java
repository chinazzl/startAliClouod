package com.alicloud.service;

import com.alicloud.model.User;

import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 15:51
 * @Description:
 */
public interface UserService {

    /**
     * 根据用户信息查看是否数据库中存在用户
     *
     * @param user
     * @return
     */
    Integer getUserToLogin(User user);

    /**
     * test
     *
     * @return
     */
    List<User> getUserList();
}
