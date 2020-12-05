package com.ytempest.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ytempest.banner.transformers.Transformers;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author ytempest
 * @since 2020/11/23
 * This is an extendable banner, you can defined yourself banner page view and banner title view and
 * bind data or update data for them
 * <p>
 * Banner will be auto play pager, it need you to control it's state by
 * {@link Banner#startAutoPlay()} and {@link Banner#stopAutoPlay()}
 */
public class Banner extends FrameLayout {

    private static final String TAG = Banner.class.getSimpleName();

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_banner_view, this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // MeasureSpec.UNSPECIFIED是为了兼容在ConstraintLayout中的warp_content
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = dp2px(getContext(), Configs.DEF_WIDTH);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = dp2px(getContext(), Configs.DEF_HEIGHT);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode));
    }

    private CtrlViewpager mViewPager;
    private LinearLayout mTitleContainer;
    private View mTitleView;

    private BannerScroller mScroller;
    private AbsBannerBinder mBinder;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleContainer = findViewById(R.id.widget_banner_title_container);
        mViewPager = findViewById(R.id.widget_banner_view_pager);

        try {
            mScroller = new BannerScroller(getContext());
            // 通过放射设置 ViewPager的 bannerScroller 以改变 BannerViewPager 页面切换的速度
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(mViewPager, mScroller);
        } catch (Exception e) {
            Log.e(TAG, "fail to set scroller for banner, error msg : " + e.getMessage());
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(mBinder.toRealPosition(position), positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mBinder.updateTitleView(mTitleView, position);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(mBinder.toRealPosition(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int pos = mViewPager.getCurrentItem();
                    int adjustPos = mBinder.caleAdjustPosition(pos);
                    mViewPager.setCurrentItem(adjustPos, false);
                    startAutoPlay();

                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    // 用户拖拽时会调用SCROLL_STATE_DRAGGING，松开时需要重设轮播状态
                    stopAutoPlay();
                }
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    /**
     * show banner data, the type of data need as same as  {@link AbsBannerBinder} that you set
     *
     * @throws NullPointerException if you not set binder by {@link Banner#setBannerBinder(AbsBannerBinder)}
     */
    public void display(List data) {
        if (data == null || data.isEmpty()) return;
        if (mBinder == null) throw new NullPointerException("Please set banner binder firstly");
        stopAutoPlay();
        mBinder.update(data);
        mViewPager.setCurrentItem(mBinder.getStartPosition(), false);
        mViewPager.setDisableScroll(isDisableUserDragging());
        startAutoPlay();
    }

    /*Play*/

    private int mPlayDuration = Configs.DURATION_PLAY;
    private boolean isPlaying;
    private boolean disableUserDragging;
    private boolean disableAutoPlay;
    private final Runnable mPlayTask = new Runnable() {
        @Override
        public void run() {
            int nextPos = mViewPager.getCurrentItem() + 1;
            nextPos = nextPos % mBinder.getAdapter().getCount();
            mViewPager.setCurrentItem(nextPos);
            startAutoPlayInternal();
        }
    };

    private void startAutoPlayInternal() {
        postDelayed(mPlayTask, mPlayDuration + mScroller.getScrollDuration());
    }

    /**
     * you need to call the method when the page visible, such as Activity#onResume()
     */
    public void startAutoPlay() {
        if (!isPlaying && isAutoPlayable()) {
            isPlaying = true;
            startAutoPlayInternal();
        }
    }

    /**
     * you need to call the method when the page invisible, such as Activity#onPause()
     */
    public void stopAutoPlay() {
        if (isPlaying) {
            isPlaying = false;
            removeCallbacks(mPlayTask);
        }
    }

    public boolean isAutoPlayable() {
        return mBinder != null && mBinder.getItemCount() > 1 && !disableAutoPlay;
    }

    public boolean isDisableUserDragging() {
        return disableUserDragging || mBinder == null || mBinder.getItemCount() <= 1;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    /*config*/

    public void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    /**
     * set the duration for page transform
     *
     * @param duration unit: microseconds
     */
    public void setScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    /**
     * set the interceptor for page transform animation
     */
    public void setScrollInterpolator(Interpolator interceptor) {
        mScroller.setInterpolator(interceptor);
    }

    /**
     * set the duration for per page show time
     *
     * @param duration unit: microseconds
     */
    public void setPlayDuration(int duration) {
        mPlayDuration = duration;
    }

    public void setDisableUserDragging(boolean disable) {
        disableUserDragging = disable;
    }

    public void setDisableAutoPlay(boolean disable) {
        this.disableAutoPlay = disable;
    }

    public void setScrollAnimation(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * set the page transformations. {@link Transformers}
     */
    public void setScrollAnimation(Class<? extends ViewPager.PageTransformer> clazz) {
        try {
            mViewPager.setPageTransformer(true, clazz.newInstance());
        } catch (Exception e) {
            Log.e(TAG, "setPageTransformer: error msg : " + e.getMessage());
        }
    }

    /**
     * set the position for banner title
     *
     * @param gravity {@link android.view.Gravity}
     */
    public void setBannerTitleGravity(int gravity) {
        mTitleContainer.setGravity(gravity);
    }

    public int getCurrentItem() {
        int pos = mViewPager.getCurrentItem();
        return mBinder == null ? 0 : mBinder.toRealPosition(pos);
    }

    /*Binder*/

    public void setBannerBinder(@NonNull AbsBannerBinder binder) {
        mBinder = binder;
        mViewPager.setAdapter(mBinder.getAdapter());
        mTitleContainer.removeAllViews();
        mTitleView = mBinder.createTitleVie(mTitleContainer);
        if (mTitleView != null) {
            mTitleContainer.addView(mTitleView);
        }
    }

    /*Listener*/

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    private static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}