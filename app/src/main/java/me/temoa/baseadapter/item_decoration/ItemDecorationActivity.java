package me.temoa.baseadapter.item_decoration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.base.adapter.SimpleBaseAdapter;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.baseadapter.R;
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
        SimpleStringAdapter adapter = new SimpleStringAdapter(this, getData());
        adapter.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View itemView, String item, int position) {
                Toast.makeText(ItemDecorationActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        final SimpleBaseAdapter<City> adapter1 = new SimpleBaseAdapter<City>(this, getCityData()) {
            @Override
            protected void convert(BaseViewHolder holder, City item, int position) {
                holder.setText(R.id.item_tv, item.getCityName());
            }

            @Override
            protected int getItemLayoutId() {
                return R.layout.item;
            }
        };
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(this);
        decoration.setListener(new StickyHeaderDecoration.GetDataListener() {
            @Override
            public boolean getIsFirst(int position) {
                return adapter1.getData().get(position).isFirst();
            }

            @Override
            public String getProvince(int position) {
                return adapter1.getData().get(position).getProvinceName();
            }
        });
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter1);
    }

    private List<City> getCityData() {
        List<City> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setCityName("惠州市");
            city.setProvinceName("广东省");
            if (i == 0) {
                city.setFirst(true);
            } else {
                city.setFirst(false);
            }
            data.add(city);
        }

        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setCityName("上海市");
            city.setProvinceName("上海");
            if (i == 0) {
                city.setFirst(true);
            } else {
                city.setFirst(false);
            }
            data.add(city);
        }

        for (int i = 0; i < 15; i++) {
            City city = new City();
            city.setCityName("厦门");
            city.setProvinceName("福建省");
            if (i == 0) {
                city.setFirst(true);
            } else {
                city.setFirst(false);
            }
            data.add(city);
        }
        return data;
    }

    static class City {
        private String cityName;
        private String provinceName;
        private boolean isFirst = false;

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

        boolean isFirst() {
            return isFirst;
        }

        void setFirst(boolean first) {
            isFirst = first;
        }
    }
}
