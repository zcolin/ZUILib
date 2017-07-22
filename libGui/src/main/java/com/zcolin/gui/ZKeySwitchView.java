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
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * 封装的左边为说明右边为选择按钮的控件
 */
public class ZKeySwitchView extends RelativeLayout {
    private static int LAYOUT_ID;

    private ZCheckTextView switchButton;
    private ImageView      ivImg;
    private TextView       tvKey;
    private int            height;
    private View           bottomLine;

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZKeySwitchView(Context context) {
        this(context, null);
    }

    public ZKeySwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKeySwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int layoutId = getSelfLayoutId() == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_view_keyswitch : LAYOUT_ID) : getSelfLayoutId();
        LayoutInflater.from(context)
                      .inflate(layoutId, this);
        switchButton = (ZCheckTextView) findViewById(R.id.switchButton);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        tvKey = (TextView) findViewById(R.id.tv_key);
        bottomLine = findViewById(R.id.view_bottomline);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZKeySwitchView, defStyle, 0);
        height = (int) a.getDimension(R.styleable.ZKeySwitchView_zksv_height, 0);
        String keyText = a.getString(R.styleable.ZKeySwitchView_zksv_key_text);
        float keyTextSize = a.getDimension(R.styleable.ZKeySwitchView_zksv_key_text_size, 0);
        int keyTextColor = a.getColor(R.styleable.ZKeySwitchView_zksv_key_text_color, 0);
        int keyStyle = a.getInt(R.styleable.ZKeySwitchView_zksv_key_style, -1);
        int keyImg = a.getResourceId(R.styleable.ZKeySwitchView_zksv_key_img, 0);
        int keyImageWidth = (int) a.getDimension(R.styleable.ZKeySwitchView_zksv_key_img_width, 0);
        int keyImageHeight = (int) a.getDimension(R.styleable.ZKeySwitchView_zksv_key_img_height, 0);
        int keyEms = a.getInteger(R.styleable.ZKeySwitchView_zksv_key_ems, 0);
        boolean isBottomLine = a.getBoolean(R.styleable.ZKeySwitchView_zksv_is_bottomline, true);
        boolean isChecked = a.getBoolean(R.styleable.ZKeySwitchView_zksv_is_checked, false);
        int valueBgOff = a.getResourceId(R.styleable.ZKeySwitchView_zksv_value_bg_off, 0);
        int valueBgOn = a.getResourceId(R.styleable.ZKeySwitchView_zksv_value_bg_on, 0);
        String valueTextOff = a.getString(R.styleable.ZKeySwitchView_zksv_value_str_off);
        String valueTextOn = a.getString(R.styleable.ZKeySwitchView_zksv_value_str_on);

        a.recycle();

        tvKey.setText(keyText);
        switchButton.setChecked(isChecked);
        if (keyStyle != -1) {
            tvKey.setTextAppearance(getContext(), keyStyle);
        }

        if (keyTextSize > 0) {
            tvKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, keyTextSize);
        }

        if (keyTextColor != 0) {
            tvKey.setTextColor(keyTextColor);
        }

        if (keyImg != 0) {
            ivImg.setImageResource(keyImg);
            if (keyImageHeight > 0) {
                ivImg.getLayoutParams().height = keyImageHeight;
            }
            if (keyImageWidth > 0) {
                ivImg.getLayoutParams().width = keyImageWidth;
            }
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }

        if (keyEms > 0) {
            tvKey.setEms(keyEms);
        }

        if (valueBgOn != 0 && valueBgOff != 0) {
            switchButton.initRes(valueBgOff, valueBgOn);
        }

        if (valueTextOn != null && valueTextOff != null) {
            switchButton.initText(valueTextOff, valueTextOn);
        }

        bottomLine.setVisibility(isBottomLine ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (height > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在此函数中返回自定义的LayoutId，
     * 但layout中的所有控件Id必须与本xml的Id相同，可以增加控件，不可以删除掉控件, 此函数返回的LayoutId的优先级高于{@link #initLayout(int)}
     */
    protected
    @LayoutRes
    int getSelfLayoutId() {
        return 0;
    }



    public void setKeyText(String key) {
        tvKey.setText(key);
    }

    public boolean isChecked() {
        return switchButton.isChecked();
    }

    public void setChecked(boolean isChecked) {
        switchButton.setChecked(isChecked);
    }

    public void setSwitchButtonRes(int bg, int bg_p) {
        switchButton.initRes(bg, bg_p);
    }

    public void setSwitchButtonText(String str, String str_p) {
        switchButton.initText(str, str_p);
    }

    public void setOncheckedListener(ZCheckTextView.CheckedCallBack listener) {
        switchButton.addOnCheckedChangeListener(listener);
    }

    public void setKeyImage(String url) {
        if (url != null) {
            ivImg.setVisibility(View.VISIBLE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = (int) getContext().getResources()
                                                                                     .getDimension(R.dimen.gui_dimens_small);
            Glide.with(getContext())
                 .load(url)
                 .into(ivImg);
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }
    }
    public String getKeyText() {
        return tvKey.getText()
                    .toString();
    }

    public TextView getTvKey() {
        return tvKey;
    }

    public ZCheckTextView getSwitchButton() {
        return switchButton;
    }

    public ImageView getIvKeyImage() {
        return ivImg;
    }

    public View getBottomLine() {
        return bottomLine;
    }
}