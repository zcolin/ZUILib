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

import com.zcolin.gui.helper.ZUIHelper;

/**
 * Dialog使用的固定高度的Scrollview
 */
class DialogFixHeightScrollView extends ScrollView {
    private int maxHeight;

    public DialogFixHeightScrollView(Context context) {
        super(context);
        init(context);
    }

    public DialogFixHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogFixHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        maxHeight = ZUIHelper.getScreenHeight(context) / 2;
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
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);

        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}