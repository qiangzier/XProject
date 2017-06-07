package com.gson;

import com.google.gson.FieldNamingPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JsonSerializable {

    /**
     * 命名策略
     * @return
     */
    FieldNamingPolicy fieldNamingPolicy() default FieldNamingPolicy.IDENTITY;

    /**
     * 是否序列化null,需要设置 {@link com.google.gson.GsonBuilder#serializeNulls()} 才能启用
     * @return 默认不序列化null
     */
    boolean serializeNulls() default false;
}
