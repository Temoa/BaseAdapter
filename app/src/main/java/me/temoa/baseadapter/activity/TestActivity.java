package me.temoa.baseadapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Collections;

import me.temoa.base.adapter.helper.HeaderFooterHelperAdapter;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.adapter.SimpleStringAdapter;

/**
 * Created by lai
 * on 2018/5/8.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SimpleStringAdapter simpleStringAdapter = new SimpleStringAdapter(this, Collections.<String>emptyList());
        final HeaderFooterHelperAdapter<String> headerFooterHelperAdapter = new HeaderFooterHelperAdapter<>(simpleStringAdapter);
        View view = LayoutInflater.from(this).inflate(R.layout.item_header_footer, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", v.getId() + "");
                headerFooterHelperAdapter.setNewData(getData());
            }
        });
        headerFooterHelperAdapter.addHeader(view);
        recyclerView.setAdapter(headerFooterHelperAdapter);
    }
}
