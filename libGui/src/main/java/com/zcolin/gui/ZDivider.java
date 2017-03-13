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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;


/**
 * 分隔线
 */
public class ZDivider extends View {

    public int height;
    public int width;

    public ZDivider(Context context) {
        this(context, null, 0);
    }

    public ZDivider(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public ZDivider(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.ZDivider, defStyle, 0);
        width = (int) a.getDimension(R.styleable.ZDivider_zdivider_width_size, 0);
        height = (int) a.getDimension(R.styleable.ZDivider_zdivider_height_size, 1);
        int color = a.getColor(R.styleable.ZDivider_zdivider_color, getResources().getColor(R.color.gui_divider));
        a.recycle();

        setBackgroundColor(color);
    }

    public ZDivider setHeight(int height) {
        this.height = height;
        return this;
    }

    public ZDivider setWidth(int width) {
        this.width = width;
        return this;
    }

    public ZDivider setColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        if (width > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}