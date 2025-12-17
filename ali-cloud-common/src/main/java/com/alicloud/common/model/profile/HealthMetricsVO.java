package com.alicloud.common.model.profile;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 身体指标历史记录表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Data
public class HealthMetricsVO implements Serializable {

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
     * 体重(kg)
     */
    private BigDecimal weight;

    /**
     * 体脂率(%)
     */
    private BigDecimal bodyFatRate;

    /**
     * 肌肉量(kg)
     */
    private BigDecimal muscleMass;

    /**
     * 腰围(cm)
     */
    private BigDecimal waistline;

    /**
     * 臀围(cm)
     */
    private BigDecimal hipline;

    /**
     * 胸围(cm)
     */
    private BigDecimal chestCircumference;

    /**
     * 收缩压(mmHg)
     */
    private Integer bloodPressureSystolic;

    /**
     * 舒张压(mmHg)
     */
    private Integer bloodPressureDiastolic;

    /**
     * 心率(次/分钟)
     */
    private Integer heartRate;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
