package me.temoa.baseadapter.expand;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2017/12/6.
 */

public class SimpleExpandAdapter extends BaseExpandAdapter<String, String> {

    private OnItemClickListener<String> mOnItemClickListener;

    void setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    SimpleExpandAdapter(Context context) {
        super(context, R.layout.item_expand_group, R.layout.item_expand_sub);
    }

    @Override
    protected void convertGroup(final BaseViewHolder holder, final GroupItem groupItem, final int groupItemIndex) {
        String date = (String) groupItem.getT();
        holder.setText(R.id.item_tv_group_name, date);
        CheckBox checkBox = holder.getView(R.id.item_checkbox);
        if (mGroupItemStatus.get(groupItemIndex)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    protected void convertSub(final BaseViewHolder holder, GroupItem<String, String> groupItem, int subItemIndex) {
        final String title = groupItem.getSubItems().get(subItemIndex);
        holder.setText(R.id.item_tv_title, title);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, title, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    protected void expand(int groupItemIndex, int subStartIndex, int subItemCount) {
        super.expand(groupItemIndex, subStartIndex, subItemCount);
    }

    @Override
    protected void collapse(int groupItemIndex, int subStartIndex, int subItemCount) {
        super.collapse(groupItemIndex, subStartIndex, subItemCount);
    }
}
