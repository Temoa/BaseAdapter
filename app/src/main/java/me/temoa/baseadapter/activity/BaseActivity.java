package me.temoa.baseadapter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.temoa.baseadapter.Constant;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2017/11/11.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        recyclerView = findViewById(R.id.main_recyclerView);
    }

    protected List<String> getData() {
        List<String> titleList = new ArrayList<>();
        Collections.addAll(titleList, Constant.titles);
        return titleList;
    }

    protected List<String> getMoreData() {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, Constant.newTitles);
        return list;
    }

}
