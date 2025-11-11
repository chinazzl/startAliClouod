package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2024/12/5
 **/
public enum UserType implements BaseEnum {

    //用户类型：0代表普通用户，1代表管理员
    NORMAL(0, "普通用户"),
    ADMIN(1, "管理员");


    @Getter
    private int code;
    @Getter
    private String message;

    UserType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
