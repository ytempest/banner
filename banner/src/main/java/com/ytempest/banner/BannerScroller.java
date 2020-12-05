package com.ytempest.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author ytempest
 * @since 2020/11/25
 */
public class BannerScroller extends Scroller {

    private final InterpolatorProxy mProxy;

    public BannerScroller(Context context) {
        this(context, new InterpolatorProxy());
    }

    private BannerScroller(Context context, InterpolatorProxy proxy) {
        super(context, proxy);
        mProxy = proxy;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    private int mDuration = Configs.DURATION_SCROLL;

    public void setScrollDuration(int duration) {
        this.mDuration = duration;
    }

    public int getScrollDuration() {
        return mDuration;
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator != null) {
            mProxy.setTargetInterpolator(interpolator);
        }
    }

    private static class InterpolatorProxy implements Interpolator {

        private Interpolator mTarget = new ViscousFluidInterpolator();

        public void setTargetInterpolator(Interpolator interpolator) {
            mTarget = interpolator;
        }

        @Override
        public float getInterpolation(float input) {
            return mTarget.getInterpolation(input);
        }
    }

    /**
     * Copy form {@link Scroller}
     */
    private static class ViscousFluidInterpolator implements Interpolator {
        /**
         * Controls the viscous fluid effect (how much of it).
         */
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        static {

            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0f / viscousFluid(1.0f);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0f - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f);
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < 1.0f) {
                x -= (1.0f - (float) Math.exp(-x));
            } else {
                float start = 0.36787944117f;   // 1/e == exp(-1)
                x = 1.0f - (float) Math.exp(1.0f - x);
                x = start + x * (1.0f - start);
            }
            return x;
        }

        @Override
        public float getInterpolation(float input) {
            final float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }
}
