package me.temoa.base.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.R;
import me.temoa.base.adapter.listener.OnLoadMoreListener;

/**
 * Created by lai
 * on 2017/11/11.
 * <p>
 * 解决 Called attach on a child which is not detached: ViewHolder. 在研究
 * ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
 */
@SuppressWarnings({"unused", "WeakerAccess"}) // public api
public class LoadMoreHelperAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int STATUS_PREPARE = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_EMPTY = 2;     // 加载完成后没有更多数据
    public static final int STATUS_COMPLETED = 3; // 加载完成
    public static final int STATUS_ERROR = -1;

    private RecyclerView.Adapter mInnerAdapter;

    private int mLoadViewLayoutId;
    private View mLoadView;

    private OnLoadMoreListener mLoadMoreListener;

    private int mLoadStatus = 0;
    private boolean isLoadMoreEnable;
    private boolean isNoMoreData;
    private boolean isScrollDown;
    private boolean isLoading;

    public void setLoadView(int id) {
        this.mLoadViewLayoutId = id;
    }

    public void setLoadView(View v) {
        mLoadView = v;
    }

    public void isLoadMoreEnable(boolean enable) {
        isLoadMoreEnable = enable;
        if (isLoadMoreEnable) {
            notifyDataSetChanged();
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public void setLoadStatus(int status) {
        mLoadStatus = status;
        isNoMoreData = status == STATUS_EMPTY;
        isLoading = status == STATUS_LOADING;
        notifyItemChanged(getItemCount() - 1);
    }

    public LoadMoreHelperAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isLoadMoreEnable && viewType == Constants.VIEW_TYPE_LOAD) {
            if (mLoadView == null && mLoadViewLayoutId == 0)
                mLoadView = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_footer_load, parent, false);
            else if (mLoadView == null)
                mLoadView = LayoutInflater.from(parent.getContext()).inflate(mLoadViewLayoutId, parent, false);
            return new BaseViewHolder(mLoadView);
        } else {
            return (BaseViewHolder) mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (isLoadMoreEnable && position == getItemCount() - 1) {
            onLoadStatusChange(holder, position, mLoadStatus);
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    protected void onLoadStatusChange(BaseViewHolder holder, int position, int status) {
        isLoading = status == STATUS_LOADING;
        switch (status) {
            case STATUS_LOADING:
                holder.itemView.setVisibility(View.VISIBLE);
                break;
            case STATUS_EMPTY:
                holder.itemView.setVisibility(View.VISIBLE);
                break;
            case STATUS_COMPLETED:
                holder.itemView.setVisibility(View.VISIBLE);
                break;
            case STATUS_ERROR:
                holder.itemView.setVisibility(View.INVISIBLE);
                break;
            case STATUS_PREPARE:
                holder.itemView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = mInnerAdapter.getItemCount();
        return isLoadMoreEnable ? (size == 0 ? 0 : size + 1) : size;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreEnable && position == getItemCount() - 1) {
            return Constants.VIEW_TYPE_LOAD;
        } else {
            return mInnerAdapter.getItemViewType(position);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (getItemViewType(holder.getLayoutPosition()) == Constants.VIEW_TYPE_LOAD) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams)
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        /*
          解决 Called attach on a child which is not detached: ViewHolder
          不一定会复现,在 setLoadStatus() 的时候回可能出现
         */
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        setLoadMoreMode(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == Constants.VIEW_TYPE_LOAD) {
                        return spanCount;
                    }
                    return 1;
                }
            });
        }
    }

    private void setLoadMoreMode(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = findLastVisibleItemPosition(recyclerView.getLayoutManager());
                    if (isLoadMoreEnable && !isNoMoreData && isScrollDown && !isLoading && lastVisibleItem + 1 == getItemCount()) {
                        mLoadMoreListener.onLoadMore();
                        setLoadStatus(STATUS_LOADING);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollDown = dy > 0;
            }
        });
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions
                    = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            int max = lastVisibleItemPositions[0];
            for (int value : lastVisibleItemPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
        return -1;
    }
}
