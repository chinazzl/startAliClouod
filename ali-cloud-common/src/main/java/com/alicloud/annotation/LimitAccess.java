package com.alicloud.annotation;

import com.alicloud.enums.LimitType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zzl
 * @Description 接口限流注解"
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitAccess {

    /**
     * 限流key
     */
    String key() default "redis_limit";

    /**
     * 限流时间 单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
