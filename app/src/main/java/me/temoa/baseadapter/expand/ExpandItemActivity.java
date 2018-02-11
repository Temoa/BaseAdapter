package me.temoa.baseadapter.expand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.baseadapter.R;
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

//        SimpleExpandAdapter adapter = new SimpleExpandAdapter(this);
//        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
//            @Override
//            public void onClick(View itemView, String item, int position) {
//                Toast.makeText(ExpandItemActivity.this, item, Toast.LENGTH_SHORT).show();
//            }
//        });
//        adapter.setData(getExpandData());
//        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        set();
    }

    private void set() {
        List<ExpandTestAdapter.BaseItem> list = new ArrayList<>();
        List<ExpandTestAdapter.ChildItem<Data>> child1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Data data = new Data();
            data.setData("hello" + i);
            data.setPic(R.drawable.ic_clear_24dp);
            ExpandTestAdapter.ChildItem<Data> childItem = new ExpandTestAdapter.ChildItem<>(data);
            child1.add(childItem);
        }
        ExpandTestAdapter.ParentItem<String> parentItem1 = new ExpandTestAdapter.ParentItem<>("2017-12-06", child1);
        parentItem1.setExpand(false);


        List<ExpandTestAdapter.ChildItem<Data>> child2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Data data = new Data();
            data.setData("world" + i);
            data.setPic(R.drawable.ic_clear_24dp);
            ExpandTestAdapter.ChildItem<Data> childItem = new ExpandTestAdapter.ChildItem<>(data);
            child2.add(childItem);
        }
        ExpandTestAdapter.ParentItem<String> parentItem2 = new ExpandTestAdapter.ParentItem<>("2017-12-05", child2);
        parentItem2.setExpand(true);

        list.add(parentItem1);
        list.add(parentItem2);

        final ExpandTestAdapter expandTestAdapter = new ExpandTestAdapter(list);
        recyclerView.setAdapter(expandTestAdapter);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ExpandTestAdapter.ChildItem<Data>> child = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    Data data = new Data();
                    data.setData("bye" + i);
                    data.setPic(R.drawable.ic_clear_24dp);
                    ExpandTestAdapter.ChildItem<Data> childItem = new ExpandTestAdapter.ChildItem<>(data);
                    child.add(childItem);
                }
                ExpandTestAdapter.ParentItem<String> item = new ExpandTestAdapter.ParentItem<>("2017-12-04", child);
                item.setExpand(false);
                expandTestAdapter.addData(item);
            }
        }, 3000);
    }

    private List<BaseExpandAdapter.GroupItem<String, String>> getExpandData() {
        List<BaseExpandAdapter.GroupItem<String, String>> list = new ArrayList<>();
        BaseExpandAdapter.GroupItem<String, String> groupItem1 = new BaseExpandAdapter.GroupItem<>("2017-12-06", getMoreData());
        BaseExpandAdapter.GroupItem<String, String> groupItem2 = new BaseExpandAdapter.GroupItem<>("2017-12-05", getData());
        list.add(groupItem1);
        list.add(groupItem2);
        return list;
    }

    static class Data {

        private String data;
        private int pic;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getPic() {
            return pic;
        }

        public void setPic(int pic) {
            this.pic = pic;
        }
    }
}
