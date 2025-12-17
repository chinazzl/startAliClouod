package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description: 压力登等级
 **/
public enum StressLevel {

    LOW(0,"低压力"),
    MEDIUM(1,"中等"),
    HIGH(2,"高压")
    ;
    @Getter
    private  int code;
    @Getter
    private String message;
    StressLevel(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
