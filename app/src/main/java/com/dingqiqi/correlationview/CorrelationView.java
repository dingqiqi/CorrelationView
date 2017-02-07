package com.dingqiqi.correlationview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 上下联动控件
 * Created by dingqiqi on 2016/11/18.
 */
public class CorrelationView extends LinearLayout {
    /**
     * 上下问
     */
    private Context mContext;
    /**
     * 头部布局
     */
    private View mHeadView;
    /**
     * 底部布局
     */
    private View mBodyView;
    /**
     * 存储按下点的数据
     */
    private float mDownX, mDownY, mCurX, mCurY;
    /**
     * 头部高度增大
     */
    private boolean mIsHeadAdd;
    /**
     * 头部高度减小
     */
    private boolean mIsHeadReduce;
    /**
     * 头部高度
     */
    private int mHeadHeight;
    /**
     * 属性动画
     */
    private ValueAnimator mValueAnimator;
    /**
     * 滑动距离
     */
    private int mScrollY;
    /**
     * 动画时间
     */
    private long mDuration = 200;
    /**
     * 移动后改变比例
     */
    private int mChangeProport = 3;

    public CorrelationView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CorrelationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CorrelationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    /**
     * 初始化变量
     */
    private void initView() {
        //设置布局垂直方向
        setOrientation(LinearLayout.VERTICAL);

        mIsHeadAdd = false;
        mIsHeadReduce = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurX = ev.getX();
                mCurY = ev.getY();
                //水平滑动距离大于垂直滑动距离，则不作处理
                if (Math.abs(mCurY - mDownY) < Math.abs(mCurX - mDownX)) {
                    break;
                }

                //头部高度减小
                if (mCurY - mDownY < 0 && mHeadView.getMeasuredHeight() == mHeadHeight) {
                    mIsHeadReduce = true;

                    return true;
                    //头部高度增大
                } else if (mCurY - mDownY > 0 && !viewCanScroll() && mHeadView.getMeasuredHeight() == 0) {
                    mIsHeadAdd = true;

                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断第二个子控件是否可以向下滑动
     *
     * @return
     */
    private boolean viewCanScroll() {
        //Log.i("aaa", " canscroll " + ViewCompat.canScrollVertically(mBodyView, -1));
        if (ViewCompat.canScrollVertically(mBodyView, -1)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurX = ev.getX();
                mCurY = ev.getY();
                //根据手指滑动距离减小headView高度
                if (mIsHeadReduce) {
                    mScrollY = (int) (mCurY - mDownY);
                    //滑动距离不能超过控件高度
                    if (Math.abs(mScrollY) > mHeadHeight) {
                        mScrollY = -mHeadHeight;
                    } else if (mScrollY > 0) {
                        //不能滑出界面外
                        mScrollY = 0;
                    }
                    //设置headView高度
                    LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
                    params.height = mHeadHeight + mScrollY;
                    mHeadView.setLayoutParams(params);

                    //根据手指滑动距离增加headView高度
                } else if (mIsHeadAdd) {
                    mScrollY = (int) (mCurY - mDownY);
                    //滑动距离不能超过控件高度
                    if (Math.abs(mScrollY) > mHeadHeight) {
                        mScrollY = mHeadHeight;
                    } else if (mScrollY < 0) {
                        //不能滑出界面外
                        mScrollY = 0;
                    }
                    //设置headView高度
                    LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
                    params.height = mScrollY;
                    mHeadView.setLayoutParams(params);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mIsHeadReduce) {
                    mIsHeadReduce = false;

                    //减小高度(手指没把headView完全划进去)
                    LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();

                    if (mHeadHeight - params.height < mHeadHeight / mChangeProport) {
                        //headView的高度还原
                        mValueAnimator = ValueAnimator.ofInt(params.height, mHeadHeight);
                    } else {
                        //headView的高度到0
                        mValueAnimator = ValueAnimator.ofInt(params.height, 0);
                    }

                    mValueAnimator.setDuration(mDuration);
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
                            params.height = value;
                            mHeadView.setLayoutParams(params);
                        }
                    });
                    mValueAnimator.start();
                } else if (mIsHeadAdd) {
                    mIsHeadAdd = false;

                    //增大高度(手指没把headView完全划出来)
                    LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();

                    if (params.height < mHeadHeight / mChangeProport) {
                        //headView的高度还原
                        mValueAnimator = ValueAnimator.ofInt(params.height, 0);
                    } else {
                        //从控件高度到最大高度
                        mValueAnimator = ValueAnimator.ofInt(params.height, mHeadHeight);
                    }

                    mValueAnimator.setDuration(mDuration);
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int value = (int) valueAnimator.getAnimatedValue();
                            LayoutParams params = (LayoutParams) mHeadView.getLayoutParams();
                            params.height = value;
                            mHeadView.setLayoutParams(params);
                        }
                    });
                    mValueAnimator.start();
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一次进来时获取控件高度
        if (mHeadHeight == 0) {
            mHeadHeight = mHeadView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //必须包含两个子View
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("必须有两个子View");
        }
        //获取子View
        mHeadView = getChildAt(0);
        mBodyView = getChildAt(1);
    }

    /**
     * 只能设置滑动的View(ps:ListView,RecycleView,ScrollView)，用于判断是否可以滑动来改变HeadView的高度
     *
     * @param view
     */
    public void setBodyView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View 不能为空");
        }
        mBodyView = view;
    }

}
