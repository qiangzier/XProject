package com.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description: TypeAdapter生成器,根据model的类名生成一个modelTypeAdapter对象
 */

public class CusTypeAdapterFactory implements TypeAdapterFactory {

    public static CusTypeAdapterFactory newInstance(){
        return new CusTypeAdapterFactory();
    }

    /**
     * 默认将model的类名加TypeAdapter作为该model的TypeAdapter类名。
     * @param modelClassName
     * @return
     */
    public static String getTypeAdapterFactoryName(String modelClassName){
        return modelClassName + "TypeAdapter";
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        String name = getTypeAdapterFactoryName(type.getRawType().getName());
        Class<?> typeAdapterClass;
        try {
            typeAdapterClass = Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Constructor<TypeAdapter<T>> constructor = null;
        try {
            constructor = (Constructor<TypeAdapter<T>>) typeAdapterClass.getDeclaredConstructor(Gson.class,TypeToken.class);
        } catch (NoSuchMethodException e) {
            new RuntimeException("Missing constructor Constructor(Gson,TypeToken) for " + name,e);
        }

        try {
            return constructor.newInstance(gson,type);
        } catch (InstantiationException e) {
            throw new RuntimeException("Can't create an instance of " + name,e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't create an instance of " + name,e);
        } catch (InvocationTargetException e) {
            throw  new RuntimeException("Can't create an instance of " + name,e);
        }
    }
}
