package com.hxl.handroid.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hxl.handroid.R;
import com.hxl.handroid.base.ArticleData;
import com.hxl.handroid.base.BaseFragment;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.http.BaseSubscriber;
import com.hxl.handroid.http.RetrofitFactory;
import com.hxl.handroid.ui.adpater.ArticleAdapter;
import com.hxl.handroid.util.RxUtils;
import com.hxl.handroid.util.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator
 * on 2018/5/10 星期四.
 */
public class CollectFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipRefresh;
    private ArticleAdapter articleAdapter;

    public static CollectFragment newInstance() {
        return new CollectFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initView() {
        mSwipRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipRefresh.setRefreshing(true);
        getArticle();
        mSwipRefresh.setOnRefreshListener(this::getArticle);
    }

    private void getArticle() {
        RetrofitFactory.getHttpService()
                .getCollectArticle(0)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(mContext) {
                    @Override
                    public void onResponse(BaseRsp<ArticleData> articleDataBaseRsp) {
                        mSwipRefresh.setRefreshing(false);
                        if (articleDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                            showArticle(articleDataBaseRsp);
                        } else {
                            ToastUtil.show(mContext, articleDataBaseRsp.errorMsg);
                        }
                    }
                });
    }

    private void showArticle(BaseRsp<ArticleData> articleDataBaseRsp) {
        if (articleDataBaseRsp.data.datas == null) {
            ToastUtil.show(mContext, "没有数据！");
            return;
        }
        List<ArticleData.Article> articleList = articleDataBaseRsp.data.datas;
        articleAdapter = new ArticleAdapter(articleList, false,true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(articleAdapter);
    }
}
