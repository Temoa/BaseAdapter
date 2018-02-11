package me.temoa.baseadapter.expand;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2018/2/11.
 */
@SuppressWarnings("WeakerAccess")
public class ExpandTestAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_PARENT = 0;
    private static final int VIEW_TYPE_CHILD = 1;

    private List<BaseItem> mItems;
    private int mRealItemCount;

    public ExpandTestAdapter(List<BaseItem> items) {
        mItems = items;
        clearData();
        mRealItemCount = mItems.size();
    }

    private void clearData() {
        for (int i = 0; i < mItems.size(); i++) {
            BaseItem item = mItems.get(i);
            if (item instanceof ParentItem) {
                if (((ParentItem) item).isExpand()) {
                    mItems.addAll(((ParentItem) item).mChildList);
                }
            }
        }
    }

    public void addData(BaseItem items) {
        Log.d("Test", mItems.size() + "");
        mItems.add(items);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int viewLayoutId;
        if (viewType == VIEW_TYPE_PARENT) {
            viewLayoutId = R.layout.item_expand_group;
        } else {
            viewLayoutId = R.layout.item_expand_sub;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewLayoutId, parent, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_PARENT) {
            final ParentItem item = (ParentItem) mItems.get(position);
            String text = (String) item.t;
            holder.setText(R.id.item_tv_group_name, text);
            CheckBox checkBox = holder.getView(R.id.item_checkbox);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int parentIndex = holder.getAdapterPosition();
                    int subItemStartIndex = parentIndex + 1;
                    if (!item.isExpand()) {
                        insertChild(parentIndex, subItemStartIndex, item);
                    } else {
                        removeChild(parentIndex, subItemStartIndex, item);
                    }
                }
            });

            if (item.isExpand) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        } else {
            ChildItem item = (ChildItem) mItems.get(position);
            final ExpandItemActivity.Data data = (ExpandItemActivity.Data) item.t;
            holder.setText(R.id.item_tv_title, data.getData());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), data.getData(), Toast.LENGTH_SHORT).show();
                    data.setData(data.getData() + data.getData());
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
        }
    }

    private void insertChild(int parentIndex, int start, ParentItem parent) {
        mRealItemCount += parent.mChildList.size();
        mItems.addAll(start, parent.mChildList);
        parent.setExpand(true);
        notifyItemRangeInserted(start, parent.mChildList.size());
        notifyItemChanged(parentIndex);
    }

    private void removeChild(int parentIndex, int start, ParentItem parent) {
        mRealItemCount -= parent.mChildList.size();
        mItems.removeAll(parent.mChildList);
        parent.setExpand(false);
        notifyItemRangeRemoved(start, parent.mChildList.size());
        notifyItemChanged(parentIndex);
    }

    @Override
    public int getItemCount() {
        return mRealItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    public interface BaseItem {
        int getType();
    }

    public static class ParentItem<T> implements BaseItem {
        final private T t;
        final private List<? extends ChildItem> mChildList;
        private boolean isExpand;

        public ParentItem(T t, List<? extends ChildItem> childList) {
            this.t = t;
            mChildList = childList;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        @Override
        public int getType() {
            return VIEW_TYPE_PARENT;
        }
    }

    public static class ChildItem<T> implements BaseItem {
        final private T t;

        public ChildItem(T t) {
            this.t = t;
        }

        @Override
        public int getType() {
            return VIEW_TYPE_CHILD;
        }
    }
}
