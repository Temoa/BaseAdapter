package me.temoa.baseadapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import me.temoa.baseadapter.item_touch_helper.SimpleItemTouchCallback;
import me.temoa.baseadapter.adapter.TouchSimpleStringAdapter;

/**
 * Created by lai
 * on 2017/11/20.
 */

public class TouchItemActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        TouchSimpleStringAdapter adapter = new TouchSimpleStringAdapter(this, getData());
        SimpleItemTouchCallback simpleItemTouchCallback = new SimpleItemTouchCallback(adapter);
        simpleItemTouchCallback.setTouchFlag(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
