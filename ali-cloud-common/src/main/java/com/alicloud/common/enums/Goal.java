package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description: 目标
 **/
public enum Goal {

    LOSE(-1,"减重"),
    MAINTAIN(0,"保持体重"),
    GAIN(1,"增重/增肌")
    ;
    @Getter
    private int code;
    @Getter
    private String message;
    Goal(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
