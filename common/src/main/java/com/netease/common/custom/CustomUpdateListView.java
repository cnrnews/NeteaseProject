package com.netease.common.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netease.common.R;
import com.netease.common.custom.inter.ICustomUpdateListViewBack;
import com.netease.common.utils.LogUtil;
import com.netease.common.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Liudeli
 * @Describe：自定义下拉刷新上拉加载
 */
public class CustomUpdateListView extends ListView implements AbsListView.OnScrollListener{

    private static final String TAG = CustomUpdateListView.class.getSimpleName();

    /**
     * 下拉刷新
     */
    private static final int DOWN_UPDATE = 111;

    /**
     * 准备刷新
     */
    private static final int PLAN_UPDATE = 112;

    /**
     * 正在刷新
     */
    private static final int PROCESS_UPDATE = 113;

    private int thisUpdateStatusValue = DOWN_UPDATE; // 默认一直是下拉刷新

    public CustomUpdateListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnScrollListener(this);

        initHeader();
        initBottom();
    }

    /**
     * 定义头部相关
     */
    private View headerView;
    private int headerViewHeight;

    private ImageView ivHeaderArrow;
    private ProgressBar pbHeader;
    private TextView tvHeaderState;
    private TextView tvHeaderLastUpdateTime;

    /**
     * 定义底部相关
     */
    private View bottomView;
    private int bottomViewHeight;
    private TextView tvBottomState;
    /**
     * 初始化头部 布局View相关
     */
    private void initHeader() {
        // 从布局中拿到一个View
        headerView = View.inflate(getContext(), R.layout.listview_header, null);

        // 获取头部各个控件的值
        ivHeaderArrow = headerView.findViewById(R.id.iv_listview_header_arrow);
        pbHeader = headerView.findViewById(R.id.pb_listview_header);
        tvHeaderState = headerView.findViewById(R.id.tv_listview_header_state);
        tvHeaderLastUpdateTime = headerView.findViewById(R.id.tv_listview_header_last_update_time);

        tvHeaderLastUpdateTime.setText(TimeUtil.getThisTiem());

        // getHieight(); 方法只能获取到控件显示后的高度
        // int headerViewHeight = headerView.getHeight();
        // 结果 headerViewHeight: 0

        // View的绘制流程：测量 onLayout onDraw

        // 所以先测量后，就能得到测量后的高度了
        headerView.measure(0, 0); // 注意：传0系统会自动去测量View高度

        // 得到测量后的高度
        headerViewHeight = headerView.getMeasuredHeight();
        Log.i(TAG, "headerViewHeight:" + headerViewHeight);

        headerView.setPadding(0, -headerViewHeight, 0 ,0);

        addHeaderView(headerView);

        initHeaderAnimation();
    }

    private void initBottom() {
        bottomView = View.inflate(getContext(), R.layout.listview_bottom, null);

        tvBottomState = bottomView.findViewById(R.id.tv_bottom_state);

        // 先测量
        bottomView.measure(0, 0);

        // 获取高度
        bottomViewHeight = bottomView.getMeasuredHeight();

        bottomView.setPadding(0, -bottomViewHeight, 0, 0);
        // bottomView.setPadding(0, 0, 0, 0);

        addFooterView(bottomView);

    }

    private RotateAnimation upRotateAnimation;
    private RotateAnimation downRotateAnimation;

    private void initHeaderAnimation() {
        upRotateAnimation = new RotateAnimation(
                0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        upRotateAnimation.setDuration(500);
        upRotateAnimation.setFillAfter(true);

        downRotateAnimation = new RotateAnimation(
                180, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        downRotateAnimation.setDuration(500);
        downRotateAnimation.setFillAfter(true);
    }

    private boolean prohibit;

    /**
     * 滑动的状态改变
     * @param view
     * @param scrollState 有三种状态
     *                    SCROLL_STATE_IDLE 代表 滑动停止状态类似于手指松开UP
     *                    SCROLL_STATE_TOUCH_SCROLL 代表滑动触摸状态
     *                    SCROLL_STATE_FLING 快速滑动 猛的一滑
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 如果是猛地滑动 或者 手指松开UP 才显示底部布局View
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 判断必须是底部的Item的时候
            if (getLastVisiblePosition() == (getCount() -1)) {

                if (!"正在刷新中...".equals(tvHeaderState.getText().toString())) {
                    bottomView.setPadding(0, 0, 0, 0);
                    LogUtil.d(TAG, ">>>如果是猛地滑动 或者 手指松开UP 才显示底部布局View");

                    // prohibit = true;

                    // 回调接口方法
                    if(customUpdateListViewBack != null) {
                        customUpdateListViewBack.upUpdateListData();
                    }
                }
            }
        }
    }

    private int firstVisibleItem;

    /**
     * ListView滑动的监听方法
     * @param view 当前ListView
     * @param firstVisibleItem 当前屏幕的第一个显示的Item
     * @param visibleItemCount 当前屏幕显示的Item数量
     * @param totalItemCount 总共Item数量
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                /*if (prohibit) {
                    return prohibit;
                }*/

                if (thisUpdateStatusValue == DOWN_UPDATE) {
                    headerView.setPadding(0, -headerViewHeight ,0 ,0);
                } else {
                    headerView.setPadding(0, 0, 0, 0);
                    thisUpdateStatusValue = PROCESS_UPDATE;
                    updateHeaderState();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                /*if (prohibit) {
                    return prohibit;
                }*/

                int cha = (int) ev.getY() - downY;
                if (this.firstVisibleItem == 0 && cha > 0) {
                    int paddingTop = -headerViewHeight + cha;
                    // Log.i(TAG, "paddingTop:" + paddingTop);

                    if (thisUpdateStatusValue == PROCESS_UPDATE) {
                        break;
                    }

                    if (paddingTop > 0 && thisUpdateStatusValue == DOWN_UPDATE) {
                        // 准备刷新
                        Log.i(TAG, "paddingTop:" + paddingTop + ">>>准备刷新");
                        thisUpdateStatusValue = PLAN_UPDATE;

                        updateHeaderState();

                    } else if (paddingTop < 0 && thisUpdateStatusValue == PLAN_UPDATE) {
                        // 正在刷新
                        Log.i(TAG, "paddingTop:" + paddingTop + ">>>正在刷新");
                        thisUpdateStatusValue = DOWN_UPDATE;

                        updateHeaderState();
                    }

                    headerView.setPadding(0, paddingTop, 0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev); // 不返回ture 而是去调用父类的方法，是保证ListView自身的滑动功能正常
    }

    private void updateHeaderState() {

        switch (thisUpdateStatusValue) {
            case DOWN_UPDATE:
                ivHeaderArrow.startAnimation(downRotateAnimation);
                tvHeaderState.setText("下拉刷新");
                break;
            case PLAN_UPDATE:
                ivHeaderArrow.startAnimation(upRotateAnimation);
                tvHeaderState.setText("准备刷新");
                break;
            case PROCESS_UPDATE:
                ivHeaderArrow.setVisibility(INVISIBLE);
                ivHeaderArrow.clearAnimation();
                pbHeader.setVisibility(VISIBLE);
                tvHeaderState.setText("正在刷新中...");

                LogUtil.d(TAG, ">>>往下拉到某个位置，下拉刷新之正在刷新...");

                prohibit = true;

                if (null != customUpdateListViewBack) {
                    customUpdateListViewBack.downUpdateListData();
                }
                break;
            default:
                break;
        }
    }

    private ICustomUpdateListViewBack customUpdateListViewBack;

    public void setCallback(ICustomUpdateListViewBack back) {
        this.customUpdateListViewBack = back;
    }

    public final int UPDATE_HEADER_RESULT_ACTION = 89;

    public final int UPDATE_BOTTOM_RESULT_ACTION = 99;

    public int updateResultStatus = -1;

    public void commit() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (updateResultStatus) {
                    case UPDATE_HEADER_RESULT_ACTION:
                        updateHeaderResult();
                        break;
                    case UPDATE_BOTTOM_RESULT_ACTION:
                        updateBottomResult();
                        break;
                    default:
                        break;
                }

                updateResultStatus = -1;
                prohibit = false;
            }
        },4000);
    }

    public void updateHeaderResult() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                headerView.setPadding(0, -headerViewHeight, 0, 0);

                // 状态还原
                ivHeaderArrow.clearAnimation();
                tvHeaderState.setText("下拉刷新");

                ivHeaderArrow.setVisibility(VISIBLE);
                pbHeader.setVisibility(INVISIBLE);

                tvHeaderLastUpdateTime.setText(TimeUtil.getThisTiem());

                thisUpdateStatusValue = DOWN_UPDATE;

            }

        }, 4000);
    }

    public void updateBottomResult() {
        /*tvBottomState.setText("加载成功");
        tvBottomState.setTextColor(Color.GREEN);*/

        //synchronized (this) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomView.setPadding(0, -bottomViewHeight, 0, 0);
            }

            //}

        }, 4000);
    }
}
