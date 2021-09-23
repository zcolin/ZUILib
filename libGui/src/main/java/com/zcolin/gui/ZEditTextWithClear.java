/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;


/**
 * 带有清除按钮的edittext
 */
public class ZEditTextWithClear extends AppCompatEditText {

    private int resDrawAble = R.drawable.gui_icon_edittext_clear;
    private int ableWidth;

    public ZEditTextWithClear(Context context) {
        this(context, null);
    }

    public ZEditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZEditTextWithClear(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resDrawAble, opts);
        ableWidth = opts.outWidth;

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setState();
            }
        });
        setState();
    }

    private void setState() {
        Drawable[] d = getCompoundDrawables();
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(d[0], d[1], null, d[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(d[0],
                                                    d[1],
                                                    getContext().getResources().getDrawable(resDrawAble),
                                                    d[3]);
        }
    }

    /**
     * 设置清除内容的图片
     */
    public ZEditTextWithClear setClearDrawable(@DrawableRes int resDrawAble) {
        this.resDrawAble = resDrawAble;
        setState();
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - ableWidth;
            if (rect.contains(eventX, eventY)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }
}
