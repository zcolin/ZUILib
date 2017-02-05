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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

/**
 * 自定义的显示百分比的View
 */
public class ZHorProgressView extends TextView {
    public static final int TEXTSTYLE_NONE     = 0;
    public static final int TEXTSTYLE_PROGRESS = 1;//百分比样好似
    public static final int TEXTSTYLE_NUMBER   = 2;//数字样式


    protected Paint paint = new Paint();
    private int fullColor;                //底色
    private int progressColor;            //进度条颜色
    private int progress;                //进度
    private int progressHeight;          //进度条的高度
    private int height;                  //View的高度
    private int maxProgress = 100;       //最大进度
    private int textMargin;              // 进度文字margin
    private int progressTextStyle = TEXTSTYLE_NONE;       // 进度文字样式

    public ZHorProgressView(Context context) {
        this(context, null);
    }

    public ZHorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.RIGHT | Gravity.CENTER);
        progressHeight = ZUIHelper.dip2px(context, 3);
        fullColor = Color.rgb(235, 235, 235);
        progressColor = Color.rgb(0, 199, 229);
        textMargin = ZUIHelper.dip2px(context, 10);
        if (getPaddingRight() == 0) {
            setPadding(getPaddingLeft(), getPaddingTop(), textMargin, getPaddingBottom());
        }
        if (getPaddingLeft() == 0) {
            setPadding(textMargin, getPaddingTop(), getPaddingRight(), getPaddingBottom());
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
        this.progressHeight = ZUIHelper.dip2px(getContext(), progressHeight);
        invalidate();
        return this;
    }

    /**
     * 设置最大进度
     */
    public ZHorProgressView setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    /**
     * 设置最大进度
     */
    public ZHorProgressView setProgressTextStyle(int progressTextStyle) {
        this.progressTextStyle = progressTextStyle;
        setProgress(progress);//设置一下初始文字
        return this;
    }


    /**
     * 设置进度
     */
    public void setProgress(int p) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > maxProgress) {
            progress = maxProgress;
        } else {
            progress = p;
        }

        if (progressTextStyle == TEXTSTYLE_NUMBER) {
            setText(p + "/" + maxProgress);
        } else if (progressTextStyle == TEXTSTYLE_PROGRESS) {
            setText(p + "%");
        }
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (height == 0) {
            height = getHeight();
        }

        if (height > 0) {
            float textWidth = 0;
            if (getText().length() > 0) {
                paint.setTextSize(getTextSize());
                textWidth = paint.measureText(getText().toString());
            }
            paint.setAntiAlias(true);
            paint.setStrokeWidth(progressHeight);
            paint.setColor(fullColor);
            float left = (getGravity() & Gravity.LEFT) == Gravity.LEFT ? textWidth + 2 * textMargin : textMargin;
            float right = (getGravity() & Gravity.RIGHT) == Gravity.RIGHT ? getWidth() - textWidth - 2 * textMargin : getWidth() - textMargin;
            float h = height / 2;
            if ((getGravity() & Gravity.TOP) == Gravity.TOP) {
                h = height - progressHeight;
            } else if ((getGravity() & Gravity.BOTTOM) == Gravity.BOTTOM) {
                h = progressHeight;
            }

            canvas.drawLine(left, h, right, h, paint);
            paint.setColor(progressColor);
            canvas.drawLine(left, h, left + right * progress / maxProgress, h, paint);
        }
    }
}
