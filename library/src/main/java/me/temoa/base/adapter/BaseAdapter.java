package me.temoa.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnItemLongClickListener;

/**
 * Created by Lai
 * on 2017/8/27 17:33
 */

@SuppressWarnings({"unused", "WeakerAccess"}) // public api
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected LayoutInflater mLayoutInflater;
    protected List<T> mItems;

    private OnItemClickListener<T> mItemClickListener;
    private OnItemLongClickListener<T> mItemLongClickListener;

    public abstract int getViewType(T item, int position);

    protected boolean isEnable(int viewType) {
        return true;
    }

    /* ------------------------------------------------------------------------------------------ */

    public void setItemClickListener(OnItemClickListener<T> listener) {
        mItemClickListener = listener;
    }

    public void setItemLongClickListener(OnItemLongClickListener<T> listener) {
        mItemLongClickListener = listener;
    }

    /* ------------------------------------------------------------------------------------------ */

    public List<T> getData() {
        return mItems;
    }

    public void setNewData(List<T> newItems) {
        mItems = newItems;
        notifyDataSetChanged();
    }

    public void addData(List<T> items) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        int originalSize = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(originalSize, items.size());
    }

    public void addData(T item) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(item);
        notifyItemChanged(getItemCount() - 1);
    }

    public void addData(int position, T item) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(position, item);
        notifyItemChanged(position);
    }

    /* ------------------------------------------------------------------------------------------ */

    public BaseAdapter(Context context, List<T> items) {
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items == null ? new ArrayList<T>() : items;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (!isEnable(holder.getItemViewType())) return;
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mItemClickListener.onClick(v, mItems.get(pos), pos);
                }
            });
        }

        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mItemLongClickListener.onLongClick(v, mItems.get(pos), pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(mItems.get(position), position);
    }
}
