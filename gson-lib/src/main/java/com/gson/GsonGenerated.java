package com.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description: 标记型接口,被标识的对象将会被TypeAdapterFactory动态加载
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GsonGenerated {

    /**
     * @return The class name of the annotation processor
     */
    String value();
}
