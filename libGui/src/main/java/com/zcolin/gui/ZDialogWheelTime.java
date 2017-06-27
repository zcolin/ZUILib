/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-2-24 下午3:54
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcolin.gui.wheelview.adapters.AbstractWheelTextAdapter;
import com.zcolin.gui.wheelview.views.OnWheelChangedListener;
import com.zcolin.gui.wheelview.views.OnWheelScrollListener;
import com.zcolin.gui.wheelview.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日期选择对话框
 */
public class ZDialogWheelTime extends ZDialog<ZDialogWheelTime> implements View.OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
    private static int LAYOUT_ID;

    protected int maxTextSize = 18;
    protected int minTextSize = 12;
    protected WheelView wvHour;
    protected WheelView wvMin;
    protected int       maxTextColor;
    protected int       minTextColor;

    protected View     vChangeBirthChild;
    protected TextView btnSure;
    protected TextView btnCancel;
    protected TextView tvTitle;     // 标题文字

    protected ArrayList<String> array_hours = new ArrayList<String>();
    protected ArrayList<String> arry_mins   = new ArrayList<String>();
    protected CalendarTextAdapter mHourAdapter;
    protected CalendarTextAdapter mMinAdapter;

    protected int currentHour = -1;
    protected int currentMin  = -1;

    protected OnTimeSubmitListener onTimeSubmitListener;

    public static ZDialogWheelTime instance(Context context) {
        return new ZDialogWheelTime(context);
    }

    public static ZDialogWheelTime instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogWheelTime(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogWheelTime(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogWheelTime(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_wheel_time : LAYOUT_ID) : layoutId);
        init(context);
    }

    private void init(Context context) {
        maxTextColor = context.getResources()
                              .getColor(R.color.gui_dlg_wheel_val);
        minTextColor = context.getResources()
                              .getColor(R.color.gui_divider);
        initHours();
        initMinutes();

        wvHour = (WheelView) findViewById(R.id.wv_hour);
        wvMin = (WheelView) findViewById(R.id.wv_min);

        int[] shadowsColors = new int[]{0x00000000, 0x00000000, 0x00000000};
        wvHour.setShadowsColors(shadowsColors);
        wvMin.setShadowsColors(shadowsColors);

        vChangeBirthChild = findViewById(R.id.ly_myinfo_changebirth_child);
        btnSure = (TextView) findViewById(R.id.dialog_okbutton);
        btnCancel = (TextView) findViewById(R.id.dialog_cancelbutton);
        tvTitle = (TextView) findViewById(R.id.dialog_tilte);

        vChangeBirthChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void prepareShow() {
        Calendar c = Calendar.getInstance();
        this.currentHour = currentHour == -1 ? c.get(Calendar.HOUR) : 0;
        this.currentMin = currentMin == -1 ? c.get(Calendar.MINUTE) : 0;
        int selectedHour = getHourIndex(currentHour);
        mHourAdapter = new CalendarTextAdapter(getContext(), array_hours, selectedHour);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(mHourAdapter);
        wvHour.setCurrentItem(selectedHour);

        int selectedMin = getMinIndex(currentMin);
        mMinAdapter = new CalendarTextAdapter(getContext(), arry_mins, selectedMin);
        wvMin.setVisibleItems(5);
        wvMin.setViewAdapter(mMinAdapter);
        wvMin.setCurrentItem(selectedMin);

        wvHour.addChangingListener(this);
        wvHour.addScrollingListener(this);
        wvMin.addChangingListener(this);
        wvMin.addScrollingListener(this);
    }

    @Override
    public void show() {
        prepareShow();
        super.show();
    }

    /**
     * 设置标题
     */
    public ZDialogWheelTime setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置默认选中
     */
    public ZDialogWheelTime setDefSelected(int hour, int minute) {
        this.currentHour = hour;
        this.currentMin = minute;
        return this;
    }

    /**
     * 设置日期设置回调
     */
    public ZDialogWheelTime setDataSubmitListener(OnTimeSubmitListener onDateSubmitListener) {
        this.onTimeSubmitListener = onDateSubmitListener;
        return this;
    }

    /**
     * 设置上下阴影
     */
    public ZDialogWheelTime setShadowColors(int[] shadowColors) {
        if (shadowColors != null && shadowColors.length >= 3) {
            wvHour.setShadowsColors(shadowColors);
            wvMin.setShadowsColors(shadowColors);
        }
        return this;
    }

    /**
     * 设置选中文字的字体大小
     */
    public ZDialogWheelTime setMaxTextSize(int maxTextSize) {
        this.maxTextSize = maxTextSize;
        return this;
    }

    /**
     * 设置未选中文字的字体大小
     */
    public ZDialogWheelTime setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
        return this;
    }

    /**
     * 设置选中文字的字体颜色
     */
    public ZDialogWheelTime setMaxTextColor(int maxTextColor) {
        this.maxTextColor = maxTextColor;
        return this;
    }

    /**
     * 设置未选中文字的字体颜色
     */
    public ZDialogWheelTime setMinTextColor(int minTextColor) {
        this.minTextColor = minTextColor;
        return this;
    }

    /**
     * 设置中线颜色
     */
    public ZDialogWheelTime setCenterLineColor(@ColorInt int color) {
        wvHour.setCenterLineColor(color);
        wvMin.setCenterLineColor(color);
        return this;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wvHour) {
            String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mHourAdapter);
            currentHour = Integer.parseInt(currentText);
        } else if (wheel == wvMin) {
            String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mMinAdapter);
            currentMin = Integer.parseInt(currentText);
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        if (wheel == wvHour) {
            String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mHourAdapter);
        } else if (wheel == wvMin) {
            String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mMinAdapter);
        }
    }

    private void initHours() {
        for (int i = 0; i <= 23; i++) {
            array_hours.add(String.format("%02d", i));
        }
    }

    private void initMinutes() {
        for (int i = 0; i <= 59; i++) {
            arry_mins.add(String.format("%02d", i));
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem) {
            super(context, R.layout.gui_item_year_date, NO_RESOURCE, currentItem, maxTextSize, minTextSize, maxTextColor, minTextColor);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            return super.getItem(index, cachedView, parent);
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }


    @Override
    public void onClick(View v) {

        if (v == btnSure) {
            if (onTimeSubmitListener != null) {
                onTimeSubmitListener.onClick(currentHour, currentMin);
            }
        } else if (v == vChangeBirthChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }


    /**
     * 设置字体大小
     */
    private void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText()
                                 .toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
                textvew.setTextColor(maxTextColor);
            } else {
                textvew.setTextSize(minTextSize);
                textvew.setTextColor(minTextColor);
            }
        }
    }

    private int getHourIndex(int hour) {
        int index = 0;
        for (int i = 23; i >= 0; i--) {
            if (i == hour) {
                return index;
            }
            index++;
        }
        return index;
    }

    private int getMinIndex(int min) {
        int index = 0;
        for (int i = 59; i >= 0; i--) {
            if (i == min) {
                return index;
            }
            index++;
        }
        return index;
    }


    public interface OnTimeSubmitListener {
        void onClick(int hour, int min);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (mHourAdapter != null && wvHour != null) {
            String currentText = (String) mHourAdapter.getItemText(wvHour.getCurrentItem());
            setTextviewSize(currentText, mHourAdapter);
        }

        if (mMinAdapter != null && wvMin != null) {
            String currentText = (String) mMinAdapter.getItemText(wvMin.getCurrentItem());
            setTextviewSize(currentText, mMinAdapter);
        }
    }
}