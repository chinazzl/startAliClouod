package com.alicloud.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2024/12/5
 **/
public enum UserStatus implements BaseEnum {

    // 账号状态（0正常 1停用）
    NORMAL(0, "正常"),
    STOP(1, "停用");

    @Getter
    private int code;
    @Getter
    private String message;

    UserStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
