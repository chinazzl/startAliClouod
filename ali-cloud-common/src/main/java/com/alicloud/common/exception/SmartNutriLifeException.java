package com.alicloud.common.exception;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description:
 **/
public class SmartNutriLifeException extends RuntimeException{

    private int code;
    public SmartNutriLifeException() {
    }

    public SmartNutriLifeException(int code, String message,Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SmartNutriLifeException(String message, Throwable cause) {
        this(500, message, cause);
    }

    public int getCode() {
        return code;
    }
}
