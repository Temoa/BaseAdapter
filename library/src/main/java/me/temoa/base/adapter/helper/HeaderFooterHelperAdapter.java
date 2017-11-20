package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class HeaderFooterHelperAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @NonNull
    private final RecyclerView.Adapter mInnerAdapter;

    private View mHeaderView;
    private View mFooterView;

    public void addHeader(View v) {
        mHeaderView = v;
    }

    public void addFooter(View v) {
        mFooterView = v;
    }

    public void removeHeader() {
        mHeaderView = null;
    }

    public void removeFooter() {
        mFooterView = null;
    }

    private int getHeaderCount() {
        return mHeaderView == null ? 0 : 1;
    }

    private int getFooterCount() {
        return mFooterView == null ? 0 : 1;
    }

    public HeaderFooterHelperAdapter(@NonNull RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == Constants.VIEW_TYPE_HEADER) {
            return new BaseViewHolder(mHeaderView);
        } else if (mFooterView != null && viewType == Constants.VIEW_TYPE_FOOTER) {
            return new BaseViewHolder(mFooterView);
        } else {
            return (BaseViewHolder) mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.VIEW_TYPE_HEADER) return;
        if (getItemViewType(position) == Constants.VIEW_TYPE_FOOTER) return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + getHeaderCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0)
            return Constants.VIEW_TYPE_HEADER;
        else if (mFooterView != null && position == getItemCount() - 1)
            return Constants.VIEW_TYPE_FOOTER;
        else
            return mInnerAdapter.getItemViewType(position - getHeaderCount());
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
