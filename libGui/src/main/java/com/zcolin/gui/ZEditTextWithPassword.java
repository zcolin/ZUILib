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
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;



/**
 * 带有密码显示按钮的edittext
 */
public class ZEditTextWithPassword extends EditText {
    private int passwordDrawable = R.drawable.gui_icon_edittext_password;
    private int showDrawable     = R.drawable.gui_icon_edittext_password_s;

    private int drawableWidth;
    private boolean isShwoAsPossword = true;

    public ZEditTextWithPassword(Context context) {
        this(context, null);
    }

    public ZEditTextWithPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZEditTextWithPassword(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), passwordDrawable, opts);
        drawableWidth = opts.outWidth;

        setState(isShwoAsPossword);
    }

    /**
     * 设置密码显示隐藏开关图片
     *
     * @param passwordDrawable 密码状态的图片
     * @param showDrawable     显示文字状态的图片
     */
    public ZEditTextWithPassword setDrawable(@DrawableRes int passwordDrawable, @DrawableRes int showDrawable) {
        this.passwordDrawable = passwordDrawable;
        this.showDrawable = showDrawable;
        init();
        return this;
    }


    /**
     * 设置密码显示状态
     */
    public void setState(boolean isShwoAsPossword) {
        this.isShwoAsPossword = isShwoAsPossword;
        if (isShwoAsPossword) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, passwordDrawable, 0);
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, showDrawable, 0);
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - drawableWidth;
            if (rect.contains(eventX, eventY)) {
                setState(!isShwoAsPossword);
            }
        }
        return super.onTouchEvent(event);
    }
}
