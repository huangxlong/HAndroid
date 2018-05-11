package com.hxl.handroid.http;

import android.app.Activity;

import com.hxl.handroid.R;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.util.LogUtils;
import com.hxl.handroid.util.NetUtil;
import com.hxl.handroid.util.ToastUtil;

import rx.Subscriber;

/**
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private Activity mContext;

    protected BaseSubscriber(Activity ctx) {
        this.mContext = ctx;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("request:", "Start");
        if (!NetUtil.isConnected(mContext)) {
            ToastUtil.show(mContext, R.string.toast_net_error);
            //取消本次订阅
            if (!isUnsubscribed()) {
                unsubscribe();
            }
            return;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        onResponse(t);
    }

    public abstract void onResponse(T t);
}
