package com.zcolin.ui.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {

    private int selectedPostion;

    public void setSelectedPostion(int selectedPostion) {
        this.selectedPostion = selectedPostion;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (selectedPostion == position) {
            textView.setTextColor(0xff373741);
            textView.getPaint().setFakeBoldText(true);
        } else {
            textView.setTextColor(0xff6d6d6d);
            textView.getPaint().setFakeBoldText(false);
        }
        return view;
    }

    /**
     * 从values->arrays.xml导入数据
     */
    public static @NonNull
    SpinnerAdapter<CharSequence> createFromResource(@NonNull Context context, @ArrayRes int textArrayResId,
            @LayoutRes int textViewResId) {
        final CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new SpinnerAdapter<>(context, textViewResId, strings);
    }

    /**
     * 从代码带入数据
     */
    public static @NonNull
    SpinnerAdapter<CharSequence> createFromResource(@NonNull Context context, CharSequence[] strArray,
            @LayoutRes int textViewResId) {
        return new SpinnerAdapter<>(context, textViewResId, strArray);
    }
}