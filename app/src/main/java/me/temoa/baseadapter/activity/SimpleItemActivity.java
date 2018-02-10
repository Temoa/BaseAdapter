package me.temoa.baseadapter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.temoa.base.adapter.BaseViewHolder;
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
        final EmptyHelperAdapter emptyHelperAdapter = new EmptyHelperAdapter(adapter);
        final LoadMoreHelperAdapter loadMoreHelperAdapter = new MyLoadMoreAdapter(emptyHelperAdapter);

        @SuppressLint("InflateParams")
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty, null, false);
        Button button = emptyView.findViewById(R.id.item_empty_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreHelperAdapter.isLoadMoreEnable(true);
                adapter.setNewData(getData());
            }
        });
        emptyHelperAdapter.setEmptyView(emptyView);

        loadMoreHelperAdapter.isLoadMoreEnable(false);
        loadMoreHelperAdapter.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCount++;
                        if (loadCount == 2) {
                            loadMoreHelperAdapter.setLoadStatus(LoadMoreHelperAdapter.STATUS_ERROR);
                            return;
                        }
                        if (loadCount >= 4) {
                            loadMoreHelperAdapter.setLoadStatus(LoadMoreHelperAdapter.STATUS_EMPTY);
                            return;
                        }
                        adapter.addData(getMoreData());
                        loadMoreHelperAdapter.setLoadStatus(LoadMoreHelperAdapter.STATUS_COMPLETED);
                    }
                }, 1000);
            }
        });
        recyclerView.setAdapter(loadMoreHelperAdapter);
    }

    class MyLoadMoreAdapter extends LoadMoreHelperAdapter {

        MyLoadMoreAdapter(RecyclerView.Adapter innerAdapter) {
            super(innerAdapter);
            setLoadView(R.layout.item_load_more);
        }

        @Override
        protected void onLoadStatusChange(BaseViewHolder holder, int position, int status) {
            switch (status) {
                case STATUS_LOADING:
                    holder.setVisible(R.id.item_load_progress_bar, true);
                    holder.setText(R.id.item_load_tv, "正在刷新");
                    break;
                case STATUS_EMPTY:
                    holder.setVisible(R.id.item_load_progress_bar, false);
                    holder.setText(R.id.item_load_tv, "- 没有更多数据 -");
                    break;
                case STATUS_COMPLETED:
                    holder.setVisible(R.id.item_load_progress_bar, true);
                    holder.setText(R.id.item_load_tv, "加载完成");
                    break;
                case STATUS_ERROR:
                    holder.setVisible(R.id.item_load_progress_bar, false);
                    holder.setText(R.id.item_load_tv, "- 加载失败 -");
                    break;
                case STATUS_PREPARE:
                    holder.setVisible(R.id.item_load_progress_bar, true);
                    holder.setText(R.id.item_load_tv, "");
            }
        }
    }
}
