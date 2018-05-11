package com.hxl.handroid.entity;

/**
 * Created by Administrator
 * on 2018/2/28 星期三.
 */

public class BaseRsp<T> {
    public static final int SUCCESS = 0;
    /**
     * 0：成功，1：失败
     */
    public int errorCode;

    public String errorMsg;

    public T data;
}
