package com.gson;

import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

@AutoService(Processor.class)
public class GsonProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if(annotations.isEmpty())
//            return true;
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(JsonSerializable.class);
        for (Element element : elements) {
            TypeAdapterWriter writer = new TypeAdapterWriter(processingEnv, (TypeElement) element);
            writer.write(new SynchronizedFiler(processingEnv.getFiler()));
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(JsonSerializable.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
