package com.annotation;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author: hezhiqiang
 * @date: 17/1/24
 * @version:
 * @description:
 */

@AutoService(Processor.class)
public class JsHandlerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(JsHandler.class);
        Map<String,TypeElement> handlers = new HashMap<>();
        String packageName = "";
        for (Element element : elements) {
            //只处理标注在类上且非抽象类的注解
            if(element.getKind() != ElementKind.CLASS || element.getModifiers().contains(Modifier.ABSTRACT)){
                break;
            }

            //获取标注注解的所有类的key
            String[] handlerNames = element.getAnnotation(JsHandler.class).value();
            //被注解的类名
//            String className = element.getSimpleName().toString();
            for (String handlerName : handlerNames) {
                handlers.put(handlerName, (TypeElement) element);
            }

            packageName = processingEnv.getElementUtils()
                    .getPackageOf(elements.iterator().next())
                    .getQualifiedName().toString();
        }


        if(!handlers.isEmpty()) {
            GenerateCodeProcessor generateCodeProcessor = new GenerateCodeProcessor(packageName, handlers);
            generateCodeProcessor.generateJavaFile(processingEnv.getFiler());
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(JsHandler.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
