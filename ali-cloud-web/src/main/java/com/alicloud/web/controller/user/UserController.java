package com.alicloud.web.controller.user;

import com.alicloud.common.model.dto.UserLoginDto;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.model.Result;
import com.alicloud.common.model.UserVo;
import com.alicloud.service.AuthService;
import com.alicloud.service.UserService;
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
    private AuthService authService;

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
     * @return
     */
    //@RequestMapping(value = "/login", method = RequestMethod.GET)
    //@ResponseBody
    //@Deprecated
    //public ModelVo login(String username, String password) {
    //    return userService.login(username, password, 0, "web");
    //}

    @GetMapping("/getUsers")
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
    public CommonResponse<AuthResponse> login(@RequestBody UserLoginDto userDto) {
        AuthResponse authResponse = authService.login(userDto);
        return CommonResponse.<AuthResponse>builder().success(authResponse).build();
    }

    /**
     * 注册
     * @param userVo
     * @return
     */
    @PostMapping("/register")
    public CommonResponse<UserVo> register(@RequestBody UserVo userVo) {

        return null;
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

    // TODO  验证Token

    // TODO  刷新Token

    // TODO 注册

    // TODO 权限检查

    // TODO 获取权限信息

}
