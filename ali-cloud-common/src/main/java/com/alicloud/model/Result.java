package com.alicloud.model;

import com.alicloud.enums.ResultCode;

import java.io.Serializable;

/**********************************
 * @author zhang zhao lin
 * @date 2022年09月28日 22:00
 * @Description:
 **********************************/
public class Result implements Serializable {
    private Boolean success;
    private int code;
    private String message;
    private Object data;

    public Result() {

    }

    public Result(Object data) {
        this.success = true;
        this.code = ResultCode.REQUEST_SUCCESS.getCode();
        this.message = ResultCode.REQUEST_SUCCESS.getMessage();
        this.data = data;
    }

    public Result(Boolean success, int code, String message, Object data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    /**
     * 请求成功
     *
     * @param data
     * @return
     */
    public static Result ok(Object data) {
        return new Result(data);
    }

    /**
     * 请求失败
     *
     * @return
     */
    public static Result error() {
        Result result = new Result();
        result.setCode(ResultCode.REQUEST_FAIL.getCode());
        result.setMessage(ResultCode.REQUEST_FAIL.getMessage());
        return result;
    }

    /**
     * 请求失败
     *
     * @param code
     * @param message
     * @return
     */
    public static Result error(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
