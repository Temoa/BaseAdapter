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

public class EmptyHelperAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private int mEmptyViewLayoutId;

    @NonNull
    private final RecyclerView.Adapter mInnerAdapter;

    public void setEmptyView(int id) {
        mEmptyViewLayoutId = id;
    }

    private boolean isEmpty() {
        return mInnerAdapter.getItemCount() == 0;
    }

    public EmptyHelperAdapter(@NonNull RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isEmpty()) {
            if (mEmptyViewLayoutId == 0) throw new RuntimeException("Should set empty view!!");
            View view = LayoutInflater.from(parent.getContext()).inflate(mEmptyViewLayoutId, parent, false);
            return new BaseViewHolder(view);
        } else {
            return (BaseViewHolder) mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (!isEmpty()) {
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        else return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) return Constants.VIEW_TYPE_EMPTY;
        else return mInnerAdapter.getItemViewType(position);
    }
}
