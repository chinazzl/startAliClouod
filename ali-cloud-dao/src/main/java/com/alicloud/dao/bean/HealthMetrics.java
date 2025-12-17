package com.alicloud.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
@TableName("health_metrics")
public class HealthMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 健康档案ID
     */
    @TableField("profile_id")
    private Long profileId;

    /**
     * 体重(kg)
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 体脂率(%)
     */
    @TableField("body_fat_rate")
    private BigDecimal bodyFatRate;

    /**
     * 肌肉量(kg)
     */
    @TableField("muscle_mass")
    private BigDecimal muscleMass;

    /**
     * 腰围(cm)
     */
    @TableField("waistline")
    private BigDecimal waistline;

    /**
     * 臀围(cm)
     */
    @TableField("hipline")
    private BigDecimal hipline;

    /**
     * 胸围(cm)
     */
    @TableField("chest_circumference")
    private BigDecimal chestCircumference;

    /**
     * 收缩压(mmHg)
     */
    @TableField("blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    /**
     * 舒张压(mmHg)
     */
    @TableField("blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    /**
     * 心率(次/分钟)
     */
    @TableField("heart_rate")
    private Integer heartRate;

    /**
     * 记录日期
     */
    @TableField("record_date")
    private LocalDate recordDate;

    /**
     * 备注信息
     */
    @TableField("notes")
    private String notes;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
