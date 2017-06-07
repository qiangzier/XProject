package com.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

public class TypeAdapterWriter {

    /**
     * 已定义好的TypeAdapterFactory
     */
    private static final ClassName cusTypeAdapterFactory = ClassName.get(CusTypeAdapterFactory.class);

    /**
     * Field的工具类,在这里作用就是记录TypeAdapter的成员变量
     */
    private final TypeRegistry typeRegistry = new TypeRegistry();

    private final ParameterizedTypeName typeToken;

    private final ParameterizedTypeName typeAdapter;

    private ClassName modelType;

    private final List<FieldDefinition> fields;

    public TypeAdapterWriter(ProcessingEnvironment environment, TypeElement element){
        modelType = ClassName.get(element);
        typeAdapter = Types.getTypeAdapter(modelType);
        typeToken = ParameterizedTypeName.get(Types.TypeToken,modelType);

        JsonSerializable annotation = element.getAnnotation(JsonSerializable.class);
        fields = new ArrayList<>();
        if(!element.getSuperclass().toString().equals(Object.class.getName())){
            fields.addAll(extractFields(annotation,
                    environment.getElementUtils().getTypeElement(element.getSuperclass().toString())));
        }

        fields.addAll(extractFields(annotation, element));
    }

    /**
     * 收集可用字段
     * @param annotation  字段配置
     * @param typeElement 当前Model
     * @return 可用字段不包括transient,final,private,static字段
     */
    private static List<FieldDefinition> extractFields(JsonSerializable annotation,
                                                       TypeElement typeElement) {
        ArrayList<FieldDefinition> result = new ArrayList<>();
        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Object obj : elements) {
            if(obj instanceof VariableElement){
                VariableElement element = (VariableElement) obj;
                if(!element.getModifiers().contains(Modifier.TRANSIENT)
                        && !element.getModifiers().contains(Modifier.FINAL)
                        && !element.getModifiers().contains(Modifier.PRIVATE)
                        && !element.getModifiers().contains(Modifier.STATIC)){
                    result.add(new FieldDefinition(annotation,element));
                }
            }
        }
        return result;
    }

    /**
     * 收集到可用字段中的复杂类型(非基本数据类型及其装箱类型和String)
     * @return
     */
    private Set<TypeName> getComplexTypes(){
        Set<TypeName> complexTypes = new HashSet<>();
        for (FieldDefinition field : fields) {
            if(!field.isSimpleType()){
                complexTypes.add(field.getType());
            }
        }
        return complexTypes;
    }

    /**
     * 根据model的类名得到TypeAdapter的类名
     * @param modelType model类
     * @return 返回TypeAdapter类名,如果Model类是内部类,会在类名加上外部类的类名加$
     */
    private static String createTypeAdapterClassName(ClassName modelType){
        List<String> names = modelType.simpleNames();
        String modelClassName;
        if(names.size() > 1){
            StringBuilder stringBuilder = new StringBuilder();
            for (String name : names) {
                stringBuilder.append(name).append('$');
            }
            if(stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '$'){
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            modelClassName = stringBuilder.toString();
        }else{
            modelClassName = names.get(0);
        }
        return CusTypeAdapterFactory.getTypeAdapterFactoryName(modelClassName);
    }

    public String getTypeAdapterName(){
        return createTypeAdapterClassName(modelType);
    }

    private TypeSpec buildTypeSpec(){
        TypeSpec.Builder typeAdapterClass = TypeSpec.classBuilder(createTypeAdapterClassName(modelType));
        typeAdapterClass.addJavadoc("This class is dynamically loaded by {@link $T}.\n",
                cusTypeAdapterFactory);
        typeAdapterClass.addAnnotation(supperessWarnings("unused"));
        typeAdapterClass.addAnnotation(gsonGenerated());
        typeAdapterClass.addModifiers(Modifier.PUBLIC);
        typeAdapterClass.superclass(typeAdapter);

        for (TypeName typeName : getComplexTypes()) {
            typeAdapterClass.addField(typeRegistry.getField(typeName));
        }
        typeAdapterClass.addMethod(MethodSpec.constructorBuilder()
                .addAnnotation(supperessWarnings("unchecked"))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Gson.class,"gson")
                .addParameter(typeToken,"typeToken")
                .addCode(typeRegistry.getFieldInitialization())
                .build());

        typeAdapterClass.addMethod(buildWriteMethod());
        typeAdapterClass.addMethod(buildReadMethod());
        return typeAdapterClass.build();
    }

    /**
     * 给生成的TypeAdapter添加{@link SuppressWarnings}注解
     * @param warnings SuppressWarnings注解的值
     * @return 返回生成的SuppressWarnings注解
     */
    private static AnnotationSpec supperessWarnings(String... warnings){
        AnnotationSpec.Builder builder = AnnotationSpec.builder(SuppressWarnings.class);
        CodeBlock.Builder names = CodeBlock.builder();
        boolean first = true;
        for (String warning : warnings) {
            if(first){
                names.add("$S",warning);
                first = false;
            }else{
                names.add(", $S",warning);
            }
        }
        if(warnings.length == 1){
            builder.addMember("value",names.build());
        }else{
            builder.addMember("value","{$L}",names.build());
        }
        return builder.build();
    }

    private static AnnotationSpec gsonGenerated(){
        return AnnotationSpec.builder(GsonGenerated.class)
                .addMember("value","$S",GsonProcessor.class.getCanonicalName())
                .build();

    }

    /**
     * 生成TypeAdapter的Write方法
     * @return
     */
    private MethodSpec buildWriteMethod(){
        MethodSpec.Builder method = MethodSpec.methodBuilder("write")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException.class)
                .addParameter(JsonWriter.class,"writer")
                .addParameter(modelType,"value");
        method.beginControlFlow("if (value == null)");
        method.addStatement("writer.nullValue()");
        method.addStatement("return");
        method.endControlFlow();
        method.addStatement("writer.beginObject()");
        for (FieldDefinition field : fields) {
            method.addCode(field.buildWriteBlock(typeRegistry,"value","writer"));
        }
        method.addStatement("writer.endObject()");
        return method.build();
    }

    /**
     * @return 生成TypeAdapter中的read方法
     */
    private MethodSpec buildReadMethod() {
        MethodSpec.Builder method = MethodSpec.methodBuilder("read")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(modelType)
                .addException(IOException.class)
                .addParameter(JsonReader.class, "reader");
        method.beginControlFlow("if (reader.peek() == $T.NULL)", ClassName.get(JsonToken.class));
        method.addStatement("reader.nextNull()");
        method.addStatement("return null");
        method.endControlFlow();
        method.addStatement("$T object = new $T()", modelType, modelType);

        method.addStatement("reader.beginObject()");
        method.beginControlFlow("while (reader.hasNext())");
        method.beginControlFlow("switch (reader.nextName())");
        for (FieldDefinition field : fields) {
            for (String name : field.getSerializedNameCandidates()) {
                method.addCode("case $S:\n", name);
            }
            method.addCode(field.buildReadCodeBlock(typeRegistry, "object", "reader"));
            method.addStatement("break");
        }
        method.addCode("default:\n");
        method.addStatement("reader.skipValue()");
        method.addStatement("break");
        method.endControlFlow(); // switch
        method.endControlFlow(); // while
        method.addStatement("reader.endObject()");

        method.addStatement("return object", modelType);

        return method.build();
    }

    private JavaFile buildJavaFile(){
        return JavaFile.builder(modelType.packageName(),buildTypeSpec())
                .skipJavaLangImports(true)
                .build();
    }

    public void write(Filer filer){
        try {
            buildJavaFile().writeTo(filer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
