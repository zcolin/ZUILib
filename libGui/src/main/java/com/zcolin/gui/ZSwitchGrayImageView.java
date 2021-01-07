package com.zcolin.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 彩色图片、灰色图片幻想转换的图片显示控件（可以用于用户头像的上线下线）
 */
public class ZSwitchGrayImageView extends AppCompatImageView {

    private Paint   paint  = new Paint();
    private boolean isGray = false;

    public ZSwitchGrayImageView(Context context) {
        super(context);
        init();
    }

    public ZSwitchGrayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZSwitchGrayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ColorMatrix colorMatrix = new ColorMatrix();
        // 置灰
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isGray) {
            canvas.saveLayer(null, paint, Canvas.ALL_SAVE_FLAG);
            super.onDraw(canvas);
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 转为灰色
     */
    public void switchGray() {
        isGray = true;
        invalidate();
    }

    /**
     * 转为正常
     */
    public void switchNormal() {
        isGray = false;
        invalidate();
    }

    /**
     * 获取图片状态
     */
    public boolean getImageViewState() {
        return isGray;
    }
}
