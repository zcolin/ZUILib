/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 可以设置滚动监听{@link #setScrollViewListener(ScrollViewListener)} 和 最大高度的{@link #setMaxHeight(int)} ScrollView
 */
public class ZScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private int maxHeight;

    public ZScrollView(Context context) {
        super(context);
    }

    public ZScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ZScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置Scrollview的最大高度
     */
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    /**
     * 设置Scrollview的最大高度
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }

        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * ScrollView的滚动监听
     */
    public interface ScrollViewListener {
        void onScrollChanged(ZScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}  