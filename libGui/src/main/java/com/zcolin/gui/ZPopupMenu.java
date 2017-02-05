/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-20 下午2:22
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 功能按钮弹出
 */
public class ZPopupMenu {
    //列表弹窗的间隔

    private OnItemOnClickListener mItemOnClickListener;
    private ArrayList<Item> listAction = new ArrayList<>();
    private PopupWindow  popupWindow;
    private RecyclerView recyclerView;
    private FrameLayout  rootLayout;
    private Context      mContext;

    public ZPopupMenu(Context context) {
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public ZPopupMenu(Context context, int width, int height) {
        this.mContext = context;
        popupWindow = new PopupWindow();
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        initUI();

        setBackground(R.color.gui_white_light);
        int padding = ZUIHelper.dip2px(mContext, 10);
        setPadding(padding, 0, padding, 0);
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        rootLayout = new FrameLayout(mContext);
        recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        rootLayout.addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        popupWindow.setContentView(rootLayout);
    }

    /**
     * 设置背景
     */
    public ZPopupMenu setBackgroundColor(int color) {
        rootLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置背景
     */
    public ZPopupMenu setBackground(Drawable background) {
        rootLayout.setBackgroundDrawable(background);
        return this;
    }

    /**
     * 设置背景
     */
    public ZPopupMenu setBackground(int background) {
        rootLayout.setBackgroundResource(background);
        return this;
    }

    /**
     * 设置边距
     */
    public ZPopupMenu setPadding(int left, int top, int right, int bottom) {
        rootLayout.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        this.show(view, 0, 0);
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view, int xoff, int yoff) {
        recyclerView.setAdapter(new MYAdapter());
        popupWindow.showAsDropDown(view, xoff, yoff);
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view, int xoff, int yoff, int gravity) {
        recyclerView.setAdapter(new MYAdapter());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(view, xoff, yoff, gravity);
        } else {
            popupWindow.showAsDropDown(view, xoff, yoff);
        }
    }

    /**
     * 显示弹窗列表界面
     */
    public void showAtLocation(View view, int xoff, int yoff, int gravity) {
        recyclerView.setAdapter(new MYAdapter());
        popupWindow.showAtLocation(view, xoff, yoff, gravity);
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addAction(String action) {
        Item item = new Item();
        item.setText(action);
        addAction(item);
        return this;
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addActions(String[] action) {
        if (action != null) {
            Item[] item = new Item[action.length];
            for (int i = 0; i < action.length; i++) {
                item[i] = new Item();
                item[i].setText(action[i]);
            }
            addActions(item);
        }
        return this;
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addAction(Item action) {
        if (action != null) {
            listAction.add(action);
        }
        return this;
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addActions(Item[] action) {
        if (action != null) {
            listAction.addAll(Arrays.asList(action));
        }
        return this;
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addActions(ArrayList<Item> action) {
        if (action != null) {
            listAction.addAll(action);
        }
        return this;
    }

    /**
     * 清除子类项
     */
    public ZPopupMenu cleanAction() {
        if (listAction.isEmpty()) {
            listAction.clear();
        }
        return this;
    }

    /**
     * 根据位置得到子类项
     */
    public Item getAction(int position) {
        if (position < 0 || position > listAction.size())
            return null;
        return listAction.get(position);
    }

    /**
     * 设置监听事件
     */
    public ZPopupMenu setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
        return this;
    }

    /**
     * 弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {

        /**
         * @return ture 消失，false不消失
         */
        public boolean onItemClick(Item item, int position);
    }

    /**
     *
     */
    public static class Item {
        public CharSequence text;
        Drawable drawable;
        Drawable background;
        int      gravity;
        float    textSize;
        int      textColor;

        public Item setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Item setDrawable(Context context, int drawable) {
            this.drawable = context.getResources()
                                   .getDrawable(drawable);
            return this;
        }

        public Item setBackground(Drawable drawable) {
            this.background = drawable;
            return this;
        }

        public Item setBackground(Context context, int drawable) {
            this.background = context.getResources()
                                     .getDrawable(drawable);
            return this;
        }

        public Item setText(CharSequence title) {
            this.text = title;
            return this;
        }

        public Item setText(Context context, int title) {
            this.text = context.getResources()
                               .getText(title);
            return this;
        }

        public Item setGravity(Context context, int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Item setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Item setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }
    }

    class MYAdapter extends RecyclerView.Adapter<MYAdapter.MYViewHolder> {
        @Override
        public MYViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayout = new TextView(parent.getContext());
            return new MYViewHolder(itemLayout);
        }

        @Override
        public void onBindViewHolder(MYViewHolder holder, final int position) {
            Item item = listAction.get(position);
            holder.textView.setTextColor(item.textColor == 0 ? mContext.getResources()
                                                                       .getColor(R.color.gui_black_light) : item.textColor);
            holder.textView.setTextSize(item.textSize == 0 ? 16 : item.textSize);
            holder.textView.setGravity(item.gravity == 0 ? Gravity.CENTER : item.gravity);
            holder.textView.setPadding(0, 10, 0, 10);
            holder.textView.setSingleLine(true);
            holder.textView.setText(item.text);
            holder.textView.setCompoundDrawablePadding(10);
            holder.textView.setBackgroundDrawable(item.background);
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(item.drawable, null, null, null);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemOnClickListener != null) {
                        if (mItemOnClickListener.onItemClick(listAction.get(position), position)) {
                            popupWindow.dismiss();
                        }
                    } else {
                        popupWindow.dismiss();
                    }
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return listAction.size();
        }

        class MYViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MYViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}
