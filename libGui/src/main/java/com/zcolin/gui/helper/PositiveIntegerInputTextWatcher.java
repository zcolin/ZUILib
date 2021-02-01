package com.zcolin.gui.helper;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * <p>
 * EditText的正整数的输入过滤规则
 * </p>
 *
 * @author JackYang
 * @since 2021/2/1 14:14
 */
public class PositiveIntegerInputTextWatcher implements TextWatcher {

    /** 整数位数 */
    private int integerDigits;

    private EditText editText;

    public PositiveIntegerInputTextWatcher(EditText editText) {
        this.editText = editText;
    }

    public PositiveIntegerInputTextWatcher(EditText editText, int integerDigits) {
        this.editText = editText;
        if (integerDigits <= 0) {
            throw new RuntimeException("整数位数必须>=0");
        }
        this.integerDigits = integerDigits;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString().trim();
        editText.removeTextChangedListener(this);

        if (integerDigits > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
            if (s.length() > integerDigits) {
                s = s.substring(0, integerDigits);
                editable.replace(0, editable.length(), s);
            }
        }

        if (s.startsWith("0") && s.length() > 1) {
            editable.replace(0, editable.length(), "0");
        }

        editText.addTextChangedListener(this);
    }

}
