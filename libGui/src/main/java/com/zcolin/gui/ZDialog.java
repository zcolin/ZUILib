package com.zcolin.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zcolin.gui.helper.ZUIHelper;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

/**
 * 对话框基类
 */
public class ZDialog<T> extends Dialog {

    private final SparseArray<View> mViews = new SparseArray<>();

    /** 是否可以取消 */
    private boolean isCancelAble = true;
    /** 对话框背景 */
    private int     resBg;
    /** 弹出消失动画 */
    private int     anim;
    /***/
    private Handler mHandler     = new Handler(Looper.getMainLooper());

    public ZDialog(Context context, @LayoutRes int layResID) {
        this(context, layResID, R.style.gui_style_dialog);
    }

    public ZDialog(Context context, View view) {
        this(context, view, R.style.gui_style_dialog);
    }

    public ZDialog(Context context, @LayoutRes int layResID, @StyleRes int theme) {
        super(context, theme);
        setContentView(layResID);
        setLayout(ZUIHelper.getSmallScreenWidth(context) / 6 * 5, 0);
    }

    public ZDialog(Context context, View view, @StyleRes int theme) {
        super(context, theme);
        setContentView(view);
        setLayout(ZUIHelper.getSmallScreenWidth(context) / 6 * 5, 0);
    }

    /**
     * 设置背景遮盖层开关
     */
    public T setBackgroundDimEnabled(boolean enabled) {
        Window window = getWindow();
        if (window != null) {
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        return (T) this;
    }

    /**
     * 设置背景遮盖层的透明度（前提条件是背景遮盖层开关必须是为开启状态）
     */
    public T setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) float dimAmount) {
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(dimAmount);
        }
        return (T) this;
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
     * 设置窗口对齐方式
     *
     * @param gravity 对齐方式
     */
    public T setGravity(int gravity) {
        getWindow().getAttributes().gravity = gravity;
        return (T) this;
    }

    /**
     * 设置弹出框背景
     *
     * @param resBg 0 纯白， 背景图片资源Id
     */
    public T setDialogBackground(@DrawableRes int resBg) {
        this.resBg = resBg;
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
        if (width != 0) {
            wl.width = width;
        }
        if (high != 0) {
            wl.height = high;
        }
        window.setAttributes(wl);
        return (T) this;
    }

    /**
     * 延迟一段时间执行
     */
    public final void postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        mHandler.postAtTime(r, this, SystemClock.uptimeMillis() + delayMillis);
    }

    @Override
    public void show() {
        if (anim != 0) {
            getWindow().setWindowAnimations(anim);
        }
        if (resBg != 0) {
            getWindow().setBackgroundDrawableResource(resBg);
        }

        // 设置触摸对话框以外的地方取消对话框
        setCanceledOnTouchOutside(isCancelAble);
        setCancelable(isCancelAble);
        super.show();
    }

    /**
     * 通过资源ID获取view
     *
     * @param resId 资源ID
     *
     * @return View
     */
    public <E extends View> E getView(int resId) {
        E view = (E) mViews.get(resId);
        if (view == null) {
            view = findViewById(resId);
            mViews.put(resId, view);
        }
        return view;
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing() && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) context)
                        .isDestroyed())) {
                    super.dismiss();
                }
            } else {
                super.dismiss();
            }
        }
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogParamSubmitListener<E> {
        boolean submit(E t);
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogParamCancelListener<E> {
        boolean cancel(E t);
    }

    /**
     * 确定回调接口
     */
    public interface ZDialogSubmitListener {
        boolean submit();
    }

    /**
     * 取消回调接口
     */
    public interface ZDialogCancelListener {
        boolean cancel();
    }

}
