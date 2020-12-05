package com.ytempest.banner.transformers;

import android.view.View;

/**
 * @author ytempest
 * @since 2020/12/2
 */
public class FlipTransformer extends AbsTransformer {

    private static final int EXIT_ANGLE = 90;
    private static final int ENTER_ANGLE = 40;

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
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationX(0);
        page.setRotationY(0);
    }

    @Override
    protected void onCurrentPageExitScreen(View page, float position) {
        float up = Math.abs(position);
        page.setTranslationX(page.getWidth() * up);
        page.setPivotX(0);
        page.setPivotY(page.getHeight() / 2F);
        page.setRotationY(up * EXIT_ANGLE);
    }

    @Override
    protected void onNextPageEnterScreen(View page, float position) {
        int width = page.getWidth();
        page.setTranslationX(-width * position);
        page.setPivotX(width);
        page.setPivotY(page.getHeight() / 2F);
        page.setRotationY(-Math.abs(position) * ENTER_ANGLE);
    }
}
