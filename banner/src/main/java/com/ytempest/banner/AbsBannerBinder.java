package com.ytempest.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ytempest
 * @since 2020/11/25
 */
public abstract class AbsBannerBinder<Item> {

    private final InternalPagerAdapter mAdapter = new InternalPagerAdapter();
    private LayoutInflater mInflater;

    private LayoutInflater getInflater(Context context) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(context);
        }
        return mInflater;
    }

    InternalPagerAdapter getAdapter() {
        return mAdapter;
    }

    void update(List<Item> list) {
        mCacheViews.clear();
        mItems.clear();
        mItems.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /*Data*/

    private final List<Item> mItems = new ArrayList<>();
    private final Map<Integer, View> mCacheViews = new HashMap<>();

    public int getItemCount() {
        return mItems.size();
    }

    public int getItemPosition(Item item) {
        return mItems.indexOf(item);
    }

    int caleAdjustPosition(int pos) {
        if (pos == 0) {
            return mItems.size();
        } else if (pos == mAdapter.getCount() - 1) {
            return mItems.size() - 1;
        }
        return pos;
    }

    int toRealPosition(int pos) {
        int size = mItems.size();
        return size == 0 ? 0 : pos % size;
    }

    int getStartPosition() {
        return mItems.size();
    }

    View createTitleVie(ViewGroup group) {
        return onCreateTitleView(getInflater(group.getContext()), group);
    }

    void updateTitleView(View titleView, int pos) {
        int realPos = toRealPosition(pos);
        Item data = mItems.get(realPos);
        onUpdateTitleView(titleView, data, realPos, mItems.size());
    }

    /**
     * to create the banner pager view.
     * Note: to loop the banner, this method will be call two seconds to create two view for banner
     * in same data
     */
    @NonNull
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Item data, int position);

    /**
     * create the title view for banner. If you create title view, it will be call the
     * {@link AbsBannerBinder#onUpdateTitleView(View, Object, int, int)} after page switch
     */
    @Nullable
    protected abstract View onCreateTitleView(LayoutInflater inflater, ViewGroup container);

    /**
     * This method will invoke when the page switch, you can do something after it. But you must
     * create title view use {@link AbsBannerBinder#onCreateTitleView(LayoutInflater, ViewGroup)}
     * for banner, otherwise the method didn't call
     */
    protected abstract void onUpdateTitleView(@Nullable View view, Item data, int position, int count);

    class InternalPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mItems.size() * 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return obj == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int pos) {
            View view = mCacheViews.get(pos);
            if (view == null) {
                int realPos = toRealPosition(pos);
                Item item = mItems.get(realPos);
                view = onCreateContentView(getInflater(container.getContext()), container, item, realPos);
                mCacheViews.put(pos, view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
