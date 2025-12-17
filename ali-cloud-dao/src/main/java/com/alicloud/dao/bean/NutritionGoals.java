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
 * 营养目标表
 * </p>
 *
 * @author Your Name
 * @since 2025-12-17
 */
@Getter
@Setter
@ToString
@TableName("nutrition_goals")
public class NutritionGoals implements Serializable {

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
     * 基础代谢率(kcal)
     */
    @TableField("bmr")
    private BigDecimal bmr;

    /**
     * 每日总能量消耗(kcal)
     */
    @TableField("tdee")
    private BigDecimal tdee;

    /**
     * 目标摄入热量(kcal)
     */
    @TableField("target_calories")
    private BigDecimal targetCalories;

    /**
     * 蛋白质推荐量(g)
     */
    @TableField("protein_grams")
    private BigDecimal proteinGrams;

    /**
     * 碳水化合物推荐量(g)
     */
    @TableField("carbs_grams")
    private BigDecimal carbsGrams;

    /**
     * 脂肪推荐量(g)
     */
    @TableField("fat_grams")
    private BigDecimal fatGrams;

    /**
     * 膳食纤维推荐量(g)
     */
    @TableField("fiber_grams")
    private BigDecimal fiberGrams;

    /**
     * 饮水量推荐(ml)
     */
    @TableField("water_ml")
    private BigDecimal waterMl;

    /**
     * 生效日期
     */
    @TableField("effective_date")
    private LocalDate effectiveDate;

    /**
     * 是否当前使用
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
