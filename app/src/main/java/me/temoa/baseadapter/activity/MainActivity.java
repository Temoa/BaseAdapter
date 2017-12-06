package me.temoa.baseadapter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.temoa.baseadapter.R;

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
                break;
        }
    }

    private void toActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
