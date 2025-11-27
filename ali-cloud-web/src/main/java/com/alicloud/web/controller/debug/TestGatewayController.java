package com.alicloud.web.controller.debug;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhaolin
 * @Date: 2025/5/31
 * @Description:
 **/
@RestController
@RequestMapping("/test/gateway")
public class TestGatewayController {

    @RequestMapping("/addRequestHeader")
    public String addRequestHeader(HttpServletRequest request) {
        String header = request.getHeader("X-Request-Acme");
        if (StringUtils.isNotBlank(header)) {
            return "header Info: " + header;
        }
        return "None found Header X-Request-Acme";
    }


}
