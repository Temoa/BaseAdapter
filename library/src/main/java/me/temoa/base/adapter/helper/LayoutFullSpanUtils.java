package me.temoa.base.adapter.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/20.
 */
@SuppressWarnings("WeakerAccess") // public api
public class LayoutFullSpanUtils {

    public static void fixStaggeredGridLayoutFullSpanView(
            RecyclerView.Adapter adapter,
            BaseViewHolder holder,
            int viewType) {

        if (adapter.getItemViewType(holder.getLayoutPosition()) == viewType) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams)
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    public static void fixGridLayoutFullSpanView(
            final RecyclerView.Adapter adapter,
            RecyclerView.LayoutManager layoutManager,
            final int viewType) {

        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == viewType) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }
}
