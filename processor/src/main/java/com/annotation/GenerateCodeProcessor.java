package com.annotation;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author: hezhiqiang
 * @date: 17/1/25
 * @version:
 * @description:
 */

public class GenerateCodeProcessor {

    private String packageName;
    private Map<String,TypeElement> handleMap ;

    public GenerateCodeProcessor(String packageName, Map<String, TypeElement> handleMap) {
        this.packageName = packageName;
        this.handleMap = handleMap;
    }

    public void generateJavaFile(Filer filer){
        try {
            JavaFile javaFile = JavaFile.builder(packageName,generateCode()).build();
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TypeSpec generateCode(){
        ClassName baseHandler = ClassName.get("com.hzq.lib.design.hybrid.handler","BaseHandler");
        ClassName string = ClassName.get("java.lang","String");
        ClassName map = ClassName.get("java.util","Map");
        ClassName hashMap = ClassName.get("java.util","HashMap");
        //方法返回类型 Map<String,BaseHandle>
        ParameterizedTypeName returnMap = ParameterizedTypeName.get(map,string,baseHandler);

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(returnMap);

        methodBuild.addStatement("$T handles = new $T<>()",returnMap,hashMap);
        if(handleMap != null){
            for (Map.Entry<String, TypeElement> entry : handleMap.entrySet()) {
                methodBuild.addStatement("handles.put($S,new $T())",entry.getKey(),entry.getValue());
            }
        }
        methodBuild.addStatement("return handles");

        TypeSpec typeSpec = TypeSpec.classBuilder("AutoGenerateHandler")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(methodBuild.build())
                .build();

        return typeSpec;
    }
}
