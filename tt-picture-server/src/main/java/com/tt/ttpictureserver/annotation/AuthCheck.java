package com.tt.ttpictureserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 * 
 * 使用示例：
 * @AuthCheck(mustRole = "admin")  // 必须是管理员
 * @AuthCheck(anyRole = {"admin", "user"})  // 管理员或普通用户都可以
 */
@Target(ElementType.METHOD)  // 只能用在方法上
@Retention(RetentionPolicy.RUNTIME)  // 运行时生效
public @interface AuthCheck {
    
    /**
     * 必须拥有的角色（只需要一个）
     */
    String mustRole() default "";
    
    /**
     * 允许的角色列表（满足其中一个即可）
     */
    String[] anyRole() default {};
}
