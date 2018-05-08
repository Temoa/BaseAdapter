package me.temoa.baseadapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnItemLongClickListener;
import me.temoa.baseadapter.Constant;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.adapter.ImageAdapter;

/**
 * Created by Lai
 * on 2017/8/28 17:00
 */

public class MultiItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        final RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final ImageAdapter adapter = new ImageAdapter(this, getData());
        adapter.setItemClickListener(new OnItemClickListener<ImageMsgItem>() {
            @Override
            public void onClick(View itemView, ImageMsgItem item, int position) {
                Toast.makeText(MultiItemActivity.this, item.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setItemLongClickListener(new OnItemLongClickListener<ImageMsgItem>() {
            @Override
            public void onLongClick(View v, ImageMsgItem item, int position) {
                Toast.makeText(MultiItemActivity.this, item.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private List<ImageMsgItem> getData() {
        List<ImageMsgItem> list = new ArrayList<>();
        for (int i = 0; i < Constant.titles.length; i++) {
            ImageMsgItem item = new ImageMsgItem();
            item.setMsg(Constant.titles[i]);
            if (i > 3 && i < 7) {
                item.setHasImage(true);
            } else {
                item.setHasImage(false);
            }
            list.add(item);
        }
        return list;
    }

    private List<ImageMsgItem> getMoreData() {
        List<ImageMsgItem> list = new ArrayList<>();
        for (int i = 0; i < Constant.newTitles.length; i++) {
            ImageMsgItem item = new ImageMsgItem();
            item.setMsg(Constant.newTitles[i]);
            if (i == 3 || i == 5) {
                item.setHasImage(true);
            } else {
                item.setHasImage(false);
            }
            list.add(item);
        }
        return list;
    }

    public static class ImageMsgItem {
        private String msg;
        private boolean hasImage;

        public String getMsg() {
            return msg;
        }

        void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isHasImage() {
            return hasImage;
        }

        void setHasImage(boolean hasImage) {
            this.hasImage = hasImage;
        }
    }
}
