package com.hxl.handroid.util;

import android.util.Log;

import com.hxl.handroid.app.AppConstant;


/**
 * Log统一管理类
 */
public class LogUtils {

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static final boolean isDebug = !AppConstant.IS_PUBLIC;
    private static final String TAG = "rst";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.i(TAG, msg.substring(i, i + 4000));
                else Log.i(TAG, msg.substring(i, msg.length()));
            }
        } else {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.d(TAG, msg.substring(i, i + 4000));
                else Log.d(TAG, msg.substring(i, msg.length()));
            }
        } else {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.e(TAG, msg.substring(i, i + 4000));
                else Log.e(TAG, msg.substring(i, msg.length()));
            }
        } else {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.v(TAG, msg.substring(i, i + 4000));
                else Log.v(TAG, msg.substring(i, msg.length()));
            }
        } else {
            Log.v(TAG, msg);
        }
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.i(tag, msg.substring(i, i + 4000));
                else Log.i(tag, msg.substring(i, msg.length()));
            }
        } else {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.d(tag, msg.substring(i, i + 4000));
                else Log.d(tag, msg.substring(i, msg.length()));
            }
        } else {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.e(tag, msg.substring(i, i + 4000));
                else Log.e(tag, msg.substring(i, msg.length()));
            }
        } else {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) Log.v(tag, msg.substring(i, i + 4000));
                else Log.v(tag, msg.substring(i, msg.length()));
            }
        } else {
            Log.v(tag, msg);
        }
    }
}
