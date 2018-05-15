package com.hxl.handroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hxl.handroid.R;
import com.hxl.handroid.app.App;
import com.hxl.handroid.app.AppConstant;
import com.hxl.handroid.base.BaseActivity;
import com.hxl.handroid.ui.fragment.CollectFragment;
import com.hxl.handroid.ui.fragment.KnowledgeFragment;
import com.hxl.handroid.util.SPUtil;
import com.hxl.handroid.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.magic_indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private TextView tvName;
    private List<String> mTitleDataList;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initFragment();
        initIndicator();
        initViewPager();
        initNavigation();
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

    private void initNavigation() {
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_android:
                    ToastUtil.show(MainActivity.this, "one");
                    break;
                case R.id.menu_collect:
                    startActivity(new Intent(MainActivity.this, CollectActivity.class));
                    break;
            }
            drawerLayout.closeDrawers();  //关闭抽屉
            return true;
        });
    }

    private void initFragment() {
        mFragments.add(KnowledgeFragment.newInstance());
        mFragments.add(CollectFragment.newInstance());
        mFragments.add(KnowledgeFragment.newInstance());
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mViewPager.setOffscreenPageLimit(3);
    }

    private void initIndicator() {
        mTitleDataList = new ArrayList<>();
        mTitleDataList.add("推荐");
        mTitleDataList.add("我的");
        mTitleDataList.add("电台");
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setFollowTouch(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int i) {

                SimplePagerTitleView view = new ColorTransitionPagerTitleView(context);
                view.setTextSize(14);
                view.setNormalColor(Color.parseColor("#50FFFFFF"));
                view.setSelectedColor(Color.WHITE);
                view.setText(mTitleDataList.get(i));
                view.setOnClickListener(v -> mViewPager.setCurrentItem(i));

                return view;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);
    }


    /**
     * 设置账号信息
     */
    public void setUser() {
        if (AppConstant.isLogin) {
            tvName.setText((String) SPUtil.get(App.getApplication(), AppConstant.USERNAME, ""));
        } else {
            tvName.setText("未登录");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.iv_menu, R.id.iv_home, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                //打开侧滑菜单
                drawerLayout.openDrawer(Gravity.START, true);
                break;
            case R.id.iv_home:
                break;
            case R.id.iv_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
    }
}
