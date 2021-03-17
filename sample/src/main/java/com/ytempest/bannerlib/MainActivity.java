package com.ytempest.bannerlib;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.ytempest.banner.Banner;
import com.ytempest.banner.transformers.Transformers;
import com.ytempest.bannerlib.bean.BannerBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Banner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner = findViewById(R.id.view_main_banner);
        mBanner.setBannerBinder(new BannerBinder());
        // 设置banner展示时间
        mBanner.setPlayDuration(2000);
        // 设置banner切换动画时间
        mBanner.setScrollDuration(1000);
        // 设置标题栏在banner的位置
        mBanner.setBannerTitleGravity(Gravity.BOTTOM);
        // 设置切换动画
        mBanner.setScrollAnimation(Transformers.FLIP);

        mBanner.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 2000);
    }

    private void loadData() {
        int[] colors = new int[]{0xFF90C8C9, 0xFFDAE6E6, 0xFF655B52, 0xFFF0AE98, 0xFFAEC8C9,};
        String[] titles = new String[]{
                "道别",
                "我对蝉说：",
                "他日再见 要待来年",
                "蝉对我说：",
                "他日重逢 要等来生",
        };
        List<BannerBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new BannerBean(i, colors[i], titles[i]));
        }
        mBanner.display(list);
    }
}
