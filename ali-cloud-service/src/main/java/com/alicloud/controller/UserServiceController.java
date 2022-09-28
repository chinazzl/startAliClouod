package com.alicloud.controller;

import com.alicloud.model.User;
import com.alicloud.service.UserService;
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

    @RequestMapping(value = "/selectAllUser")
    public List<User> selectAllUser() {
        return userService.getUserList();
    }
}
