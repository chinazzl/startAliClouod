package com.alicloud.handler;

import com.alibaba.fastjson.JSONObject;
import com.alicloud.model.CommonResponse;
import com.alicloud.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: zhaolin
 * @Date: 2024/12/14
 * @Description: 用户权限认证失败
 **/
@Component
public class DeniedAccessExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) {
        CommonResponse<String> response = CommonResponse.<String>builder().failed(HttpStatus.SC_UNAUTHORIZED, "用户无权限访问该功能，请联系系统负责人").build();
        String responseJson = JSONObject.toJSONString(response);
        WebUtils.renderString(httpServletResponse,responseJson);
    }
}
