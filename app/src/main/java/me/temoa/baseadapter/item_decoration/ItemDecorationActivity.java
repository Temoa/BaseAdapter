package me.temoa.baseadapter.item_decoration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.baseadapter.activity.BaseActivity;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

/**
 * Created by lai
 * on 2018/2/28.
 */

public class ItemDecorationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        CustomItemDecoration itemDecoration = new CustomItemDecoration.Builder(this)
                .style(CustomItemDecoration.Style.PATH)
                .build();
        recyclerView.addItemDecoration(itemDecoration);
        SimpleStringAdapter adapter = new SimpleStringAdapter(this, getData());
        adapter.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(ItemDecorationActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
