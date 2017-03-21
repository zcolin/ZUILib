/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-3-21 下午2:01
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zcolin.gui.helper.ZUIHelper;

/**
 * 圆点指示器view
 */
public class ZIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private int mIndicatorSelectedResId;
    private int mIndicatorUnselectedResId;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int count;
    private int startPosition; //无限循环，起始点

    public ZIndicator(Context context) {
        this(context, null);
    }

    public ZIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZIndicator, defStyle, 0);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.ZIndicator_zindicator_width, ZUIHelper.dip2px(context, 5));
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.ZIndicator_zindicator_height, ZUIHelper.dip2px(context, 5));
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.ZIndicator_zindicator_margin, ZUIHelper.dip2px(context, 2));
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.ZIndicator_zindicator_drawable_selected, R.drawable.gui_banner_gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.ZIndicator_zindicator_drawable_unselected, R.drawable.gui_banner_white_radius);
        typedArray.recycle();
    }


    public void setUpWithViewPager(ViewPager pager) {
        if (pager != null) {
            if (pager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.count = pager.getAdapter()
                              .getCount();
            setUpWithViewPager(pager, 0, count);
        }
    }

    /**
     * 设定支持循环
     *
     * @param startPosition 初始位置
     * @param realCount     真实的数据数量
     */
    public void setUpWithViewPager(ViewPager pager, int startPosition, int realCount) {
        this.startPosition = startPosition;
        this.count = realCount;
        if (pager != null) {
            pager.addOnPageChangeListener(this);
        }

        createIndicator();
    }

    private void createIndicator() {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(getContext());
            LayoutParams params = new LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            addView(imageView, params);
        }
    }

    /**
     * <p>获取在屏保列表中的实际位置</p>
     * 为了无限轮播，location记录的是一个整体的位置，有可能非常大</br>
     * 所以要获取实际的位置。
     */
    public int getRealPosition(int position) {
        int midPosition = position - startPosition;
        if (midPosition >= 0) {
            return midPosition % count;
        } else {
            int i = Math.abs(midPosition) % count;
            return i == 0 ? 0 : count - i;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int realPosition = getRealPosition(position);
        for (int i = 0; i < count; i++) {
            if (realPosition == i) {
                ((ImageView) getChildAt(i)).setImageResource(mIndicatorSelectedResId);
            } else {
                ((ImageView) getChildAt(i)).setImageResource(mIndicatorUnselectedResId);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
