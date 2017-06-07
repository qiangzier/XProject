package com.gson;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description: Field的工具类,负责记录TypeAdapter的成员变量
 */

public class TypeRegistry {
    private static final ClassName $Gson$Types = ClassName.get(com.google.gson.internal.$Gson$Types.class);
    private final Map<TypeName,FieldSpec> registry = new HashMap<>();

    private CodeBlock toInitializer(TypeName typeName){
        return CodeBlock.of("gson.getAdapter($T.get($L))",Types.TypeToken,toTypeExpr(typeName));
    }

    private CodeBlock toTypeExpr(TypeName typeName) {
        if(typeName instanceof ParameterizedTypeName){
            ParameterizedTypeName p = (ParameterizedTypeName) typeName;
            CodeBlock.Builder builder = CodeBlock.builder();
            for (int i = 0; i <p.typeArguments.size(); i++){
                TypeName t = p.typeArguments.get(i);
                if(i != 0){
                    builder.add(",$L",toTypeExpr(t));
                }else{
                    builder.add("$L",toTypeExpr(t));
                }
            }
            return CodeBlock.of("$T.newParameterizedTypeWithOwner(null,$T.class,$L)",
                    $Gson$Types,p.rawType,builder.build());
        }else{
            return CodeBlock.of("$T.class",typeName);
        }
    }

    private static String toName(TypeName type){
        return "$" + type.toString().replaceAll("[^a-zA-Z0-9]+","\\$");
    }

    FieldSpec getField(TypeName type){
        FieldSpec fieldSpec = FieldSpec.builder(Types.getTypeAdapter(type),toName(type))
                .addModifiers(new Modifier[]{Modifier.PRIVATE,Modifier.FINAL})
                .build();
        registry.put(type,fieldSpec);
        return fieldSpec;
    }

    CodeBlock getFieldInitialization(){
        CodeBlock.Builder block = CodeBlock.builder();
        Set<TypeName> keys = registry.keySet();
        for (TypeName key : keys) {
            FieldSpec fieldSpec = registry.get(key);
            if(key instanceof ParameterizedTypeName){
                block.addStatement("$N = ($T) $L",
                        fieldSpec,Types.getTypeAdapter(key),toInitializer(key));
            }else{
                block.addStatement("$N = $L",fieldSpec,toInitializer(key));
            }
        }
        return block.build();
    }
}
