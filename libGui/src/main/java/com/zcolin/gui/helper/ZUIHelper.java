/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.gui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 工具辅助类
 */
public class ZUIHelper {

    /**
     * 获取屏幕宽度(较小的宽度，横屏时为屏幕高度)
     */
    public static int getSmallScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height > width ? width : height;
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 将px值转换为dp值
     *
     * @param pxValue px像素值
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Activity context) {
        int statusBarHeight = 0;

        try {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statusBarHeight == 0) {
            Rect frame = new Rect();
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    /**
     * 拷贝图片
     */
    public static void copyPic(String sourcePath, String tagPath, int width, int height, int errorRange) {
        Bitmap map = null;
        if (width < 0 || height < 0) {
            map = BitmapFactory.decodeFile(sourcePath);
        } else {
            map = decodeBitmap(sourcePath, width, height, errorRange);
        }

        File file = new File(tagPath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            map.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (map != null && !map.isRecycled()) {
                map.recycle();
            }
        }
    }

    /**
     * 图片缩放（等比缩放）
     *
     * @param fileName   图片文件路径
     * @param errorRange 误差范围
     */
    private static Bitmap decodeBitmap(String fileName, int width, int height, int errorRange) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 设为true，BitmapFactory.decodeFile(Stringpath, Options opt)并不会真的返回一个Bitmap给你，仅会把它的宽，高取回
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // 计算缩放比例
        options.inSampleSize = calculateOriginal(options, width, height, errorRange);
        options.inJustDecodeBounds = false;
        System.out.println("samplesize:" + options.inSampleSize);
        return BitmapFactory.decodeFile(fileName, options);
    }

    /**
     * 计算图片缩放比例,只能缩放2的指数
     *
     * @param reqWidth   缩放后的宽度
     * @param reqHeight  缩放后的高度
     * @param errorRange 误差范围
     * @return 计算的缩放比例
     */
    private static int calculateOriginal(BitmapFactory.Options options, int reqWidth, int reqHeight, int errorRange) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height < width) {
            int temp = height;
            height = width;
            width = temp;
        }

        if (reqHeight < reqWidth) {
            int temp = reqHeight;
            reqHeight = reqWidth;
            reqWidth = temp;
        }

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //允许200误差
            while (halfHeight / inSampleSize - reqHeight > -errorRange && halfWidth / inSampleSize - reqWidth > -errorRange) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
