package com.zcolin.ui.demo.adapter;

import com.zcolin.ui.demo.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * 主页面 ViewPager适配器
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private MainActivity mainActivity;

    public MainPagerAdapter(MainActivity mainPresenter, FragmentManager fm) {
        super(fm);
        this.mainActivity = mainPresenter;
    }

    @Override
    public int getCount() {
        return MainActivity.TAB_POSITION.length;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mainActivity.getFragByPosition(arg0);
    }
}
