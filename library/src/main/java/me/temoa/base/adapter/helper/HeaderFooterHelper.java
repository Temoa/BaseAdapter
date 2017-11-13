package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
    private int headerViewLayoutId;
    private int footerViewLayoutId;

    private boolean hasHeader;
    private boolean hasFooter;

    public void setHeaderView(View view) {
        this.headerView = view;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

    public void setHeaderView(int id) {
        this.headerViewLayoutId = id;
    }

    public void setFooterView(int id) {
        this.footerViewLayoutId = id;
    }

    public void setHeader(boolean flag) {
        hasHeader = flag;
        notifyDataSetChanged();
    }

    public void setFooter(boolean flag) {
        hasFooter = flag;
        notifyDataSetChanged();
    }

    private int getHeaderCount() {
        return hasHeader ? 1 : 0;
    }

    private int getFooterCount() {
        return hasFooter ? 1 : 0;
    }

    @NonNull
    private final RecyclerView.Adapter innerAdapter;

    public HeaderFooterHelper(@NonNull RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (hasHeader && viewType == Constants.VIEW_TYPE_HEADER) {
            if (headerView == null) {
                headerView = LayoutInflater.from(parent.getContext()).inflate(headerViewLayoutId, parent, false);
            }
            return new BaseViewHolder(headerView);
        } else if (hasFooter && viewType == Constants.VIEW_TYPE_FOOTER) {
            if (footerView == null) {
                footerView = LayoutInflater.from(parent.getContext()).inflate(footerViewLayoutId, parent, false);
            }
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
        if (hasHeader && position == 0) return Constants.VIEW_TYPE_HEADER;
        else if (hasFooter && position == getItemCount() - 1) return Constants.VIEW_TYPE_FOOTER;
        else return innerAdapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        fixStaggeredGridLayoutFullSpanView(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        fixGridLayoutFullSpanView(layoutManager);
    }

    private void fixStaggeredGridLayoutFullSpanView(BaseViewHolder holder) {
        if (getItemViewType(holder.getLayoutPosition()) == Constants.VIEW_TYPE_FOOTER
                || getItemViewType(holder.getLayoutPosition()) == Constants.VIEW_TYPE_HEADER) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams)
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    private void fixGridLayoutFullSpanView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == Constants.VIEW_TYPE_FOOTER
                            || getItemViewType(position) == Constants.VIEW_TYPE_HEADER) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }
}
