package com.ytempest.banner.transformers;

import android.view.View;

/**
 * @author ytempest
 * @since 2020/11/30
 */
public class DampingTransformer extends AbsTransformer {

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
        page.setAlpha(1);
    }

    @Override
    protected void onCurrentPageExitScreen(View page, float position) {
    }

    @Override
    protected void onNextPageEnterScreen(View page, float position) {
        page.setTranslationX(-page.getWidth() / 5f * position);
        page.setAlpha(1 - Math.abs(position));
    }

}
