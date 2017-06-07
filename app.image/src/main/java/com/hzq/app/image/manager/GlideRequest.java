package com.hzq.app.image.manager;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description: 使用glide图片库
 */

public class GlideRequest implements ImagerListener {
    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId, Object tag) {

    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId, int errorId) {

    }

    @Override
    public void display(Context context, ImageView imageView, String url, int progressId) {

    }

    @Override
    public void display(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, Uri uri) {
        Glide.with(context).load(uri).into(imageView);
    }
}
