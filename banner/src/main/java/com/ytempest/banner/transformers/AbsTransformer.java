package com.ytempest.banner.transformers;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * @author ytempest
 * @since 2020/11/30
 */
public abstract class AbsTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        // 当前页面: ViewPager的左边框线所在的页面为当前页面
        if (position < -1) { // [-Infinity,-1)、-1 -> -Infinity : 当前页面的左边页面被当前页面替换时调用
            onPrePageOffset(page, position);

        } else if (position <= 0) { // [-1,0]、0 -> -1 : 当前页面退出屏幕时调用
            onCurrentPageExitScreen(page, position);

        } else if (position <= 1) { // (0,1]、1 -> 0 : 当前页面的右边页面进入屏幕时调用
            onNextPageEnterScreen(page, position);

        } else if (1 < position) { // (1,+Infinity]、Infinity -> 1 : 当前页面的右边页被其右边页替换时调用
            onNextPageOffset(page, position);
        }
    }

    protected void onPrePageOffset(View page, float position) {
    }

    protected abstract void onCurrentPageExitScreen(View page, float position);

    protected abstract void onNextPageEnterScreen(View page, float position);

    protected void onNextPageOffset(View page, float position) {
    }

}
