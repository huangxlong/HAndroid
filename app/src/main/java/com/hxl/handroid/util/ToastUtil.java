package com.hxl.handroid.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hxl.handroid.R;


/**
 * 自定义Toast统一管理类
 * <p/>
 * Created by Administrator on 2018/2/23.
 */
public class ToastUtil {

    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Toast mToast = null;

    /**
     * 默认短时间Toast
     *
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 默认短时间Toast
     *
     * @param context
     * @param message
     */
    public static void show(Context context, int message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * Toast
     *
     * @param context
     * @param resId
     * @param duration
     */
    public static void show(Context context, int resId, int duration) {
        show(context, context.getString(resId), duration);
    }

    /**
     * 自定义Toast样式
     *
     * @param context
     * @param message
     */
    public static void show(final Context context, final String message, int duration) {
        if (mToast == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            mToast.setView(view);
        }
        mToast.setDuration(duration);
        TextView textView = (TextView) mToast.getView().findViewById(R.id.toast_text);
        textView.setText(message);
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
