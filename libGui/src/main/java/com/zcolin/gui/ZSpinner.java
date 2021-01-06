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
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZSpinner extends AppCompatTextView implements View.OnClickListener {

    private List<String>         list;
    private OnItemSelectListener listener;
    private String               selectedText;

    public ZSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        setOnClickListener(this);
    }

    public void initData(String[] array) {
        initData(array, null);
    }

    public void initData(ArrayList<String> list) {
        initData(list, null);
    }

    public void initData(String[] array, String strDef) {
        list = new ArrayList<>();
        Collections.addAll(list, array);
        initData(list, strDef);
    }

    public void initData(List<String> list, String strDef) {
        this.list = list;
        this.selectedText = strDef;
        if (list != null && list.size() > 0) {
            setText(selectedText);
        }
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        ZDialogMenu.instance(getContext()).setDatas(list, selectedText).addSubmitListener(position -> {
            this.selectedText = list.get(position);
            setText(list.get(position));
            if (listener != null) {
                listener.onItemClick(position, list.get(position));
            }
            return true;
        }).show();
    }

    public List<String> getList() {
        return list;
    }

    public interface OnItemSelectListener {
        void onItemClick(int position, String str);
    }
}
