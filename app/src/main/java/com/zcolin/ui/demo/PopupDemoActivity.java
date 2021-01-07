package com.zcolin.ui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fosung.ui.R;
import com.zcolin.gui.helper.ZUIHelper;
import com.zcolin.gui.popup.ZListPopup;
import com.zcolin.gui.popup.ZPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * popup的示例
 */
public class PopupDemoActivity extends FragmentActivity {

    private Activity   mActivity;
    private ZPopup     mNormalPopup;
    private ZListPopup mListPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_demo);
        mActivity = this;

        Button btnPopup = findViewById(R.id.btn_popup);
        Button btnListPopup = findViewById(R.id.btn_list_popup);

        btnPopup.setOnClickListener(v -> {
            initNormalPopupIfNeed();
            mNormalPopup.setAnimStyle(ZPopup.ANIM_GROW_FROM_CENTER);
            mNormalPopup.setPreferredDirection(ZPopup.DIRECTION_TOP);
            mNormalPopup.show(v);
        });
        btnListPopup.setOnClickListener(v -> {
            initListPopupIfNeed();
            mListPopup.setAnimStyle(ZPopup.ANIM_GROW_FROM_CENTER);
            mListPopup.setPreferredDirection(ZPopup.DIRECTION_TOP);
            mListPopup.show(v);
        });
    }

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new ZPopup(mActivity, ZPopup.DIRECTION_NONE);
            TextView textView = new TextView(mActivity);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(ZUIHelper.dip2px(mActivity, 250), WRAP_CONTENT));
            textView.setLineSpacing(ZUIHelper.dip2px(mActivity, 5), 1.0f);
            int padding = ZUIHelper.dip2px(mActivity, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText("Popup 可以设置其位置以及显示和隐藏的动画");
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_mid));
            mNormalPopup.setContentView(textView);
            mNormalPopup.setOnDismissListener(() -> {
                // 处理业务逻辑
            });
        }
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {
            String[] listItems = new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5",};
            List<String> data = new ArrayList<>();
            Collections.addAll(data, listItems);
            ArrayAdapter adapter = new ArrayAdapter<>(mActivity, R.layout.simple_list_item, data);
            mListPopup = new ZListPopup(mActivity, ZPopup.DIRECTION_NONE, adapter);
            mListPopup.create(ZUIHelper.dip2px(mActivity, 250),
                              ZUIHelper.dip2px(mActivity, 200),
                              (adapterView, view, i, l) -> {
                                  Toast.makeText(mActivity, "Item " + (i + 1), Toast.LENGTH_SHORT).show();
                                  mListPopup.dismiss();
                              });
            mListPopup.setOnDismissListener(() -> {
                //处理业务逻辑
            });
        }
    }
}
