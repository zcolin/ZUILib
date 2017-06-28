/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;


/**
 * 菜单弹出框
 */
public class ZDialogMenu extends ZDialog<ZDialogMenu> {
    private static int LAYOUT_ID;

    protected LinearLayout                         llMenu;        //菜单容器布局
    protected TextView                             tvTitle;        //标题控件
    protected ZDialogParamSubmitInterface<Integer> submitInter;    // 点击确定按钮回调接口
    protected Context                              context;

    public static ZDialogMenu instance(Context context) {
        return new ZDialogMenu(context);
    }

    public static ZDialogMenu instance(Context context, @LayoutRes int layoutId) {
        return new ZDialogMenu(context, layoutId);
    }

    /**
     * 如果用户需要自己使用布局替代此xml文件，则需要在Application中初始化此函数，
     * 传入自定义的Layout，layout中的所有Id必须与本xml的Id相同
     */
    public static void initLayout(int layoutId) {
        LAYOUT_ID = layoutId;
    }

    public ZDialogMenu(Context context) {
        this(context, 0);
    }

    /**
     * @param context
     */
    public ZDialogMenu(Context context, @LayoutRes int layoutId) {
        super(context, layoutId == 0 ? (LAYOUT_ID == 0 ? R.layout.gui_dlg_menu : LAYOUT_ID) : layoutId);
        this.context = context;
        initRes();
    }

    private void initRes() {
        llMenu = getView(R.id.dialogmenu_ll);
        tvTitle = getView(R.id.dialogmenu_title);
    }

    public ZDialogMenu setTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(str);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    public ZDialogMenu setDatas(ArrayList<String> listData) {
        return setDatas(listData, null);
    }

    public ZDialogMenu setDatas(String[] arrStr) {
        return setDatas(arrStr, null);
    }

    public ZDialogMenu setDatas(ArrayList<String> listData, String defSelected) {
        return setDatas(listData.toArray(new String[listData.size()]), defSelected);
    }

    public ZDialogMenu setDatas(String[] arrStr, String defSelected) {
        return setDatas(arrStr, defSelected, getContext().getResources()
                                                         .getColorStateList(R.color.gui_listitem_dialogmenu_selector));
    }

    public ZDialogMenu setDatas(String[] arrStr, String defSelected, ColorStateList colorStateList) {
        int padding = (int) context.getResources()
                                   .getDimension(R.dimen.gui_dimens_big);
        int paddingLR = (int) context.getResources()
                                     .getDimension(R.dimen.gui_dimens_mid);
        float textSize = context.getResources()
                                .getDimension(R.dimen.gui_textsize_normal);
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        llMenu.removeAllViews();
        for (int i = 0, attrStrLength = arrStr.length; i < attrStrLength; i++) {
            final int index = i;
            String anAttrStr = arrStr[i];
            TextView tv = new TextView(context);
            tv.setText(anAttrStr);
            tv.setBackgroundResource(R.drawable.gui_dlg_menu_selector);
            tv.setTextSize(getContext().getResources()
                                       .getDimension(R.dimen.gui_textsize_normal));
            tv.setTextColor(colorStateList);
            tv.setPadding(paddingLR, padding, paddingLR, padding);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (anAttrStr != null && anAttrStr.equals(defSelected)) {
                tv.setSelected(true);
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (submitInter != null) {
                        submitInter.submit(index);
                        cancel();
                    }
                }
            });
            if (i > 0) {
                llMenu.addView(new ZDivider(context).setHeight(1));
            }
            llMenu.addView(tv, layout);
        }
        return this;
    }


    /**
     * 从底部显示对话框
     */
    public void showFromBottom() {
        tvTitle.setGravity(Gravity.CENTER);
        setGravity(Gravity.BOTTOM);
        setLayout(ZUIHelper.getScreenWidth(context), 0);
        setAnim(R.style.style_anim_dialog_bottom);
        show();
    }

    /**
     * 添加点击回调接口
     */
    public ZDialogMenu addSubmitListener(ZDialogParamSubmitInterface<Integer> submitInter) {
        this.submitInter = submitInter;
        return this;
    }
}
