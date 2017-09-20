package me.temoa.base.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.animation.BaseAnimation;
import me.temoa.base.adapter.animation.ScaleInAnimation;
import me.temoa.base.adapter.listener.OnClickGestureListener;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnItemLongClickListener;
import me.temoa.base.adapter.listener.OnLoadMoreListener;

/**
 * Created by Lai
 * on 2017/8/27 17:33
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    // -1默认为加载更多view type
    private static final int VIEW_TYPE_FOOTER = -1;

    private boolean isOpenLoadAnimation = false;
    private BaseAnimation mBaseAnimation;
    private int mAnimationLastPosition = -1;
    private long mAnimationDuration = 300;

    private boolean isOpenLoadMore;
    private boolean isScrollDown;
    private boolean isLoading = false;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mItems;

    private OnLoadMoreListener mLoadMoreListener;
    private OnItemClickListener<T> mItemClickListener;
    private OnItemLongClickListener<T> mItemLongClickListener;

    public abstract int getViewType(T item, int position);

    /* ------------------------------------------------------------------------------------------ */
    public void openLoadAnimation() {
        isOpenLoadAnimation = true;
        mBaseAnimation = new ScaleInAnimation();
    }

    public void openLoadAnimation(BaseAnimation animation) {
        isOpenLoadAnimation = true;
        mBaseAnimation = animation;
    }

    public void setAnimationDuration(long l) {
        mAnimationDuration = l;
    }

    public void openLoadMore(boolean isOpenLoadMore) {
        this.isOpenLoadMore = isOpenLoadMore;
    }

    public void setLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    /**
     * 加载更多完成时回调方法
     */
    public void setLoadCompleted() {
        isLoading = false;
        notifyItemRemoved(getItemCount() - 1);
    }

    public void setItemClickListener(OnItemClickListener<T> listener) {
        mItemClickListener = listener;
    }

    public void setItemLongClickListener(OnItemLongClickListener<T> listener) {
        mItemLongClickListener = listener;
    }

    public void setNewData(List<T> newItems) {
        mItems = newItems;
        notifyItemRangeChanged(0, newItems.size());
    }

    public void addData(List<T> items) {
        int originalSize = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(originalSize, items.size());
    }

    /* ------------------------------------------------------------------------------------------ */

    BaseAdapter(Context context, List<T> items) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items == null ? new ArrayList<T>() : items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holderView = null;
        if (isOpenLoadMore && viewType == VIEW_TYPE_FOOTER) {
            holderView = mLayoutInflater.inflate(R.layout.item_footer_load, parent, false);
        }
        return new ViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isOpenLoadMore && getItemViewType(position) == VIEW_TYPE_FOOTER) return;

        setLoadAnimation(holder.itemView, holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return isOpenLoadMore ? (mItems.size() == 0 ? 0 : mItems.size() + 1) : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isOpenLoadMore && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        } else {
            return getViewType(mItems.get(position), position);
        }
    }

    /**
     * 判断是否是 footerView
     *
     * @param viewType int
     * @return boolean
     */
    protected boolean isNotFooterItem(int viewType) {
        return viewType != VIEW_TYPE_FOOTER;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        fixStaggeredGridLayoutFullSpanView(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        fixGridLayoutFullSpanView(layoutManager);

        setClickEvent(recyclerView);

        if (isOpenLoadMore) setLoadMoreMode(recyclerView);
    }

    private void fixStaggeredGridLayoutFullSpanView(ViewHolder holder) {
        if (getItemViewType(holder.getLayoutPosition()) == VIEW_TYPE_FOOTER) {
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
                    if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
                        return gridLayoutManager.getSpanCount();
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
                    if (isScrollDown && !isLoading && lastVisibleItem + 1 == getItemCount()) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.onLoadMore();
                            isLoading = true;
                        }
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

    private void setClickEvent(RecyclerView recyclerView) {
        final GestureDetectorCompat detector = new GestureDetectorCompat(recyclerView.getContext(),
                new OnClickGestureListener(recyclerView) {
                    @Override
                    public void onClick(View v, int position) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onClick(v, mItems.get(position), position);
                        }
                    }

                    @Override
                    public void onLongClick(View v, int position) {
                        if (mItemLongClickListener != null) {
                            mItemLongClickListener.onLongClick(v, mItems.get(position), position);
                        }
                    }
                });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                detector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                detector.onTouchEvent(e);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    /**
     * 设置 Item 进入的动画
     *
     * @param v        itemView
     * @param position itemView's position
     */
    private void setLoadAnimation(View v, int position) {
        if (isOpenLoadAnimation && mBaseAnimation != null && position > mAnimationLastPosition) {
            mBaseAnimation.getAnimator(v).setDuration(mAnimationDuration).start();
            mAnimationLastPosition = position;
        }
    }
}
