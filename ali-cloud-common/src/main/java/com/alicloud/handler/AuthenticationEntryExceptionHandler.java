package com.alicloud.handler;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alicloud.model.CommonResponse;
import com.alicloud.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: zhaolin
 * @Date: 2024/12/14
 * @Description:  用户认证状态异常处理类
 **/
@Component
@Slf4j
public class AuthenticationEntryExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)  {
        log.error(e.getMessage(), e);
        CommonResponse<String> response = CommonResponse.<String>builder().failed(HttpStatus.SC_FORBIDDEN, "用户未认证，请联系系统负责人").build();
        String responseJson = JSONObject.toJSONString(response);
        WebUtils.renderString(httpServletResponse,responseJson);
    }
}
