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
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 带有一个输入框的对话框 ，有两个按钮
 */
public class ZDialogEdit extends ZDialog<ZDialogEdit> implements OnClickListener {
    private static int LAYOUT_ID;

    protected TextView tvMakeSure;            // 确定按钮
    protected TextView tvCancel;              // 取消按钮
    protected TextView tvTitle;               // 消息内容
    protected EditText etEdit;                 // 编辑框

    private ZDialogParamSubmitInterface<String> submitInterface;
    private ZDialogCancelInterface              cancelInterface;

    public static ZDialogEdit instance(Context context) {
        return new ZDialogEdit(context);
    }

    public static ZDialogEdit instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogEdit(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogEdit(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogEdit(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_edit : LAYOUT_ID) : layoutId);
        initRes();
    }

    private void initRes() {
        tvMakeSure = getView(R.id.dialog_okbutton);
        tvCancel = getView(R.id.dialog_cancelbutton);
        tvTitle = getView(R.id.dlgeditone_title);
        etEdit = getView(R.id.dlgeditone_edit);
        etEdit.requestFocus();
        tvMakeSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public ZDialogEdit setTitle(String strTitle) {
        tvTitle.setText(strTitle);
        return this;
    }

    public ZDialogEdit setEditText(String str) {
        etEdit.setText(str);
        etEdit.setSelection(etEdit.getText().length());
        return this;
    }

    public ZDialogEdit setOkBtnText(String str) {
        tvMakeSure.setText(str == null ? "确定" : str);
        return this;
    }

    public ZDialogEdit setCancelBtnText(String str) {
        tvCancel.setText(str == null ? "取消" : str);
        return this;
    }

    public ZDialogEdit setHint(String str) {
        etEdit.setHint(str);
        return this;
    }

    /**
     * 设置说明文字
     */
    public ZDialogEdit setInstruction(CharSequence str) {
        TextView tv = getView(R.id.dlgeditone_instruction);
        if (str != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(str);
        } else {
            tv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置输入框最大输入长度
     */
    public ZDialogEdit setMaxLength(int length) {
        etEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        return this;
    }

    /**
     * 设置输入框限制的输入类型
     *
     * @param type @see InputType
     */
    public ZDialogEdit setInputType(int type) {
        etEdit.setInputType(type);
        return this;
    }

    /**
     * 输入框选择全部
     */
    public ZDialogEdit selectAll() {
        etEdit.selectAll();
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDialogEdit addSubmitStrListener(ZDialogParamSubmitInterface<String> submitInterface) {
        this.submitInterface = submitInterface;
        return this;
    }

    /**
     * 添加取消回调接口
     */
    public ZDialogEdit addCancelListener(ZDialogCancelInterface cancelInterface) {
        this.cancelInterface = cancelInterface;
        return this;
    }

    /**
     * 光标移到最后
     */
    public ZDialogEdit setSelectionEnd() {
        etEdit.setSelection(etEdit.getEditableText()
                                  .length());
        return this;
    }

    /**
     * 获取输入框对象
     */
    public EditText getEditText() {
        return etEdit;
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v == tvMakeSure) {
            if (submitInterface != null) {
                boolean flag = submitInterface.submit(etEdit.getText()
                                                            .toString());
                if (flag) {
                    cancel();
                }
            }
        } else if (v == tvCancel) {
            if (cancelInterface != null) {
                cancelInterface.cancel();
            } else {
                cancel();
            }
        }
    }
}
