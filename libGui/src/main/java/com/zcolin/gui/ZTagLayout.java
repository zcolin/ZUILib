/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-1-13 上午11:31
 * ********************************************************
 */

package com.zcolin.gui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcolin.gui.helper.ZUIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签控件流式布局
 */
public class ZTagLayout extends RelativeLayout {
    public static final float   DEFAULT_TAG_TEXT_SIZE              = 14f;                       //默认字体大小
    public static final float   DEFAULT_TAG_DELETE_INDICATOR_SIZE  = 14f;                       //默认删除按钮大小
    public static final float   DEFAULT_TAG_LAYOUT_BORDER_SIZE     = 0f;                        //默认边框宽度
    public static final float   DEFAULT_TAG_RADIUS                 = 100;                       //默认圆角角度
    public static final int     DEFAULT_TAG_LAYOUT_COLOR           = Color.parseColor("#00BFFF");//默认背景颜色
    public static final int     DEFAULT_TAG_LAYOUT_COLOR_PRESS     = Color.parseColor("#88363636");//默认背景按下颜色
    public static final int     DEFAULT_TAG_TEXT_COLOR             = Color.parseColor("#ffffff");//默认字体颜色
    public static final int     DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");//默认删除按钮颜色
    public static final int     DEFAULT_TAG_LAYOUT_BORDER_COLOR    = Color.parseColor("#ffffff");//默认边框颜色
    public static final String  DEFAULT_TAG_DELETE_ICON            = "×";                       //删除
    public static final boolean DEFAULT_TAG_IS_DELETABLE           = false;                     //是否可以删除

    private int mWidth;             //本控件的宽度
    private int lineMargin;         //行纵向margin
    private int tagMargin;          //tag横向margin
    private int textPaddingLeft;    //文字padding    
    private int textPaddingRight;   //文字padding
    private int textPaddingTop;     //文字padding
    private int texPaddingBottom;   //文字padding
    private List<Tag> mTags = new ArrayList<>(); //tag集合
    private LayoutInflater      mInflater;
    private OnTagClickListener  mClickListener;//点击回调
    private OnTagDeleteListener mDeleteListener;//删除回调

    public ZTagLayout(Context context) {
        super(context, null);
        init(context, null, 0, 0);
    }

    public ZTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ZTagLayout(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        init(ctx, attrs, defStyle, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZTagLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        float def1 = ZUIHelper.dip2px(this.getContext(), 5);
        float def2 = ZUIHelper.dip2px(this.getContext(), 8);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ZTagLayout, defStyle, defStyleRes);
        this.lineMargin = (int) typeArray.getDimension(R.styleable.ZTagLayout_lineMargin, def1);
        this.tagMargin = (int) typeArray.getDimension(R.styleable.ZTagLayout_tagMargin, def1);
        int textPadding = (int) typeArray.getDimension(R.styleable.ZTagLayout_textPadding, -1);
        this.textPaddingLeft = textPadding != -1 ? textPadding : (int) typeArray.getDimension(R.styleable.ZTagLayout_textPaddingLeft, def2);
        this.textPaddingRight = textPadding != -1 ? textPadding : (int) typeArray.getDimension(R.styleable.ZTagLayout_textPaddingRight, def2);
        this.textPaddingTop = textPadding != -1 ? textPadding : (int) typeArray.getDimension(R.styleable.ZTagLayout_textPaddingTop, def1);
        this.texPaddingBottom = textPadding != -1 ? textPadding : (int) typeArray.getDimension(R.styleable.ZTagLayout_textPaddingBottom, def1);
        typeArray.recycle();
        mWidth = ZUIHelper.getScreenWidth(context);
        // this.setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    /**
     * 创建Tag
     */
    public Tag createTag(String text) {
        return new Tag(text);
    }

    /**
     * 添加Tag，如果在循环中，不要调用次函数，使用addTags
     */
    public void addTag(Tag tag) {
        mTags.add(tag);
        setUpTags();
    }

    /**
     * 批量添加tag
     */
    public void addTags(String[] tags) {
        if (tags == null || tags.length <= 0)
            return;
        for (String item : tags) {
            Tag tag = new Tag(item);
            mTags.add(tag);
        }
        setUpTags();
    }

    /**
     * 批量添加tag
     */
    public void addTags(List<Tag> tagList) {
        if (tagList == null || tagList.size() <= 0)
            return;
        mTags.addAll(tagList);
        setUpTags();
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void remove(int position) {
        mTags.remove(position);
        setUpTags();
    }

    public void removeAllTags() {
        mTags.clear();
        setUpTags();
    }

    public int getLineMargin() {
        return lineMargin;
    }

    public void setLineMargin(float lineMargin) {
        this.lineMargin = ZUIHelper.dip2px(getContext(), lineMargin);
    }

    public int getTagMargin() {
        return tagMargin;
    }

    public void setTagMargin(float tagMargin) {
        this.tagMargin = ZUIHelper.dip2px(getContext(), tagMargin);
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(float textPaddingLeft) {
        this.textPaddingLeft = ZUIHelper.dip2px(getContext(), textPaddingLeft);
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(float textPaddingRight) {
        this.textPaddingRight = ZUIHelper.dip2px(getContext(), textPaddingRight);
    }

    public int getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = ZUIHelper.dip2px(getContext(), textPaddingTop);
    }

    public int getTexPaddingBottom() {
        return texPaddingBottom;
    }

    public void setTexPaddingBottom(float texPaddingBottom) {
        this.texPaddingBottom = ZUIHelper.dip2px(getContext(), texPaddingBottom);
    }

    public void setOnTagClickListener(OnTagClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setOnTagDeleteListener(OnTagDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }


    private void setUpTags() {
        if (getVisibility() != View.VISIBLE)
            return;
        removeAllViews();
        float total = getPaddingLeft() + getPaddingRight();
        int listIndex = 1;   // List Index
        int index_bottom = 1;// The Tag to add below
        int index_header = 1;// The header tag of this line
        Tag tag_pre = null;
        for (Tag item : mTags) {
            final int position = listIndex - 1;
            final Tag tag = item;

            final View tagLayout = mInflater.inflate(R.layout.gui_view_tagview, null);
            tagLayout.setId(listIndex);
            tagLayout.setBackgroundDrawable(getSelector(tag));
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tv_tag_item_contain);
            tagView.setText(tag.text);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
            params.setMargins(textPaddingLeft, textPaddingTop, textPaddingRight, texPaddingBottom);
            tagView.setLayoutParams(params);
            tagView.setTextColor(getTextSelector(tag));
            tagView.setTextSize(tag.tagTextSize);
            if (tag.ems > 0) {
                tagView.setEms(tag.ems);
            }
            if (tag.maxEms > 0) {
                tagView.setMaxEms(tag.maxEms);
            }
            if (tag.minEms > 0) {
                tagView.setMinEms(tag.minEms);
            }
            tagLayout.setSelected(tag.isSelected);
            tagLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int size = getChildCount();
                    for (int i = 0; i < size; i++) {
                        getChildAt(i).setSelected(tagLayout == getChildAt(i));
                    }

                    if (mClickListener != null) {
                        mClickListener.onTagClick(position, tag);
                    }
                }
            });

            TextView deletableView = (TextView) tagLayout.findViewById(R.id.tv_tag_item_delete);
            if (tag.isDeletable) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(tag.deleteIcon);
                deletableView.setPadding(0, textPaddingTop, textPaddingRight, texPaddingBottom);
                deletableView.setTextColor(tag.deleteIndicatorColor);
                deletableView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tag.deleteIndicatorSize);
                deletableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZTagLayout.this.remove(position);
                        if (mDeleteListener != null) {
                            mDeleteListener.onTagDeleted(position, tag);
                        }
                    }
                });
            } else {
                deletableView.setVisibility(View.GONE);
            }

            //测量tabLayout的大小
            tagLayout.measure(0, 0);
            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tagParams.bottomMargin = lineMargin;
            if (mWidth <= total + tagMargin + tagLayout.getMeasuredWidth()) {//此行放不下
                tagParams.addRule(RelativeLayout.BELOW, index_bottom);
                total = getPaddingLeft() + getPaddingRight();
                index_bottom = listIndex;
                index_header = listIndex;
            } else {
                tagParams.addRule(RelativeLayout.ALIGN_TOP, index_header);
                if (listIndex != index_header) {
                    tagParams.addRule(RelativeLayout.RIGHT_OF, listIndex - 1);
                    tagParams.leftMargin = tagMargin;
                    total += tagMargin;
                    if (tag_pre.tagTextSize < tag.tagTextSize) {
                        index_bottom = listIndex;
                    }
                }
            }
            total += tagLayout.getMeasuredWidth();
            addView(tagLayout, tagParams);
            tag_pre = tag;
            listIndex++;
        }
    }

    private Drawable getSelector(Tag tag) {
        if (tag.isSetLayoutDrawable || tag.layoutDrawable != null)
            return tag.layoutDrawable;
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gd_normal = new GradientDrawable();
        gd_normal.setColor(tag.layoutColor);
        gd_normal.setCornerRadius(tag.radius);
        if (tag.layoutBorderSize > 0) {
            gd_normal.setStroke(ZUIHelper.dip2px(getContext(), tag.layoutBorderSize), tag.layoutBorderColor);
        }
        GradientDrawable gd_press = new GradientDrawable();
        gd_press.setColor(tag.layoutPressColor);
        gd_press.setCornerRadius(tag.radius);

        GradientDrawable gd_select = new GradientDrawable();
        gd_press.setColor(tag.layoutSelectColor);
        gd_press.setCornerRadius(tag.radius);

        states.addState(new int[]{android.R.attr.state_pressed}, gd_press);
        states.addState(new int[]{android.R.attr.state_selected}, gd_select);
        //must add state_pressed first，or state_pressed will not take effect
        states.addState(new int[]{}, gd_normal);
        return states;
    }

    private ColorStateList getTextSelector(Tag tag) {
        if (tag.tagTextColor == tag.tagPressTextColor && tag.tagTextColor == tag.tagSelectTextColor) {
            return ColorStateList.valueOf(tag.tagTextColor);
        }

        int[][] arrState = new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_selected}, {}};
        int[] arrColor = new int[]{tag.tagPressTextColor, tag.tagSelectTextColor, tag.tagTextColor};
        return new ColorStateList(arrState, arrColor);
    }

    public class Tag {
        public int id = 0;
        public String text;
        public int     tagTextColor         = DEFAULT_TAG_TEXT_COLOR;
        public int     tagPressTextColor    = DEFAULT_TAG_TEXT_COLOR;
        public int     tagSelectTextColor   = DEFAULT_TAG_TEXT_COLOR;
        public float   tagTextSize          = DEFAULT_TAG_TEXT_SIZE;
        public int     layoutColor          = DEFAULT_TAG_LAYOUT_COLOR;
        public int     layoutPressColor     = DEFAULT_TAG_LAYOUT_COLOR_PRESS;
        public int     layoutSelectColor    = DEFAULT_TAG_LAYOUT_COLOR_PRESS;
        public boolean isDeletable          = DEFAULT_TAG_IS_DELETABLE;
        public int     deleteIndicatorColor = DEFAULT_TAG_DELETE_INDICATOR_COLOR;
        public float   deleteIndicatorSize  = DEFAULT_TAG_DELETE_INDICATOR_SIZE;
        public float   radius               = DEFAULT_TAG_RADIUS;
        public String  deleteIcon           = DEFAULT_TAG_DELETE_ICON;
        public float   layoutBorderSize     = DEFAULT_TAG_LAYOUT_BORDER_SIZE;
        public int     layoutBorderColor    = DEFAULT_TAG_LAYOUT_BORDER_COLOR;
        public int      minEms;
        public int      maxEms;
        public int      ems;
        public boolean  isSelected;
        public Drawable layoutDrawable;
        boolean isSetLayoutDrawable;
        public Object data;

        public Tag(String text) {
            this.text = text;
        }

        public Tag setId(int id) {
            this.id = id;
            return this;
        }

        public Tag setTextColor(int tagTextColor) {
            this.tagTextColor = tagTextColor;
            return this;
        }

        public Tag setPressTextColor(int tagTextColor) {
            this.tagPressTextColor = tagTextColor;
            return this;
        }

        public Tag setSelectTextColor(int tagTextColor) {
            this.tagSelectTextColor = tagTextColor;
            return this;
        }

        public Tag setTextSize(int tagTextSize) {
            this.tagTextSize = tagTextSize;
            return this;
        }

        public Tag setLayoutColor(int color) {
            this.layoutColor = color;
            return this;
        }

        public Tag setLayoutPressColor(int color) {
            this.layoutPressColor = color;
            return this;
        }

        public Tag setLayoutSelectColor(int color) {
            this.layoutSelectColor = color;
            return this;
        }


        public Tag setIsDeleteAble(boolean isDeletable) {
            this.isDeletable = isDeletable;
            return this;
        }

        public Tag setDeleteIndicatorColor(int deleteIndicatorColor) {
            this.deleteIndicatorColor = deleteIndicatorColor;
            return this;
        }

        public Tag setDeleteIndicatorSize(float deleteIndicatorSize) {
            this.deleteIndicatorSize = deleteIndicatorSize;
            return this;
        }

        public Tag setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Tag setRadius(String deleteIcon) {
            this.deleteIcon = deleteIcon;
            return this;
        }

        public Tag setLayoutBorderSize(float layoutBorderSize) {
            this.layoutBorderSize = layoutBorderSize;
            return this;
        }

        public Tag setLayoutBorderColor(int layoutBorderColor) {
            this.layoutBorderColor = layoutBorderColor;
            return this;
        }

        public Tag setLayoutDrawable(int drawable) {
            this.layoutDrawable = getContext().getResources()
                                              .getDrawable(drawable);
            return this;
        }

        public Tag setBackground(Drawable drawable) {
            isSetLayoutDrawable = true;
            this.layoutDrawable = drawable;
            return this;
        }

        public Tag setData(Object obj) {
            this.data = obj;
            return this;
        }

        public Tag setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
            return this;
        }

        public Tag setMaxEms(int maxEms) {
            this.maxEms = maxEms;
            return this;
        }

        public Tag setMinEms(int minEms) {
            this.minEms = minEms;
            return this;
        }

        public Tag setEms(int ems) {
            this.ems = ems;
            return this;
        }

        public Object getData() {
            return data;
        }
    }

    /**
     * listener for tag delete
     */
    public interface OnTagClickListener {
        void onTagClick(int position, Tag tag);
    }

    /**
     * listener for tag delete
     */
    public interface OnTagDeleteListener {
        void onTagDeleted(int position, Tag tag);
    }
}