package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import me.temoa.base.adapter.BaseAdapter;
import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2018/5/8.
 */

@SuppressWarnings("unused") //public api
public class BaseHelperAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    @NonNull
    private final BaseAdapter<T> mInnerAdapter;

    BaseHelperAdapter(@NonNull BaseAdapter<T> innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    public List<T> getData() {
        return mInnerAdapter.getData();
    }

    public void setNewData(List<T> newItems) {
        mInnerAdapter.setNewData(newItems);
        notifyDataSetChanged();
    }

    public void addData(List<T> items) {
        mInnerAdapter.addData(items);
        notifyDataSetChanged();
    }

    public void addData(T item) {
        mInnerAdapter.addData(item);
        notifyDataSetChanged();
    }

    public void addData(int position, T item) {
        mInnerAdapter.addData(position, item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
