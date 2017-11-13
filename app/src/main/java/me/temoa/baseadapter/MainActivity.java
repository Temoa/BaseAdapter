package me.temoa.baseadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.temoa.base.adapter.helper.EmptyHelper;
import me.temoa.base.adapter.helper.LoadMoreHelper;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnItemLongClickListener;
import me.temoa.base.adapter.listener.OnLoadMoreListener;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private int loadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.main_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final SimpleStringAdapter adapter = new SimpleStringAdapter(this, null);
        adapter.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(MainActivity.this, item + " [click]" + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setItemLongClickListener(new OnItemLongClickListener<String>() {
            @Override
            public void onLongClick(View v, String item, int position) {
                Toast.makeText(MainActivity.this, item + " [long click]" + position, Toast.LENGTH_SHORT).show();
            }
        });
        final EmptyHelper helper1 = new EmptyHelper(adapter);
        helper1.setEmptyView(R.layout.item_empty);
        final LoadMoreHelper helper = new LoadMoreHelper(helper1);
        helper.openLoadMore();
        helper.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
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
        mRecyclerView.setAdapter(helper);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(getData());
                helper.notifyDataSetChanged();
            }
        }, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_multi) {
            startActivity(new Intent(this, MultiItemActivity.class));
        } else if (item.getItemId() == R.id.action_header_footer) {
            startActivity(new Intent(this, HeaderFooterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
