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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 多选框组合弹出框
 */
public class ZDialogCheckBox extends ZDialog<ZDialogCheckBox> implements OnClickListener {
    private static int LAYOUT_ID;

    protected TextView                                        tvTitle;        //标题
    protected ZDialogParamSubmitInterface<ArrayList<String>>  valueSubmitInter;    // 点击确定按钮回调接口
    protected ZDialogParamSubmitInterface<ArrayList<Integer>> positionSubmitInter;    // 点击确定按钮回调接口
    protected CheckBox[]                                      arrCheckBox;    //CheckBox数组
    protected LinearLayout                                    llCheckBox;    //CheckBox视图集合
    protected TextView                                        tvCancel;
    protected TextView                                        tvSubmit;

    public static ZDialogCheckBox instance(Context context) {
        return new ZDialogCheckBox(context);
    }

    public static ZDialogCheckBox instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogCheckBox(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogCheckBox(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogCheckBox(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_checkboxgroup : LAYOUT_ID) : layoutId);
        initRes();
    }

    private void initRes() {
        tvTitle = getView(R.id.dialogcheckboxgroup_title);
        llCheckBox = getView(R.id.dialogcheckboxgroup_ll);
        tvCancel = getView(R.id.dialog_cancelbutton);
        tvSubmit = getView(R.id.dialog_okbutton);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    public ZDialogCheckBox setTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(str);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    public ZDialogCheckBox setDatas(ArrayList<String> listData, ArrayList<String> arrSelectedStr) {
        setDatas(listData.toArray(new String[listData.size()]), arrSelectedStr);
        return this;
    }

    public ZDialogCheckBox setDatas(String[] arrStr, ArrayList<String> arrSelectedStr) {
        int paddingVer = (int) getContext().getResources()
                                           .getDimension(R.dimen.gui_dimens_mid);
        int paddingHor = (int) getContext().getResources()
                                           .getDimension(R.dimen.gui_dimens_big);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.leftMargin = paddingHor;
        layout.rightMargin = paddingHor;

        llCheckBox.removeAllViews();
        arrCheckBox = new CheckBox[arrStr.length];
        for (int i = 0; i < arrStr.length; i++) {
            arrCheckBox[i] = new CheckBox(getContext());
            arrCheckBox[i].setText(arrStr[i]);
            arrCheckBox[i].setTextAppearance(getContext(), R.style.Gui_TextStyle_GrayDark_Normal);
            arrCheckBox[i].setPadding(0, paddingVer, 0, paddingVer);
            arrCheckBox[i].setBackgroundResource(R.drawable.gui_dlg_radio_selector);
            arrCheckBox[i].setId(i + 100);

            if (arrSelectedStr != null && arrSelectedStr.size() > 0) {
                for (String s : arrSelectedStr) {
                    if (arrStr[i].equals(s)) {
                        arrCheckBox[i].setChecked(true);
                        break;
                    }
                }
            }
            llCheckBox.addView(arrCheckBox[i], layout);
        }
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDialogCheckBox addValueSubmitListener(ZDialogParamSubmitInterface<ArrayList<String>> submitInter) {
        this.valueSubmitInter = submitInter;
        return this;
    }

    /**
     * 添加确定回调接口
     */
    public ZDialogCheckBox addPositionSubmitListener(ZDialogParamSubmitInterface<ArrayList<Integer>> submitInter) {
        this.positionSubmitInter = submitInter;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_okbutton) {
            if (valueSubmitInter != null) {
                if (arrCheckBox != null) {
                    ArrayList<String> list = new ArrayList<>();
                    for (CheckBox anArrCheckBox : arrCheckBox) {
                        if (anArrCheckBox.isChecked()) {
                            list.add(anArrCheckBox.getText()
                                                  .toString());
                        }
                    }
                    valueSubmitInter.submit(list);
                }
                cancel();
            }

            if (positionSubmitInter != null) {
                if (arrCheckBox != null) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 0; i < arrCheckBox.length; i++) {
                        if (arrCheckBox[i].isChecked()) {
                            list.add(i);
                        }
                    }
                    positionSubmitInter.submit(list);
                }
                cancel();
            }
        } else if (v.getId() == R.id.dialog_cancelbutton) {
            cancel();
        }
    }
}
