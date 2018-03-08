package me.temoa.baseadapter.item_decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by lai
 * on 2018/3/1.
 * <p>
 * 分割线实现吸顶的效果
 */

public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private GetDataListener mListener;

    private Paint mTextPaint, mBgPaint;
    private int mTopHeight = 100;

    private Drawable mDivider;
    private Rect mBounds = new Rect();

    StickyHeaderDecoration(Context context) {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(36);

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        c.save();
        final int left;
        final int right;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            c.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getLeft() + parent.getPaddingLeft();
        final int right = parent.getRight() - parent.getPaddingRight();
        String preProvinceName;
        String currentProvinceName = null;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            preProvinceName = currentProvinceName;
            currentProvinceName = mListener.getProvince(position);
            if (currentProvinceName == null
                    || TextUtils.equals(currentProvinceName, preProvinceName)) {
                continue;
            }
            int viewBottom = child.getBottom();
            float bottom = Math.max(mTopHeight, child.getTop());
            if (position + 1 < itemCount) {
                if (mListener.getIsFirst(position + 1) && viewBottom < bottom) {
                    bottom = viewBottom;
                }
            }

            c.drawRect(left, bottom - mTopHeight, right, bottom, mBgPaint);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float baseLine = bottom - (mTopHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentProvinceName, left + mTopHeight / 2, baseLine, mTextPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mListener.getIsFirst(parent.getChildAdapterPosition(view))) {
            outRect.top = mTopHeight;
        }
    }

    void setListener(GetDataListener listener) {
        mListener = listener;
    }

    public interface GetDataListener {
        boolean getIsFirst(int position);

        String getProvince(int position);
    }
}
