package com.alicloud.common.model.profile;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户健康档案表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Getter
@Setter
@ToString
public class HealthProfilesVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID，关联users表
     */
    private Long userId;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Byte age;

    /**
     * 身高(cm)
     */
    private BigDecimal height;

    /**
     * 体重(kg)
     */
    private BigDecimal weight;

    /**
     * 活动系数
     */
    private BigDecimal activityLevel;

    /**
     * 健身目标
     */
    private String goal;

    /**
     * 是否为当前使用的档案
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
