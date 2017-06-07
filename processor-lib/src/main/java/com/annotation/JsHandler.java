package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: hezhiqiang
 * @date: 17/1/24
 * @version:
 * @description:
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JsHandler {
    String[] value();
}
