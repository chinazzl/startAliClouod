package com.alicloud.common.model.profile;


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
public class HealthAssessmentsVO implements Serializable {

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
     * 评估类型
     */
    private String assessmentType;

    /**
     * 每周运动次数
     */
    private Byte exerciseFrequency;

    /**
     * 每次运动时长(分钟)
     */
    private Integer exerciseDuration;

    /**
     * 运动强度
     */
    private String exerciseIntensity;

    /**
     * 每日睡眠时长(小时)
     */
    private Byte sleepHours;

    /**
     * 睡眠质量
     */
    private String sleepQuality;

    /**
     * 压力水平
     */
    private String stressLevel;

    /**
     * 进餐模式
     */
    private String mealPattern;

    /**
     * 饮食限制
     */
    private String dietaryRestrictions;

    /**
     * 食物过敏
     */
    private String foodAllergies;

    /**
     * 每日饮水量(ml)
     */
    private BigDecimal waterIntakeMl;

    /**
     * 慢性疾病
     */
    private String chronicDiseases;

    /**
     * 正在服用的药物
     */
    private String medications;

    /**
     * 营养补充剂
     */
    private String supplements;

    /**
     * 评估日期
     */
    private LocalDate assessmentDate;

    /**
     * 下次评估日期
     */
    private LocalDate nextAssessmentDate;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
