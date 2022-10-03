package com.alicloud.exception;

/**********************************
 * @author zhang zhao lin
 * @date 2022年10月03日 15:50
 * @Description: 自定义异常
 **********************************/
public class UserException  extends RuntimeException{
    private int code;
    private String message;

    public UserException(String message,Throwable cause) {
        super(message,cause);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
