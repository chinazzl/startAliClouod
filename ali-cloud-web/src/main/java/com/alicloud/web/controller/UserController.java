package com.alicloud.web.controller;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.model.UserVo;
import com.alicloud.api.service.user.UserService;
import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.model.CommonResponse;
import com.alicloud.model.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Julyan
 * @version V1.0
 * @Date: 2022/9/25 13:12
 * @Description:
 */
@RestController
@RequestMapping("/v1/users")
//@RefreshScope
public class UserController {

    @Value("${result.code}")
    private Integer code;

    @Value("${result.message}")
    private String message;

    @Value("${result.success}")
    private Boolean success;

//    @DubboReference
    @Resource
    UserService userService;

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
    @Deprecated
    public ModelVo login(String username, String password) {
        return userService.login(username, password, 0, "web");
    }

    @GetMapping("/getUsers")
//    @PreAuthorize("hasAuthority('system:usr:list')")
    @PreAuthorize("@customAuth.hasPermission('system:usr:list')")
    public Result getUsers() {
        List<UserVo> userTotalList = userService.getUserTotalList();
        return Result.ok(userTotalList);
    }

    /**
     * 接口登录功能
     *
     * @param userDto 用户名请求
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse<Map<String,String>> login(@RequestBody UserLoginDto userDto) {
        Map<String,String> userInfoMap = userService.login(userDto);
        return CommonResponse.<Map<String,String>>builder().success(userInfoMap).build();
    }

    @GetMapping("/logout")
    public CommonResponse<String> logout() {
        Boolean isLogout = userService.logout();
        String message = isLogout ? "注销成功": "注销失败";
        return CommonResponse.<String>builder().success(message).build();
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
