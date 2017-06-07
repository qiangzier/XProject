package com.gson;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.VariableElement;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description: Model字段
 */

public class FieldDefinition {

    private static final ClassName stringClz = ClassName.get(String.class);

    private final JsonSerializable config;

    /**
     * 该字段的类名
     */
    private final TypeName type;

    /**
     * 该字段的名称
     */
    private final String fieldName;

    /**
     * 该字段序列化名称
     */
    private final String serializedName;

    private final List<String> serializedNameCandidates;

    public FieldDefinition(JsonSerializable annotation, VariableElement element) {
        this.config = annotation;
        type = TypeName.get(element.asType());
        fieldName = element.getSimpleName().toString();
        serializedNameCandidates = new ArrayList<>();

        SerializedName anno = element.getAnnotation(SerializedName.class);
        if(anno != null){
            serializedName = anno.value();
            serializedNameCandidates.add(serializedName);
            Collections.addAll(serializedNameCandidates,anno.alternate());
        }else{
            serializedName = translateName(fieldName);
            serializedNameCandidates.add(serializedName);
        }
    }

    private String translateName(String fieldName) {
        switch (config.fieldNamingPolicy()){
            case UPPER_CAMEL_CASE:
                return upperCaseFirstLetter(fieldName);
            case UPPER_CAMEL_CASE_WITH_SPACES:
                return upperCaseFirstLetter(separateCamelCase(fieldName," "));
            case LOWER_CASE_WITH_UNDERSCORES:
                return separateCamelCase(fieldName,"_").toLowerCase(Locale.ENGLISH);
            case LOWER_CASE_WITH_DASHES:
                return separateCamelCase(fieldName, "-").toLowerCase(Locale.ENGLISH);
            default:return fieldName;
        }

    }

    private static String upperCaseFirstLetter(String name){
        StringBuilder builder = new StringBuilder();
        int index = 0;
        char firstCharacter = name.charAt(index);
        while (index < name.length() - 1){
            if(Character.isLetter(firstCharacter)){
                break;
            }

            builder.append(firstCharacter);
            firstCharacter = name.charAt(++index);
        }

        if(index == name.length()){
            return builder.toString();
        }

        if(!Character.isUpperCase(firstCharacter)){
            String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter),name,++index);
            return builder.append(modifiedTarget).toString();
        }else{
            return name;
        }
    }

    private static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
        return (indexOfSubstring < srcString.length())
                ? firstCharacter + srcString.substring(indexOfSubstring)
                : String.valueOf(firstCharacter);
    }

    private static String separateCamelCase(String name, String separator) {
        StringBuilder translation = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }

    /**
     * 是否需要指定的TypeAdapter,注意不包含long和Long,
     * @return
     */
    public boolean isSimpleType(){
        return type.equals(TypeName.BOOLEAN)
                || type.equals(TypeName.INT)
                || type.equals(TypeName.BYTE)
                || type.equals(TypeName.SHORT)
                || type.equals(TypeName.FLOAT)
                || type.equals(TypeName.DOUBLE)
                || type.equals(TypeName.BOOLEAN.box())
                || type.equals(TypeName.INT.box())
                || type.equals(TypeName.BYTE.box())
                || type.equals(TypeName.SHORT.box())
                || type.equals(TypeName.FLOAT.box())
                || type.equals(TypeName.DOUBLE.box())
                || type.equals(stringClz);
    }

    public TypeName getType() {
        return type;
    }

    public List<String> getSerializedNameCandidates() {
        return serializedNameCandidates;
    }


    public CodeBlock buildWriteBlock(TypeRegistry typeRegistry,String object,String writer){
        CodeBlock.Builder block = CodeBlock.builder();
        if(!type.isPrimitive() && !config.serializeNulls()){
            block.beginControlFlow("if ($L.$L != null)",object,fieldName);
        }
        block.addStatement("$L.name($S)",writer,serializedName);
        if(!type.isPrimitive() && config.serializeNulls()){
            block.beginControlFlow("if ($L.$L != null)",object,fieldName);
        }

        if(isSimpleType()){
            block.addStatement("$L.value($L.$L)",writer,object,fieldName);
        }else{
            block.addStatement("$N.write($L,$L.$L)",typeRegistry.getField(type),writer,object,fieldName);
        }
        if(!type.isPrimitive()){
            block.endControlFlow();
            if(config.serializeNulls()){
                block.beginControlFlow("else");
                block.addStatement("$L.nullValue()",writer);
                block.endControlFlow();
            }
        }
        return block.build();
    }

    /**
     * 生成该字段在{@link com.google.gson.TypeAdapter#read(JsonReader)}方法中的语句
     *
     * @param typeRegistry 这里就是个工具类
     * @param object       字段名
     * @param reader       {@link JsonReader}实例
     * @return 该字段在{@link com.google.gson.TypeAdapter#read(JsonReader)}方法中的语句
     */
    public CodeBlock buildReadCodeBlock(TypeRegistry typeRegistry, String object, String reader) {
        CodeBlock.Builder block = CodeBlock.builder();
        TypeName unboxType;
        try {
            unboxType = type.unbox();
        } catch (UnsupportedOperationException e) {
            unboxType = type;
        }
        if (unboxType.equals(TypeName.BOOLEAN)) {
            block.addStatement("$L.$L = $L.nextBoolean()", object, fieldName, reader);
        } else if (unboxType.equals(TypeName.LONG)) {
            block.addStatement("$L.$L = $L.nextLong()", object, fieldName, reader);

        } else if (unboxType.equals(TypeName.INT)
                || unboxType.equals(TypeName.BYTE) || unboxType.equals(TypeName.SHORT)) {
            block.addStatement("$L.$L = ($T) $L.nextLong()", object, fieldName, unboxType, reader);
        } else if (unboxType.equals(TypeName.DOUBLE)) {
            block.addStatement("$L.$L = $L.nextDouble()", object, fieldName, reader);
        } else if (unboxType.equals(TypeName.FLOAT)) {
            block.addStatement("$L.$L = ($T) $L.nextDouble()", object, fieldName, unboxType, reader);
        } else if (unboxType.equals(stringClz)) {
            block.addStatement("$L.$L = $L.nextString()", object, fieldName, reader);
        } else {
            block.addStatement("$L.$L = $N.read($L)",
                    object, fieldName, typeRegistry.getField(type), reader);
        }

        return block.build();
    }
}
