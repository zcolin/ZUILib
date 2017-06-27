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
public class ZDialogWheelDate extends ZDialog<ZDialogWheelDate> implements View.OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
    private static int LAYOUT_ID;

    protected int     maxTextSize  = 18;
    protected int     minTextSize  = 12;
    protected int     maxYear      = 2050;
    protected int     minYear      = 1950;
    protected boolean isCurDateMax = false;//是否设置当前时间为最大时间
    protected int maxTextColor;
    protected int minTextColor;

    protected WheelView wvYear;
    protected WheelView wvMonth;
    protected WheelView wvDay;

    protected View     vChangeBirthChild;
    protected TextView btnSure;
    protected TextView btnCancel;
    protected TextView tvTitle;     // 标题文字

    protected ArrayList<String> arry_years  = new ArrayList<String>();
    protected ArrayList<String> arry_months = new ArrayList<String>();
    protected ArrayList<String> arry_days   = new ArrayList<String>();
    protected CalendarTextAdapter mYearAdapter;
    protected CalendarTextAdapter mMonthAdapter;
    protected CalendarTextAdapter mDaydapter;

    protected int month;
    protected int day;

    protected int currentYear  = getYear();
    protected int currentMonth = 1;
    protected int currentDay   = 1;


    protected boolean issetdata = false;

    protected OnDateSubmitListener onDateSubmitListener;

    public static ZDialogWheelDate instance(Context context) {
        return new ZDialogWheelDate(context);
    }

    public static ZDialogWheelDate instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogWheelDate(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogWheelDate(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogWheelDate(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_wheel_date : LAYOUT_ID) : layoutId);
        init(context);
    }

    private void init(Context context) {
        maxTextColor = context.getResources()
                              .getColor(R.color.gui_dlg_wheel_val);
        minTextColor = context.getResources()
                              .getColor(R.color.gui_divider);

        wvYear = (WheelView) findViewById(R.id.wv_birth_year);
        wvMonth = (WheelView) findViewById(R.id.wv_birth_month);
        wvDay = (WheelView) findViewById(R.id.wv_birth_day);

        int[] shadowsColors = new int[]{0x00000000, 0x00000000, 0x00000000};
        wvYear.setShadowsColors(shadowsColors);
        wvMonth.setShadowsColors(shadowsColors);
        wvDay.setShadowsColors(shadowsColors);

        vChangeBirthChild = findViewById(R.id.ly_myinfo_changebirth_child);
        btnSure = (TextView) findViewById(R.id.dialog_okbutton);
        btnCancel = (TextView) findViewById(R.id.dialog_cancelbutton);
        tvTitle = (TextView) findViewById(R.id.dialog_tilte);

        vChangeBirthChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void prepareShow() {
        if (!issetdata) {
            initData();
        }
        initYears(isCurDateMax ? getYear() : maxYear);
        int selectedYear = setYear(currentYear);
        mYearAdapter = new CalendarTextAdapter(getContext(), arry_years, selectedYear);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(selectedYear);

        initMonths(month);
        int selectedMonth = setMonth(currentMonth);
        mMonthAdapter = new CalendarTextAdapter(getContext(), arry_months, selectedMonth);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(selectedMonth);

        initDays(day);
        int selectedDay = currentDay - 1;
        mDaydapter = new CalendarTextAdapter(getContext(), arry_days, selectedDay);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(selectedDay);

        wvYear.addChangingListener(this);
        wvYear.addScrollingListener(this);
        wvMonth.addChangingListener(this);
        wvMonth.addScrollingListener(this);
        wvDay.addChangingListener(this);
        wvDay.addScrollingListener(this);
    }

    @Override
    public void show() {
        prepareShow();
        super.show();
    }

    /**
     * 设置标题
     */
    public ZDialogWheelDate setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置默认选中
     */
    public ZDialogWheelDate setDefSelected(int year, int month, int day) {
        setDate(year, month, day);
        return this;
    }

    /**
     * 设置日期设置回调
     */
    public ZDialogWheelDate setDataSubmitListener(OnDateSubmitListener onDateSubmitListener) {
        this.onDateSubmitListener = onDateSubmitListener;
        return this;
    }

    /**
     * 设置上下阴影
     */
    public ZDialogWheelDate setShadowColors(int[] shadowColors) {
        if (shadowColors != null && shadowColors.length >= 3) {
            wvYear.setShadowsColors(shadowColors);
            wvMonth.setShadowsColors(shadowColors);
            wvDay.setShadowsColors(shadowColors);
        }
        return this;
    }

    /**
     * 设置选中文字的字体大小
     */
    public ZDialogWheelDate setMaxTextSize(int maxTextSize) {
        this.maxTextSize = maxTextSize;
        return this;
    }

    /**
     * 设置未选中文字的字体大小
     */
    public ZDialogWheelDate setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
        return this;
    }

    /**
     * 设置选中文字的字体颜色
     */
    public ZDialogWheelDate setMaxTextColor(int maxTextColor) {
        this.maxTextColor = maxTextColor;
        return this;
    }

    /**
     * 设置未选中文字的字体颜色
     */
    public ZDialogWheelDate setMinTextColor(int minTextColor) {
        this.minTextColor = minTextColor;
        return this;
    }

    /**
     * 设置最大年份，默认为当前年
     */
    public ZDialogWheelDate setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        return this;
    }

    /**
     * 设置最小年份，默认为1950
     */
    public ZDialogWheelDate setMinYear(int minYear) {
        this.minYear = minYear;
        return this;
    }

    /**
     * 设置是否当前日期为最大值
     */
    public ZDialogWheelDate setIsCurDateMax(boolean curDateMax) {
        isCurDateMax = curDateMax;
        return this;
    }

    /**
     * 设置中线颜色
     */
    public ZDialogWheelDate setCenterLineColor(@ColorInt int color) {
        wvYear.setCenterLineColor(color);
        wvMonth.setCenterLineColor(color);
        wvDay.setCenterLineColor(color);
        return this;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wvYear) {
            String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mYearAdapter);
            currentYear = Integer.parseInt(currentText);
            setYear(currentYear);
            initMonths(month);
            mMonthAdapter = new CalendarTextAdapter(getContext(), arry_months, 0);
            wvMonth.setVisibleItems(5);
            wvMonth.setViewAdapter(mMonthAdapter);
            wvMonth.setCurrentItem(0);
        } else if (wheel == wvMonth) {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mMonthAdapter);
            currentMonth = Integer.parseInt(currentText);
            setMonth(currentMonth);
            initDays(day);
            mDaydapter = new CalendarTextAdapter(getContext(), arry_days, 0);
            wvDay.setVisibleItems(5);
            wvDay.setViewAdapter(mDaydapter);
            wvDay.setCurrentItem(0);
        } else if (wheel == wvDay) {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mDaydapter);
            currentDay = Integer.parseInt(currentText);
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        if (wheel == wvYear) {
            String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mYearAdapter);
        } else if (wheel == wvMonth) {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mMonthAdapter);
        } else if (wheel == wvDay) {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mDaydapter);
        }
    }

    private void initYears(int year) {
        for (int i = year; i > minYear; i--) {
            arry_years.add(i + "");
        }
    }

    private void initMonths(int months) {
        arry_months.clear();
        for (int i = 1; i <= months; i++) {
            arry_months.add(i + "");
        }
    }

    private void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= days; i++) {
            arry_days.add(i + "");
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
            if (onDateSubmitListener != null) {
                onDateSubmitListener.onClick(currentYear, currentMonth, currentDay);
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
    private void setTextViewStyle(String curriteItemText, CalendarTextAdapter adapter) {
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

    private int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    private int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    private int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    private void initData() {
        setDate(getYear(), getMonth(), getDay());
    }

    /**
     * 设置年月日
     */
    private void setDate(int year, int month, int day) {
        issetdata = true;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
        if (year == getYear() && isCurDateMax) {
            this.month = getMonth();
        } else {
            this.month = 12;
        }
        calDays(year, month);
    }

    /**
     * 设置年份
     */
    private int setYear(int year) {
        int topYear = isCurDateMax ? getYear() : maxYear;
        int yearIndex = 0;
        if (year != getYear() || !isCurDateMax) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }
        for (int i = topYear; i > minYear; i--) {
            if (i == year) {
                return yearIndex;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    /**
     * 设置月份
     */
    private int setMonth(int month) {
        int monthIndex = 0;
        calDays(currentYear, month);
        for (int i = 1; i < this.month; i++) {
            if (month == i) {
                return monthIndex;
            } else {
                monthIndex++;
            }
        }
        return monthIndex;
    }

    /**
     * 计算每月多少天
     */
    private void calDays(int year, int month) {
        boolean leayyear = false;
        leayyear = year % 4 == 0 && year % 100 != 0;
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29;
                    } else {
                        this.day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30;
                    break;
            }
        }

        if (year == getYear() && month == getMonth() && isCurDateMax) {
            this.day = getDay();
        }
    }


    public interface OnDateSubmitListener {
        void onClick(int year, int month, int day);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (mYearAdapter != null && wvYear != null) {
            String currentText = (String) mYearAdapter.getItemText(wvYear.getCurrentItem());
            setTextViewStyle(currentText, mYearAdapter);
        }

        if (mMonthAdapter != null && wvMonth != null) {
            String currentText = (String) mMonthAdapter.getItemText(wvMonth.getCurrentItem());
            setTextViewStyle(currentText, mMonthAdapter);
        }

        if (mDaydapter != null && wvDay != null) {
            String currentText = (String) mDaydapter.getItemText(wvDay.getCurrentItem());
            setTextViewStyle(currentText, mDaydapter);
        }
    }
}