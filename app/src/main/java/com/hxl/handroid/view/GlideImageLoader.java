package com.hxl.handroid.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator
 * on 2018/4/9 星期一.
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(final Context context, Object path, final ImageView imageView) {
        Glide.with(context).load(path).centerCrop().into(imageView);
    }
}
