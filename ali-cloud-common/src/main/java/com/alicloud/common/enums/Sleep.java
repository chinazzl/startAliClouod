package com.alicloud.common.enums;

import lombok.Getter;

/**
 * @author: zhaolin
 * @Date: 2025/12/17
 * @Description: 睡眠质量
 **/
public enum Sleep {

    POOR(0, "欠佳"),
    FAIR(1, "需要注意"),
    GOOD(2, "很好"),
    EXCELLENT(3, "极佳");
    @Getter
    private int code;
    @Getter
    private String message;

    Sleep(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
