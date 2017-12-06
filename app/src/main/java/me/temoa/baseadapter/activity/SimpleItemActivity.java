package me.temoa.baseadapter.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import me.temoa.base.adapter.helper.EmptyHelperAdapter;
import me.temoa.base.adapter.helper.LoadMoreHelperAdapter;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnItemLongClickListener;
import me.temoa.base.adapter.listener.OnLoadMoreListener;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

public class SimpleItemActivity extends BaseActivity {

    private int loadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final SimpleStringAdapter adapter = new SimpleStringAdapter(this, null);
        adapter.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(SimpleItemActivity.this, item + " [click]" + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setItemLongClickListener(new OnItemLongClickListener<String>() {
            @Override
            public void onLongClick(View v, String item, int position) {
                Toast.makeText(SimpleItemActivity.this, item + " [long click]" + position, Toast.LENGTH_SHORT).show();
            }
        });
        final EmptyHelperAdapter helper1 = new EmptyHelperAdapter(adapter);
        helper1.setEmptyView(R.layout.item_empty);
        final LoadMoreHelperAdapter helper = new LoadMoreHelperAdapter(helper1);
        helper.openLoadMore();
        helper.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCount++;
                        helper.setLoadCompleted();
                        adapter.addData(getMoreData());
                        if (loadCount >= 3) {
                            helper.closeLoadMore();
                        }
                    }
                }, 1000);
            }
        });
        recyclerView.setAdapter(helper);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(getData());
                helper.notifyDataSetChanged();
            }
        }, 1000);
    }
}
