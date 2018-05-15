package com.hxl.handroid.http;

import com.hxl.handroid.base.ArticleData;
import com.hxl.handroid.entity.BannerData;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.entity.LoginData;
import com.hxl.handroid.entity.SearchData;
import com.hxl.handroid.entity.SearchHotData;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator
 * on 2018/2/28 星期三.
 */

public interface HttpService {

    /**
     * 获取banner数据
     * http://www.wanandroid.com/banner/json
     *
     * @return BannerData
     */
    @GET("banner/json")
    Observable<BaseRsp<List<BannerData>>> getBanner();

    /**
     * 获取文章列表
     *
     * @param num 页数
     * @return 文章列表
     */
    @GET("article/list/{num}/json")
    Observable<BaseRsp<ArticleData>> getArticleList(@Path("num") int num);

    /**
     * 用户登录
     * http://www.wanandroid.com/user/login
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录数据
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseRsp<LoginData>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 获取收藏文章列表
     *
     * @param num 页数
     * @return 文章列表数据
     */
    @GET("lg/collect/list/{num}/json")
    Observable<BaseRsp<ArticleData>> getCollectArticle(@Path("num") int num);


    /**
     * 添加收藏
     *
     * @param id 文章id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<BaseRsp<ArticleData>> addCollectArticle(@Path("id") int id);


    /**
     * 取消收藏文章
     *
     * @param id 文章id
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseRsp<ArticleData>> deleteCollectArticle(@Path("id") int id);


    /**
     * 获取搜索热词
     *
     * @return 搜索热词数据
     */
    @GET("hotkey/json")
    Observable<BaseRsp<List<SearchHotData>>> getSearchHotData();


    /**
     * 关键词搜索
     *
     * @param page 搜索结果页数
     * @param key  搜索关键词
     * @return
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<BaseRsp<ArticleData>> getSearchResultData(@Path("page") int page, @Field("k") String key);

}
