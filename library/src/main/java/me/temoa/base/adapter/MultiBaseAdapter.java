package me.temoa.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lai
 * on 2017/8/26 15:41
 */

public abstract class MultiBaseAdapter<T> extends BaseAdapter<T> {

    public MultiBaseAdapter(Context context, List<T> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent,viewType);
        View itemView = mLayoutInflater.inflate(getItemLayoutId(viewType), parent, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int viewType = holder.getItemViewType();
        convert(holder, mItems.get(position), position, viewType);
    }

    @Override
    public int getViewType(T item, int position) {
        return getMultiViewType(item, position);
    }

    protected abstract void convert(BaseViewHolder holder, T item, int position, int viewType);

    protected abstract int getItemLayoutId(int viewType);

    protected abstract int getMultiViewType(T item, int position);
}
