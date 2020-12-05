package com.ytempest.bannerlib;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ytempest.banner.AbsBannerBinder;
import com.ytempest.bannerlib.bean.BannerBean;

import java.util.Locale;

/**
 * @author heqidu
 * @since 2020/12/5
 */
public class BannerBinder extends AbsBannerBinder<BannerBean> implements View.OnClickListener {

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, BannerBean data, int position) {
        View view = inflater.inflate(R.layout.item_banner_content, container, false);
        ImageView imageView = view.findViewById(R.id.iv_banner_content_img);
        imageView.setBackgroundColor(data.getImgColor());
        TextView titleView = view.findViewById(R.id.tv_banner_content_title);
        titleView.setText(String.valueOf(data.getNo()));
        view.setOnClickListener(this);
        view.setTag(data);
        return view;
    }

    @Nullable
    @Override
    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.item_banner_title, container, false);
    }

    @Override
    protected void onUpdateTitleView(@Nullable View view, BannerBean data, int position, int count) {
        if (view == null) return;
        TextView titleView = view.findViewById(R.id.tv_banner_title_text);
        titleView.setText(data.getTitle());
        TextView countView = view.findViewById(R.id.tv_banner_title_count);
        countView.setText(String.format(Locale.US, "%d/%d", position + 1, count));
    }

    @Override
    public void onClick(View v) {
        BannerBean bean = (BannerBean) v.getTag();
        Toast.makeText(v.getContext(), bean.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
