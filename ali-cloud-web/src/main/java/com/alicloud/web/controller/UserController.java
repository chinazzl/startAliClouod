package com.alicloud.web.controller;

import com.alicloud.api.service.user.UserService;
import com.alicloud.api.vo.ModelVo;
import com.alicloud.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
    private UserService userService;

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
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public ModelVo login(String username, String password) {
        return userService.login(username, password, 0, "web");
    }


    /*@LimitAccess(count = 5)
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
    }*/

}
