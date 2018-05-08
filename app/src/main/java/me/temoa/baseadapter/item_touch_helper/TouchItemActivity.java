package me.temoa.baseadapter.item_touch_helper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import me.temoa.baseadapter.R;
import me.temoa.baseadapter.activity.BaseActivity;

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

        TouchSimpleStringAdapter touchSimpleStringAdapter = new TouchSimpleStringAdapter(this, getData());
        SimpleItemTouchCallback simpleItemTouchCallback = new SimpleItemTouchCallback(touchSimpleStringAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(touchSimpleStringAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.touch_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_linear:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_grid:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
