package com.hzq.lib.design.hybrid.handler;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.annotation.JsHandler;
import com.hzq.lib.design.hybrid.HybridConstants;
import com.hzq.lib.design.hybrid.JavaScriptBridge;
import com.hzq.lib.design.hybrid.JsCallback;
import com.hzq.lib.design.hybrid.NativeResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:    定制原生dialog
 *
 * 调用实例:
 * function showAndroidDialog(){
      var params = {"title":"test Dialog","message":"hello dialog","buttonLabels":['Ok','CANCEL']};
      native.callNative("showModal",JSON.stringify(params),"callId");
    }
 */

@JsHandler(HybridConstants.HandlerName.ALERT_DIALOG_METHOD)
public class AlertDialogHandler extends BaseHandler {
    @Override
    public void handle(JavaScriptBridge bridge, JSONObject params, final JsCallback callback, String hName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(bridge.getmContext());
        try {
            if(params.has(HybridConstants.ParamsName.TITLE)){
                builder.setTitle(params.getString(HybridConstants.ParamsName.TITLE));
            }
            if(params.has(HybridConstants.ParamsName.MESSAGE)){
                builder.setMessage(params.getString(HybridConstants.ParamsName.MESSAGE));
            }
            if(params.has(HybridConstants.ParamsName.BUTTOM_LABELS)){
                JSONArray array = params.getJSONArray(HybridConstants.ParamsName.BUTTOM_LABELS);
                if(array.length() == 1){
                    builder.setPositiveButton(array.getString(0), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.callback(new NativeResponse(getData(0)));
                        }
                    });
                }else if(array.length() == 2){
                    builder.setPositiveButton(array.getString(0), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.callback(new NativeResponse(getData(0)));
                        }
                    }).setNegativeButton(array.getString(1), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.callback(new NativeResponse(getData(1)));
                        }
                    });
                }else if(array.length() > 2){

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.create().show();
    }

    @Override
    public String[] handleName() {
        return new String[]{HybridConstants.HandlerName.ALERT_DIALOG_METHOD};
    }

    private JSONObject getData(int pos){
        JSONObject data = new JSONObject();
        try {
            data.put(HybridConstants.ParamsName.BUTTOM_INDEX, pos);
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return data;
    }
}
