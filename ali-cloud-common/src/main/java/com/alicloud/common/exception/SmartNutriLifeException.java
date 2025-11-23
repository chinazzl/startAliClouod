package com.alicloud.common.exception;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/11/22
 * @Description:
 **/
@Getter
public class SmartNutriLifeException extends RuntimeException{

    private int code;
    public SmartNutriLifeException() {
    }

    public SmartNutriLifeException(String message) {
        super(message);
    }

    public SmartNutriLifeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SmartNutriLifeException(int code, String message,Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SmartNutriLifeException(String message, Throwable cause) {
        this(500, message, cause);
    }

}
