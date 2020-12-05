package com.ytempest.banner.transformers;

import android.view.View;

/**
 * @author ytempest
 * @since 2020/11/27
 */
public class DefaultTransformer extends AbsTransformer {

    @Override
    protected void onPrePageOffset(View page, float position) {
        super.onPrePageOffset(page, position);
        reset(page);
    }

    @Override
    protected void onNextPageOffset(View page, float position) {
        super.onNextPageOffset(page, position);
        reset(page);
    }

    private void reset(View page) {
        page.setTranslationX(0);
        page.setAlpha(1F);
        page.setScaleX(1);
        page.setScaleY(1);
    }

    @Override
    protected void onCurrentPageExitScreen(View page, float position) {
    }

    @Override
    protected void onNextPageEnterScreen(View page, float position) {
        page.setTranslationX(-position * page.getWidth());
        page.setAlpha(1F - position);
        float scale = Math.max(0, 1F - position * 0.3F);
        page.setScaleX(scale);
        page.setScaleY(scale);
    }


}
