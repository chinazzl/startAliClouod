package com.alicloud.api.feign.auth;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.utils.jwt.JWTInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@FeignClient(name="alicloud-authService",path = "/auth")
public interface AuthServiceFeign {
    // 用户登录
    @PostMapping("/login")
    CommonResponse<AuthResponse> login(@RequestBody UserLoginDto  userLoginDto);

    // 验证TOken
    @PostMapping("/validateToken")
    JWTInfo validateToken(String token);
    // 刷新Token

    // 注册

    // 权限检查

    // 获取权限信息
}
