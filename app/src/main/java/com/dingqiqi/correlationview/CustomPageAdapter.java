package com.dingqiqi.correlationview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingqiqi on 2016/11/18.
 */
public class CustomPageAdapter extends PagerAdapter {

    private Context mContext;
    private ListView mListView;

    private View mCurrentView;

    public CustomPageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null, false);

        List<String> mList = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            mList.add("左右滑动看看    第" + position + "页    " + i);
        }

        mListView = (ListView) view.findViewById(R.id.item_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.activity_list_item, android.R.id.text1, mList);
        mListView.setAdapter(adapter);

        container.addView(view);

        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
    }

    /**
     * 获取当前页面
     * @return
     */
    public View getCurrentView() {
        return mCurrentView;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
