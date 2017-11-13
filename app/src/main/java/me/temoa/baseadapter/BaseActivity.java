package me.temoa.baseadapter;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lai
 * on 2017/11/11.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

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
