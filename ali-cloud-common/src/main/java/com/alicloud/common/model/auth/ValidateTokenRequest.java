package com.alicloud.common.model.auth;

import lombok.Data;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
@Data
public class ValidateTokenRequest {

    private String token;

    /**
     * 请求资源路径
     */
    private String resource;
    /**
     * 请求Http方法
     */
    private String method;
}
