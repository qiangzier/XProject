package com.hzq.lib.design.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/22
 * @version:
 * @description:
 */

public class StringUtils {
    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        try {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            Log.d("checkPermission",""+e.getMessage());
            return true;
        }
    }

    public static String getString(Object obj, String path) {
        Object value = get(obj, path);
        if (value == null)
            return "";

        if (value instanceof String)
            return (String) value;

        String s = String.valueOf(value);
        if (s != null)
            return s;
        return "";
    }

    /*
    * obj may be JSONObject or JSONArray object
    * path为路径，类似"a/3/b"，可以一次性取出多层的值
    * */
    public static Object get(Object jsonObj, String path) {

        if(jsonObj==null||path==null)
            return null;

        // 获取key
        String key;
        int index = path.indexOf('/');
        if (index < 0) {
            key = path;
            path = null;
        } else {
            key = path.substring(0, index);
            path = path.substring(index + 1);
        }

        // 获取第一部分
        Object value = null;
        try {
            if(jsonObj instanceof JSONObject)
                value = ((JSONObject)jsonObj).get(key);
            else if(jsonObj instanceof JSONArray)
            {
                JSONArray ary=(JSONArray)jsonObj;
                int n=(int)Double.parseDouble(key);
                value=ary.get(n);
            }
        } catch (Exception e) {
        }

        if (value != null && path != null) {
            value = get(value, path);
        }

        return value;
    }

    public static boolean isColorDark(@ColorInt int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}
