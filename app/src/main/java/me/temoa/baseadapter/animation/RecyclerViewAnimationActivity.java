package me.temoa.baseadapter.animation;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import me.temoa.base.adapter.BaseViewHolder;
import me.temoa.baseadapter.R;
import me.temoa.baseadapter.activity.BaseActivity;

/**
 * Created by lai
 * on 2018/3/14.
 * <p>
 * study from https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
 */

public class RecyclerViewAnimationActivity extends BaseActivity {

    private int animResId;
    private MyAdapter mMyAdapter;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private RecyclerView.LayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        recyclerView = findViewById(R.id.anim_recyclerView);

        recyclerView.setBackgroundColor(Color.parseColor("#B0BEC5"));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        animResId = R.anim.layout_anim_linear_fall_down;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, animResId);
        recyclerView.setLayoutAnimation(animationController);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        mMyAdapter = new MyAdapter();
        recyclerView.setAdapter(mMyAdapter);
    }

    private void reloadRecyclerViewAnimation() {
        recyclerView.setLayoutManager(mMyAdapter.getLayoutManager());

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, animResId);
        recyclerView.setLayoutAnimation(animationController);

        mMyAdapter.notifyDataSetChanged();

//        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anim_1:
                if (mMyAdapter.getLayoutManager() instanceof GridLayoutManager) {
                    animResId = R.anim.layout_anim_grid_fall_down;
                } else {
                    animResId = R.anim.layout_anim_linear_fall_down;
                }
                break;
            case R.id.action_anim_2:
                if (mMyAdapter.getLayoutManager() instanceof GridLayoutManager) {
                    animResId = R.anim.layout_anim_grid_right_in;
                } else {
                    animResId = R.anim.layout_anim_linear_right_in;
                }
                break;
            case R.id.action_anim_3:
                if (mMyAdapter.getLayoutManager() instanceof GridLayoutManager) {
                    animResId = R.anim.layout_anim_grid_bottom_in;
                } else {
                    animResId = R.anim.layout_anim_linear_bottom_in;
                }
                break;
            case R.id.action_layout_1:
                mMyAdapter.setLayoutManager(mLinearLayoutManager);
                animResId = R.anim.layout_anim_linear_fall_down;
                break;
            case R.id.action_layout_2:
                mMyAdapter.setLayoutManager(mGridLayoutManager);
                animResId = R.anim.layout_anim_grid_fall_down;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reload(View view) {
        reloadRecyclerViewAnimation();
    }

    private static class MyAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        private RecyclerView.LayoutManager mLayoutManager;

        void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
        }

        RecyclerView.LayoutManager getLayoutManager() {
            return mLayoutManager;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mLayoutManager instanceof GridLayoutManager) {
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_card_grid, parent, false);
                GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
                lp.width = width / 4 - 2;
                lp.height = width / 4 - 2;
                itemView.setLayoutParams(lp);
                return new BaseViewHolder(itemView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_card, parent, false);
                return new BaseViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }
}
