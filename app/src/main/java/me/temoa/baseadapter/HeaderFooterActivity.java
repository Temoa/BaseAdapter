package me.temoa.baseadapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import me.temoa.base.adapter.helper.HeaderFooterHelper;
import me.temoa.base.adapter.helper.LoadMoreHelper;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnLoadMoreListener;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class HeaderFooterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SimpleStringAdapter adapter = new SimpleStringAdapter(this, getData());
        adapter.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(HeaderFooterActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        final HeaderFooterHelper helper = new HeaderFooterHelper(adapter);
        // 这里遇到一个坑
        // 这里给 Adapter 传入一个 View 用于创建 ViewHolder, 就无法传入 onCreateViewHolder() 的参数 parent
        // 如果item 的布局的根布局是除 RelativeLayout 之外的其他布局或者只有一个view，会导致无法居中的问题
        // 但是如果跟布局是 RelativeLayout 就不会出现这个问题
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_header_footer, null);
        helper.addHeader(headerView);

        final LoadMoreHelper loadMoreHelper = new LoadMoreHelper(helper);
        loadMoreHelper.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HeaderFooterActivity.this, "load succeed", Toast.LENGTH_SHORT).show();
                        loadMoreHelper.setLoadCompleted();
                        loadMoreHelper.closeLoadMore();
                    }
                }, 1000);
            }
        });

        recyclerView.setAdapter(loadMoreHelper);
        helper.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_footer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_header:
                return true;
            case R.id.action_add_footer:
                return true;
            case R.id.action_remove_header:
                return true;
            case R.id.action_remove_footer:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
