package com.hxl.handroid.http;

import android.util.Log;

import com.google.gson.Gson;
import com.hxl.handroid.BuildConfig;
import com.hxl.handroid.app.App;
import com.hxl.handroid.app.CSConfig;
import com.hxl.handroid.http.cookies.CookiesManager;
import com.hxl.handroid.util.SPUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator
 * on 2018/2/28 星期三.
 */

public class RetrofitFactory {

    private static final String BASE_URL = "http://www.wanandroid.com/";
    private static final String TAG = "request";
    private static final String ED_UUID = "ed_uuid";
    private final static int CONNECT_TIMEOUT = 30;

    private static OkHttpClient okHttpClient;
    private static HttpService httpService;

    private static OkHttpClient getHttp() {
        if (okHttpClient == null) {
            //打印请求日志拦截器
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(TAG, message);
                }
            });
            if (BuildConfig.DEBUG)
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            //添加header拦截器
            Interceptor tokenInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String uuid = (String) SPUtil.get(App.getApplication(), ED_UUID, "");
                    assert uuid != null;
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("x-auth-token", CSConfig.instance().getSession())
                            .addHeader("ed_uuid", uuid)
                            .build();
                    return chain.proceed(request);
                }
            };


            okHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(tokenInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .cookieJar(new CookiesManager())
                    .build();
        }
        return okHttpClient;
    }

    public static HttpService getHttpService() {
        if (httpService == null) {
            synchronized (RetrofitFactory.class) {
                if (httpService == null) {
                    httpService = new Retrofit.Builder().baseUrl(BASE_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))
                            .client(getHttp())
                            .build()
                            .create(HttpService.class);
                }
            }
        }
        return httpService;
    }
}
