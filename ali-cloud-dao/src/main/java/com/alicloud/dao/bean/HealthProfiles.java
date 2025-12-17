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
@TableName("health_profiles")
public class HealthProfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联users表
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 年龄
     */
    @TableField("age")
    private Byte age;

    /**
     * 身高(cm)
     */
    @TableField("height")
    private BigDecimal height;

    /**
     * 体重(kg)
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 活动系数
     */
    @TableField("activity_level")
    private BigDecimal activityLevel;

    /**
     * 健身目标
     */
    @TableField("goal")
    private String goal;

    /**
     * 是否为当前使用的档案
     */
    @TableField("is_active")
    private Boolean isActive;

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
