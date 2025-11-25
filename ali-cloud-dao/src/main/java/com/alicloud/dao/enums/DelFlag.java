package com.alicloud.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2024/12/5
 **/
public enum DelFlag implements BaseEnum {

    //  '删除标志（0代表未删除，1代表已删除）
    NO_DEL(0, "未删除"),
    DEL(1, "已删除");

    @EnumValue
    private int code;
    @Getter
    private String message;

    DelFlag(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
}
