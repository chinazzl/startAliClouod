package com.alicloud.common.exception;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description:
 **/
public class NotLoginException extends SmartNutriLifeException {

    public NotLoginException() {}

    public NotLoginException(int code,String message,Throwable cause) {
        super(code, message, cause);
    }
}
