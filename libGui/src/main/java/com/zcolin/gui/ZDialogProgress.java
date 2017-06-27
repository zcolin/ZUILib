/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 进度条封装类
 */
public class ZDialogProgress extends ProgressDialog {
    private static int LAYOUT_ID;

    protected ProgressBar  progressBar;
    protected TextView     tvMessage;
    protected CharSequence strMessage;
    protected Drawable     indeterminateDrawable;
    private   int          layoutId;

    public static ZDialogProgress instance(Context context) {
        return new ZDialogProgress(context);
    }

    public static ZDialogProgress instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogProgress(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogProgress(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogProgress(Context context, @LayoutRes int layoutId) {
        super(context);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(R.color.gui_transparent);
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_view_progress : LAYOUT_ID) : layoutId);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                tvMessage = (TextView) findViewById(R.id.progressBar_tv);
                progressBar = (ProgressBar) findViewById(R.id.progressBar_pg);
                if (indeterminateDrawable != null) {
                    progressBar.setIndeterminateDrawable(indeterminateDrawable);
                }
                setTextMessage(strMessage);
            }
        });
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setMessage(CharSequence message) {
        if (isShowing()) {
            setTextMessage(message);
        }
        strMessage = message;
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (isShowing() && d != null) {
            setIndeterminateDrawable(d);
        }
        indeterminateDrawable = d;
    }

    private void setTextMessage(CharSequence message) {
        if (tvMessage != null) {
            if (TextUtils.isEmpty(message)) {
                tvMessage.setVisibility(View.GONE);
            } else {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(message);
            }
        }
    }
}
