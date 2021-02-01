package com.zcolin.gui;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zcolin.gui.helper.PositiveIntegerInputTextWatcher;

import androidx.annotation.Nullable;

/**
 * <p>
 * 自定义加减控件
 * </p>
 *
 * @author JackYang
 * @since 2021/2/1 13:29
 */
public class ZAddAndSubtractView extends LinearLayout implements View.OnClickListener {

    private static final int DEFAULT_COUNT = 0;
    private static final int TYPE_SUBTRACT = 0;
    private static final int TYPE_ADD      = 1;

    private final Window             window;
    private final InputMethodManager imm;
    private       int                mCount;
    private       boolean            mHasFocus;

    private ImageView ivSubtract;
    private ImageView ivAdd;
    private EditText  etCount;

    public ZAddAndSubtractView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.gui_add_and_subtract_view, this);
        window = ((Activity) context).getWindow();
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        ivSubtract = findViewById(R.id.iv_substract);
        ivAdd = findViewById(R.id.iv_add);
        etCount = findViewById(R.id.et_count);

        setViewSize(ivSubtract);
        setViewSize(ivAdd);
    }

    private void initData() {
        setSubtractImageResource(R.drawable.gui_subtract_icon);
        setAddImageResource(R.drawable.gui_add_icon);
        mCount = DEFAULT_COUNT;
        setCount(mCount);
    }

    private void initListener() {
        ivSubtract.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        etCount.addTextChangedListener(new PositiveIntegerInputTextWatcher(etCount));
        etCount.setOnFocusChangeListener((v, hasFocus) -> mHasFocus = hasFocus);
    }

    /**
     * 重新设置控件的宽高，保证显示正方形
     */
    private void setViewSize(View view) {
        view.post(() -> {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int width = view.getWidth();
            int height = view.getHeight();
            if (width < height) {
                layoutParams.height = width;
            } else if (width > height) {
                layoutParams.width = height;
            }
            view.setLayoutParams(layoutParams);
        });
    }

    /**
     * 设置减号图片资源
     */
    public void setSubtractImageResource(int subtractDrawable) {
        ivSubtract.setImageResource(subtractDrawable);
    }

    /**
     * 设置加号图片资源
     */
    public void setAddImageResource(int addDrawable) {
        ivAdd.setImageResource(addDrawable);
    }

    /**
     * 设置EditText的颜色
     */
    public void setEditTextColor(int color) {
        etCount.setTextColor(color);
    }

    /**
     * 设置数量
     */
    public void setCount(int count) {
        this.mCount = count;
        if (mCount >= 0) {
            ivSubtract.setVisibility(VISIBLE);
            etCount.setVisibility(VISIBLE);
        } else {
            ivSubtract.setVisibility(INVISIBLE);
            etCount.setVisibility(INVISIBLE);
        }
        etCount.setText(String.valueOf(mCount));
    }

    /**
     * 获取数量
     */
    public int getCount() {
        String countText = etCount.getText().toString().trim();
        if (!TextUtils.isEmpty(countText)) {
            return Integer.parseInt(countText);
        } else {
            return DEFAULT_COUNT;
        }
    }

    /**
     * 隐藏软键盘及输入光标
     */
    private void hideSoftInput() {
        etCount.clearFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(etCount.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        // 点击操作时隐藏软键盘
        if (mHasFocus) {
            hideSoftInput();
        }

        String countText = etCount.getText().toString();
        if (TextUtils.isEmpty(countText)) {
            mCount = DEFAULT_COUNT;
            etCount.setText(String.valueOf(mCount));
            return;
        } else {
            mCount = Integer.parseInt(etCount.getText().toString().trim());
        }

        int id = v.getId();
        if (id == R.id.iv_substract) {
            if (mCount > 0) {
                mCount--;
                setCount(mCount);
                if (mOnCountChangedListener != null) {
                    mOnCountChangedListener.onCountChanged(this, TYPE_SUBTRACT, getCount());
                }
            }
        } else if (id == R.id.iv_add) {
            mCount++;
            setCount(mCount);
            if (mOnCountChangedListener != null) {
                mOnCountChangedListener.onCountChanged(this, TYPE_ADD, getCount());
            }
        }
    }


    private OnCountChangedListener mOnCountChangedListener;

    public void setOnCountChangedListener(OnCountChangedListener listener) {
        this.mOnCountChangedListener = listener;
    }

    public interface OnCountChangedListener {
        void onCountChanged(View view, int type, int count);
    }

}
