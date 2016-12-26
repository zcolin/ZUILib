/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-12-21 下午3:16
 * ********************************************************
 */

package com.fosung.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


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
