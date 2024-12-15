package com.alicloud.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhaolin
 * @Date: 2024/12/14
 * @Description:
 **/
public class WebUtils {

    public static void renderString(HttpServletResponse response, String str) {
        try {
            response.setContentType("text/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
