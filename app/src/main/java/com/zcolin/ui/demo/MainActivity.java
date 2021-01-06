package com.zcolin.ui.demo;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fosung.ui.R;
import com.zcolin.frame.app.BaseFrameFrag;
import com.zcolin.frame.util.DisplayUtil;
import com.zcolin.gui.ZBadgeView;
import com.zcolin.gui.ZIndicator;
import com.zcolin.gui.ZTabView;
import com.zcolin.gui.ZViewPager;
import com.zcolin.ui.demo.adapter.MainPagerAdapter;
import com.zcolin.ui.demo.fragment.ViewFragment;

import androidx.appcompat.app.AppCompatActivity;


/**
 * 程序主页面
 */
public class MainActivity extends AppCompatActivity {
    public static final int[] TAB_POSITION = new int[]{0, 1, 2};

    private BaseFrameFrag[] arrTabFrag = new BaseFrameFrag[TAB_POSITION.length];
    private ZTabView        tabView;
    private ZViewPager      mViewPager;
    private ZIndicator      indicator;

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
        tabView = findViewById(R.id.view_tabview);
        mViewPager = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.indicator);
    }

    private void initData() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mainPagerAdapter);
        indicator.setUpWithViewPager(mViewPager);
        setUpTab();
    }

    public void setUpTab() {
        tabView.setUpViewPager(mViewPager);
        tabView.addZTab(getNewTab("View"));
        tabView.addZTab(getNewTab("View"));

        ZTabView.ZTab tab2 = getNewTab("View");
        RelativeLayout rlTab = new RelativeLayout(this);
        rlTab.addView(tab2,
                      new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                 ViewGroup.LayoutParams.WRAP_CONTENT));
        ZBadgeView badgeView = new ZBadgeView(this, tab2);
        badgeView.setText("55");
        badgeView.show();
        badgeView.setTextSize(10);
        tabView.addZTab(rlTab);
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
}
