package com.hxl.handroid.ui.adpater;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxl.handroid.R;
import com.hxl.handroid.base.ArticleData;

import java.util.List;

/**
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public class ArticleAdapter extends BaseQuickAdapter<ArticleData.Article, BaseViewHolder> {
    private Boolean mHasHeader;
    private Boolean mIsCollect;

    public ArticleAdapter(@Nullable List<ArticleData.Article> data, Boolean hasHeader, Boolean isCollect) {
        super(R.layout.item_article, data);
        this.mHasHeader = hasHeader;
        this.mIsCollect = isCollect;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleData.Article item) {
        int position;
        if (mHasHeader) {
            position = helper.getLayoutPosition() - 1;
        } else {
            position = helper.getLayoutPosition();
        }
        ArticleData.Article datas = getData().get(position);
        if (mIsCollect) {
            helper.setImageResource(R.id.iv_good, R.drawable.love_act);
        } else {
            if (datas.collect) {
                helper.setImageResource(R.id.iv_good, R.drawable.love_act);
            } else {
                helper.setImageResource(R.id.iv_good, R.drawable.love_normal);
            }
        }

        helper.setText(R.id.tv_author, datas.author)
                .setText(R.id.tv_title, datas.title)
                .setText(R.id.tv_flag, datas.chapterName)
                .setText(R.id.tv_time, datas.niceDate)
                .addOnClickListener(R.id.tv_flag)
                .addOnClickListener(R.id.iv_good);
    }
}
