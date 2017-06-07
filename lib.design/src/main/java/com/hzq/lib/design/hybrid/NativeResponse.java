package com.hzq.lib.design.hybrid;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:回传给js的数据包装类
 */

public class NativeResponse {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILED = 1;

    private int status;
    private String message;
    private JSONObject data;

    public NativeResponse(int status, String msg, JSONObject data) {
        this.status = status;
        this.message = msg;
        this.data = data;
    }

    public NativeResponse(int status, String msg) {
        this.status = status;
        this.message = msg;
    }

    public NativeResponse(JSONObject data) {
        this.data = data;
        this.status = STATUS_SUCCESS;
    }

    public static NativeResponse errorResponse(String errorMsg){
        return new NativeResponse(STATUS_FAILED,errorMsg,null);
    }

    public String toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HybridConstants.ParamsName.STATUS,status);
            jsonObject.put(HybridConstants.ParamsName.MESSAGE,message);
            jsonObject.put(HybridConstants.ParamsName.DATA,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
