/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-23 上午10:36
 * ********************************************************
 */

package com.zcolin.ui.demo;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fosung.ui.R;


/**
 * 其他的一些View的示例
 */
public class ZKVViewActivity extends FragmentActivity {
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zkeyvalue);
    }
}
