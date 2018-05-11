package com.hxl.handroid.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxl.handroid.R;
import com.hxl.handroid.app.AppConstant;
import com.hxl.handroid.base.ArticleData;
import com.hxl.handroid.base.BaseFragment;
import com.hxl.handroid.entity.BannerData;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.entity.LoginData;
import com.hxl.handroid.http.BaseSubscriber;
import com.hxl.handroid.http.RetrofitFactory;
import com.hxl.handroid.ui.WebActivity;
import com.hxl.handroid.ui.activity.LoginActivity;
import com.hxl.handroid.ui.activity.MainActivity;
import com.hxl.handroid.ui.adpater.ArticleAdapter;
import com.hxl.handroid.util.RxUtils;
import com.hxl.handroid.util.SPUtil;
import com.hxl.handroid.util.ToastUtil;
import com.hxl.handroid.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public class KnowledgeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecycler;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Banner mBanner;
    private List<String> mBannerTitleList;
    private List<String> mBannerUrlList;
    private View bannerView;
    private int currentPage = 0;    //当前页数
    private List<ArticleData.Article> articleList = new ArrayList<>();
    private ArticleAdapter articleAdapter;
    private String username;
    private String password;

    public static KnowledgeFragment newInstance() {
        return new KnowledgeFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void initView() {
        bannerView = LayoutInflater.from(mContext).inflate(R.layout.layout_banner, null);
        mBanner = bannerView.findViewById(R.id.banner);
        initRecyclerView();
        mSwipeRefresh.setOnRefreshListener(this::getBannerAndArticle);
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefresh.setRefreshing(true);
        username = (String) SPUtil.get(mContext, AppConstant.USERNAME, "");
        password = (String) SPUtil.get(mContext, AppConstant.PASSWORD, "");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            autoLogin();
        } else {
            getBannerAndArticle();
        }
    }

    /**
     * RecyclerView初始化，点击相关
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        articleAdapter = new ArticleAdapter(articleList, true, false);
        articleAdapter.addHeaderView(bannerView);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(articleAdapter);

        articleAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra(LINK_URL, ((ArticleData.Article) adapter.getData().get(position)).link);
            intent.putExtra(LINK_TITLE, ((ArticleData.Article) adapter.getData()
                    .get(position)).title);
            startActivity(intent);
        });

        articleAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ArticleData.Article article = (ArticleData.Article) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_flag:
                    //分类查看

                    break;
                case R.id.iv_good:
                    //收藏
                    if (article.collect) {
                        //取消收藏
                        RetrofitFactory.getHttpService()
                                .deleteCollectArticle(article.id)
                                .compose(RxUtils.rxSchedulerHelper())
                                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(mContext) {
                                    @Override
                                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                            article.collect = false;
                                            ToastUtil.show(mContext, "取消收藏！");
                                            articleAdapter.notifyDataSetChanged();
                                        } else {
                                            ToastUtil.show(mContext, articleDataBaseRsp.errorMsg);
                                        }
                                    }
                                });
                    } else {
                        //添加收藏
                        RetrofitFactory.getHttpService()
                                .addCollectArticle(article.id)
                                .compose(RxUtils.rxSchedulerHelper())
                                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(mContext) {
                                    @Override
                                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                            article.collect = true;
                                            ToastUtil.show(mContext, "收藏成功！");
                                            articleAdapter.notifyDataSetChanged();
                                        } else {
                                            if (articleDataBaseRsp.errorMsg.contains("登录")) {
                                                startActivityForResult(new Intent(mContext, LoginActivity.class), AppConstant.LOGING_CODE);
                                            } else {
                                                ToastUtil.show(mContext, articleDataBaseRsp.errorMsg);
                                            }
                                        }
                                    }
                                });
                    }
                    break;
            }
        });

        //加载更多
        articleAdapter.setOnLoadMoreListener(() -> {
            mRecycler.postDelayed(() -> {
                currentPage++;
                RetrofitFactory.getHttpService()
                        .getArticleList(currentPage)
                        .compose(RxUtils.rxSchedulerHelper())
                        .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(mContext) {
                            @Override
                            public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                    if (articleDataBaseRsp.data == null || articleDataBaseRsp.data.size == 0) {
                                        ToastUtil.show(mContext, "没有数据！");
                                        return;
                                    }
                                    if (articleDataBaseRsp.data.curPage == articleDataBaseRsp.data.pageCount) {
                                        articleAdapter.loadMoreEnd();
                                    } else {
                                        articleAdapter.loadMoreComplete();
                                    }
                                    articleList.addAll(articleDataBaseRsp.data.datas);
                                    articleAdapter.notifyDataSetChanged();
                                } else {
                                    articleAdapter.loadMoreFail();
                                    ToastUtil.show(mContext, articleDataBaseRsp.errorMsg);
                                }
                            }
                        });
            }, 500);
        }, mRecycler);
    }

    /**
     * 刷新
     */
    private void getBannerAndArticle() {
        Observable<BaseRsp<ArticleData>> articleObservable = RetrofitFactory.getHttpService()
                .getArticleList(0);
        Observable<BaseRsp<List<BannerData>>> bannerObservable = RetrofitFactory.getHttpService()
                .getBanner();
        Observable.zip(bannerObservable, articleObservable, (bannerResponse, articleListResponse) -> {
            HashMap<String, Object> map = new HashMap<>(2);
            map.put(AppConstant.BANNER_DATA, bannerResponse);
            map.put(AppConstant.ARTICLE_DATA, articleListResponse);
            return map;
        })
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<HashMap<String, Object>>(mContext) {
                    @Override
                    public void onResponse(HashMap<String, Object> map) {
                        mSwipeRefresh.setRefreshing(false);
                        BaseRsp<List<BannerData>> bannerData = RxUtils.cast(map.get(AppConstant.BANNER_DATA));
                        BaseRsp<ArticleData> articleData = RxUtils.cast(map.get(AppConstant.ARTICLE_DATA));
                        if (bannerData.errorCode == BaseRsp.SUCCESS) {
                            showBanner(bannerData);
                        } else {
                            ToastUtil.show(mContext, bannerData.errorMsg);
                        }
                        if (articleData.errorCode == BaseRsp.SUCCESS) {
                            currentPage = 0;
                            articleAdapter.notifyLoadMoreToLoading();
                            showArticle(articleData);
                        } else {
                            ToastUtil.show(mContext, articleData.errorMsg);
                        }
                    }
                });
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        Observable<BaseRsp<LoginData>> loginObservable = RetrofitFactory.getHttpService()
                .login(username, password);
        Observable<BaseRsp<ArticleData>> articleObservable = RetrofitFactory.getHttpService()
                .getArticleList(0);
        Observable<BaseRsp<List<BannerData>>> bannerObservable = RetrofitFactory.getHttpService()
                .getBanner();
        Observable.zip(loginObservable, bannerObservable, articleObservable, (loginResponse, bannerResponse, articleListResponse) -> {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put(AppConstant.LOGIN_DATA, loginResponse);
            map.put(AppConstant.BANNER_DATA, bannerResponse);
            map.put(AppConstant.ARTICLE_DATA, articleListResponse);
            return map;
        })
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<HashMap<String, Object>>(mContext) {
                    @Override
                    public void onResponse(HashMap<String, Object> map) {
                        mSwipeRefresh.setRefreshing(false);
                        BaseRsp<LoginData> loginResponse = RxUtils.cast(map.get(AppConstant.LOGIN_DATA));
                        if (loginResponse.errorCode == BaseRsp.SUCCESS) {
                            SPUtil.put(mContext, AppConstant.USERNAME, loginResponse.data.username);
                            SPUtil.put(mContext, AppConstant.PASSWORD, loginResponse.data.password);
                            AppConstant.isLogin = true;
                            ToastUtil.show(mContext, "自动登录成功！");
                            ((MainActivity) getActivity()).setUser();
                        } else {
                            ToastUtil.show(mContext, loginResponse.errorMsg);
                        }
                        showBanner(RxUtils.cast(map.get(AppConstant.BANNER_DATA)));
                        showArticle(RxUtils.cast(map.get(AppConstant.ARTICLE_DATA)));
                    }
                });
    }

    /**
     * 展示文章列表
     *
     * @param articleDataBaseRsp
     */
    private void showArticle(BaseRsp<ArticleData> articleDataBaseRsp) {
        if (articleDataBaseRsp == null || articleDataBaseRsp.data == null) {
            ToastUtil.show(mContext, "没有数据！");
            return;
        }
        articleList.clear();
        articleList.addAll(articleDataBaseRsp.data.datas);
        articleAdapter.notifyDataSetChanged();
    }

    /**
     * 展示banner
     *
     * @param bannerBaseRsp
     */
    private void showBanner(BaseRsp<List<BannerData>> bannerBaseRsp) {
        if (bannerBaseRsp == null || bannerBaseRsp.data == null) {
            ToastUtil.show(mContext, "没有数据！");
            return;
        }
        mBannerTitleList = new ArrayList<>();
        List<String> bannerImageList = new ArrayList<>();
        mBannerUrlList = new ArrayList<>();
        List<BannerData> bannerDataList = bannerBaseRsp.data;
        for (BannerData bannerData : bannerDataList) {
            mBannerTitleList.add(bannerData.title);
            bannerImageList.add(bannerData.imagePath);
            mBannerUrlList.add(bannerData.url);
        }
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .isAutoPlay(true)
                .setDelayTime(2500)
                .setImages(bannerImageList)
                .start();

        mBanner.setOnBannerListener(position -> {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra(AppConstant.LINK_URL, mBannerUrlList.get(position));
            intent.putExtra(AppConstant.LINK_TITLE, mBannerTitleList.get(position));
            startActivity(intent);
        });
    }

    @OnClick(R.id.iv_top)
    public void onClick(View v) {
        if (mRecycler != null) mRecycler.smoothScrollToPosition(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.LOGING_CODE) {
            //刷新
            mSwipeRefresh.setRefreshing(true);
            getBannerAndArticle();
        }
    }
}
