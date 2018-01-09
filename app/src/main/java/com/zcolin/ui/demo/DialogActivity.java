/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.ui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fosung.ui.R;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZAlert;
import com.zcolin.gui.ZConfirm;
import com.zcolin.gui.ZDialogCheckBox;
import com.zcolin.gui.ZDialogEdit;
import com.zcolin.gui.ZDialogMenu;
import com.zcolin.gui.ZDialogRadioGroup;
import com.zcolin.gui.ZDialogWheelDate;
import com.zcolin.gui.ZDialogWheelTime;

import java.util.ArrayList;

/**
 * 带JsBridge的webview的Demo
 */
public class DialogActivity extends FragmentActivity implements OnClickListener {
    private Activity     mActivity;
    private LinearLayout llContent;
    private ArrayList<Button> listButton = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        mActivity = this;

        init();
    }

    private void init() {
        llContent = findViewById(R.id.ll_content);
        listButton.add(addButton("ZAlert"));
        listButton.add(addButton("ZConfirm"));
        listButton.add(addButton("ZDlgRadioGroup"));
        listButton.add(addButton("ZDlgMenu"));
        listButton.add(addButton("ZDialogWheelDate"));
        listButton.add(addButton("ZDialogEdit"));
        listButton.add(addButton("ZDialogCheckBox"));
        listButton.add(addButton("ZDialogWheelTime"));

        for (Button btn : listButton) {
            btn.setOnClickListener(this);
        }
    }

    private Button addButton(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(mActivity);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        llContent.addView(button, params);
        return button;
    }

    @Override
    public void onClick(View v) {
        if (v == listButton.get(0)) {
            new ZAlert(mActivity).setTitle("ZAlert").setMessage("这是一个Alert").show();
        } else if (v == listButton.get(1)) {
            new ZConfirm(mActivity).setTitle("ZConfirm").setMessage("这是一个通用对话框").addSubmitListener(() -> {
                ToastUtil.toastShort("点击了确定");
                return true;
            }).show();
        } else if (v == listButton.get(2)) {
            final String[] arrt = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogRadioGroup(mActivity).setTitle("ZDialogRadioGroup").setDatas(arrt, "menu1").addSubmitListener(integer -> {
                ToastUtil.toastShort("选择了" + arrt[integer]);
                return true;
            }).show();
        } else if (v == listButton.get(3)) {
            final String[] arrt = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogMenu(mActivity).setTitle("ZDialogMenu").setDatas(arrt).addSubmitListener(integer -> {
                ToastUtil.toastShort("选择了" + arrt[integer]);
                return true;
            })
                                      //                    .setGravity(Gravity.BOTTOM)
                                      //                    .setLayout(ScreenUtil.getScreenWidth(mActivity),0)
                                      //                    .setAnim(R.style.style_anim_dialog_bottom)
                                      .show();
        } else if (v == listButton.get(4)) {
            new ZDialogWheelDate(mActivity).setTitle("ZDialogWheelDate")
                                           .setDataSubmitListener((year, month, day) -> ToastUtil.toastShort(year + "-" + month + "-" + day))
                                           .show();
        } else if (v == listButton.get(5)) {
            new ZDialogEdit(mActivity).setTitle("ZDialogEdit").setEditText("回填数据").addSubmitStrListener(s -> {
                ToastUtil.toastShort("输入框数据" + s);
                return true;
            }).show();
        } else if (v == listButton.get(6)) {
            final String[] arrStr = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogCheckBox(mActivity).setTitle("ZDialogCheckbBox").setDatas(arrStr, null).addValueSubmitListener(s -> {
                String str = "";
                for (String s1 : s) {
                    str += s1;
                    str += ",";
                }
                ToastUtil.toastShort("选中数据" + str);

                return true;
            }).show();
        } else if (v == listButton.get(7)) {
            new ZDialogWheelTime(mActivity).setTitle("ZDialogWheelTime")
                                           .setDataSubmitListener((hour, minute) -> ToastUtil.toastShort(hour + ":" + minute))
                                           .show();
        }
    }
}
