package com.alicloud.common.enums;

/**
 * 数据状态枚举
 */
public enum DataStatus {
    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 已过期
     */
    EXPIRED(1, "已过期"),

    /**
     * 待删除
     */
    TO_DELETE(2, "待删除"),

    /**
     * 已删除
     */
    DELETED(3, "已删除");

    private final Integer code;
    private final String desc;

    DataStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DataStatus getByCode(Integer code) {
        for (DataStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}