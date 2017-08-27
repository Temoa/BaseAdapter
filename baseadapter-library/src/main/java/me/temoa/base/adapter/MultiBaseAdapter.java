package me.temoa.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lai
 * on 2017/8/26 15:41
 */

public abstract class MultiBaseAdapter<T> extends SingleBaseAdapter<T> {

    public MultiBaseAdapter(Context context, List<T> items) {
        super(context, items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = null;
        if (isOpenLoadMore && viewType == VIEW_TYPE_FOOTER) {
            holderView = mLayoutInflater.inflate(R.layout.item_footer_load, parent, false);
        } else {
            holderView = mLayoutInflater.inflate(getItemLayoutId(viewType), parent, false);
        }
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isOpenLoadMore && getItemViewType(position) == VIEW_TYPE_FOOTER) return;
        convert(holder, mItems.get(position), position, getItemViewType(position));
    }

    @Override
    public int getItemViewType(int position) {
        return getMultiViewType(mItems.get(position), position);
    }

    protected abstract void convert(ViewHolder holder, T item, int position, int viewType);

    protected abstract int getItemLayoutId(int viewType);

    protected abstract int getMultiViewType(T item, int position);
}
