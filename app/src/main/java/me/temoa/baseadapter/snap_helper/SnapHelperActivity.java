package me.temoa.baseadapter.snap_helper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;

import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.SimpleBaseAdapter;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.activity.BaseActivity;

/**
 * Created by lai
 * on 2018/3/8.
 */

public class SnapHelperActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        recyclerView.setLayoutManager(
//                new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        PagerAdapter adapter = new PagerAdapter(this, getData());
        adapter.addData(getMoreData());
        adapter.addData(getMoreData());
        adapter.addData(getMoreData());
        recyclerView.setAdapter(adapter);

//        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(recyclerView);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
//        linearSnapHelper.attachToRecyclerView(recyclerView);
        CustomLinearSnapHelper customLinearSnapHelper = new CustomLinearSnapHelper();
        customLinearSnapHelper.attachToRecyclerView(recyclerView);
    }

    static class PagerAdapter extends SimpleBaseAdapter<String> {

        PagerAdapter(Context context, List<String> items) {
            super(context, items);
        }

        @Override
        protected void convert(BaseViewHolder holder, String item, int position) {

        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.item_snap;
        }
    }
}
