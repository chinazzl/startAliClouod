package com.alicloud.common.enums;

import static org.apache.http.HttpStatus.*;

/**********************************
 * @author zhang zhao lin
 * @date 2022年10月02日 0:26
 * @Description:
 **********************************/
public enum ResultCode {
    /**
     * 请求成功
     */
    REQUEST_SUCCESS(SC_OK, "请求成功"),
    /**
     * 请求失败
     */
    REQUEST_FAIL(SC_INTERNAL_SERVER_ERROR, "服务器错误"),

    REQUEST_TIMEOUT(-999, "远程调用失败"),
    TOKEN_SIGNATURE_INVALID(SC_FORBIDDEN,"Token签名验证失败"),

    TOKEN_PARSE_ERROR(500,"Token转换错误")
    ;


    /**
     * 返回消息
     */
    private final String message;
    /**
     * 返回编码
     */
    private final int code;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
