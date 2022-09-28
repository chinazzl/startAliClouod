package com.alicloud.web.controller;

import com.alicloud.model.User;
import com.alicloud.web.remote.UserRemote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 13:12
 * @Description:
 */
@RestController
@RequestMapping("/v1/users")
@RefreshScope
public class UserController {

    @Value("${result.code}")
    private Integer code;

    @Value("${result.message}")
    private String message;

    @Value("${result.success}")
    private Boolean success;

    @Resource
    private UserRemote userRemote;

    /**
     * 读取配置文件
     *
     * @return
     */
    @RequestMapping("/getReultConfig")
    public String getReultConfig() {
        System.out.println(success);
        return code == null ? "读取配置失败" : message + "::" + code;
    }

    @RequestMapping("/userlist")
    public List<User> getUserList() {
        return userRemote.getUserList();
    }
}
