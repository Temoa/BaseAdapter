package me.temoa.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lai
 * on 2017/8/26 16:05
 */

public abstract class SimpleBaseAdapter<T> extends BaseAdapter<T> {

    protected abstract void convert(BaseViewHolder holder, T item, int position);

    protected abstract int getItemLayoutId();

    public SimpleBaseAdapter(Context context, List<T> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View itemView = mLayoutInflater.inflate(getItemLayoutId(), parent, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        convert(holder, mItems.get(position), position);
    }

    @Override
    public int getViewType(T item, int position) {
        return 0;
    }
}
