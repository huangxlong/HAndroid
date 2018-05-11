package com.hxl.handroid.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hxl.handroid.R;
import com.hxl.handroid.app.AppConstant;
import com.hxl.handroid.base.BaseActivity;
import com.hxl.handroid.entity.BaseRsp;
import com.hxl.handroid.entity.LoginData;
import com.hxl.handroid.http.BaseSubscriber;
import com.hxl.handroid.http.RetrofitFactory;
import com.hxl.handroid.util.RxUtils;
import com.hxl.handroid.util.SPUtil;
import com.hxl.handroid.util.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator
 * on 2018/5/10 星期四.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_user)
    MaterialEditText etUser;
    @BindView(R.id.et_psd)
    MaterialEditText etPsd;

    @Override
    protected int getLayout() {
        return R.layout.acitivity_login;
    }

    @Override
    protected void initView() {
        tvTitle.setText("用户登录");
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                onBackPressed();
                break;
            case R.id.btn_commit:
                login();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        showLoadingDialog();
        String user = etUser.getText().toString().trim();
        String psd = etPsd.getText().toString().trim();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(psd)) {
            hideLoadingDialog();
            ToastUtil.show(this, "用户名或密码不能为空");
            return;
        }
        RetrofitFactory.getHttpService()
                .login(user, psd)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseSubscriber<BaseRsp<LoginData>>(this) {
                    @Override
                    public void onResponse(BaseRsp<LoginData> loginDataBaseRsp) {
                        hideLoadingDialog();
                        if (loginDataBaseRsp.errorCode == BaseRsp.SUCCESS) {
                            LoginData data = loginDataBaseRsp.data;
                            SPUtil.put(LoginActivity.this, AppConstant.USERNAME, data.username);
                            SPUtil.put(LoginActivity.this, AppConstant.PASSWORD, data.password);
                            AppConstant.isLogin = true;
                            onBackPressed();
                        } else {
                            ToastUtil.show(LoginActivity.this, loginDataBaseRsp.errorMsg);
                        }
                    }
                });


    }
}
