package com.ytempest.banner.transformers;

import android.view.View;

/**
 * @author ytempest
 * @since 2020/12/2
 */
public class Rotation3DTransformer extends AbsTransformer {

    private static final int ANGLE = 90;
    private static final float AMPLITUDE = 1.52F;

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
        page.setRotationY(0);
        page.setAlpha(1);
    }

    @Override
    protected void onCurrentPageExitScreen(View page, float position) {
        position = Math.abs(position);
        float up = (float) Math.pow(position, AMPLITUDE);
        page.setPivotX(page.getWidth());
        page.setPivotY(page.getHeight() / 2F);
        page.setRotationY(-up * ANGLE);
        page.setAlpha(0.5F + 0.5F * (1 - position));
    }

    @Override
    protected void onNextPageEnterScreen(View page, float position) {
        page.setPivotX(0);
        page.setPivotY(page.getHeight() / 2F);
        position = Math.abs(position);
        float up = (float) Math.pow(position, AMPLITUDE);
        page.setRotationY(up * ANGLE);
        page.setAlpha(0.5F + 0.5F * (1 - position));
    }
}
