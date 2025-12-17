package com.alicloud.common.model.profile;


import lombok.Data;
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
@Data
public class NutritionGoalsVO implements Serializable {

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
     * 基础代谢率(kcal)
     */
    private BigDecimal bmr;

    /**
     * 每日总能量消耗(kcal)
     */
    private BigDecimal tdee;

    /**
     * 目标摄入热量(kcal)
     */
    private BigDecimal targetCalories;

    /**
     * 蛋白质推荐量(g)
     */
    private BigDecimal proteinGrams;

    /**
     * 碳水化合物推荐量(g)
     */
    private BigDecimal carbsGrams;

    /**
     * 脂肪推荐量(g)
     */
    private BigDecimal fatGrams;

    /**
     * 膳食纤维推荐量(g)
     */
    private BigDecimal fiberGrams;

    /**
     * 饮水量推荐(ml)
     */
    private BigDecimal waterMl;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 是否当前使用
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
