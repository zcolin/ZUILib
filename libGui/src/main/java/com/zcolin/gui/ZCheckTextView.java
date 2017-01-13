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
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



/**
 * TextView重写，实现多选框功能
 */
public class ZCheckTextView extends TextView implements OnClickListener {

    private int     bg_p      = R.drawable.gui_checktextview_on;         // 选中的背景
    private int     bg        = R.drawable.gui_checktextview_off;        // 未选中的背景
    private boolean isChecked = false;
    private CheckedCallBack checkCallBack;                       // 单选选择按键时回调接口

    private int    checkTag;                                    // 标识
    private String str_p;                                       // 选中的文字
    private String str;                                        // 未选中的背景
    private boolean isCanCancel = true;                        // 选中之后是否可以再次点击取消

    public ZCheckTextView(Context context) {
        this(context, null);
    }

    public ZCheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setBackgroundResource(bg);
    }

    /**
     * 初始化资源
     */
    public void initRes(int bg, int bg_p) {
        this.bg = bg;
        this.bg_p = bg_p;
        setBackgroundResource(bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 初始化资源
     */
    public void initText(String str, String str_p) {
        this.str = str;
        this.str_p = str_p;
    }

    public void setCanCancel(boolean isCanCancel) {
        this.isCanCancel = isCanCancel;
    }

    public void addOnCheckedChangeListener(CheckedCallBack checkCallBack) {
        this.checkCallBack = checkCallBack;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if (isChecked) {
            setSelected(true);
            setBackgroundResource(bg_p);
            if (str_p != null)
                setText(str_p);
        } else {
            setSelected(false);
            setBackgroundResource(bg);
            if (str_p != null)
                setText(str);
        }
    }

    public void setCheckTag(int checkTag) {
        this.checkTag = checkTag;
    }

    @Override
    public void onClick(View v) {
        if (!isCanCancel) {
            if (isChecked) {
                return;
            }
        }
        setChecked(!isChecked);
        if (checkCallBack != null) {
            checkCallBack.onCheckCallBack(this, checkTag);
        }
    }

    public interface CheckedCallBack {

        void onCheckCallBack(ZCheckTextView checkTv, int checkTag);
    }
}
