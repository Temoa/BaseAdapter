package me.temoa.baseadapter.item_touch_helper;

/**
 * Created by lai
 * on 2017/11/20.
 * <p>
 * ItemTouchHelper interface
 */

public interface ItemTouchHelperAdapter {

    /**
     * When ItemTouchHelper.Callback onMove is called
     *
     * @param from start position
     * @param to   end position
     */
    void onItemMove(int from, int to);

    /**
     * When ItemTouchHelper.Callback onSwiped is called
     *
     * @param position target position
     */
    void onItemRemove(int position);
}
