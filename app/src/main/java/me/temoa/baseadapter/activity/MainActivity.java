package me.temoa.baseadapter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.temoa.baseadapter.R;
import me.temoa.baseadapter.animation.RecyclerViewAnimationActivity;
import me.temoa.baseadapter.expand.ExpandItemActivity;
import me.temoa.baseadapter.sticky.StickyActivity;
import me.temoa.baseadapter.item_touch_helper.TouchItemActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                toActivity(SimpleItemActivity.class);
                break;
            case R.id.btn2:
                toActivity(MultiItemActivity.class);
                break;
            case R.id.btn3:
                toActivity(HeaderFooterActivity.class);
                break;
            case R.id.btn4:
                toActivity(TouchItemActivity.class);
                break;
            case R.id.btn5:
                toActivity(ExpandItemActivity.class);
                break;
            case R.id.btn6:
                toActivity(StickyActivity.class);
                break;
            case R.id.btn7:
                toActivity(RecyclerViewAnimationActivity.class);
                break;
        }
    }

    private void toActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
