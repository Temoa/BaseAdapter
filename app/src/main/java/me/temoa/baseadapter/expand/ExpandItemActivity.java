package me.temoa.baseadapter.expand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.baseadapter.activity.BaseActivity;

/**
 * Created by lai
 * on 2017/12/6.
 */

public class ExpandItemActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SimpleExpandAdapter adapter = new SimpleExpandAdapter(this);
        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(ExpandItemActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setData(getExpandData());
        recyclerView.setAdapter(adapter);
    }

    private List<BaseExpandAdapter.GroupItem<String, String>> getExpandData() {
        List<BaseExpandAdapter.GroupItem<String, String>> list = new ArrayList<>();
        BaseExpandAdapter.GroupItem<String, String> groupItem1 = new BaseExpandAdapter.GroupItem<>("2017-12-06", getMoreData());
        BaseExpandAdapter.GroupItem<String, String> groupItem2 = new BaseExpandAdapter.GroupItem<>("2017-12-05", getData());
        list.add(groupItem1);
        list.add(groupItem2);
        return list;
    }
}
