package com.hxl.handroid.util;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public class RxUtils {
    /**
     * 统一线程处理
     *
     * @return ObservableTransformer
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 类型转换
     *
     * @param object
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }
}
