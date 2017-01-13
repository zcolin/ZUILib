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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fosung.gui.R;
import com.zcolin.gui.wheelview.adapters.AbstractWheelTextAdapter;
import com.zcolin.gui.wheelview.views.OnWheelChangedListener;
import com.zcolin.gui.wheelview.views.OnWheelScrollListener;
import com.zcolin.gui.wheelview.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日期选择对话框
 */
public class ZDialogWheelDate extends ZDialog implements View.OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
    private static int LAYOUT_ID;

    protected int TEXT_MAXSIZE = 18;
    protected int TEXT_MINSIZE = 12;

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


    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogWheelDate(Context context) {
        super(context, LAYOUT_ID == 0 ? R.layout.gui_dlg_wheel_date : LAYOUT_ID);
        init();
    }


    private void init() {
        wvYear = (WheelView) findViewById(R.id.wv_birth_year);
        wvMonth = (WheelView) findViewById(R.id.wv_birth_month);
        wvDay = (WheelView) findViewById(R.id.wv_birth_day);

        int[] shadowsColors = new int[]{0xeeefefef, 0xeaefefef, 0x33efefef};
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
        initYears();
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

    public ZDialogWheelDate setDefSelected(int year, int month, int day) {
        setDate(year, month, day);
        return this;
    }

    public ZDialogWheelDate setDataSubmitListener(OnDateSubmitListener onDateSubmitListener) {
        this.onDateSubmitListener = onDateSubmitListener;
        return this;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wvYear) {
            String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mYearAdapter);
            currentYear = Integer.parseInt(currentText);
            setYear(currentYear);
            initMonths(month);
            mMonthAdapter = new CalendarTextAdapter(getContext(), arry_months, 0);
            wvMonth.setVisibleItems(5);
            wvMonth.setViewAdapter(mMonthAdapter);
            wvMonth.setCurrentItem(0);
        } else if (wheel == wvMonth) {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mMonthAdapter);
            currentMonth = Integer.parseInt(currentText);
            setMonth(currentMonth);
            initDays(day);
            mDaydapter = new CalendarTextAdapter(getContext(), arry_days, 0);
            wvDay.setVisibleItems(5);
            wvDay.setViewAdapter(mDaydapter);
            wvDay.setCurrentItem(0);
        } else if (wheel == wvDay) {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mDaydapter);
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
            setTextviewSize(currentText, mYearAdapter);
        } else if (wheel == wvMonth) {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mMonthAdapter);
        } else if (wheel == wvDay) {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextviewSize(currentText, mDaydapter);
        }
    }

    private void initYears() {
        for (int i = getYear(); i > 1950; i--) {
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
            super(context, R.layout.gui_item_year_date, NO_RESOURCE, currentItem, TEXT_MAXSIZE, TEXT_MINSIZE);
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
    private void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText()
                                 .toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(TEXT_MAXSIZE);
            } else {
                textvew.setTextSize(TEXT_MINSIZE);
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
        this.currentDay = 1;
        this.currentMonth = 1;
    }

    /**
     * 设置年月日
     */
    private void setDate(int year, int month, int day) {
        issetdata = true;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
        if (year == getYear()) {
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
        int yearIndex = 0;
        if (year != getYear()) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }
        for (int i = getYear(); i > 1950; i--) {
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
        if (year == getYear() && month == getMonth()) {
            this.day = getDay();
        }
    }


    public interface OnDateSubmitListener {
        void onClick(int year, int month, int day);
    }
}