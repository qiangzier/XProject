package com.hzq.lib.design.hybrid.handler;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.annotation.JsHandler;
import com.hzq.lib.design.hybrid.HybridConstants;
import com.hzq.lib.design.hybrid.JavaScriptBridge;
import com.hzq.lib.design.hybrid.JsCallback;
import com.hzq.lib.design.hybrid.NativeResponse;
import com.hzq.lib.design.utils.StringUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:
 */

@JsHandler(HybridConstants.HandlerName.ALERT_CAMERA_METHOD)
public class Camerahandler extends BaseHandler {
    private Activity mContext;
    private JsCallback callback;

    @Override
    public void handle(JavaScriptBridge bridge, JSONObject params, JsCallback callback, String hName) {
        this.mContext = (Activity) bridge.getmContext();
        this.callback = callback;
        takePicture(mContext);
    }

    private void takePicture(Activity context) {
        if (StringUtils.checkPermission(context, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(intent, HybridConstants.RequestCode.TAKE_PHOTO_WITH_DATA);
        } else {
            mContext = context;
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    HybridConstants.RequestCode.RC_PERMISSION_CAMERA);
        }
    }

    @Override
    public String[] handleName() {
        return new String[]{HybridConstants.HandlerName.ALERT_CAMERA_METHOD};
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //处理相机回调
        if(resultCode == mContext.RESULT_OK && requestCode == HybridConstants.RequestCode.TAKE_PHOTO_WITH_DATA){
            Bundle bundle = intent.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            String fileName;
            if(bitmap != null) {
                FileOutputStream b = null;
                File file = new File("/sdcard/myImage/");
                file.mkdirs();// 创建文件夹，名称为pk4fun // 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。网上流传的其他Demo这里的照片名称都写死了，则会发生无论拍照多少张，后一张总会把前一张照片覆盖。细心的同学还可以设置这个字符串，比如加上“ＩＭＧ”字样等；然后就会发现sd卡中myimage这个文件夹下，会保存刚刚调用相机拍出来的照片，照片名称不会重复。
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                String str = format.format(new Date());
                fileName = "/sdcard/myImage/" + str + ".jpg";
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(fileName)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 17/1/19  上传图片
                            if(callback != null){
                                callback.callback(new NativeResponse(new JSONObject()));
                            }
                        }
                    }).start();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.callback = null;
        this.mContext = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == HybridConstants.RequestCode.RC_PERMISSION_CAMERA && mContext != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture(mContext);
            } else {
                Toast.makeText(mContext, "权限被禁止，无法拍照", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
