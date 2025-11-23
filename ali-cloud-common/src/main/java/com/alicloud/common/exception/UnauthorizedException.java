package com.alicloud.common.exception;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description:
 **/
public class UnauthorizedException extends SmartNutriLifeException {

    public UnauthorizedException(String message) {
        super(401,message);
    }
    public UnauthorizedException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
