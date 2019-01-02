/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     19-1-2 上午11:03
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

/**
 * 滑动验证view
 */
public class ZSlideVerifyView extends RelativeLayout {
    private int errorScope = 5; // 误差范围
    private TextView          textView;
    private SeekBar           seekBar;
    private OnSuccessListener successListener;
    private int               thumbWidth;

    public ZSlideVerifyView(Context context) {
        super(context);
    }

    public ZSlideVerifyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textView = new TextView(context);
        textView.setText("请按住滑块，拖动到最右边");
        textView.setTextColor(Color.parseColor("#888888"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ZUIHelper.dip2px(context, 14));
        textView.setGravity(Gravity.CENTER);

        seekBar = new ValidationSeekBar(context);
        seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener());
        seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.gui_slideverify_bg));
        setThumb(getResources().getDrawable(R.drawable.gui_slideverify_thumb));
        seekBar.setDuplicateParentStateEnabled(true);//去掉seekbar按下效果
        seekBar.setMax(100);
        seekBar.setProgress(0);
        seekBar.setThumbOffset(-3);

        addView(seekBar, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(textView, params);
    }

    /**
     * 设置误差范围
     */
    public ZSlideVerifyView setErrorScope(int errorScope) {
        this.errorScope = errorScope;
        return this;
    }

    public ZSlideVerifyView setThumb(Drawable thumb) {
        thumbWidth = thumb.getIntrinsicWidth();
        seekBar.setThumb(thumb);
        return this;
    }

    public ZSlideVerifyView setProgressDrawable(Drawable bg) {
        seekBar.setProgressDrawable(bg);
        return this;
    }

    public ZSlideVerifyView addSuccessListener(OnSuccessListener successListener) {
        this.successListener = successListener;
        return this;
    }

    public boolean getSuccess() {
        return seekBar.getProgress() >= seekBar.getMax() - errorScope;
    }

    class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getProgress() >= seekBar.getMax() - errorScope) {
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.WHITE);
                textView.setText("完成验证");
            } else {
                textView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekBar.getProgress() < seekBar.getMax() - errorScope) {
                seekBar.setProgress(0);
                textView.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.GRAY);
                textView.setText("请按住滑块，拖动到最右边");
            } else {
                seekBar.setProgress(seekBar.getMax());
                if (successListener != null) {
                    successListener.onSuccess();
                }
            }
        }
    }

    public interface OnSuccessListener {
        void onSuccess();
    }


    /**
     * 屏蔽滑块之外触摸事件的seekbar
     */
    class ValidationSeekBar extends AppCompatSeekBar {
        private boolean clickStatus = true; //点击状态
        private int xDown;

        public ValidationSeekBar(Context context) {
            super(context);
        }

        public ValidationSeekBar(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            //过滤SeekBar的点击事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xDown = (int) event.getX();
                clickStatus = true;
                if (xDown > thumbWidth * 1.5) {
                    clickStatus = false;
                    return true;
                }
            }

            //过滤SeekBar的点击事件
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clickStatus = true;
                if (xDown > thumbWidth * 1.5) {
                    clickStatus = false;
                    return true;
                }
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!clickStatus) {
                    return true;
                }
            }
            return super.dispatchTouchEvent(event);
        }
    }
}