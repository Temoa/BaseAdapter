package me.temoa.base.adapter.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 */
public class LoadMoreHelper extends RecyclerView.Adapter<BaseViewHolder> {

    private RecyclerView.Adapter innerAdapter;

    private int loadViewLayoutId;
    private View loadView;

    private OnLoadMoreListener mLoadMoreListener;

    private boolean isOpenLoadMore;
    private boolean isScrollDown;
    private boolean isLoading = false;

    public void setLoadView(int id) {
        this.loadViewLayoutId = id;
    }

    public void setLoadView(View v) {
        loadView = v;
    }

    public void openLoadMore() {
        this.isOpenLoadMore = true;
    }

    public void closeLoadMore() {
        this.isOpenLoadMore = false;
    }

    public void setLoadMoreListener(OnLoadMoreListener listener) {
        openLoadMore();
        mLoadMoreListener = listener;
    }

    public void setLoadCompleted() {
        isLoading = false;
        loadView.setVisibility(View.GONE);
    }

    public LoadMoreHelper(RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isOpenLoadMore && viewType == Constants.VIEW_TYPE_LOAD) {
            if (loadView == null && loadViewLayoutId == 0)
                loadView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load, parent, false);
            else if (loadView == null)
                loadView = LayoutInflater.from(parent.getContext()).inflate(loadViewLayoutId, parent, false);
            return new BaseViewHolder(loadView);
        } else {
            return (BaseViewHolder) innerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isOpenLoadMore && position == getItemCount() - 1) {
            return;
        }
        innerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        int size = innerAdapter.getItemCount();
        return isOpenLoadMore ? (size == 0 ? 0 : size + 1) : size;
    }

    @Override
    public int getItemViewType(int position) {
        if (isOpenLoadMore && position == getItemCount() - 1) {
            return Constants.VIEW_TYPE_LOAD;
        } else {
            return innerAdapter.getItemViewType(position);
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isOpenLoadMore) {
            LayoutFullSpanUtils.fixStaggeredGridLayoutFullSpanView(this, holder, Constants.VIEW_TYPE_LOAD);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (isOpenLoadMore) {
            LayoutFullSpanUtils.fixGridLayoutFullSpanView(this, layoutManager, Constants.VIEW_TYPE_LOAD);
            setLoadMoreMode(recyclerView);
        }
    }

    private void setLoadMoreMode(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = findLastVisibleItemPosition(recyclerView.getLayoutManager());
                    if (isOpenLoadMore && isScrollDown && !isLoading && lastVisibleItem + 1 == getItemCount()) {
                        mLoadMoreListener.onLoadMore();
                        loadView.setVisibility(View.VISIBLE);
                        isLoading = true;
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
