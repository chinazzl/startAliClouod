package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description: 运动强度
 **/
public enum Exercise_intensity {

    LOW(0,"低强度"),
    MEDIUM(1,"中强度"),
    HIGH(2,"高强度")
    ;
    @Getter
    private int code;
    @Getter
    private String message;
    Exercise_intensity(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
