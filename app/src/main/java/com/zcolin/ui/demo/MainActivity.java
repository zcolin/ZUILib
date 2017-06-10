/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-23 上午10:36
 * ********************************************************
 */

package com.zcolin.ui.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.fosung.ui.R;
import com.zcolin.frame.app.BaseFrameFrag;
import com.zcolin.frame.util.DisplayUtil;
import com.zcolin.gui.ZIndicator;
import com.zcolin.gui.ZTabView;
import com.zcolin.gui.ZViewPager;
import com.zcolin.ui.demo.adapter.MainPagerAdapter;
import com.zcolin.ui.demo.fragment.ViewFragment;


/**
 * 程序主页面
 */
public class MainActivity extends AppCompatActivity {
    public static final int[] TAB_POSITION = new int[]{0, 1, 2};

    private BaseFrameFrag[] arrTabFrag = new BaseFrameFrag[TAB_POSITION.length];
    private ZViewPager mViewPager;
    private ZTabView   tabView;
    private ZIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRes();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initRes() {
        mViewPager = (ZViewPager) findViewById(R.id.view_pager);
        tabView = (ZTabView) findViewById(R.id.view_tabview);
        indicator = (ZIndicator) findViewById(R.id.indicator);
    }

    private void initData() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mainPagerAdapter);
        indicator.setUpWithViewPager(mViewPager);
        setUpTab();
    }

    public void setUpTab() {
        tabView.initAsTabIcon(mViewPager);
        tabView.setOnPageChangeListener(new MainPagerListener());

        tabView.addZTab(getNewTab("View"));
        tabView.addZTab(getNewTab("View"));
        tabView.addZTab(getNewTab("View"));
    }

    /*
     * 创建ZTab
     */
    private ZTabView.ZTab getNewTab(String str) {
        float textSize = getResources().getDimension(R.dimen.textsize_small);
        ZTabView.ZTab tab = tabView.getNewTextTab(str);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tab.setTextColor(getResources().getColorStateList(R.color.main_text_color_selector));
        int padding = DisplayUtil.dip2px(this, 10);
        tab.setPadding(padding, padding, padding, padding);
        tab.setBackgroundColor(getResources().getColor(R.color.blue_mid));
        return tab;
    }

    /**
     * 根据位置获取Frag
     *
     * @param pos frag在viewpager中的位置
     */
    public BaseFrameFrag getFragByPosition(int pos) {
        if (arrTabFrag[pos] == null) {
            arrTabFrag[pos] = getNewFragByPos(pos);
        }
        return arrTabFrag[pos];
    }

    /*
     * 根据传入的位置创建新的Frag
     */
    private BaseFrameFrag getNewFragByPos(int i) {
        BaseFrameFrag frag = null;
        if (i == TAB_POSITION[0]) {
            frag = ViewFragment.newInstance();
        } else if (i == TAB_POSITION[1]) {
            frag = ViewFragment.newInstance();
        } else if (i == TAB_POSITION[2]) {
            frag = ViewFragment.newInstance();
        }
        return frag;
    }

    /*
    * ViewPager监听类 
    */
    private class MainPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == TAB_POSITION[1]) {

            } else {

            }
        }
    }
}
