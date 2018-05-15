package com.hxl.handroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.flexbox.FlexboxLayout;
import com.hxl.handroid.R;
import com.hxl.handroid.app.AppConstant;
import com.hxl.handroid.base.ArticleData;
import com.hxl.handroid.base.BaseActivity;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.entity.SearchData;
import com.hxl.handroid.entity.SearchHotData;
import com.hxl.handroid.http.BaseSubscriber;
import com.hxl.handroid.http.RetrofitFactory;
import com.hxl.handroid.util.RxUtils;
import com.hxl.handroid.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator
 * on 2018/5/11 星期五.
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.flexboxLayout)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.et_search)
    EditText etSearch;

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        getSearchHot();
    }

    private void getSearchHot() {
        RetrofitFactory.getHttpService()
                .getSearchHotData()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<BaseRsp<List<SearchHotData>>>(this) {
                    @Override
                    public void onResponse(BaseRsp<List<SearchHotData>> listBaseRsp) {
                        if (listBaseRsp.errorCode == BaseRsp.SUCCESS) {
                            if (listBaseRsp.data.size() == 0) {
                                return;
                            }
                            List<SearchHotData> hotDataList = listBaseRsp.data;

                        } else {
                            ToastUtil.show(SearchActivity.this, listBaseRsp.errorMsg);
                        }
                    }
                });
    }


    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                search();
                break;
        }
    }

    private void search() {
        String searchKey = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchKey)) {
            return;
        }
        showLoadingDialog();
        RetrofitFactory.getHttpService()
                .getSearchResultData(0, searchKey)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<BaseRsp<ArticleData>>(this) {
                    @Override
                    public void onResponse(BaseRsp<ArticleData> searchDataBaseRsp) {
                        hideLoadingDialog();
                        if (searchDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                            intent.putExtra(AppConstant.ARTICLE_DATA, searchDataBaseRsp.data);
                            intent.putExtra(AppConstant.SEARCH_TITLE, searchKey);
                            startActivity(intent);
                        } else {
                            ToastUtil.show(SearchActivity.this, searchDataBaseRsp.errorMsg);
                        }
                    }
                });
    }
}
