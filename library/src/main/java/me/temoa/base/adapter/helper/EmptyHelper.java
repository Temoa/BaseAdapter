package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class EmptyHelper extends RecyclerView.Adapter<BaseViewHolder> {

    private int emptyViewLayoutId;

    @NonNull
    private final RecyclerView.Adapter innerAdapter;

    public void setEmptyView(int id) {
        emptyViewLayoutId = id;
    }

    private boolean isEmpty() {
        return innerAdapter.getItemCount() == 0;
    }

    public EmptyHelper(@NonNull RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            if (emptyViewLayoutId == 0) throw new RuntimeException("Should set empty view!!");
            View view = LayoutInflater.from(parent.getContext()).inflate(emptyViewLayoutId, parent, false);
            return new BaseViewHolder(view);
        } else {
            return (BaseViewHolder) innerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (!isEmpty()) {
            innerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        else return innerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) return Constants.VIEW_TYPE_EMPTY;
        else return innerAdapter.getItemViewType(position);
    }
}
