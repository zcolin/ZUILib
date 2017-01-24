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
import android.view.LayoutInflater;
import android.widget.LinearLayout;



/**
 * 对话框底部按钮通用控件
 */
public class ZDialogBottomView extends LinearLayout {
    private static int LAYOUT_ID;

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZDialogBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context)
                      .inflate(LAYOUT_ID == 0 ? R.layout.gui_dlg_bottomitem : LAYOUT_ID, this);
    }
}
