package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseAdapter;
import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/11.
 */
@SuppressWarnings("unused") // public api
public class HeaderFooterHelperAdapter<T> extends BaseHelperAdapter<T> {

    @NonNull
    private final BaseAdapter<T> mInnerAdapter;

    private View mHeaderView;
    private View mFooterView;

    public void addHeader(View v) {
        mHeaderView = v;
        notifyItemInserted(0);
    }

    public void addFooter(View v) {
        mFooterView = v;
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeHeader() {
        mHeaderView = null;
        notifyItemRemoved(0);
    }

    public void removeFooter() {
        mFooterView = null;
        notifyItemRemoved(getItemCount() - 1);
    }

    private int getHeaderCount() {
        return mHeaderView == null ? 0 : 1;
    }

    private int getFooterCount() {
        return mFooterView == null ? 0 : 1;
    }

    public HeaderFooterHelperAdapter(@NonNull BaseAdapter<T> innerAdapter) {
        super(innerAdapter);
        this.mInnerAdapter = innerAdapter;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == Constants.VIEW_TYPE_HEADER) {
            return new BaseViewHolder(mHeaderView);
        } else if (mFooterView != null && viewType == Constants.VIEW_TYPE_FOOTER) {
            return new BaseViewHolder(mFooterView);
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
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
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (getItemViewType(holder.getLayoutPosition()) == Constants.VIEW_TYPE_HEADER
                || getItemViewType(holder.getLayoutPosition()) == Constants.VIEW_TYPE_FOOTER) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams)
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == Constants.VIEW_TYPE_HEADER
                            || getItemViewType(position) == Constants.VIEW_TYPE_FOOTER) {
                        return spanCount;
                    }
                    return 1;
                }
            });
        }
    }
}
