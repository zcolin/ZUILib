package com.zcolin.gui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcolin.gui.wheelview.adapters.AbstractWheelTextAdapter;
import com.zcolin.gui.wheelview.views.OnWheelChangedListener;
import com.zcolin.gui.wheelview.views.OnWheelScrollListener;
import com.zcolin.gui.wheelview.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * 日期、时间选择对话框
 */
public class ZDialogWheelDateAndTime extends ZDialog<ZDialogWheelDateAndTime> implements View.OnClickListener,
        OnWheelChangedListener, OnWheelScrollListener {
    private static int LAYOUT_ID;

    protected int maxTextSize = 14;
    protected int minTextSize = 12;
    protected int maxTextColor;
    protected int minTextColor;

    /** 是否设置当前时间为最大时间 */
    protected boolean isCurDateMax = false;
    protected boolean issetdata    = false;
    protected int     month;
    protected int     day;
    protected int     currentYear  = getYear();
    protected int     maxYear      = 2050;
    protected int     minYear      = 1950;
    protected int     currentMonth = 1;
    protected int     currentDay   = 1;
    protected int     currentHour  = -1;
    protected int     currentMin   = -1;

    protected ArrayList<String>   yearsList  = new ArrayList<>();
    protected ArrayList<String>   monthsList = new ArrayList<>();
    protected ArrayList<String>   daysList   = new ArrayList<>();
    protected ArrayList<String>   hoursList  = new ArrayList<>();
    protected ArrayList<String>   minsList   = new ArrayList<>();
    protected CalendarTextAdapter mYearAdapter;
    protected CalendarTextAdapter mMonthAdapter;
    protected CalendarTextAdapter mDaydapter;
    protected CalendarTextAdapter mHourAdapter;
    protected CalendarTextAdapter mMinAdapter;

    protected WheelView wvYear;
    protected WheelView wvMonth;
    protected WheelView wvDay;
    protected WheelView wvHour;
    protected WheelView wvMin;
    protected View      vChangeBirthChild;
    protected TextView  btnSure;
    protected TextView  btnCancel;
    protected TextView  tvTitle;

    protected OnDateAndTimeSubmitListener onDateAndTimeSubmitListener;

    public static ZDialogWheelDateAndTime instance(Context context) {
        return new ZDialogWheelDateAndTime(context);
    }

    public static ZDialogWheelDateAndTime instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogWheelDateAndTime(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogWheelDateAndTime(Context context) {
        this(context, 0);
    }

    public ZDialogWheelDateAndTime(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_wheel_date_and_time : LAYOUT_ID) : layoutId);
        init(context);
    }

    private void init(Context context) {
        maxTextColor = context.getResources().getColor(R.color.gui_dlg_wheel_val);
        minTextColor = context.getResources().getColor(R.color.gui_divider);

        wvYear = findViewById(R.id.wv_birth_year);
        wvMonth = findViewById(R.id.wv_birth_month);
        wvDay = findViewById(R.id.wv_birth_day);
        wvHour = findViewById(R.id.wv_hour);
        wvMin = findViewById(R.id.wv_min);

        int[] shadowsColors = new int[]{0x00000000, 0x00000000, 0x00000000};
        wvYear.setShadowsColors(shadowsColors);
        wvMonth.setShadowsColors(shadowsColors);
        wvDay.setShadowsColors(shadowsColors);
        wvHour.setShadowsColors(shadowsColors);
        wvMin.setShadowsColors(shadowsColors);

        vChangeBirthChild = findViewById(R.id.ly_myinfo_changebirth_child);
        btnSure = findViewById(R.id.dialog_okbutton);
        btnCancel = findViewById(R.id.dialog_cancelbutton);
        tvTitle = findViewById(R.id.dialog_tilte);

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
        mYearAdapter = new CalendarTextAdapter(getContext(), yearsList, selectedYear);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(selectedYear);

        initMonths(month);
        int selectedMonth = setMonth(currentMonth);
        mMonthAdapter = new CalendarTextAdapter(getContext(), monthsList, selectedMonth);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(selectedMonth);

        initDays(day);
        int selectedDay = currentDay - 1;
        mDaydapter = new CalendarTextAdapter(getContext(), daysList, selectedDay);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(selectedDay);

        initHours();
        Calendar c = Calendar.getInstance();
        this.currentHour = currentHour == -1 ? c.get(Calendar.HOUR) : currentHour;
        this.currentMin = currentMin == -1 ? c.get(Calendar.MINUTE) : currentMin;
        int selectedHour = getHourIndex(currentHour);
        mHourAdapter = new CalendarTextAdapter(getContext(), hoursList, selectedHour);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(mHourAdapter);
        wvHour.setCurrentItem(selectedHour);

        initMinutes();
        int selectedMin = getMinIndex(currentMin);
        mMinAdapter = new CalendarTextAdapter(getContext(), minsList, selectedMin);
        wvMin.setVisibleItems(5);
        wvMin.setViewAdapter(mMinAdapter);
        wvMin.setCurrentItem(selectedMin);

        wvYear.addChangingListener(this);
        wvYear.addScrollingListener(this);
        wvMonth.addChangingListener(this);
        wvMonth.addScrollingListener(this);
        wvDay.addChangingListener(this);
        wvDay.addScrollingListener(this);
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
    public ZDialogWheelDateAndTime setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置默认选中
     */
    public ZDialogWheelDateAndTime setDefSelected(int year, int month, int day, int hour, int minute) {
        setDate(year, month, day);
        setTime(hour, minute);
        return this;
    }

    /**
     * 设置日期设置回调
     */
    public ZDialogWheelDateAndTime setDataSubmitListener(OnDateAndTimeSubmitListener onDateAndTimeSubmitListener) {
        this.onDateAndTimeSubmitListener = onDateAndTimeSubmitListener;
        return this;
    }

    /**
     * 设置上下阴影
     */
    public ZDialogWheelDateAndTime setShadowColors(int[] shadowColors) {
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
    public ZDialogWheelDateAndTime setMaxTextSize(int maxTextSize) {
        this.maxTextSize = maxTextSize;
        return this;
    }

    /**
     * 设置未选中文字的字体大小
     */
    public ZDialogWheelDateAndTime setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
        return this;
    }

    /**
     * 设置选中文字的字体颜色
     */
    public ZDialogWheelDateAndTime setMaxTextColor(int maxTextColor) {
        this.maxTextColor = maxTextColor;
        return this;
    }

    /**
     * 设置未选中文字的字体颜色
     */
    public ZDialogWheelDateAndTime setMinTextColor(int minTextColor) {
        this.minTextColor = minTextColor;
        return this;
    }

    /**
     * 设置最大年份，默认为当前年
     */
    public ZDialogWheelDateAndTime setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        return this;
    }

    /**
     * 设置最小年份，默认为1950
     */
    public ZDialogWheelDateAndTime setMinYear(int minYear) {
        this.minYear = minYear;
        return this;
    }

    /**
     * 设置是否当前日期为最大值
     */
    public ZDialogWheelDateAndTime setIsCurDateMax(boolean curDateMax) {
        isCurDateMax = curDateMax;
        return this;
    }

    /**
     * 设置中线颜色
     */
    public ZDialogWheelDateAndTime setCenterLineColor(@ColorInt int color) {
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
            mMonthAdapter = new CalendarTextAdapter(getContext(), monthsList, 0);
            wvMonth.setVisibleItems(5);
            wvMonth.setViewAdapter(mMonthAdapter);
            wvMonth.setCurrentItem(0);
        } else if (wheel == wvMonth) {
            String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mMonthAdapter);
            currentMonth = Integer.parseInt(currentText);
            setMonth(currentMonth);
            initDays(day);
            mDaydapter = new CalendarTextAdapter(getContext(), daysList, 0);
            wvDay.setVisibleItems(5);
            wvDay.setViewAdapter(mDaydapter);
            wvDay.setCurrentItem(0);
        } else if (wheel == wvDay) {
            String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mDaydapter);
            currentDay = Integer.parseInt(currentText);
        } else if (wheel == wvHour) {
            String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mHourAdapter);
            currentHour = Integer.parseInt(currentText);
        } else if (wheel == wvMin) {
            String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mMinAdapter);
            currentMin = Integer.parseInt(currentText);
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
        } else if (wheel == wvHour) {
            String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mHourAdapter);
        } else if (wheel == wvMin) {
            String currentText = (String) mMinAdapter.getItemText(wheel.getCurrentItem());
            setTextViewStyle(currentText, mMinAdapter);
        }
    }

    private void initYears(int year) {
        for (int i = year; i > minYear; i--) {
            yearsList.add(i + "");
        }
    }

    private void initMonths(int months) {
        monthsList.clear();
        for (int i = 1; i <= months; i++) {
            monthsList.add(i + "");
        }
    }

    private void initDays(int days) {
        daysList.clear();
        for (int i = 1; i <= days; i++) {
            daysList.add(i + "");
        }
    }

    private void initHours() {
        for (int i = 0; i <= 23; i++) {
            hoursList.add(String.format("%02d", i));
        }
    }

    private void initMinutes() {
        for (int i = 0; i <= 59; i++) {
            minsList.add(String.format("%02d", i));
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected CalendarTextAdapter(Context context, List<String> list, int currentItem) {
            super(context,
                  R.layout.gui_item_year_date,
                  NO_RESOURCE,
                  currentItem,
                  maxTextSize,
                  minTextSize,
                  maxTextColor,
                  minTextColor);
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
            if (onDateAndTimeSubmitListener != null) {
                onDateAndTimeSubmitListener.onClick(currentYear, currentMonth, currentDay, currentHour, currentMin);
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
            currentText = textvew.getText().toString();
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
     * 设置时间
     */
    private void setTime(int hour, int minute) {
        this.currentHour = hour;
        this.currentMin = minute;
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
                default:
                    break;
            }
        }

        if (year == getYear() && month == getMonth() && isCurDateMax) {
            this.day = getDay();
        }
    }

    private int getHourIndex(int hour) {
        int index = 0;
        for (int i = 0; i <= 23; i++) {
            if (i == hour) {
                return index;
            }
            index++;
        }
        return index;
    }

    private int getMinIndex(int min) {
        int index = 0;
        for (int i = 0; i <= 59; i++) {
            if (i == min) {
                return index;
            }
            index++;
        }
        return index;
    }

    public interface OnDateAndTimeSubmitListener {
        void onClick(int year, int month, int day, int hour, int minute);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
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
        if (mHourAdapter != null && wvHour != null) {
            String currentText = (String) mHourAdapter.getItemText(wvHour.getCurrentItem());
            setTextViewStyle(currentText, mHourAdapter);
        }
        if (mMinAdapter != null && wvMin != null) {
            String currentText = (String) mMinAdapter.getItemText(wvMin.getCurrentItem());
            setTextViewStyle(currentText, mMinAdapter);
        }
    }
}