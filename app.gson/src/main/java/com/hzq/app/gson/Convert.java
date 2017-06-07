package com.hzq.app.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.gson.CusTypeAdapterFactory;

import java.lang.reflect.Type;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

public class Convert {
    private static Gson sGson;
    public static Gson gson(){
        if(sGson == null){
            sGson = newInstance();
        }
        return sGson;
    }

    private static Gson newInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(CusTypeAdapterFactory.newInstance());
        return gsonBuilder.create();
    }

    public static <T> T fromJson(String json,Class<T> type) throws JsonIOException,JsonSyntaxException{
        return gson().fromJson(json,type);
    }

    public static <T> T fromJson(String json, Type type){
        return gson().fromJson(json,type);
    }

    public static String toJson(Object src){
        return gson().toJson(src);
    }

    public static String toJson(Object src,Type type){
        return gson().toJson(src,type);
    }
}
