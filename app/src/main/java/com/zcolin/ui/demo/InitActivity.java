package com.zcolin.ui.demo;

import android.os.Bundle;

import com.fosung.ui.R;
import com.zcolin.frame.util.ActivityUtil;

import androidx.fragment.app.FragmentActivity;

/**
 * 启动页
 */
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
