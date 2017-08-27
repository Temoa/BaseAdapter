package me.temoa.base.adapter.listener;

import android.view.View;

/**
 * Created by Lai
 * on 2017/8/27 16:44
 */

public interface OnItemClickListener<T> {
    void onClick(View itemView, T item, int position);
}
