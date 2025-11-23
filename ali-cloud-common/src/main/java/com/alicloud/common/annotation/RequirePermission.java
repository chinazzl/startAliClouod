package com.alicloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhaolin
 * @Date: 2025/11/23
 * @Description: 权限验证注解
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /**
     * 需要的权限代码
     */
    String[] value() default {};

    /**
     * 权限验证模式
     */
    Logical logical() default Logical.AND;

    /**
     * 未授权时的消息
     */
    String message() default "权限不足";

    /**
     * 逻辑操作符
     */
    enum Logical {
        AND, OR
    }

}
