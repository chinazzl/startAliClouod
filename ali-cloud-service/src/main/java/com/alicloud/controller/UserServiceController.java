package com.alicloud.controller;

import com.alicloud.model.User;
import com.alicloud.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2022年09月28日 21:58
 * @Description:
 **********************************/
@RestController
@RequestMapping("/userService")
public class UserServiceController {

    @Resource
    private UserService userService;

    /**
     * 判断是否用户信息已注册
     *
     * @param user
     * @return
     */
    @RequestMapping("/getUserToLogin")
    public Integer getUserToLogin(@RequestBody User user) {
        return userService.getUserToLogin(user);
    }

    @RequestMapping(value = "/selectAllUser")
    public List<User> selectAllUser() {
        return userService.getUserList();
    }
}
