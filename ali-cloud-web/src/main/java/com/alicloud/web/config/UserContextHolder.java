package com.alicloud.web.config;

import com.alicloud.dao.bean.User;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description:
 **/
public class UserContextHolder {

    ThreadLocal<User> userThreadLocal = new ThreadLocal<>();



}
