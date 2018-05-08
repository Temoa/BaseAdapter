package me.temoa.baseadapter.expand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;

/**
 * Created by lai
 * on 2017/12/4.
 */

@SuppressWarnings({"unused", "WeakerAccess"}) // public api
public abstract class BaseExpandAdapter<T, V> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<GroupItem<T, V>> mItems = new ArrayList<>();
    protected List<Boolean> mGroupItemStatus = new ArrayList<>();

    private int mGroupLayoutId;
    private int mSubLayoutId;

    public BaseExpandAdapter(Context context) {
        this(context, 0, 0);
    }

    public BaseExpandAdapter(Context context, int groupLayoutId, int subLayoutId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGroupLayoutId = groupLayoutId;
        mSubLayoutId = subLayoutId;
    }

    public void setLayout(int groupLayoutId, int subLayoutId) {
        mGroupLayoutId = groupLayoutId;
        mSubLayoutId = subLayoutId;
    }

    public void setData(List<GroupItem<T, V>> newData) {
        mItems = newData;
        initGroupItemStatus();
        notifyDataSetChanged();
    }

    /**
     * 初始化 groupItemStatus
     * 开合状态为关: false
     */
    private void initGroupItemStatus() {
        mGroupItemStatus.clear();
        for (int i = 0; i < mItems.size(); i++) {
            mGroupItemStatus.add(false);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mGroupLayoutId == 0 || mSubLayoutId == 0) {
            throw new RuntimeException("Please set up group item layout or sub item layout.");
        }
        View v;
        BaseViewHolder viewHolder;
        if (viewType == ItemStatus.VIEW_TYPE_GROUP) {
            v = mLayoutInflater.inflate(mGroupLayoutId, parent, false);
            viewHolder = new BaseViewHolder(v);
        } else {
            v = mLayoutInflater.inflate(mSubLayoutId, parent, false);
            viewHolder = new BaseViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, int position) {
        final ItemStatus itemStatus = getItemStatusByPosition(position);
        final GroupItem<T, V> groupItem = mItems.get(itemStatus.getGroupItemIndex());

        if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_GROUP) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int groupItemIndex = itemStatus.getGroupItemIndex();
                    int subItemStartIndex = holder.getAdapterPosition() + 1;
                    int subItemSize = groupItem.getSubItems().size();

                    if (!mGroupItemStatus.get(itemStatus.getGroupItemIndex())) {
                        expand(groupItemIndex, subItemStartIndex, subItemSize);
                    } else {
                        collapse(groupItemIndex, subItemStartIndex, subItemSize);
                    }
                }
            });
            convertGroup(holder, groupItem, itemStatus.getGroupItemIndex());
        } else {
            convertSub(holder, groupItem, itemStatus.getSubItemIndex());
        }
    }

    protected abstract void convertGroup(BaseViewHolder holder, GroupItem<T, V> groupItem, int groupItemIndex);

    protected abstract void convertSub(BaseViewHolder holder, GroupItem<T, V> groupItem, int subItemIndex);

    protected void expand(int groupItemIndex, int subStartIndex, int subItemCount) {
        mGroupItemStatus.set(groupItemIndex, true);
        notifyItemChanged(groupItemIndex);
        notifyItemRangeInserted(subStartIndex, subItemCount);
    }

    protected void collapse(int groupItemIndex, int subStartIndex, int subItemCount) {
        mGroupItemStatus.set(groupItemIndex, false);
        notifyItemChanged(groupItemIndex);
        notifyItemRangeRemoved(subStartIndex, subItemCount);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemStatusByPosition(position).getViewType();
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mGroupItemStatus.size() == 0) {
            return 0;
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (mGroupItemStatus.get(i)) {
                itemCount += mItems.get(i).getSubItems().size() + 1;
            } else {
                itemCount++;
            }
        }
        return itemCount;
    }

    private ItemStatus getItemStatusByPosition(int position) {
        ItemStatus itemStatus = new ItemStatus();
        int count = 0; // 计算 groupItemIndex = i 时，position 最大值
        int i;
        // 轮询 groupItem 的开关状态
        for (i = 0; i < mGroupItemStatus.size(); i++) {
            if (count == position) { // pos 刚好等于计数时，item 为 groupItem
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_GROUP);
                itemStatus.setGroupItemIndex(i);
                break;
            } else if (count > position) { // pos 大于计数时，item为groupItem(i - 1) 中的某个 subItem
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUB);
                itemStatus.setGroupItemIndex(i - 1);
                itemStatus.setSubItemIndex(position - (count - mItems.get(i - 1).getSubItems().size()));
                break;
            }
            // 无论 groupItem 状态是开或者关，它在列表中都会存在，所以 count++
            count++;
            // 当轮询到的 groupItem 的状态为“开”的话，count 需要加上该 groupItem 下面的子项目数目
            if (mGroupItemStatus.get(i)) {
                count += mItems.get(i).getSubItems().size();
            }
        }

        // 简单地处理当轮询到最后一项 groupItem 的时候
        if (i >= mGroupItemStatus.size()) {
            itemStatus.setGroupItemIndex(i - 1);
            itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUB);
            itemStatus.setSubItemIndex(position - (count - mItems.get(i - 1).getSubItems().size()));
        }
        return itemStatus;
    }

    public static class GroupItem<T, V> {
        private T t;
        private List<V> subItems;

        public GroupItem(T t, List<V> subItems) {
            this.t = t;
            this.subItems = subItems;
        }

        public T getT() {
            return t;
        }

        public List<V> getSubItems() {
            return subItems;
        }
    }

    public static class ItemStatus {
        static final int VIEW_TYPE_GROUP = 0;
        static final int VIEW_TYPE_SUB = 1;

        private int viewType;
        private int groupItemIndex = 0;
        private int subItemIndex = 0;

        int getViewType() {
            return viewType;
        }

        void setViewType(int viewType) {
            this.viewType = viewType;
        }

        int getGroupItemIndex() {
            return groupItemIndex;
        }

        void setGroupItemIndex(int groupItemIndex) {
            this.groupItemIndex = groupItemIndex;
        }

        int getSubItemIndex() {
            return subItemIndex;
        }

        void setSubItemIndex(int subItemIndex) {
            this.subItemIndex = subItemIndex;
        }
    }
}
