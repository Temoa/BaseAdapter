package me.temoa.baseadapter.item_touch_helper;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by lai
 * on 2017/11/20.
 * <p>
 * RecyclerView ItemTouchHelp Callback implement
 */

public class SimpleItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;
    private int mDragFlag, mSwipeFlag;

    /**
     * @param dragFlag  ItemTouchHelper.UP
     *                  ItemTouchHelper.DOWN
     *                  ItemTouchHelper.UP | itemTouchHelper.DOWN
     * @param swipeFlag ItemTouchHelper.START
     *                  ItemTouchHelper.END
     *                  ItemTouchHelper.START | ItemTouchHelper.END
     */
    public void setTouchFlag(int dragFlag, int swipeFlag) {
        mDragFlag = dragFlag;
        mSwipeFlag = swipeFlag;
    }

    public SimpleItemTouchCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDragFlag, mSwipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        mAdapter.onItemMove(from, to);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        mAdapter.onItemRemove(pos);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setElevation(dp2px(recyclerView.getContext(), 6));
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.itemView.setElevation(dp2px(recyclerView.getContext(), 2));
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
