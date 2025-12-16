package com.alicloud.common.exception;

/**
 * @author: zhaolin
 * @Date: 2025/12/13
 * @Description: 令牌过期异常
 **/
public class TokenExpiredException extends SmartNutriLifeException {

    private static final int TOKEN_EXPIRED_CODE = 40101;

    public TokenExpiredException(String message) {
        super(TOKEN_EXPIRED_CODE, message);
    }

    public TokenExpiredException(String message, Throwable throwable) {
        super(TOKEN_EXPIRED_CODE, message, throwable);
    }
}