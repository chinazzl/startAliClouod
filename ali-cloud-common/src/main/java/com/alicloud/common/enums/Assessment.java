package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description: 个人评估
 **/
public enum Assessment {

    INITIAL(0,"初始评估"),
    FOLLOW_UP(1,"跟进评估"),
    PERIODIC(2,"周期评估")
    ;

    @Getter
    private int code;
    @Getter
    private String message;
    Assessment(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
