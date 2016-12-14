package com.dingqiqi.correlationview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<String> mList;

    private CorrelationView mCorrelationView;
    /**
     * 第一次加载
     */
    private boolean mIsFirst = true;

    private TextView mTextView;
    private CustomPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mCorrelationView = (CorrelationView) findViewById(R.id.correlationView1);
        mTextView = (TextView) findViewById(R.id.tv);

        mList = new ArrayList<>();

        for (int i = 0; i < 70; i++) {
            mList.add("点我看看" + i);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new CustomPageAdapter(this);
        mViewPager.setAdapter(mAdapter);
        //解决超过3个界面后，之后的界面联动失败
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mIsFirst) {
                    mIsFirst = false;
                    View view = mViewPager.getChildAt(position);
                    if (view == null) {
                        view = mAdapter.getCurrentView();
                    }

                    if (view instanceof ViewGroup) {
                        mCorrelationView.setBodyView(((ViewGroup) view).getChildAt(0));
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("aaa", "start " + position);
                View view = mViewPager.getChildAt(position);

                if (view == null) {
                    //解决超过3个界面后，之后的界面获取为null
                    view = mAdapter.getCurrentView();
                }

                if (view != null && view instanceof ViewGroup) {
                    //Log.i("aaa", (((ViewGroup) view).getChildAt(0) instanceof ListView) + " end " + position);
                    mCorrelationView.setBodyView(((ViewGroup) view).getChildAt(0));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirstActivity.this, "head click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
