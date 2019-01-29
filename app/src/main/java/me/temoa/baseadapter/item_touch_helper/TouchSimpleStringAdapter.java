package me.temoa.baseadapter.item_touch_helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

/**
 * Created by lai
 * on 2017/11/20.
 */

public class TouchSimpleStringAdapter extends SimpleStringAdapter implements ItemTouchHelperAdapter {

    TouchSimpleStringAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onItemMove(int from, int to) {
        // Collections.swap(mItems, from, to);
        // notifyItemMoved(from, to);
        // ItemTouchHelper.Callback onMove 方法是实时调用的,item 的 position 也需要实时调用, 不是的话 position 会错乱
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemRemove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
