package me.temoa.baseadapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        helper.setHeaderView(R.layout.item_header_footer);
        helper.setHeader(true);
        helper.setFooterView(R.layout.item_header_footer);
        helper.setFooter(true);

        final LoadMoreHelper loadMoreHelper = new LoadMoreHelper(helper);
        loadMoreHelper.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HeaderFooterActivity.this, "load succeed", Toast.LENGTH_SHORT).show();
                        loadMoreHelper.setLoadCompleted();
//                        loadMoreHelper.closeLoadMore();
                    }
                }, 1000);
            }
        });

        recyclerView.setAdapter(loadMoreHelper);
        helper.notifyDataSetChanged();
    }
}
