/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-21 上午11:38
 * *********************************************************
 */

package com.fosung.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogProgress(Context context) {
        super(context);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(R.color.gui_transparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID == 0 ? R.layout.gui_view_progress : LAYOUT_ID);
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
