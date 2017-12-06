package me.temoa.baseadapter.item_touch_helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;
import me.temoa.baseadapter.item_touch_helper.ItemTouchHelperAdapter;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2017/11/20.
 */

public class TouchSimpleStringAdapter extends SimpleStringAdapter implements ItemTouchHelperAdapter {

    public TouchSimpleStringAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(mItems, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemRemove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
