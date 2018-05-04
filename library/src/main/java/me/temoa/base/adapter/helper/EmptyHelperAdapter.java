package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/11/11.
 * 如果用网格布局的话，设置空布局无法全屏(可以通过瀑布布局曲线救国)
 */
@SuppressWarnings("unused") // public api
public class EmptyHelperAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private int mEmptyViewLayoutId;
    private View mEmptyView;

    @NonNull
    private final RecyclerView.Adapter mInnerAdapter;

    public void setEmptyView(int id) {
        mEmptyViewLayoutId = id;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    private boolean isEmpty() {
        return mInnerAdapter.getItemCount() == 0;
    }

    public EmptyHelperAdapter(@NonNull RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isEmpty()) {
            if (mEmptyView != null) {
                FrameLayout frameLayout = new FrameLayout(mEmptyView.getContext());
                final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                final ViewGroup.LayoutParams lp = mEmptyView.getLayoutParams();
                if (lp != null) {
                    params.width = lp.width;
                    params.height = lp.height;
                }
                frameLayout.setLayoutParams(params);
                frameLayout.addView(mEmptyView);
                return new BaseViewHolder(frameLayout);
            } else {
                if (mEmptyViewLayoutId == 0) {
                    throw new RuntimeException("Should set a empty view!!");
                }
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(mEmptyViewLayoutId, parent, false);
                return new BaseViewHolder(emptyView);
            }
        } else {
            return (BaseViewHolder) mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
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

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isEmpty()) {
            LayoutFullSpanUtils.fixStaggeredGridLayoutFullSpanView(this, holder, Constants.VIEW_TYPE_EMPTY);
        }
    }
}
