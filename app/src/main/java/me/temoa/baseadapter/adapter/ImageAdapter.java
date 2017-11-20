package me.temoa.baseadapter.adapter;

import android.content.Context;

import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.MultiBaseAdapter;
import me.temoa.baseadapter.activity.MultiItemActivity;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class ImageAdapter extends MultiBaseAdapter<MultiItemActivity.ImageMsgItem> {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_IMAGE = 2;

    public ImageAdapter(Context context, List<MultiItemActivity.ImageMsgItem> items) {
        super(context, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemActivity.ImageMsgItem item, int position, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            holder.setText(R.id.item_tv, item.getMsg());
        } else {
            holder.setText(R.id.item_has_image_tv, item.getMsg());
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return R.layout.item;
        } else {
            return R.layout.item_has_image;
        }
    }

    @Override
    protected int getMultiViewType(MultiItemActivity.ImageMsgItem item, int position) {
        if (!item.isHasImage()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }
}
