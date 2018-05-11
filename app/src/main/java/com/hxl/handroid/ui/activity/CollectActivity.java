package com.hxl.handroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxl.handroid.R;
import com.hxl.handroid.base.ArticleData;
import com.hxl.handroid.base.BaseActivity;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.http.BaseSubscriber;
import com.hxl.handroid.http.RetrofitFactory;
import com.hxl.handroid.ui.WebActivity;
import com.hxl.handroid.ui.adpater.ArticleAdapter;
import com.hxl.handroid.util.RxUtils;
import com.hxl.handroid.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator
 * on 2018/5/11 星期五.
 */
public class CollectActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipRefresh;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private int currentPage = 0;
    private ArticleAdapter articleAdapter;
    private List<ArticleData.Article> articleList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        tvTitle.setText("收藏");
        mSwipRefresh.setColorSchemeResources(R.color.colorPrimary);
        initRecyclerView();
        mSwipRefresh.setRefreshing(true);
        getArticle();
        mSwipRefresh.setOnRefreshListener(this::getArticle);
    }

    private void initRecyclerView() {
        articleAdapter = new ArticleAdapter(articleList, false, true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(articleAdapter);

        articleAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(CollectActivity.this, WebActivity.class);
            intent.putExtra(LINK_URL, ((ArticleData.Article) adapter.getData().get(position)).link);
            intent.putExtra(LINK_TITLE, ((ArticleData.Article) adapter.getData()
                    .get(position)).title);
            startActivity(intent);
        });

        articleAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ArticleData.Article article = (ArticleData.Article) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_good:
                    //取消收藏
                    RetrofitFactory.getHttpService()
                            .deleteCollectArticle(article.originId)
                            .compose(RxUtils.rxSchedulerHelper())
                            .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(CollectActivity.this) {
                                @Override
                                public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                    if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                        adapter.getData().remove(position);
                                        ToastUtil.show(CollectActivity.this, "取消收藏！");
                                        articleAdapter.notifyItemRemoved(position);
                                    } else {
                                        ToastUtil.show(CollectActivity.this, articleDataBaseRsp.errorMsg);
                                    }
                                }
                            });
                    break;
                case R.id.tv_flag:
                    break;
            }
        });

        //加载更多
        articleAdapter.setOnLoadMoreListener(() -> {
            currentPage++;
            RetrofitFactory.getHttpService()
                    .getCollectArticle(currentPage)
                    .compose(RxUtils.rxSchedulerHelper())
                    .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                        @Override
                        public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                            if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                if (articleDataBaseRsp.data == null || articleDataBaseRsp.data.size == 0) {
                                    ToastUtil.show(CollectActivity.this, "没有数据！");
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
                                ToastUtil.show(CollectActivity.this, articleDataBaseRsp.errorMsg);
                            }
                        }
                    });
        }, mRecyclerView);
    }

    private void getArticle() {
        RetrofitFactory.getHttpService()
                .getCollectArticle(0)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                    @Override
                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                        mSwipRefresh.setRefreshing(false);
                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                            currentPage = 0;
                            articleAdapter.notifyLoadMoreToLoading();
                            showArticle(articleDataBaseRsp);
                        } else {
                            ToastUtil.show(CollectActivity.this, articleDataBaseRsp.errorMsg);
                        }
                    }
                });
    }

    private void showArticle(BaseRsp<ArticleData> articleDataBaseRsp) {
        if (articleDataBaseRsp.data.datas == null || articleDataBaseRsp.data.datas.size() == 0) {
            ToastUtil.show(this, "没有数据！");
            return;
        }
        articleList.clear();
        articleList.addAll(articleDataBaseRsp.data.datas);
        articleAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
