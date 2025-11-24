package com.alicloud.web.controller;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.api.feign.auth.AuthServiceFeign;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.UserVo;
import com.alicloud.service.UserService;
import com.alicloud.api.bean.vo.ModelVo;
import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.model.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Value("${result.code:200}")
    private Integer code;

    @Value("${result.message:message}")
    private String message;

    @Value("${result.success:true}")
    private Boolean success;

//    @DubboReference
    @Resource
    UserService userService;

    @Resource
    AuthServiceFeign authServiceFeign;

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
    //@RequestMapping(value = "/login", method = RequestMethod.GET)
    //@ResponseBody
    //@Deprecated
    //public ModelVo login(String username, String password) {
    //    return userService.login(username, password, 0, "web");
    //}

    @GetMapping("/getUsers")
//    @PreAuthorize("hasAuthority('system:usr:list')")
//    @PreAuthorize("!@customAuth.hasPermission('system:usr:list1')")
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
    public CommonResponse<AuthResponse> login(@RequestBody UserLoginDto userDto) {
        return authServiceFeign.login(userDto);
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
