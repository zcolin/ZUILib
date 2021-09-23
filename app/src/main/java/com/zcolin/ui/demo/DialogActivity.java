package com.zcolin.ui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fosung.ui.R;
import com.zcolin.gui.ZAlert;
import com.zcolin.gui.ZConfirm;
import com.zcolin.gui.ZDialogCheckBox;
import com.zcolin.gui.ZDialogEdit;
import com.zcolin.gui.ZDialogMenu;
import com.zcolin.gui.ZDialogRadioGroup;
import com.zcolin.gui.ZDialogWheelDate;
import com.zcolin.gui.ZDialogWheelDateAndTime;
import com.zcolin.gui.ZDialogWheelTime;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;

/**
 * 弹出框类型的View示例
 */
public class DialogActivity extends FragmentActivity implements OnClickListener {

    private Activity          mActivity;
    private LinearLayout      llContent;
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
        listButton.add(addButton("ZDialogEdit"));
        listButton.add(addButton("ZDialogCheckBox"));
        listButton.add(addButton("ZDialogWheelTime"));
        listButton.add(addButton("ZDialogWheelDate"));
        listButton.add(addButton("ZDialogWheelDateAndTime"));

        for (Button btn : listButton) {
            btn.setOnClickListener(this);
        }
    }

    private Button addButton(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                         ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(mActivity);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        llContent.addView(button, params);
        return button;
    }

    /**
     * 个位前面补零
     */
    public String fillInOneZero(int singleDigit) {
        return String.format("%02d", singleDigit);
    }

    @Override
    public void onClick(View v) {
        if (v == listButton.get(0)) {
            new ZAlert(mActivity).setTitle("ZAlert").setMessage("这是一个Alert").autoDismiss(false).show();
        } else if (v == listButton.get(1)) {
            new ZConfirm(mActivity).setTitle("ZConfirm").setMessage("这是一个通用对话框").addSubmitListener(() -> {
                Toast.makeText(mActivity, "点击了确定", Toast.LENGTH_SHORT).show();
                return true;
            }).show();
        } else if (v == listButton.get(2)) {
            final String[] arrt = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogRadioGroup(mActivity).setTitle("ZDialogRadioGroup")
                                            .setDatas(arrt, "menu1")
                                            .addSubmitListener(integer -> {
                                                Toast.makeText(mActivity, "选择了" + arrt[integer], Toast.LENGTH_SHORT)
                                                     .show();
                                                return true;
                                            })
                                            .show();
        } else if (v == listButton.get(3)) {
            final String[] arrt = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogMenu(mActivity).setTitle("ZDialogMenu").setDatas(arrt).addSubmitListener(integer -> {
                Toast.makeText(mActivity, "选择了" + arrt[integer], Toast.LENGTH_SHORT).show();
                return true;
            })
                                      //                    .setGravity(Gravity.BOTTOM)
                                      //                    .setLayout(ScreenUtil.getScreenWidth(mActivity),0)
                                      //                    .setAnim(R.style.style_anim_dialog_bottom)
                                      .show();
        } else if (v == listButton.get(4)) {
            new ZDialogEdit(mActivity).setTitle("ZDialogEdit").setEditText("回填数据").addSubmitStrListener(s -> {
                Toast.makeText(mActivity, "输入框数据" + s, Toast.LENGTH_SHORT).show();
                return true;
            }).show();
        } else if (v == listButton.get(5)) {
            final String[] arrStr = new String[]{"menu1", "menu2", "menu3", "menu4", "menu5"};
            new ZDialogCheckBox(mActivity).setTitle("ZDialogCheckBox")
                                          .setDatas(arrStr, null)
                                          .addValueSubmitListener(s -> {
                                              String str = "";
                                              for (String s1 : s) {
                                                  str += s1;
                                                  str += ",";
                                              }
                                              Toast.makeText(mActivity, "选中数据" + str, Toast.LENGTH_SHORT).show();
                                              return true;
                                          })
                                          .show();
        } else if (v == listButton.get(6)) {
            new ZDialogWheelTime(mActivity).setTitle("ZDialogWheelTime")
                                           .setDataSubmitListener((int hour, int minute) -> {
                                               Toast.makeText(mActivity,
                                                              fillInOneZero(hour) + ":" + fillInOneZero(minute),
                                                              Toast.LENGTH_SHORT).show();
                                           })
                                           .show();
        } else if (v == listButton.get(7)) {
            new ZDialogWheelDate(mActivity).setTitle("ZDialogWheelDate").setDataSubmitListener((year, month, day) -> {
                Toast.makeText(mActivity,
                               year + "-" + fillInOneZero(month) + "-" + fillInOneZero(day),
                               Toast.LENGTH_SHORT).show();
            }).show();
        } else if (v == listButton.get(8)) {
            new ZDialogWheelDateAndTime(mActivity).setTitle("ZdialogWheelDateAndTime")
                                                  .setDataSubmitListener((year, month, day, hour, minute) -> {
                                                      Toast.makeText(mActivity,
                                                                     year + "-" + fillInOneZero(month) + "-" + fillInOneZero(
                                                                             day) + " " + fillInOneZero(hour) + ":" + fillInOneZero(
                                                                             minute),
                                                                     Toast.LENGTH_SHORT).show();
                                                  })
                                                  .show();
        }
    }
}
