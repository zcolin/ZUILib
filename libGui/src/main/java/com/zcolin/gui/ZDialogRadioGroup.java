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
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.LayoutRes;

/**
 * 单选框组合弹出框
 */
public class ZDialogRadioGroup extends ZDialog<ZDialogRadioGroup> implements View.OnClickListener {

    private static int LAYOUT_ID;

    protected ZDialogParamSubmitListener<Integer> submitInter;

    protected TextView   tvTitle;
    protected RadioGroup rgChoise;
    protected TextView   tvCancel;
    protected TextView   tvSubmit;

    public static ZDialogRadioGroup instance(Context context) {
        return new ZDialogRadioGroup(context);
    }

    public static ZDialogRadioGroup instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogRadioGroup(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogRadioGroup(Context context) {
        this(context, 0);
    }

    public ZDialogRadioGroup(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_radiogroup : LAYOUT_ID) : layoutId);
        initRes();
    }

    private void initRes() {
        tvTitle = getView(R.id.dialogradiogroup_title);
        rgChoise = getView(R.id.dialogradiogroup_radiogroup);
        tvCancel = getView(R.id.dialog_cancelbutton);
        tvSubmit = getView(R.id.dialog_okbutton);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public ZDialogRadioGroup setTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(str);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    public ZDialogRadioGroup setDatas(List<String> listData, String defStr) {
        setDatas(listData.toArray(new String[listData.size()]), defStr);
        return this;
    }

    public ZDialogRadioGroup setDatas(String[] attrStr, String defStr) {
        int paddingVer = (int) getContext().getResources().getDimension(R.dimen.gui_dimens_mid);
        int paddingHor = (int) getContext().getResources().getDimension(R.dimen.gui_dimens_big);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.leftMargin = paddingHor;
        layout.rightMargin = paddingHor;

        rgChoise.removeAllViews();
        for (int i = 0; i < attrStr.length; i++) {
            RadioButton btn = new RadioButton(getContext());
            btn.setText(attrStr[i]);
            btn.setTextAppearance(getContext(), R.style.Gui_TextStyle_GrayDark_Normal);
            btn.setPadding(0, paddingVer, 0, paddingVer);
            btn.setButtonDrawable(null);
            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.gui_dialog_radiogroup_selector, 0);
            btn.setCompoundDrawablePadding(paddingHor);
            btn.setBackground(null);
            btn.setId(i + 100);
            if (attrStr[i].equals(defStr)) {
                btn.setChecked(true);
            }
            rgChoise.addView(btn, layout);
        }
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDialogRadioGroup addSubmitListener(ZDialogParamSubmitListener<Integer> submitInter) {
        this.submitInter = submitInter;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_okbutton) {
            if (submitInter != null && rgChoise.getCheckedRadioButtonId() > 0) {
                submitInter.submit(rgChoise.getCheckedRadioButtonId() - 100);
                cancel();
            }
        } else if (v.getId() == R.id.dialog_cancelbutton) {
            cancel();
        }
    }
}
