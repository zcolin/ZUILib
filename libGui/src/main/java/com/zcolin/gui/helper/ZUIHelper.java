/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-2-5 上午9:36
 * ********************************************************
 */
package com.zcolin.gui.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 工具辅助类
 */
public class ZUIHelper {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay()
          .getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay()
          .getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 将px值转换为dp值
     *
     * @param pxValue px像素值
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources()
                                   .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources()
                                   .getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
