package com.hxl.handroid.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hxl.handroid.R;
import com.hxl.handroid.app.AppConstant;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator
 * on 2018/5/14 星期一.
 */
public class SearchResultActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ArticleAdapter articleAdapter;
    private int currentPage = 0;
    private List<ArticleData.Article> articleList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        ArticleData articleData = (ArticleData) Objects.requireNonNull(getIntent().getExtras())
                .get(AppConstant.ARTICLE_DATA);
        String title = (String) getIntent().getExtras().get(AppConstant.SEARCH_TITLE);
        tvTitle.setText(title);

        articleList.clear();
        assert articleData != null;
        articleList.addAll(articleData.datas);
        articleAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        articleAdapter = new ArticleAdapter(articleList, false, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(articleAdapter);

        articleAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, WebActivity.class);
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
                                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                                    @Override
                                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                            article.collect = false;
                                            ToastUtil.show(SearchResultActivity.this, "取消收藏！");
                                            articleAdapter.notifyDataSetChanged();
                                        } else {
                                            ToastUtil.show(SearchResultActivity.this, articleDataBaseRsp.errorMsg);
                                        }
                                    }
                                });
                    } else {
                        //添加收藏
                        RetrofitFactory.getHttpService()
                                .addCollectArticle(article.id)
                                .compose(RxUtils.rxSchedulerHelper())
                                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                                    @Override
                                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                            article.collect = true;
                                            ToastUtil.show(SearchResultActivity.this, "收藏成功！");
                                            articleAdapter.notifyDataSetChanged();
                                        } else {
                                            if (articleDataBaseRsp.errorMsg.contains("登录")) {
                                                startActivityForResult(new Intent(SearchResultActivity.this, LoginActivity.class), AppConstant.LOGING_CODE);
                                            } else {
                                                ToastUtil.show(SearchResultActivity.this, articleDataBaseRsp.errorMsg);
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
            mRecyclerView.postDelayed(() -> {
                currentPage++;
                RetrofitFactory.getHttpService()
                        .getArticleList(currentPage)
                        .compose(RxUtils.rxSchedulerHelper())
                        .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                            @Override
                            public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                                if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                                    if (articleDataBaseRsp.data == null || articleDataBaseRsp.data.size == 0) {
                                        ToastUtil.show(SearchResultActivity.this, "没有数据！");
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
                                    ToastUtil.show(SearchResultActivity.this, articleDataBaseRsp.errorMsg);
                                }
                            }
                        });
            }, 500);
        }, mRecyclerView);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
