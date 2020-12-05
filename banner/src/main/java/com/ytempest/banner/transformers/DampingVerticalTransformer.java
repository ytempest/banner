package com.ytempest.banner.transformers;

import android.view.View;

/**
 * @author ytempest
 * @since 2020/11/30
 */
public class DampingVerticalTransformer extends AbsTransformer {

    private static final float ALPHA_RANGE = 0.7F;

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
        page.setTranslationY(0);
        page.setAlpha(1);
    }

    @Override
    protected void onCurrentPageExitScreen(View page, float position) {
        position = Math.abs(position);
        page.setTranslationX(page.getWidth() * position);
        page.setTranslationY(-page.getHeight() * position);
        page.setAlpha(1 - ALPHA_RANGE + (ALPHA_RANGE) * (1 - position));
    }

    @Override
    protected void onNextPageEnterScreen(View page, float position) {
        position = Math.abs(position);
        page.setTranslationX(-page.getWidth() * position);
        page.setTranslationY(page.getHeight() * position);
        page.setAlpha(1 - ALPHA_RANGE + (ALPHA_RANGE) * (1 - position));
    }

}
