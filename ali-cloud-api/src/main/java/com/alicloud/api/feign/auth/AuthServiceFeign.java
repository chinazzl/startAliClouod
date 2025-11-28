package com.alicloud.api.feign.auth;

import com.alicloud.api.bean.dto.UserLoginDto;
import com.alicloud.common.config.feign.OpenFeignOkHttpConfiguration;
import com.alicloud.common.model.AuthResponse;
import com.alicloud.common.model.CommonResponse;
import com.alicloud.common.utils.jwt.JWTInfo;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@FeignClient(name="alicloud-authService", path = "/auth", configuration = OpenFeignOkHttpConfiguration.class)
public interface AuthServiceFeign {
    // 用户登录
    @RequestLine("POST /login")
    CommonResponse<AuthResponse> login(@RequestBody UserLoginDto  userLoginDto);

    // 验证Token
    @RequestLine("GET /validateToken")
    JWTInfo validateToken(@RequestParam("token") String token);
    // 刷新Token

    // 注册

    // 权限检查

    // 获取权限信息
}
