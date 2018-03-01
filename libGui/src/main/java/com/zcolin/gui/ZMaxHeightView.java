/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-3-1 上午8:45
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zcolin.gui.helper.ZUIHelper;

/**
 * 先判断是否设定了mMaxHeight，如果设定了mMaxHeight，则直接使用mMaxHeight的值，
 * 如果没有设定mMaxHeight，则判断是否设定了mMaxRatio，如果设定了mMaxRatio的值 则使用此值与屏幕高度的乘积作为最高高度
 */
public class ZMaxHeightView extends RelativeLayout {

    private static final float DEFAULT_MAX_RATIO  = 0.6f;
    private static final float DEFAULT_MAX_HEIGHT = 0f;

    private float mMaxRatio  = DEFAULT_MAX_RATIO;
    private float mMaxHeight = DEFAULT_MAX_HEIGHT;

    public ZMaxHeightView(Context context) {
        super(context);
        init();
    }

    public ZMaxHeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ZMaxHeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZMaxHeightView);
        mMaxRatio = a.getFloat(R.styleable.ZMaxHeightView_zmaxheight_ratio, DEFAULT_MAX_RATIO);
        mMaxHeight = a.getDimension(R.styleable.ZMaxHeightView_zmaxheight, DEFAULT_MAX_HEIGHT);
        a.recycle();
    }

    private void init() {
        if (mMaxHeight <= 0) {
            mMaxHeight = mMaxRatio * ZUIHelper.getScreenHeight(getContext());
        } else {
            mMaxHeight = Math.min(mMaxHeight, mMaxRatio * ZUIHelper.getScreenHeight(getContext()));
        }
    }

    /**
     * 设置最大定高度占屏幕比例
     */
    public void setMaxHeightRatio(int ratio) {
        mMaxRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置最大定高度
     */
    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        init();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mMaxHeight > 0) {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            heightSize = heightSize <= mMaxHeight ? heightSize : (int) mMaxHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}