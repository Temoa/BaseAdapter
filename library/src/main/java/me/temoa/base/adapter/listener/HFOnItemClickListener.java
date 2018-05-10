package me.temoa.base.adapter.listener;

import android.view.View;

/**
 * Created by lai
 * on 2018/5/10.
 */

public interface HFOnItemClickListener<T> {
    void onHeaderFooterItemClick(View v, int position, int viewType);

    void onNormalItemClick(View v, T t, int position);
}
