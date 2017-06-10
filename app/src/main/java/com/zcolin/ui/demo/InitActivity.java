/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-23 上午10:36
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
