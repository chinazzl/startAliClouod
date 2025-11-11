package com.alicloud.common.model;

import com.alicloud.common.config.security.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhaolin
 * @Date: 2025/9/23
 * @Description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private LoginUser userData;
}
