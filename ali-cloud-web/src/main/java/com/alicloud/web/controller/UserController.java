package com.alicloud.web.controller;

import com.alicloud.annotation.LimitAccess;
import com.alicloud.model.Result;
import com.alicloud.model.User;
import com.alicloud.utils.CookieUtils;
import com.alicloud.web.remote.UserRemote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 接口登录功能
     * @param user
     * @return
     */
    @LimitAccess(count = 5)
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = {"application/json"})
    public Result login(HttpServletRequest request, HttpServletResponse response,@RequestBody User user) {
        Integer userId = userRemote.getUserToLogin(user);
        // 已注册
        if (userId != null) {
            user.setId(userId);
            // 设置cookie
            CookieUtils.setCookie(request,response,"login",String.valueOf(userId));
            request.getSession().setAttribute("user",user);
            return Result.ok(user);
        }
        return Result.error();
    }

    /**
     * 测试服务调通情况
     * @return
     */
    @RequestMapping("/userlist")
    public List<User> getUserList() {
        return userRemote.getUserList();
    }
}
