package com.zcolin.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * <p>
 * 验证码倒计时控件
 * </p>
 *
 * @author TYR
 * @since 2021/9/15 9:58
 */
public final class ZCountdownView extends AppCompatTextView implements Runnable {

    /** 默认计时秒数 */
    private              int    mTotalSecond = 60;
    /** 秒数单位文本 */
    private static final String TIME_UNIT    = "S";

    /** 当前秒数 */
    private int          mCurrentSecond;
    /** 记录原有的文本 */
    private CharSequence mRecordText;

    public ZCountdownView(Context context) {
        super(context);
    }

    public ZCountdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZCountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开始倒计时
     */
    public void start() {
        mRecordText = getText();
        setEnabled(false);
        mCurrentSecond = mTotalSecond;
        post(this);
    }

    /**
     * 结束倒计时
     */
    public void stop() {
        setText(mRecordText);
        setEnabled(true);
    }

    /**
     * 设置倒计时总秒数
     */
    public void setTotalTime(int totalTime) {
        this.mTotalSecond = totalTime;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void run() {
        if (mCurrentSecond == 0) {
            stop();
            return;
        }
        mCurrentSecond--;
        setText(mCurrentSecond + " " + TIME_UNIT);
        postDelayed(this, 1_000L);
    }
}