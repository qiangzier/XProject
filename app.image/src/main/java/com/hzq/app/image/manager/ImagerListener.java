package com.hzq.app.image.manager;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:
 */

public interface ImagerListener {
    void display(Context context, ImageView imageView,String url, int progressId, int errorId,Object tag);
    void display(Context context, ImageView imageView,String url, int progressId, int errorId);
    void display(Context context, ImageView imageView,String url, int progressId);
    void display(Context context, ImageView imageView,String url);
    void display(Context context, ImageView imageView, Uri uri);
}
