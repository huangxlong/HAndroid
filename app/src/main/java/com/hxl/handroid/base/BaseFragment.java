package com.hxl.handroid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import com.hxl.handroid.R;
import com.hxl.handroid.view.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/22.
 */

public abstract class BaseFragment extends Fragment {
    public static String LINK_URL = "link_url";
    public static String LINK_TITLE = "link_title";
    private Unbinder mBinder;
    protected Activity mContext;
    protected View rootView;
    protected long mClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), null);
        mBinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mBinder.unbind();
    }

    protected abstract int getLayout();

    protected abstract void initView();


    protected LoadingDialog mLoadingDialog;

    protected void showLoadingDialog() {
        mContext = getActivity();
        if (isValidContext(mContext)) {
            mContext.runOnUiThread(() -> {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(getActivity());
                }

                mLoadingDialog.setMessage(getString(R.string.text_loading));
                mLoadingDialog.show();
            });
        }
    }

    protected void showLoadingDialog(final String text) {
        mContext = getActivity();
        if (isValidContext(mContext)) {
            mContext.runOnUiThread(() -> {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new LoadingDialog(getActivity());
                }

                mLoadingDialog.setMessage(text);
                mLoadingDialog.show();
            });
        }
    }

    protected void hideLoadingDialog() {
        if (isValidContext(mContext)) {
            mContext.runOnUiThread(() -> {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
            });
        }
    }

    protected boolean isClickSoon() {
        if (mClickTime == 0) {
            mClickTime = System.currentTimeMillis();
            return false;
        } else if (System.currentTimeMillis() - mClickTime < 500) {
            return true;
        } else {
            mClickTime = System.currentTimeMillis();
        }
        return false;
    }

    protected boolean isValidContext(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(activity == null || activity.isDestroyed() || activity.isFinishing());
        } else {
            return !(activity == null || activity.isFinishing());
        }
    }


    /**
     * 关闭软键盘
     */
    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

    }

}
