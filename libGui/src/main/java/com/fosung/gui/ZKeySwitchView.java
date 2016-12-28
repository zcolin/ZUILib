/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-20 下午4:34
 **********************************************************/
package com.fosung.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fosung.frame.imageloader.ImageLoaderUtils;


/**
 * 封装的左边为说明右边为选择按钮的控件
 */
public class ZKeySwitchView extends RelativeLayout {
    private static int LAYOUT_ID;

    private ZCheckTextView switchButton;
    private ImageView      ivImg;
    private TextView       tvKey;
    private int            height;

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZKeySwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKeySwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context)
                      .inflate(LAYOUT_ID == 0 ? R.layout.gui_view_keyswitch : LAYOUT_ID, this);
        switchButton = (ZCheckTextView) findViewById(R.id.switchButton);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        tvKey = (TextView) findViewById(R.id.tv_key);
        View bottomLine = findViewById(R.id.view_bottomline);

        TypedArray a = context.getTheme()
                              .obtainStyledAttributes(attrs, R.styleable.ZKeySwitchView, defStyle, 0);
        height = (int) a.getDimension(R.styleable.ZKeySwitchView_zksv_height, 0);
        String keyText = a.getString(R.styleable.ZKeySwitchView_zksv_key_text);
        float keyTextSize = a.getDimension(R.styleable.ZKeySwitchView_zksv_key_text_size, 0);
        int keyTextColor = a.getColor(R.styleable.ZKeySwitchView_zksv_key_text_color, 0);
        int keyStyle = a.getInt(R.styleable.ZKeySwitchView_zksv_key_style, -1);
        int keyImg = a.getResourceId(R.styleable.ZKeySwitchView_zksv_key_img, 0);
        int keyEms = a.getInteger(R.styleable.ZKeySwitchView_zksv_key_ems, 0);
        boolean isBottomLine = a.getBoolean(R.styleable.ZKeySwitchView_zksv_is_bottomline, true);
        boolean isChecked = a.getBoolean(R.styleable.ZKeySwitchView_zksv_is_checked, false);
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
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }

        if (keyEms > 0) {
            tvKey.setEms(keyEms);
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

    public String getKeyText() {
        return tvKey.getText()
                    .toString();
    }

    public void setKeyText(String key) {
        tvKey.setText(key);
    }

    public ZCheckTextView getSwitchButton() {
        return switchButton;
    }

    public boolean isChecked() {
        return switchButton.isChecked();
    }

    public void setChecked(boolean isChecked) {
        switchButton.setChecked(isChecked);
    }

    public void setOncheckedListener(ZCheckTextView.CheckedCallBack listener) {
        switchButton.addOnCheckedChangeListener(listener);
    }

    public void setKeyImage(String url) {
        if (url != null) {
            ivImg.setVisibility(View.VISIBLE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = (int) getContext().getResources()
                                                                                .getDimension(R.dimen.gui_dimens_small);
            ImageLoaderUtils.displayImage(getContext(), url, ivImg);
        } else {
            ivImg.setVisibility(View.GONE);
            ((LayoutParams) ivImg.getLayoutParams()).rightMargin = 0;
        }
    }
}