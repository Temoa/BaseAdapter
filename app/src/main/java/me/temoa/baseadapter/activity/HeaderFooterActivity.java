package me.temoa.baseadapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import me.temoa.base.adapter.helper.Constants;
import me.temoa.base.adapter.helper.HeaderFooterHelperAdapter;
import me.temoa.base.adapter.listener.HFOnItemClickListener;
import me.temoa.base.adapter.listener.HFOnItemLongClickListener;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

/**
 * Created by lai
 * on 2017/11/11.
 */

public class HeaderFooterActivity extends BaseActivity {

    private HeaderFooterHelperAdapter<String> mHeaderFooterHelperAdapter;
    private View mHeaderFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SimpleStringAdapter adapter = new SimpleStringAdapter(this, getData());
        mHeaderFooterHelperAdapter = new HeaderFooterHelperAdapter<>(adapter);
        // 这里遇到一个坑
        // 这里给 Adapter 传入一个 View 用于创建 ViewHolder
        // 如果item 的布局的根布局是除 RelativeLayout 之外的其他布局或者只有一个view，会导致无法居中的问题
        // 但是如果跟布局是 RelativeLayout 就不会出现这个问题
        mHeaderFooterView = LayoutInflater.from(this).inflate(R.layout.item_header_footer, null);
        mHeaderFooterHelperAdapter.addHeader(mHeaderFooterView);
        mHeaderFooterHelperAdapter.addFooter(mHeaderFooterView);
        mHeaderFooterHelperAdapter.setItemClickListener(new HFOnItemClickListener<String>() {
            @Override
            public void onHeaderFooterItemClick(View v, int position, int viewType) {
                Toast.makeText(
                        HeaderFooterActivity.this,
                        viewType == Constants.VIEW_TYPE_HEADER ? "Header" : "Footer",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNormalItemClick(View v, String s, int position) {
                Toast.makeText(HeaderFooterActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        mHeaderFooterHelperAdapter.setItemLongClickListener(new HFOnItemLongClickListener<String>() {
            @Override
            public void onHeaderFooterItemClick(View v, int position, int viewType) {
                Toast.makeText(
                        HeaderFooterActivity.this,
                        viewType == Constants.VIEW_TYPE_HEADER ? "Long Header" : "Long Footer",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNormalItemClick(View v, String s, int position) {
                Toast.makeText(HeaderFooterActivity.this, "Long " + s, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mHeaderFooterHelperAdapter);
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
                mHeaderFooterHelperAdapter.addHeader(mHeaderFooterView);
                return true;
            case R.id.action_add_footer:
                mHeaderFooterHelperAdapter.addFooter(mHeaderFooterView);
                return true;
            case R.id.action_remove_header:
                mHeaderFooterHelperAdapter.removeHeader();
                return true;
            case R.id.action_remove_footer:
                mHeaderFooterHelperAdapter.removeFooter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
