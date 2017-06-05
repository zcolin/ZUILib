/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zcolin.gui.helper.ZUIHelper;

/**
 * 对话框基类
 */
public class ZDialog<T> extends Dialog {
    private final SparseArray<View> mViews       = new SparseArray<>();
    private       boolean           isCancelAble = true;//是否可以取消
    private int resBg;                  //对话框背景
    private int anim;                   //弹出消失动画

    /**
     * @param layResID layoutId
     */
    public ZDialog(Context context, @LayoutRes int layResID) {
        super(context);
        setContentView(layResID);
        setLayout(ZUIHelper.getScreenWidth(context) / 6 * 5, 0);
    }

    /**
     * @param view view
     */
    public ZDialog(Context context, View view) {
        super(context);
        setContentView(view);
        setLayout(ZUIHelper.getScreenWidth(context) / 6 * 5, 0);
    }

    /**
     * @param layResID layoutId
     */
    public ZDialog(Context context, @LayoutRes int layResID, @StyleRes int theme) {
        super(context, theme);
        setContentView(layResID);
        setLayout(ZUIHelper.getScreenWidth(context) / 6 * 5, 0);
    }

    /**
     * @param view view
     */
    public ZDialog(Context context, View view, @StyleRes int theme) {
        super(context, theme);
        setContentView(view);
        setLayout(ZUIHelper.getScreenWidth(context) / 6 * 5, 0);
    }

    /**
     * 对话框显示
     *
     * @param isCancelAble 点击其他地方是否可消失
     */
    public T setIsCancelAble(boolean isCancelAble) {
        this.isCancelAble = isCancelAble;
        return (T) this;
    }

    /**
     * 设置弹出框显示隐藏动画
     *
     * @param anim 弹出框显示隐藏传入动画
     */
    public T setAnim(@StyleRes int anim) {
        this.anim = anim;
        return (T) this;
    }

    /**
     * 设置弹出框背景
     *
     * @param resBg -1透明， 0纯白， 背景图片资源Id
     */
    public T setDialogBackground(@DrawableRes int resBg) {
        this.resBg = resBg;
        return (T) this;
    }


    /**
     * 设置窗口对齐方式
     *
     * @param gravity 对齐方式
     */
    public T setGravity(int gravity) {
        getWindow().getAttributes().gravity = gravity;
        return (T) this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha 透明度
     */
    public T setAlpha(int alpha) {
        getWindow().getAttributes().alpha = alpha;
        return (T) this;
    }

    /**
     * 设置窗口显示偏移
     *
     * @param x x小于0左移，大于0右移
     * @param y y小于0上移，大于0下移
     */
    public T setWindowDeploy(int x, int y) {
        if (x != 0 || y != 0) {
            Window window = getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();

            // 根据x，y坐标设置窗口需要显示的位置
            wl.x = x;
            wl.y = y;
            window.setAttributes(wl);
        }
        return (T) this;
    }

    /**
     * 设定Dialog的固定大小
     *
     * @param width 宽
     * @param high  高
     */
    public T setLayout(int width, int high) {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        if (width > 0)
            wl.width = width;
        if (high > 0)
            wl.height = high;
        window.setAttributes(wl);
        return (T) this;
    }

    @Override
    public void show() {
        if (anim != 0) {
            getWindow().setWindowAnimations(anim);
        }
        if (resBg != 0) {
            getWindow().setBackgroundDrawableResource(resBg);
        }
        setCanceledOnTouchOutside(isCancelAble);// 设置触摸对话框以外的地方取消对话框
        setCancelable(isCancelAble);
        super.show();
    }

    /**
     * 通过资源ID获取view
     *
     * @param resId 资源ID
     * @return View
     */
    public <E extends View> E getView(int resId) {
        E view = (E) mViews.get(resId);
        if (view == null) {
            view = (E) findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogParamSubmitInterface<E> {
        boolean submit(E t);
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogParamCancelInterface<E> {
        boolean cancel(E t);
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogSubmitInterface {
        boolean submit();
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogCancelInterface {
        boolean cancel();
    }
}
