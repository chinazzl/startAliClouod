package com.alicloud.common.model.profile;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 健康目标设置表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Data
public class HealthObjectivesVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 健康档案ID
     */
    private Long profileId;

    /**
     * 目标类型
     */
    private String objectiveType;

    /**
     * 目标数值
     */
    private BigDecimal targetValue;

    /**
     * 当前数值
     */
    private BigDecimal currentValue;

    /**
     * 单位(kg, %, cm等)
     */
    private String unit;

    /**
     * 目标完成日期
     */
    private LocalDate targetDate;

    /**
     * 优先级(1-5)
     */
    private Byte priority;

    /**
     * 是否已达成
     */
    private Boolean isAchieved;

    /**
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
