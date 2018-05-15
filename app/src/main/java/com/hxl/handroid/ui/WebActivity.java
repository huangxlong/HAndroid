package com.hxl.handroid.ui;

import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxl.handroid.R;
import com.hxl.handroid.base.BaseActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private AgentWeb mAgentWeb;

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        String articleLink = getIntent().getExtras().getString(LINK_URL);
        String articleTitle = getIntent().getExtras().getString(LINK_TITLE);
        tvTitle.setText(Html.fromHtml(articleTitle));
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frameLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setMainFrameErrorView(R.layout.layout_webview_error, -1)
                .createAgentWeb()
                .ready()
                .go(articleLink);

        WebView mWebView = mAgentWeb.getWebCreator().getWebView();
        WebSettings mSettings = mWebView.getSettings();

        mSettings.setJavaScriptEnabled(true);
        mSettings.setSupportZoom(true);
        mSettings.setBuiltInZoomControls(true);
        //不显示缩放按钮
        mSettings.setDisplayZoomControls(false);
        //设置自适应屏幕，两者合用
        //将图片调整到适合WebView的大小
        mSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        mSettings.setLoadWithOverviewMode(true);
        //自适应屏幕
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }

    @OnClick(R.id.iv_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mAgentWeb.back()) {
            mAgentWeb.back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
