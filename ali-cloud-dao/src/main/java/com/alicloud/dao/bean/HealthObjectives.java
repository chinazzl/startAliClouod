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
 * 健康目标设置表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Getter
@Setter
@ToString
@TableName("health_objectives")
public class HealthObjectives implements Serializable {

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
     * 目标类型
     */
    @TableField("objective_type")
    private String objectiveType;

    /**
     * 目标数值
     */
    @TableField("target_value")
    private BigDecimal targetValue;

    /**
     * 当前数值
     */
    @TableField("current_value")
    private BigDecimal currentValue;

    /**
     * 单位(kg, %, cm等)
     */
    @TableField("unit")
    private String unit;

    /**
     * 目标完成日期
     */
    @TableField("target_date")
    private LocalDate targetDate;

    /**
     * 优先级(1-5)
     */
    @TableField("priority")
    private Byte priority;

    /**
     * 是否已达成
     */
    @TableField("is_achieved")
    private Boolean isAchieved;

    /**
     * 是否激活
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 备注
     */
    @TableField("notes")
    private String notes;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
