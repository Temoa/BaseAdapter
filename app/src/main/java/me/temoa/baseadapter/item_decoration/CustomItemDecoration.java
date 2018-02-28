package me.temoa.baseadapter.item_decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lai
 * on 2018/2/28.
 */

@SuppressWarnings({"WeakerAccess"}) // public api
public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "CustomItemDecoration";

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Style mStyle = Style.DEFAULT;
    private Drawable mDivider;
    private final Rect mBounds = new Rect();
    private int color;
    private int mMarginLeft = 0;
    private int mMarginRight = 0;
    private Paint mPaint;

    public enum Style {
        DEFAULT,
        DRAWABLE,
        PATH
    }

    public CustomItemDecoration(Builder builder) {
        mStyle = builder.mStyle;
        if (mStyle == Style.DEFAULT) {
            final TypedArray a = builder.mContext.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        } else if (mStyle == Style.DRAWABLE) {
            mDivider = builder.mDrawable;
        } else {
            mPaint = builder.mPaint;
            if (mPaint == null) {
                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(1);
                mPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 10.0f}, 0));
                mPaint.setColor(Color.BLACK);
            }
        }

        mMarginLeft = builder.mMarginLeft;
        mMarginRight = builder.mMarginRight;
    }

    public void setColor(@ColorInt int color) {
        if (color == 0) {
            throw new IllegalArgumentException("Color cannot be 0.");
        }
        int size = 1;
        if (mDivider != null) {
            size = mDivider.getIntrinsicHeight();
        }
        mDivider = new DividerDrawable(color, size);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null
                || (mDivider == null && mStyle == Style.DRAWABLE
                || mDivider == null && mStyle == Style.DEFAULT)) {
            return;
        }
        c.save();
        final int left;
        final int right;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mMarginLeft;
            right = parent.getWidth() - parent.getPaddingRight() - mMarginRight;
            c.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = mMarginLeft;
            right = parent.getWidth() - mMarginRight;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());

            if (mStyle == Style.DRAWABLE || mStyle == Style.DEFAULT) {
                final int top = bottom - mDivider.getIntrinsicHeight();
                // draw drawable
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            } else {
                // draw path
                Log.d(TAG, "onDraw: ");
                Path path = new Path();
                path.moveTo(left, bottom);
                path.lineTo(right, bottom);
                c.drawPath(path, mPaint);
            }
        }
        c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }

    public static class Builder {

        private Context mContext;
        private Style mStyle;
        private Drawable mDrawable;
        private int mColor;
        private int mMarginLeft = 0;
        private int mMarginRight = 0;
        private Paint mPaint;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder style(Style style) {
            mStyle = style;
            return this;
        }

        public Builder drawable(Drawable drawable) {
            mDrawable = drawable;
            return this;
        }

        public Builder color(int color) {
            mColor = color;
            return this;
        }

        public Builder paint(Paint paint) {
            mPaint = paint;
            return this;
        }

        public CustomItemDecoration build() {
            return new CustomItemDecoration(this);
        }
    }

    private class DividerDrawable extends ColorDrawable {
        private int size;

        public DividerDrawable(int color, int size) {
            super(color);
            this.size = size;
        }

        @Override
        public int getIntrinsicHeight() {
            return size;
        }

        @Override
        public int getIntrinsicWidth() {
            return size;
        }
    }
}
