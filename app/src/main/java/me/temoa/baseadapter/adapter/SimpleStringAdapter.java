package me.temoa.baseadapter.adapter;

import android.content.Context;

import java.util.List;

import me.temoa.base.adapter.SimpleBaseAdapter;
import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class SimpleStringAdapter extends SimpleBaseAdapter<String> {

    public SimpleStringAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item, int position) {
        holder.setText(R.id.item_tv, item);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item;
    }
}
