package com.alicloud.dao.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户健康评估表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Data
@TableName("health_assessments")
public class HealthAssessments implements Serializable {

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
     * 评估类型
     */
    @TableField("assessment_type")
    private String assessmentType;

    /**
     * 每周运动次数
     */
    @TableField("exercise_frequency")
    private Byte exerciseFrequency;

    /**
     * 每次运动时长(分钟)
     */
    @TableField("exercise_duration")
    private Integer exerciseDuration;

    /**
     * 运动强度
     */
    @TableField("exercise_intensity")
    private String exerciseIntensity;

    /**
     * 每日睡眠时长(小时)
     */
    @TableField("sleep_hours")
    private Byte sleepHours;

    /**
     * 睡眠质量
     */
    @TableField("sleep_quality")
    private String sleepQuality;

    /**
     * 压力水平
     */
    @TableField("stress_level")
    private String stressLevel;

    /**
     * 进餐模式
     */
    @TableField("meal_pattern")
    private String mealPattern;

    /**
     * 饮食限制
     */
    @TableField("dietary_restrictions")
    private String dietaryRestrictions;

    /**
     * 食物过敏
     */
    @TableField("food_allergies")
    private String foodAllergies;

    /**
     * 每日饮水量(ml)
     */
    @TableField("water_intake_ml")
    private BigDecimal waterIntakeMl;

    /**
     * 慢性疾病
     */
    @TableField("chronic_diseases")
    private String chronicDiseases;

    /**
     * 正在服用的药物
     */
    @TableField("medications")
    private String medications;

    /**
     * 营养补充剂
     */
    @TableField("supplements")
    private String supplements;

    /**
     * 评估日期
     */
    @TableField("assessment_date")
    private LocalDate assessmentDate;

    /**
     * 下次评估日期
     */
    @TableField("next_assessment_date")
    private LocalDate nextAssessmentDate;

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
}
