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
import android.support.annotation.LayoutRes;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 普遍对话框，有一个确定按钮
 */
public class ZAlert extends ZDialog<ZAlert> implements OnClickListener {
    private static int LAYOUT_ID;

    protected ZDialogSubmitInterface submitInterface;    // 点击确定按钮回调接口
    protected TextView               tvOK;            // 确定按钮
    protected TextView               tvMessage;           // 消息内容
    protected TextView               tvTitle;

    public static ZAlert instance(Context context) {
        return new ZAlert(context);
    }

    public static ZAlert instance(Context context, @LayoutRes int layoutId) {
        return new ZAlert(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    /**
     * @param context
     */
    public ZAlert(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZAlert(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_alert : LAYOUT_ID) : layoutId);
        initRes();
    }

    /**
     * 设置标题
     */
    public ZAlert setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置内容
     */
    public ZAlert setMessage(String msg) {
        tvMessage.setText(msg);
        return this;
    }

    /**
     * 设置按钮文字
     */
    public ZAlert setBtnText(String text) {
        tvOK.setText(text);
        return this;
    }

    /**
     * 设置提示内容对齐方式
     */
    public ZAlert setMessageGravity(int gravity) {
        tvMessage.setGravity(gravity);
        return this;
    }

    /**
     * 追加字符串，会自动回车
     *
     * @param strMsg 要追加的内容
     */
    public ZAlert append(String strMsg) {
        String str = tvMessage.getText()
                              .toString();
        if (str.length() > 0) {
            if (!str.contains(strMsg)) {
                tvMessage.append("\n" + strMsg);
            }
        } else {
            tvMessage.setText(strMsg);
        }
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZAlert addSubmitListener(ZDialogSubmitInterface submitInterface) {
        this.submitInterface = submitInterface;
        return this;
    }

    @Override
    public void show() {
        if (tvTitle.getText()
                   .length() == 0) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }

        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v == tvOK) {
            cancel();
            if (submitInterface != null) {
                submitInterface.submit();
            }
        }
    }


    private void initRes() {
        TextView tvCancel = getView(R.id.dialog_cancelbutton);
        tvCancel.setVisibility(View.GONE);

        tvOK = getView(R.id.dialog_okbutton);
        tvMessage = getView(R.id.dialog_message);
        tvTitle = getView(R.id.dialog_tilte);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());

        tvOK.setOnClickListener(this);
    }
}
