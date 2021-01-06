/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-3-5 上午11:03
 * ********************************************************
 */
package com.zcolin.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;


/**
 * 自定义图标的TabView，多用于底部导航栏
 * <p>
 * 建议使用{@link {@link TabLayout } }
 */
public class ZTabView extends RelativeLayout implements OnClickListener, OnPageChangeListener {
    private int curTab = 0;                      //当前停留的Tab Index
    private LinearLayout llTabLay;                        //盛放TabView的容器
    private ZTabListener tabListener;                    //tab切换时回调接口
    private ViewPager    pager;                         //盛放内容的ViewPager
    private boolean      isSmoothScroll;                //viewpager是否平滑滚动
    private Context      context;

    public ZTabView(Context context) {
        this(context, null);
    }

    public ZTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        llTabLay = new LinearLayout(context);
        llTabLay.setOrientation(LinearLayout.HORIZONTAL);
        addView(llTabLay, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 增加tab选中回调
     */
    public void addZTabListener(ZTabListener tabListener) {
        this.tabListener = tabListener;
    }

    public void setOrientation(int orientation) {
        if (orientation == LinearLayout.VERTICAL) {
            llTabLay.setOrientation(orientation);
            llTabLay.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
            llTabLay.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        } else if (orientation == LinearLayout.HORIZONTAL) {
            llTabLay.setOrientation(orientation);
            llTabLay.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            llTabLay.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        } else {
            throw new IllegalArgumentException("orientation 只能为LinearLayout.HORIZONTAL或者LinearLayout.VERTICAL");
        }
    }

    public void setUpViewPager(ViewPager pager) {
        if (this.pager == pager) {
            return;
        }

        this.pager = pager;
        pager.addOnPageChangeListener(this);
        PagerAdapter adapter = pager.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 增加tab
     */
    public void addZTab(ZTab tab) {
        LinearLayout.LayoutParams params;
        if (llTabLay.getOrientation() == LinearLayout.HORIZONTAL) {
            params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        } else {
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        llTabLay.addView(tab, params);
        initTabItem(tab);
    }

    /**
     * 增加tab
     */
    public void addZTab(ViewGroup viewGroup) {
        ZTab tab = getChildZTab(viewGroup);
        if (tab == null) {
            throw new IllegalArgumentException("ViewGroup 中没有子控件是ZTab类型");
        }

        LinearLayout.LayoutParams params;
        if (llTabLay.getOrientation() == LinearLayout.HORIZONTAL) {
            params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        } else {
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        llTabLay.addView(viewGroup, params);
        initTabItem(tab);
    }

    private ZTab getChildZTab(ViewGroup viewGroup) {
        if (viewGroup.getChildCount() > 0) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ZTab) {
                    return (ZTab) view;
                } else if (view instanceof ViewGroup) {
                    return getChildZTab((ViewGroup) view);
                }
            }
        }
        return null;
    }

    private ZTab initTabItem(ZTab tab) {
        tab.setOnClickListener(this);
        tab.tabIndex = llTabLay.getChildCount() - 1;

        //默认选中第一个
        if (llTabLay.getChildCount() == 1) {
            llTabLay.getChildAt(0).setSelected(true);
        }
        return tab;
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewTextTab(String text) {
        return new ZTab(context, text);
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(int stateListDrawable, String text) {
        return new ZTab(context, stateListDrawable, text);
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(int drawAbleCommon, int drawAbleSelected, String text) {
        return new ZTab(context, drawAbleCommon, drawAbleSelected, text);
    }

    public LinearLayout getLlTabLay() {
        return llTabLay;
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(Drawable drawAbleCommon, Drawable drawAbleSelected, String text) {
        return new ZTab(context, drawAbleCommon, drawAbleSelected, text);
    }

    /**
     * 选中某个Tab 不会调用onTabSelect
     *
     * @param tab 选中的tabIndex
     */
    public void selectTab(int tab) {
        if (curTab == tab) {
            return;
        }

        int childCount = llTabLay.getChildCount();
        if (tab > childCount) {
            tab = childCount - 1;
        }

        if (tab < 0) {
            tab = 0;
        }

        for (int i = 0; i < childCount; i++) {
            if (tab != i) {
                llTabLay.getChildAt(i).setSelected(false);
            } else {
                llTabLay.getChildAt(i).setSelected(true);
            }
        }
        curTab = tab;
    }

    /**
     * 获取当前Tab Position
     */
    public int getCurZTab() {
        return curTab;
    }

    public void setSmoothScroll(boolean isSmoothScroll) {
        this.isSmoothScroll = isSmoothScroll;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ZTab) {
            boolean isIntercept = false;
            if (tabListener != null) {
                isIntercept = tabListener.onTabSelected((ZTab) v, ((ZTab) v).tabIndex);
            }

            if (!isIntercept && pager != null) {
                pager.setCurrentItem(((ZTab) v).tabIndex, isSmoothScroll);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        selectTab(arg0);
    }

    /**
     * 自定义TAB
     */
    public class ZTab extends androidx.appcompat.widget.AppCompatTextView {
        int tabIndex;

        private ZTab(Context context) {
            super(context);
        }

        private ZTab(Context context, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            setText(text);
        }

        private ZTab(Context context, int stateListDrawable, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDrawablesWithIntrinsicBounds(0, stateListDrawable, 0, 0);
            setText(text);
        }

        private ZTab(Context context, int iconCommon, int iconSelected, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDerawableTop(getResources().getDrawable(iconCommon), getResources().getDrawable(iconSelected));
            setText(text);
        }

        private ZTab(Context context, Drawable iconCommon, Drawable iconSelected, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDerawableTop(iconCommon, iconSelected);
            setText(text);
        }

        private void setCompoundDerawableTop(Drawable iconCommon, Drawable iconSelected) {
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_selected}, iconSelected);
            drawable.addState(new int[]{}, iconCommon);
            setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    /**
     * Tab选中回调
     */
    public interface ZTabListener {
        boolean onTabSelected(ZTab arg0, int index);
    }
}
