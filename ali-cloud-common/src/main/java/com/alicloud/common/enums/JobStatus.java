package com.alicloud.common.enums;

/**
 * 任务状态枚举
 */
public enum JobStatus {
    /**
     * 运行中
     */
    RUNNING(0, "运行中"),

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 失败
     */
    FAILED(2, "失败"),

    /**
     * 已取消
     */
    CANCELLED(3, "已取消");

    private final Integer code;
    private final String desc;

    JobStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static JobStatus getByCode(Integer code) {
        for (JobStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}