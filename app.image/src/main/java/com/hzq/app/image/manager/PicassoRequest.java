package com.hzq.app.image.manager;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description: 使用Picasso 图片加载库
 */

public class PicassoRequest implements ImagerListener {
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
        Picasso.with(context).load(url).into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, Uri uri) {
        Picasso.with(context).load(uri).into(imageView);
    }
}
