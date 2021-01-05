/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     19-1-29 上午9:54
 * ********************************************************
 */
package com.zcolin.gui;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcolin.gui.helper.ZUIHelper;


/**
 * 图片详情查看页面，使用viewpager滑动
 */
public class ZDialogPhotoDetail extends ZDialog<ZDialogPhotoDetail> {
    private PhotoBean photoBean;
    private ZViewPager viewPager;
    private TextView tvLeft;
    private TextView tvNum;

    public ZDialogPhotoDetail(Activity context, PhotoBean photoBean) {
        super(context, R.layout.gui_dlg_photodetail);
        setLayout(ZUIHelper.getScreenWidth(context), ZUIHelper.getScreenHeight(context));
        this.photoBean = photoBean;

        viewPager = getView(R.id.view_pager);
        viewPager.setCatchTouchException(true);
        tvNum = getView(R.id.tv_num);
        tvLeft = findViewById(R.id.btn_left);

        resizeHeaderLayout(context);
        tvLeft.setOnClickListener(v -> dismiss());
        initData();
    }

    private void resizeHeaderLayout(Activity context) {
        RelativeLayout rlTab = getView(R.id.rl_header);
        int statusBarHeight = ZUIHelper.getStatusBarHeight(context);
        rlTab.setPadding(0, statusBarHeight, 0, 0);
        rlTab.getLayoutParams().height += statusBarHeight;
    }

    private void initData() {
        viewPager.setAdapter(new PhotoPagerAdapter(photoBean.images));
        viewPager.setOnPageChangeListener(new PagerLinstener());
        int index = photoBean.index;
        viewPager.setCurrentItem(index, false);
        if (index == 0) {
            tvNum.setText((index + 1) + "/" + photoBean.images.length);
        }
    }

    class PagerLinstener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            tvNum.setText((position + 1) + "/" + photoBean.images.length);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class PhotoPagerAdapter extends PagerAdapter {
        private String[] lstData;

        public PhotoPagerAdapter(String[] lstData) {
            this.lstData = lstData;
        }

        @Override
        public int getCount() {
            return lstData.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0.equals(arg1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZoomImageView imageView = new ZoomImageView(container.getContext());
            container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(container.getContext()).load(lstData[position]).into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            final View view = (View) object;
            container.removeView(view);
        }
    }

    public static class PhotoBean {
        public String[] images;
        public int index;
    }
}
