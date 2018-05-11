package com.hxl.handroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.hxl.handroid.R;
import com.hxl.handroid.app.App;
import com.hxl.handroid.util.LogUtils;
import com.hxl.handroid.util.ToastUtil;
import com.hxl.handroid.view.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Base
 * Created by Administrator on 2018/2/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = "BaseActivity";
    public static String LINK_URL = "link_url";
    public static String LINK_TITLE = "link_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        if(Build.VERSION.SDK_INT>=21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(getLayout());
        ButterKnife.bind(this);
        App.addActivity(this);
        setWindowStatusBarColor(this, R.color.colorPrimary);
        LogUtils.d("Activity:", TAG);
        initView();
    }

    protected abstract int getLayout();

    protected abstract void initView();

    //设置状态栏颜色
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        closeKeyboard();
        super.onBackPressed();
    }


    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();
        if (view != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onDestroy() {
        App.removeActivity(this);
        super.onDestroy();

    }

    protected LoadingDialog mLoadingDialog;

    public void showLoadingDialog() {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(BaseActivity.this);
                }

                mLoadingDialog.setMessage(getString(R.string.text_loading));
                mLoadingDialog.show();
//                    DialogUtil.showLoading(mContext);
            }
        });
    }

    public void showLoadingDialog(final String text) {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(BaseActivity.this);
                }

                mLoadingDialog.setMessage(text);
                mLoadingDialog.show();
//                    DialogUtil.showLoading(mContext, text);
            }
        });
    }

    public void hideLoadingDialog() {
        if (isDestroy()) {
            return;
        }
//        if (mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
//                DialogUtil.dismisLoading();
            }
        });
    }

    public void showToast(final String msg) {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(BaseActivity.this, msg + "");
            }
        });
    }

    protected boolean isDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this.isFinishing() || this.isDestroyed()) {
                return true;
            }
        } else {
            if (this.isFinishing()) {
                return true;
            }
        }
        return false;
    }

}
