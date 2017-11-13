package me.temoa.base.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by Lai
 * on 2017/9/20 21:04
 */

public abstract class OnClickGestureListener extends GestureDetector.SimpleOnGestureListener {

    private RecyclerView mRecyclerView;

    public abstract void onClick(View v, int position);

    public abstract void onLongClick(View v, int position);

    protected OnClickGestureListener(RecyclerView recyclerView) {
        super();
        mRecyclerView = recyclerView;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) mRecyclerView.getChildViewHolder(childView);
            int position = baseViewHolder.getLayoutPosition();
            onClick(baseViewHolder.itemView, position);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) mRecyclerView.getChildViewHolder(childView);
            int position = baseViewHolder.getLayoutPosition();
            onLongClick(baseViewHolder.itemView, position);
        }
    }
}
