package com.frodo.bigbong.micro.framework.interceptor;

import java.lang.annotation.*;

/**
 * @author: frodoking
 * @date: 2019/12/15
 * @description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public  @interface  MicroServiceMethodAnnotation {
    boolean needPrintArgs() default true;
}
