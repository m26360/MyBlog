package com.mana.blogapi.common.aop;


import java.lang.annotation.*;

@Target({ElementType.METHOD})//放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /**
     * 自定义注解
     */

    String module() default "";

    String operator() default "";
}
