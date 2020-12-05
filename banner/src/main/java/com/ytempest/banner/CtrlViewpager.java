package com.ytempest.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author ytempest
 * @since 2020/11/26
 */
public class CtrlViewpager extends ViewPager {

    public CtrlViewpager(Context context) {
        this(context, null);
    }

    public CtrlViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isDisableScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isDisableScroll && super.onTouchEvent(ev);
    }

    private boolean isDisableScroll;

    public void setDisableScroll(boolean disable) {
        isDisableScroll = disable;
    }
}
