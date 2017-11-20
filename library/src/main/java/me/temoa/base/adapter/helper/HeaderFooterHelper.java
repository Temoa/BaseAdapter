package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class HeaderFooterHelper extends RecyclerView.Adapter<BaseViewHolder> {

    private View headerView;
    private View footerView;

    public void addHeader(View v) {
        headerView = v;
    }

    public void addFooter(View v) {
        footerView = v;
    }

    public void removeHeader() {
        headerView = null;
    }

    public void removeFooter() {
        footerView = null;
    }

    private int getHeaderCount() {
        return headerView == null ? 0 : 1;
    }

    private int getFooterCount() {
        return footerView == null ? 0 : 1;
    }

    @NonNull
    private final RecyclerView.Adapter innerAdapter;

    public HeaderFooterHelper(@NonNull RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == Constants.VIEW_TYPE_HEADER) {
            return new BaseViewHolder(headerView);
        } else if (footerView != null && viewType == Constants.VIEW_TYPE_FOOTER) {
            return new BaseViewHolder(footerView);
        } else {
            return (BaseViewHolder) innerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.VIEW_TYPE_HEADER) return;
        if (getItemViewType(position) == Constants.VIEW_TYPE_FOOTER) return;
        innerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return innerAdapter.getItemCount() + getHeaderCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0)
            return Constants.VIEW_TYPE_HEADER;
        else if (footerView != null && position == getItemCount() - 1)
            return Constants.VIEW_TYPE_FOOTER;
        else
            return innerAdapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LayoutFullSpanUtils.fixStaggeredGridLayoutFullSpanView(this, holder, Constants.VIEW_TYPE_HEADER);
        LayoutFullSpanUtils.fixStaggeredGridLayoutFullSpanView(this, holder, Constants.VIEW_TYPE_FOOTER);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        LayoutFullSpanUtils.fixGridLayoutFullSpanView(this, layoutManager, Constants.VIEW_TYPE_HEADER);
        LayoutFullSpanUtils.fixGridLayoutFullSpanView(this, layoutManager, Constants.VIEW_TYPE_FOOTER);
    }
}
