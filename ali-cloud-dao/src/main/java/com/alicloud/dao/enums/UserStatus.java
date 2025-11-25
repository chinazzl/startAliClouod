package com.alicloud.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    @EnumValue
    private int code;
    @Getter
    private String message;

    UserStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
}
