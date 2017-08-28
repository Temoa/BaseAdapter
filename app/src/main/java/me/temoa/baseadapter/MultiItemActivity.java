package me.temoa.baseadapter;

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

import me.temoa.base.adapter.MultiBaseAdapter;
import me.temoa.base.adapter.ViewHolder;
import me.temoa.base.adapter.listener.OnItemClickListener;
import me.temoa.base.adapter.listener.OnLoadMoreListener;

/**
 * Created by Lai
 * on 2017/8/28 17:00
 */

public class MultiItemActivity extends AppCompatActivity {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_IMAGE = 2;

    private final String[] titles = {
            "苍井空老师Twitter正式宣布不再拍摄AV",
            "初音未来代言红米NOTE-世界第一公主助力雷布斯",
            "让美国再次伟大-DID推出第45任美利坚合众国总统唐纳德·特朗普兵人",
            "Linked Horizon继续为「进击的巨人」创作演唱主题曲 杉田智和头发日渐稀薄-日刊和邪晚间版",
            "武田玲奈 川口春奈-Weekly Playboy 2017年第七期",
            "客户要喂翔面包 70岁大爷四连发-日本小姐Twitter异闻录",
            "四格漫画「笨女孩」动画化确定今夏播出",
            "「银河护卫队2」「加勒比海盗：死无对证」「速度与激情8」「金刚狼3」「海滩救护队」超级碗预告片",
            "喜剧萌番受追捧-d anime store 2017年一月新番调查排行榜",
            "金爆+乃木坂46-「犬夜叉」舞台剧公布犬夜叉定妆照",
            "不死大叔与复仇少女-「无限之住人」真人预告片公布",
            "白玉微瑕-白色内衣套图第四弹",
            "千年一遇也带不动-桥本环奈所属偶像组合Rev.from DVL将在3月底解散",
            "当小鲜肉遇上国标舞-7月新番「舞动青春」PV第二弹公布",
            "「进击的巨人」舞台剧三位主演定妆照公开"
    };

    private final String[] newTitles = {
            "阿宅要个好妹妹-日宅评选“最希望她成为你妹妹”的角色",
            "冷酷无情秉正义，抓尽贪官和污吏-《口水三国》第114集 满宠篇",
            "知名声优徳井青空创作漫画「不要输！！恶之军团！」动画化四月播出",
            "F巨乳写真偶像都丸纱也华写真图集第二弹",
            "「冰上的尤里」第三卷封面公开亚洲三小哥 「点兔」×罗森推出情人节相当商品-日刊和邪晚间版",
            "地域黑群马县-漫画「你还是不懂群马」改编日剧与电影 宫祥太朗主演",
            "偷走你的心-「初音未来 -Project DIVA- F 2nd」初音未来偷心恶魔.ver手办"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.multi_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final MultiBaseAdapter<ImageMsgItem> adapter = new MultiBaseAdapter<ImageMsgItem>(this, getData()) {
            @Override
            protected void convert(ViewHolder holder, ImageMsgItem item, int position, int viewType) {
                if (viewType == VIEW_TYPE_NORMAL) {
                    holder.setText(R.id.item_tv, item.getMsg());
                } else {
                    holder.setText(R.id.item_has_image_tv, item.getMsg());
                }
            }

            @Override
            protected int getItemLayoutId(int viewType) {
                if (viewType == VIEW_TYPE_NORMAL) {
                    return R.layout.item;
                } else {
                    return R.layout.item_has_image;
                }
            }

            @Override
            protected int getMultiViewType(ImageMsgItem item, int position) {
                if (!item.isHasImage()) {
                    return VIEW_TYPE_NORMAL;
                } else {
                    return VIEW_TYPE_IMAGE;
                }
            }
        };
        adapter.setItemClickListener(new OnItemClickListener<ImageMsgItem>() {
            @Override
            public void onClick(View itemView, ImageMsgItem item, int position) {
                Toast.makeText(MultiItemActivity.this, item.getMsg() + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.openLoadMore(true);
        adapter.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoadCompleted();
                        adapter.addData(getMoreData());
                    }
                }, 1000);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private List<ImageMsgItem> getData() {
        List<ImageMsgItem> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ImageMsgItem item = new ImageMsgItem();
            item.setMsg(titles[i]);
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
        for (int i = 0; i < newTitles.length; i++) {
            ImageMsgItem item = new ImageMsgItem();
            item.setMsg(newTitles[i]);
            if (i == 3 || i == 5) {
                item.setHasImage(true);
            } else {
                item.setHasImage(false);
            }
            list.add(item);
        }
        return list;
    }

    private class ImageMsgItem {
        private String msg;
        private boolean hasImage;

        String getMsg() {
            return msg;
        }

        void setMsg(String msg) {
            this.msg = msg;
        }

        boolean isHasImage() {
            return hasImage;
        }

        void setHasImage(boolean hasImage) {
            this.hasImage = hasImage;
        }
    }
}
