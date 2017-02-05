/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * 半透明帮助说明控件
 */
public class ZGuideHelpView {
    private int    layoutId;
    private int    knowViewId;
    private String pageTag;
    private boolean mCancel = false;

    private Activity    activity;
    private FrameLayout rootLayout;
    private View        layoutView;
    private SharedPreferences sharedPreferences;

    private ZGuideHelpView(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("guide_help", Context.MODE_PRIVATE);
    }

    /**
     * 设置页面布局
     */
    public ZGuideHelpView setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    /**
     * 设置取消按钮，“我知道了”
     */
    public ZGuideHelpView setKnowViewId(@IdRes int knowViewId) {
        this.knowViewId = knowViewId;
        return this;
    }

    /**
     * 引导唯一的标记，用于判断是否已经显示的标志，不同引导页必须不一样
     */
    public ZGuideHelpView setPageTag(String pageTag) {
        this.pageTag = pageTag;
        return this;
    }

    /**
     * 点击除了按钮之外的其他区域是否也消失
     */
    public ZGuideHelpView setCloseOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        return this;
    }

    /**
     * 是否忽略已经弹出过的记录强制弹出
     */
    public void show(boolean isForce) {
        if (isForce ||!sharedPreferences.getBoolean(pageTag, false)) {
            if (TextUtils.isEmpty(this.pageTag)) {
                throw new RuntimeException("the guide page must set page tag");
            }

            rootLayout = (FrameLayout) activity.findViewById(android.R.id.content);
            layoutView = View.inflate(activity, layoutId, null);
            if (layoutView != null) {
                layoutView.findViewById(knowViewId)
                          .setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  cancel();
                              }
                          });

                layoutView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mCancel) {
                            cancel();
                        }
                        return true;  //消费事件，不下发
                    }
                });

                rootLayout.addView(layoutView);
            }
        }
    }

    /**
     * 显示，调用之后会显示出来
     */
    public void show() {
        show(false);
    }

    /**
     * 从页面移除，关闭引导帮助
     */
    public void cancel() {
        if (rootLayout != null && layoutView != null) {
            rootLayout.removeView(layoutView);
            sharedPreferences.edit().putBoolean(pageTag, true).apply();
        }
    }
}