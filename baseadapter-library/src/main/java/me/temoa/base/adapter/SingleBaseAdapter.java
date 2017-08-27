package me.temoa.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lai
 * on 2017/8/26 16:05
 */

public abstract class SingleBaseAdapter<T> extends BaseAdapter<T> {

    public SingleBaseAdapter(Context context, List<T> items) {
        super(context, items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != VIEW_TYPE_FOOTER) {
            View itemView = mLayoutInflater.inflate(getItemLayoutId(), parent, false);
            return new ViewHolder(itemView);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (getItemViewType(position) != VIEW_TYPE_FOOTER)
            convert(holder, mItems.get(position), position);
    }

    @Override
    public int getViewType(T item, int position) {
        return 0;
    }

    protected abstract void convert(ViewHolder holder, T item, int position);

    protected abstract int getItemLayoutId();
}
