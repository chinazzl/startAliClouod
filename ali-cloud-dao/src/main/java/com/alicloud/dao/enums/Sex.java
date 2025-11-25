package com.alicloud.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2024/12/5
 **/
public enum Sex implements BaseEnum {
    //用户性别（0男，1女，2未知）

    MALE(0, "男性"),
    FEMALE(1, "女性"),
    UNKNOWN(2, "未知");

    @Getter
    @EnumValue
    private int code;
    @Getter
    private String message;

    Sex(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
