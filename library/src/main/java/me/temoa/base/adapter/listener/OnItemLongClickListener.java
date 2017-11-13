package me.temoa.base.adapter.listener;

import android.view.View;

/**
 * Created by Lai
 * on 2017/8/27 16:54
 */

public interface OnItemLongClickListener<T> {
    void onLongClick(View v, T item, int position);
}
