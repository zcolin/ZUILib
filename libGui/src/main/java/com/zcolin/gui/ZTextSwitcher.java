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
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 可以定时滚动的TextSwitcher，自动截取文字
 */
public class ZTextSwitcher extends TextSwitcher {
    private TextHandler textHandler = new TextHandler();
    private MyRunnable  myRunnable  = new MyRunnable();
    private Paint       mPaint      = new Paint();
    private int         textColor   = Color.WHITE;
    private int         textGravity = Gravity.CENTER;
    private int textSize;

    private int  curPosition    = 0;     //当前循环的条数
    private int  showLine       = 2;    //固定行数
    private long switchInterval = 2000;//切换间隔
    private String            text;         //设置需要自动切割的文字
    private ArrayList<String> listText;     //设置需要循环的文字
    private boolean           isNeedCutout;//是否需要截取
    private int               width;        //获取的控件宽度
    private boolean           isStart;      //是否已经开始循环展示
    private boolean           isSetFactory; //是否已经设置了Factory

    public ZTextSwitcher(Context context) {
        this(context, null);
    }

    public ZTextSwitcher(Context context, AttributeSet attr) {
        super(context, attr);
        textSize = ZUIHelper.dip2px(context, 16);

        super.setInAnimation(context, R.anim.ztextswitcher_slide_in);
        super.setOutAnimation(context, R.anim.ztextswitcher_slide_out);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > 0 && !isStart) {
            startSwitcher();
        }
    }

    /**
     * 使用dp
     */
    public ZTextSwitcher setTextSize(int textSize) {
        this.textSize = ZUIHelper.dip2px(getContext(), textSize);
        mPaint.setTextSize(this.textSize);
        return this;
    }

    /**
     * 设置循环的text
     * <p>
     * 如果text的长度超过textLine的长度，会自动截取
     */
    public ZTextSwitcher setText(String text) {
        this.text = text;
        isNeedCutout = true;
        listText = null; //list置空，重新测量计算
        return this;
    }

    /**
     * 设置循环的text
     */
    public ZTextSwitcher setText(ArrayList<String> listText) {
        this.listText = listText;
        return this;
    }

    /**
     * 设置循环的text
     */
    public ZTextSwitcher setText(String[] arrText) {
        if (arrText != null && arrText.length > 0) {
            this.listText = new ArrayList<>();
            Collections.addAll(listText, arrText);
        }
        return this;
    }

    /**
     * 设置字体颜色
     */
    public ZTextSwitcher setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置字体颜色
     */
    public ZTextSwitcher setTextGravity(int gravity) {
        this.textGravity = gravity;
        return this;
    }

    /**
     * 设置固定行数
     */
    public ZTextSwitcher setShowLine(int showLine) {
        this.showLine = showLine;
        return this;
    }

    /**
     * 设置切换间隔
     */
    public ZTextSwitcher setSwitchInterval(long switchInterval) {
        this.switchInterval = switchInterval;
        return this;
    }

    /**
     * 设置出动画
     */
    public ZTextSwitcher setOutAnima(Context context, int anim) {
        super.setOutAnimation(context, anim);
        return this;
    }

    /**
     * 设置进动画
     */
    public ZTextSwitcher setInAnima(Context context, int anim) {
        super.setInAnimation(context, anim);
        return this;
    }


    /**
     * 开始循环播放
     * <p>
     * 在Activity或者Fragment的onResume中调用
     */
    public void startSwitcher() {
        if ((listText == null || listText.size() == 0) && isNeedCutout && !TextUtils.isEmpty(text) && width > 0) {
            listText = getTextList(text, getMeasuredWidth());
        }

        if (!isStart && listText != null && listText.size() > 0) {
            isStart = true;
            if (!isSetFactory) {
                isSetFactory = true;
                setFactory(new ViewFactory() {
                    @Override
                    public android.view.View makeView() {
                        TextView textView = new TextView(getContext());
                        textView.setMaxLines(showLine);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        textView.setTextColor(textColor);
                        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, textGravity));
                        //如果设置的不需要截取的ListText，则需要显示...
                        if (!isNeedCutout) {
                            textView.setEllipsize(TextUtils.TruncateAt.END);
                        }
                        return textView;
                    }
                });
            }
            textHandler.post(myRunnable);
        }
    }

    /**
     * 停止循环播放
     * <p>
     * 在Activity或者Fragment的onPause中调用
     */
    public void stopSwitcher() {
        if (isStart) {
            isStart = false;
            textHandler.removeCallbacks(myRunnable);
            listText = null;
        }
    }

    public boolean isStart() {
        return isStart;
    }


    public boolean isInit() {
        return isSetFactory && listText != null;
    }

    /*
    根据数据拼接每屏显示的字符串
     */
    private ArrayList<String> getTextList(String text, float viewWidth) {
        /*获取每行数据*/
        ArrayList<String> textList = new ArrayList<>();
        float length = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (length < viewWidth) {
                builder.append(text.charAt(i));
                length += mPaint.measureText(text.substring(i, i + 1));
                if (i == text.length() - 1) {
                    textList.add(builder.toString());
                }
            } else {
                textList.add(builder.substring(0, builder.length() - 1));
                builder.delete(0, builder.length() - 1);
                length = mPaint.measureText(text.substring(i, i + 1));
                i--;
            }
        }
            
        /*合并每showLine行的数据*/
        ArrayList<String> newTextList = new ArrayList<>();
        String lastStr = null;
        for (int i = 0; i < textList.size(); i++) {
            if (i % showLine != 0) {
                lastStr += textList.get(i);
                //到最大行数
                if (i % showLine == showLine - 1) {
                    newTextList.add(lastStr);
                }
            } else {
                lastStr = textList.get(i);
                if (i == textList.size() - 1) {
                    newTextList.add(lastStr);
                }
            }
        }
        return newTextList;
    }


    class TextHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ZTextSwitcher.super.setText(listText.get(curPosition));

            /*当数据1屏时才开始循环播放*/
            if (listText.size() > 1) {
                curPosition++;
                if (curPosition == listText.size()) {
                    curPosition = 0;
                }

                //是否继续循环，防止内存泄漏
                boolean isCycle = true;
                if (getContext() != null && getContext() instanceof Activity) {
                    isCycle = Build.VERSION.SDK_INT >= 17 && !((Activity) getContext()).isDestroyed();
                }

                if (isCycle) {
                    textHandler.postDelayed(myRunnable, switchInterval);
                }
            }
        }
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            textHandler.sendEmptyMessage(0);
        }
    }
}