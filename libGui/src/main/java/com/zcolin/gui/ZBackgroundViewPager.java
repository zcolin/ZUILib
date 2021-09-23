package com.zcolin.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 可以设置背景，并且有视差效果的ViewPager
 */
public class ZBackgroundViewPager extends ViewPager {

    private       float           mBackgroundX = -1.0F;
    private       float           fraction     = 0.0F;
    private       Rect            mBackgroundOriginalRect;
    private       RectF           mBackgroundNewRect;
    private       int             mBitmapRes   = -1;
    private       int             mFactor      = 1;
    private       Bitmap          mBitmap;
    private final DataSetObserver observer;

    public ZBackgroundViewPager(Context context) {
        this(context, null);
    }

    public ZBackgroundViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.observer = new DataSetObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                ZBackgroundViewPager.this.processBitmap();
                ZBackgroundViewPager.this.invalidate();
                ZBackgroundViewPager.this.requestLayout();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                ZBackgroundViewPager.this.processBitmap();
                ZBackgroundViewPager.this.invalidate();
                ZBackgroundViewPager.this.requestLayout();
            }
        };

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZBackgroundViewPager);
        if (this.mFactor < 1) {
            throw new IllegalArgumentException("Factor value can't be less than 1");
        } else {
            this.mBitmapRes = a.getResourceId(R.styleable.ZBackgroundViewPager_bg_view_pager_image, -1);
            a.recycle();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        float over = (float) (position * this.getWidth() + positionOffsetPixels);
        this.mBackgroundX = -over * this.fraction;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.processBitmap();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            int save = canvas.save();
            Rect rect = canvas.getClipBounds();
            canvas.translate((float) rect.left, (float) rect.top);
            canvas.drawBitmap(this.mBitmap,
                              this.mBackgroundOriginalRect,
                              new RectF(this.mBackgroundX,
                                        0.0F,
                                        this.mBackgroundNewRect.right + this.mBackgroundX,
                                        this.mBackgroundNewRect.bottom),
                              null);
            canvas.restoreToCount(save);
        }

        super.dispatchDraw(canvas);
    }

    private void processBitmap() {
        int w = this.getWidth();
        int h = this.getHeight();
        if (this.mBitmapRes > -1) {
            Drawable drawable = this.getResources().getDrawable(this.mBitmapRes);
            if (drawable instanceof BitmapDrawable) {
                this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                this.mBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(this.mBitmap);
                drawable.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
                drawable.draw(canvas);
            }
        }

        if (this.mBitmap != null && w != 0 && h != 0) {
            float count = 1.0F;
            if (this.getAdapter() != null && this.getAdapter().getCount() > 0) {
                count = (float) this.getAdapter().getCount();
            }

            int imgWidth = this.mBitmap.getWidth();
            int imgHeight = this.mBitmap.getHeight();
            float ratio = (float) imgWidth / (float) imgHeight;
            float width = (float) h * ratio;
            float height = (float) h;
            this.mBackgroundOriginalRect = new Rect(0, 0, imgWidth, imgHeight);
            this.mBackgroundNewRect = new RectF(0.0F, 0.0F, width, height);
            Rect sizeRect = new Rect(0, 0, w * (int) ratio, h);
            this.fraction = (float) sizeRect.width() / ((float) w * count);
        }
    }

    public void setFlowImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.processBitmap();
    }

    public void setFlowImage(int resId) {
        this.mBitmap = BitmapFactory.decodeResource(this.getResources(), resId);
        this.processBitmap();
        this.invalidate();
        this.requestLayout();
    }

    public void setFlowFactor(int factor) {
        if (factor < 1) {
            throw new IllegalArgumentException("Factor value can't be less than 1");
        } else {
            this.mFactor = factor;
            this.processBitmap();
            this.invalidate();
            this.requestLayout();
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (this.getAdapter() != null) {
            this.getAdapter().unregisterDataSetObserver(this.observer);
        }

        super.setAdapter(adapter);
        if (adapter == null) {
            this.processBitmap();
            this.invalidate();
            this.requestLayout();
        } else {
            adapter.registerDataSetObserver(this.observer);
        }
    }
}
