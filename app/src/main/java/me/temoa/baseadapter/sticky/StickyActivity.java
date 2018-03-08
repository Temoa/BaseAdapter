package me.temoa.baseadapter.sticky;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.SimpleBaseAdapter;
import me.temoa.baseadapter.R;

/**
 * Created by lai
 * on 2018/3/1.
 */

public class StickyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

        final RecyclerView recyclerView = findViewById(R.id.recycler);
        final TextView header = findViewById(R.id.sticky_tv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View stickyInfoView = recyclerView
                        .findChildViewUnder(header.getMeasuredWidth() / 2, 5);
                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    header.setText(stickyInfoView.getContentDescription().toString());
                }

                View transInfoView = recyclerView.findChildViewUnder(
                        header.getMeasuredWidth() / 2, header.getMeasuredHeight() + 1);
                if (transInfoView != null && transInfoView.getContentDescription() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - header.getMeasuredHeight();

                    if (transViewStatus == Adapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            header.setTranslationY(dealtY);
                        } else {
                            header.setTranslationY(0);
                        }
                    } else if (transViewStatus == Adapter.NONE_STICKY_VIEW) {
                        header.setTranslationY(0);
                    }
                }
            }
        });
        recyclerView.setAdapter(new Adapter(this, getCityData()));
    }

    static class Adapter extends SimpleBaseAdapter<City> {
        static final int FIRST_STICKY_VIEW = 1;
        static final int HAS_STICKY_VIEW = 2;
        static final int NONE_STICKY_VIEW = 3;

        Adapter(Context context, List<City> items) {
            super(context, items);
        }

        @Override
        protected void convert(BaseViewHolder holder, City city, int position) {
            TextView stickyHeader = holder.getView(R.id.sticky_tv);
            if (position == 0) {
                stickyHeader.setVisibility(View.VISIBLE);
                stickyHeader.setText(city.getProvinceName());
                holder.itemView.setTag(FIRST_STICKY_VIEW);
            } else {
                if (!TextUtils.equals(
                        mItems.get(position).getProvinceName(),
                        mItems.get(position - 1).getProvinceName())) {
                    stickyHeader.setVisibility(View.VISIBLE);
                    stickyHeader.setText(city.getProvinceName());
                    holder.itemView.setTag(HAS_STICKY_VIEW);
                } else {
                    stickyHeader.setVisibility(View.GONE);
                    holder.itemView.setTag(NONE_STICKY_VIEW);
                }
            }
            holder.itemView.setContentDescription(city.getProvinceName());
            holder.setText(R.id.tv, city.getCityName());
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.item_sticky;
        }
    }

    private List<City> getCityData() {
        List<City> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setCityName("惠州市");
            city.setProvinceName("广东省");
            data.add(city);
        }

        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setCityName("上海市");
            city.setProvinceName("上海");
            data.add(city);
        }

        for (int i = 0; i < 15; i++) {
            City city = new City();
            city.setCityName("厦门");
            city.setProvinceName("福建省");
            data.add(city);
        }
        return data;
    }

    static class City {
        private String cityName;
        private String provinceName;

         String getCityName() {
            return cityName;
        }

         void setCityName(String cityName) {
            this.cityName = cityName;
        }

         String getProvinceName() {
            return provinceName;
        }

         void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }
    }
}
