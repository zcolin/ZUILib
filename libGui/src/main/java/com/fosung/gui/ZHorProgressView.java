/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fosung.frame.utils.DisplayUtil;


/**
 * 自定义的显示百分比的View
 */
public class ZHorProgressView extends TextView {

    protected Paint paint = new Paint();
    private int fullColor;                //底色
    private int progressColor;            //进度条颜色
    private int progress;                //进度
    private int progressHeight;          //进度条的高度
    private int height;                  //View的高度

    public ZHorProgressView(Context context) {
        this(context, null);
    }

    public ZHorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int defaultWidth = DisplayUtil.dip2px(context, 100);
        setWidth(defaultWidth);
        progressHeight = DisplayUtil.dip2px(context, 3);
        fullColor = Color.rgb(153, 153, 153);
        progressColor = Color.rgb(0, 199, 229);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (height == 0) {
            height = getHeight();
        }

        if (height > 0) {
            paint.setAntiAlias(true);
            paint.setStrokeWidth(progressHeight);
            paint.setColor(fullColor);
            canvas.drawLine(0, height / 2, getWidth() / 3 * 2, height / 2, paint);
            paint.setColor(progressColor);
            canvas.drawLine(0, height / 2, getWidth() / 3 * 2 * progress / 100, height / 2, paint);
        }
    }

    /**
     * 设置进度条颜色
     *
     * @param fullColor     底色
     * @param progressColor 进度条色
     */
    public ZHorProgressView setProgressColor(@ColorInt int fullColor, @ColorInt int progressColor) {
        this.fullColor = fullColor;
        this.progressColor = progressColor;
        invalidate();
        return this;
    }

    /**
     * 设置进度条高度
     *
     * @param progressHeight 进度条高度，单位DP
     */
    public ZHorProgressView setProgressHeight(int progressHeight) {
        this.progressHeight = DisplayUtil.dip2px(getContext(), progressHeight);
        invalidate();
        return this;
    }

    /**
     * 设置进度
     *
     * @param p 百分比
     */
    public void setProgress(int p) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        } else {
            progress = p;
        }

    }

    /**
     * 设置进度，同时设置文字
     */
    public void setProgressWithText(int p) {
        setProgress(p);
        setText(p + "%");
    }
}
