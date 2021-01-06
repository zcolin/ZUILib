/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.gui;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以实现不接受触摸事件{@link #setCanScroll(boolean)}的viewpager
 */
public class ZViewPager extends ViewPager {

    private boolean isCanScroll = true;
    private boolean isCatch     = false;

    public ZViewPager(Context context) {
        super(context);
    }

    public ZViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void setCatchTouchException(boolean isCatch) {
        this.isCatch = isCatch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCatch) {
            try {
                return isCanScroll && super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return isCanScroll && super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCatch) {
            try {
                return isCanScroll && super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return isCanScroll && super.onTouchEvent(ev);
        }
    }
}