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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * 封装的左边为说明右边为值的控件
 */
public class ZKeyValueEditView extends RelativeLayout {
    private static int LAYOUT_ID;

    private TextView  tvKey;
    private EditText  etValue;
    private ImageView ivArrow;
    private ImageView ivImg;
    private int       height;
    private View      bottomLine;

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZKeyValueEditView(Context context) {
        this(context, null);
    }

    public ZKeyValueEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZKeyValueEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setFocusableInTouchMode(true);
        int layoutId = getSelfLayoutId() == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_view_keyvalueedit : LAYOUT_ID) : getSelfLayoutId();
        LayoutInflater.from(context)
                      .inflate(layoutId, this);
        tvKey = (TextView) findViewById(R.id.tv_key);
        etValue = (EditText) findViewById(R.id.et_value);
        ivArrow = (ImageView) findViewById(R.id.iv_arrow);
        ivImg = (ImageView) findViewById(R.id.iv_img);
        bottomLine = findViewById(R.id.view_bottomline);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZKeyValueEditView, defStyle, 0);
        height = (int) a.getDimension(R.styleable.ZKeyValueEditView_zkve_height, 0);
        String keyText = a.getString(R.styleable.ZKeyValueEditView_zkve_key_text);
        float keyTextSize = a.getDimension(R.styleable.ZKeyValueEditView_zkve_key_text_size, 0);
        int keyTextColor = a.getColor(R.styleable.ZKeyValueEditView_zkve_key_text_color, 0);
        int keyStyle = a.getInt(R.styleable.ZKeyValueEditView_zkve_key_style, -1);
        String keyGravity = a.getString(R.styleable.ZKeyValueEditView_zkve_key_gravity);
        int keyImg = a.getResourceId(R.styleable.ZKeyValueEditView_zkve_key_img, 0);
        int keyImageWidth = (int) a.getDimension(R.styleable.ZKeyValueEditView_zkve_key_img_width, 0);
        int keyImageHeight = (int) a.getDimension(R.styleable.ZKeyValueEditView_zkve_key_img_height, 0);
        int keyEms = a.getInteger(R.styleable.ZKeyValueEditView_zkve_key_ems, 0);

        String valueText = a.getString(R.styleable.ZKeyValueEditView_zkve_value_text);
        String valueGravity = a.getString(R.styleable.ZKeyValueEditView_zkve_value_gravity);
        float valueTextSize = a.getDimension(R.styleable.ZKeyValueEditView_zkve_value_text_size, 0);
        int valueTextColor = a.getColor(R.styleable.ZKeyValueEditView_zkve_value_text_color, 0);
        int valueStyle = a.getInt(R.styleable.ZKeyValueEditView_zkve_value_style, -1);
        String hintText = a.getString(R.styleable.ZKeyValueEditView_zkve_value_hint);
        int maxline = a.getInt(R.styleable.ZKeyValueEditView_zkve_value_maxline, 1);
        int valueBackground = a.getResourceId(R.styleable.ZKeyValueEditView_zkve_value_background, 0);
        boolean isArrow = a.getBoolean(R.styleable.ZKeyValueEditView_zkve_is_arrow, false);
        boolean isBottomLine = a.getBoolean(R.styleable.ZKeyValueEditView_zkve_is_bottomline, true);

        a.recycle();

        tvKey.setText(keyText);
        if (keyStyle != -1) {
            tvKey.setTextAppearance(getContext(), keyStyle);
        }

        if (keyTextSize > 0) {
            tvKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, keyTextSize);
        }

        if (keyTextColor != 0) {
            tvKey.setTextColor(keyTextColor);
        }

        if ("right".equals(keyGravity)) {
            tvKey.setGravity(Gravity.RIGHT);
        } else {
            tvKey.setGravity(Gravity.LEFT);
        }

        if ("right".equals(valueGravity)) {
            etValue.setGravity(Gravity.RIGHT);
        } else {
            etValue.setGravity(Gravity.LEFT);
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

        if (!TextUtils.isEmpty(valueText)) {
            etValue.setText(valueText);
        } else if (!TextUtils.isEmpty(hintText)) {
            etValue.setHint(hintText);
        }

        if (valueStyle != -1) {
            etValue.setTextAppearance(getContext(), valueStyle);
        }

        if (valueTextSize > 0) {
            etValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        }

        if (valueTextColor != 0) {
            etValue.setTextColor(valueTextColor);
        }

        if (maxline > 0) {
            etValue.setMaxLines(maxline);
        }

        if (valueBackground != 0) {
            etValue.setBackgroundResource(valueBackground);
        }

        bottomLine.setVisibility(isBottomLine ? View.VISIBLE : View.GONE);
        ivArrow.setVisibility(isArrow ? View.VISIBLE : View.GONE);
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

    public void setValueText(String valueText) {
        etValue.setText(valueText);
    }

    public void setValueHintText(String valueHintText) {
        etValue.setHint(valueHintText);
    }

    public void setIsBottomLine(boolean isBottomLine) {
        bottomLine.setVisibility(isBottomLine ? View.VISIBLE : View.GONE);
    }

    public void setSelection(int index) {
        etValue.setSelection(index);
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

    public String getValueText() {
        return etValue.getText()
                      .toString();
    }

    public TextView getTvKey() {
        return tvKey;
    }

    public TextView getEtValue() {
        return etValue;
    }

    public ImageView getIvArrow() {
        return ivArrow;
    }

    public ImageView getIvKeyImage() {
        return ivImg;
    }

    public View getBottomLine() {
        return bottomLine;
    }
}