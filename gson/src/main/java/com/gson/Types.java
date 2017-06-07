package com.gson;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

public class Types {
    public static final ClassName TypeToken = ClassName.get(com.google.gson.reflect.TypeToken.class);
    public static final ClassName TypeAdapter = ClassName.get(com.google.gson.TypeAdapter.class);

    public static ParameterizedTypeName getTypeAdapter(TypeName typeName){
        return ParameterizedTypeName.get(TypeAdapter,typeName.box());
    }
}
