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
 * 可以设定最大宽高、最小宽高、固定宽高的容器
 * <p>
 * 同时设置最大高度和最大高度比例，以两个中间大的为准
 * 同时设置最小高度和最小高度比例，以两个中间小的为准（>0）
 * 同时设置最大宽度和最大宽度比例，以两个中间大的为准
 * 同时设置最小宽度和最小宽度比例，以两个中间小的为准（>0）
 * <p>
 * 若设置了固定宽度，直接使用固定宽度，最大和最小不在起作用
 * 若设置了固定高度，直接使用固定高度，最大和最小不在起作用
 * 固定宽度取固定宽度和固定宽度比例两个中间较大的值
 */
public class ZGroupView extends RelativeLayout {
    private float maxHeightRatio;
    private int   maxHeight;
    private float minHeightRatio;
    private int   minHeight;
    private float maxWidthRatio;
    private int   maxWidth;
    private float minWidthRatio;
    private int   minWidth;
    private float fixHeightRatio;
    private int   fixHeight;
    private float fixWidthRatio;
    private int   fixWidth;

    public ZGroupView(Context context) {
        super(context);
        init();
    }

    public ZGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ZGroupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZGroupView);
        maxHeightRatio = a.getFloat(R.styleable.ZGroupView_zmaxheight_ratio, 0);
        maxHeight = (int) a.getDimension(R.styleable.ZGroupView_zmaxheight, 0);
        minHeightRatio = a.getFloat(R.styleable.ZGroupView_zminheight_ratio, 0);
        minHeight = (int) a.getDimension(R.styleable.ZGroupView_zminheight, 0);
        maxWidthRatio = a.getFloat(R.styleable.ZGroupView_zmaxwidth_ratio, 0);
        maxWidth = (int) a.getDimension(R.styleable.ZGroupView_zmaxwidth, 0);
        minWidthRatio = a.getFloat(R.styleable.ZGroupView_zminwidth_ratio, 0);
        minWidth = (int) a.getDimension(R.styleable.ZGroupView_zminwidth, 0);
        fixHeightRatio = a.getFloat(R.styleable.ZGroupView_zfixheight_ratio, 0);
        fixHeight = (int) a.getDimension(R.styleable.ZGroupView_zfixheight, 0);
        fixWidthRatio = a.getFloat(R.styleable.ZGroupView_zfixwidth_ratio, 0);
        fixWidth = (int) a.getDimension(R.styleable.ZGroupView_zfixwidth, 0);
        a.recycle();
    }

    private void init() {
        maxHeight = (int) Math.max(maxHeight, maxHeightRatio * ZUIHelper.getScreenHeight(getContext()));
        if (minHeight > 0 && minHeightRatio > 0) {
            minHeight = (int) Math.min(minHeight, minHeightRatio * ZUIHelper.getScreenHeight(getContext()));
        } else if (minHeightRatio > 0) {
            minHeight = (int) (minHeightRatio * ZUIHelper.getScreenHeight(getContext()));
        }

        maxWidth = (int) Math.max(maxWidth, maxWidthRatio * ZUIHelper.getScreenWidth(getContext()));
        if (minWidth > 0 && minWidthRatio > 0) {
            minWidth = (int) Math.min(minWidth, minWidthRatio * ZUIHelper.getScreenWidth(getContext()));
        } else if (minWidthRatio > 0) {
            minWidth = (int) (minWidthRatio * ZUIHelper.getScreenWidth(getContext()));
        }

        fixWidth = (int) Math.max(fixWidth, fixWidthRatio * ZUIHelper.getScreenWidth(getContext()));
        fixHeight = (int) Math.max(fixHeight, fixHeightRatio * ZUIHelper.getScreenHeight(getContext()));
    }

    /**
     * 设置最大定高度占屏幕比例
     */
    public void setMaxHeightRatio(int ratio) {
        maxHeightRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置最大高度
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        init();
        requestLayout();
    }

    /**
     * 设置最大宽度占屏幕比例
     */
    public void setMaxWidthRatio(int ratio) {
        maxWidthRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        init();
        requestLayout();
    }

    /**
     * 设置最小高度占屏幕比例
     */
    public void setMinHeightRatio(int ratio) {
        minHeightRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置最小高度
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        init();
        requestLayout();
    }

    /**
     * 设置最小宽度占屏幕比例
     */
    public void setMinWidthRatio(int ratio) {
        minWidthRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置最小宽度
     */
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        init();
        requestLayout();
    }

    /**
     * 设置固定高度占屏幕比例
     */
    public void setFixHeightRatio(int ratio) {
        fixHeightRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置固定高度
     */
    public void setFixHeight(int height) {
        this.fixHeight = height;
        init();
        requestLayout();
    }

    /**
     * 设置固定宽度占屏幕比例
     */
    public void setFixWidthRatio(int ratio) {
        fixWidthRatio = ratio;
        init();
        requestLayout();
    }

    /**
     * 设置固定宽度
     */
    public void setFixWidth(int width) {
        this.fixWidth = width;
        init();
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        if (fixHeight > 0) {
            height = fixHeight;
        } else if (maxHeight > 0 && minHeight > 0) {
            height = Math.min(MeasureSpec.getSize(heightMeasureSpec), maxHeight);
            height = Math.max(height, minHeight);
        } else if (maxHeight > 0) {
            height = Math.min(MeasureSpec.getSize(heightMeasureSpec), maxHeight);
        } else if (minHeight > 0) {
            height = Math.max(MeasureSpec.getSize(heightMeasureSpec), minHeight);
        }

        if (height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
        }

        int width = 0;
        if (fixWidth > 0) {
            width = fixWidth;
        } else if (maxWidth > 0 && minWidth > 0) {
            width = Math.min(MeasureSpec.getSize(widthMeasureSpec), maxWidth);
            width = Math.max(width, minWidth);
        } else if (maxWidth > 0) {
            width = Math.min(MeasureSpec.getSize(widthMeasureSpec), maxWidth);
        } else if (minWidth > 0) {
            width = Math.max(MeasureSpec.getSize(widthMeasureSpec), minWidth);
        }
        if (width > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}