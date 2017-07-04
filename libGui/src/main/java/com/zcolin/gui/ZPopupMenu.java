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
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能按钮弹出
 */
public class ZPopupMenu {
    //列表弹窗的间隔

    private OnItemOnClickListener mItemOnClickListener;
    private ArrayList<Item> listAction = new ArrayList<>();
    private PopupWindow  popupWindow;
    private RecyclerView recyclerView;
    private LinearLayout rootLayout;
    private Context      mContext;
    private int          itemWidth;
    private boolean      isAddDimView;
    private boolean      isDim;

    public static ZPopupMenu instance(Context context) {
        return new ZPopupMenu(context);
    }

    public static ZPopupMenu instance(Context context, int width, int height) {
        return new ZPopupMenu(context, width, height);
    }

    public ZPopupMenu(Context context) {
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public ZPopupMenu(Context context, int width, int height) {
        this.mContext = context;
        itemWidth = width;
        popupWindow = new PopupWindow();
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        initUI();

        setBackgroundColor(Color.WHITE);
        int padding = ZUIHelper.dip2px(mContext, 3);
        setPadding(0, padding, 0, padding);
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        rootLayout = new LinearLayout(mContext);
        recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        rootLayout.addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(Color.parseColor("#55000000"));
        ll.addView(rootLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        popupWindow.setContentView(ll);
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
     * 设置分割线
     */
    public ZPopupMenu addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
        return this;
    }

    /**
     * 设置分割线
     */
    public ZPopupMenu addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        recyclerView.addItemDecoration(itemDecoration, index);
        return this;
    }

    /**
     * 设置背景是否变暗
     */
    public ZPopupMenu setIsDim(boolean isDim) {
        this.isDim = isDim;
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
        show(view, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view, int xoff, int yoff, int gravity) {
        if (isDim && !isAddDimView) {
            View dismissView = new View(mContext);
            ((LinearLayout) popupWindow.getContentView()).addView(dismissView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            dismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            isAddDimView = true;
        }

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
        if (isDim && !isAddDimView) {
            View dismissView = new View(mContext);
            ((LinearLayout) popupWindow.getContentView()).addView(dismissView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            dismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            isAddDimView = true;
        }

        recyclerView.setAdapter(new MYAdapter());
        popupWindow.showAtLocation(view, xoff, yoff, gravity);
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addAction(String action) {
        return addAction(action, false);
    }

    /**
     * 添加子类项
     */
    public ZPopupMenu addAction(String action, boolean isSelected) {
        Item item = new Item();
        item.setText(action);
        item.isSelected = isSelected;
        addAction(item);
        return this;
    }

    /**
     * 添加子类项集合
     */
    public ZPopupMenu addActions(String[] action) {
        return addActions(action, -1);
    }

    /**
     * 添加子类项集合
     */
    public ZPopupMenu addActions(String[] action, int selectedIndex) {
        if (action != null) {
            addActions(Arrays.asList(action), selectedIndex);
        }
        return this;
    }

    /**
     * 添加子类项集合
     */
    public ZPopupMenu addActions(List<String> listAction, int selectedIndex) {
        if (listAction != null) {
            for (int i = 0; i < listAction.size(); i++) {
                Item item = new Item();
                item.setText(listAction.get(i));
                if (i == selectedIndex) {
                    item.isSelected = true;
                }
                addAction(item);
            }
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
    public ZPopupMenu setOnItemClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
        return this;
    }

    public ZPopupMenu setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        popupWindow.setOnDismissListener(onDismissListener);
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
        Drawable       drawableLeft;
        int            drawablePadding;
        Drawable       background;
        boolean        isSetBackground;//是否用户主动设置了background的标志
        int            gravity;
        float          textSize;
        int            textColor;
        ColorStateList colorStateList;
        boolean        isSelected;

        int paddingLeft   = 0;
        int paddingRight  = 0;
        int paddingTop    = 0;
        int paddingBottom = 0;

        public Item setDrawableLeft(Drawable drawableLeft) {
            this.drawableLeft = drawableLeft;
            return this;
        }

        public Item setDrawableLeft(Context context, int drawableLeft) {
            this.drawableLeft = context.getResources()
                                       .getDrawable(drawableLeft);
            return this;
        }

        /**
         * 单位dp
         */
        public Item setDrawablePadding(int padding) {
            this.drawablePadding = padding;
            return this;
        }

        public Item setBackground(Drawable drawable) {
            isSetBackground = true;
            this.background = drawable;
            return this;
        }

        public Item setBackground(Context context, int drawable) {
            isSetBackground = true;
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

        /**
         * 单位sp
         */
        public Item setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Item setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Item setTextColorStateList(ColorStateList colorStateList) {
            this.colorStateList = colorStateList;
            return this;
        }

        /**
         * 单位DP
         */
        public Item setPadding(int left, int top, int right, int bottom) {
            this.paddingLeft = left;
            this.paddingRight = right;
            this.paddingTop = top;
            this.paddingBottom = bottom;
            return this;
        }

        public Item setSelected(boolean isSelected) {
            this.isSelected = isSelected;
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

            Context context = holder.itemView.getContext();
            holder.textView.setPadding(item.paddingLeft, item.paddingTop, item.paddingRight, item.paddingBottom);
            if (item.colorStateList != null) {
                holder.textView.setTextColor(item.colorStateList);
            } else if (item.textColor == 0) {
                holder.textView.setTextColor(mContext.getResources()
                                                     .getColorStateList(R.color.gui_listitem_popup_selector));
            } else {
                holder.textView.setTextColor(item.textColor);
            }
            holder.textView.setTextSize(item.textSize == 0 ? 16 : item.textSize);
            holder.textView.setGravity(item.gravity == 0 ? Gravity.CENTER : item.gravity);
            holder.textView.setSingleLine(true);
            holder.textView.setText(item.text);
            holder.textView.setCompoundDrawablePadding(ZUIHelper.dip2px(context, item.drawablePadding));
            if (item.isSetBackground) {
                holder.textView.setBackgroundDrawable(item.background);
            } else {
                holder.textView.setBackgroundResource(R.drawable.gui_listitem_popup_selector);
            }
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(item.drawableLeft, null, null, null);
            holder.textView.setSelected(item.isSelected);
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

            //此代码是为了使用ZRecyclerView中的ZRecycleViewDivider的时候，最后一条不画线，如果使用自定义的Divider，可以自己处理
            if (position == getItemCount() - 1) {
                holder.itemView.setTag("reservedView");
            } else {
                holder.itemView.setTag(null);
            }


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
                textView.setLayoutParams(new RecyclerView.LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
