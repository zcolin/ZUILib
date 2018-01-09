/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */

package com.zcolin.ui.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fosung.ui.R;
import com.zcolin.frame.util.ActivityUtil;


public class InitActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        load();

        ActivityUtil.startActivity(this, MainActivity.class);
        this.finish();
    }

    private void load() {
        //TODO 加载数据
    }
}
