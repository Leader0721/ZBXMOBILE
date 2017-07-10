package com.zbxn.widget.pulltorefreshlv;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zbxn.R;


/**
 * 带下拉刷新和上拉加载的自定义视图
 *
 * @author ChaiHongwei
 */
public class PullRefreshListView extends SwipeRefreshLayout
        implements
        OnScrollListener {
    private static final String TAG = PullRefreshListView.class
            .getSimpleName();
    /**
     * 列表视图
     */
    private ListView mListView;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mOnScrollListener;
    /**
     * 下拉刷新或上拉加载的监听器
     */
    private OnPullListener mOnPullListener;
    /**
     * 是否还有数据能加载
     */
    private boolean mHasMoreData = true;
    /**
     * 下拉刷新是否可用
     */
    private boolean mPullRefreshEnable = true;
    /**
     * 上拉加载是否可用
     */
    private boolean mPullLoadEnable = true;
    /**
     * 设置空是否可用
     */
    private boolean mSetEmptyEnable = true;
    /**
     * 底部加载进度视图
     */
    private ProgressBar pbLoading;
    /**
     * 底部提示信息视图
     */
    private TextView tvMsg;
    /**
     * 是否正在加载
     */
    private boolean mIsLoading = false;
    /**
     * 是否正在刷新
     */
    private boolean mIsRefreshing = false;
    /**
     * 底部视图是否初始化完毕
     */
    private boolean mIsFooterInited = false;
    /**
     * 是否正在滚动
     */
    private boolean mIsScrolling = false;

    //是否添加了头部
    private boolean isAddHeader = false;
    //是否添加了底部
    private boolean isAddFooter = false;

    public PullRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PullToRefresh);

        mPullRefreshEnable = a.getBoolean(
                R.styleable.PullToRefresh_isRefreshEnable, true);
        mPullLoadEnable = a.getBoolean(R.styleable.PullToRefresh_isLoadEnable,
                true);
        mSetEmptyEnable = a.getBoolean(R.styleable.PullToRefresh_isSetEmptyEnable,
                true);


        a.recycle();

        init(context);
    }

    private void init(final Context context) {
        mListView = new ListView(context);
        mListView.setSelector(android.R.color.transparent);
        mListView.setCacheColorHint(0x00000000);
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        mListView.setVerticalFadingEdgeEnabled(false);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setDivider(context.getResources().getDrawable(R.color.pub_line_color));
        mListView.setDividerHeight(1);

        mListView.setOnScrollListener(this);

        // 设置是否支持下拉刷新
        setEnabled(mPullRefreshEnable);

        // 如果支持上拉加载,则给视图添加底部视图
        if (mPullLoadEnable) {
            addFooterView();
        }

        RelativeLayout layout = new RelativeLayout(context);
        layout.addView(mListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (mSetEmptyEnable) {
            LayoutInflater listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
            View emptyView = listContainer.inflate(R.layout.base_prompt_loading_empty_list, null);
            emptyView.setVisibility(GONE);
            mListView.setEmptyView(emptyView);
            layout.addView(emptyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        addView(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // addView(mListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // 设置下拉刷新事件监听器
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                startRefreshing();
            }
        });

    }

    /**
     * 当视图可见并且能向上滚动时返回true,否则返回false
     */
    @Override
    public boolean canChildScrollUp() {
        final ListView listView = getListView();
        if (listView.getVisibility() == View.VISIBLE) {
            return canListViewScrollUp();
        } else {
            return false;
        }
    }

    /**
     * 检查是否能向上滚动视图
     *
     * @return 若能向上滚动视图则返回true，否则返回false
     */
    private boolean canListViewScrollUp() {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(mListView, -1);
        } else {
            return mListView.getChildCount() > 0
                    && (mListView.getFirstVisiblePosition() > 0 || mListView
                    .getChildAt(0).getTop() < mListView.getPaddingTop());
        }
    }

    /**
     * 添加底部视图
     */
    @SuppressLint("InflateParams")
    private void addFooterView() {
        View footerView = LayoutInflater.from(getContext()).inflate(
                R.layout.pull_to_refresh_footer_layout, null);
        pbLoading = findViewById(footerView, R.id.pbLoading);
        tvMsg = findViewById(footerView, R.id.tvMsg);

        isAddFooter = true;
        mListView.addFooterView(footerView, null, false);

        mIsFooterInited = true;
    }

    /**
     * 查找子视图
     *
     * @param view 父视图
     * @param id   子视图id
     * @return 返回相应类型的子视图
     */
    @SuppressWarnings("unchecked")
    private <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    /**
     * 判断最后一个列表项是否完全显示出来
     *
     * @return true完全显示, 否则就没有完全显示出来
     */
    private boolean isLastItemVisible() {
        final ListAdapter adapter = mListView.getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition
                    - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    /**
     * 判断第一项是否完全显示出来
     *
     * @return true完全显示, 否则就没有完全显示出来
     */
    @SuppressWarnings("unused")
    private boolean isFirstItemVisible() {
        final ListAdapter adapter = mListView.getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int firstItemTop = (mListView.getChildCount() > 0) ? mListView
                .getChildAt(0).getTop() : 0;
        if (firstItemTop >= 0) {
            return true;
        }

        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mPullLoadEnable && mHasMoreData) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isLastItemVisible()) {
                    startLoading();
                }

                mIsScrolling = false;
            } else {
                mIsScrolling = true;
            }
        }
        // 触发用户自己的滚动状态事件
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        Log.d(TAG, firstVisibleItem + " " + visibleItemCount + " "
//                + totalItemCount);
        // 如果停止滚动且一直在第一屏,并且没有正在加载数据,并且还有数据要加载,就不显示底部加载视图
        if (!mIsScrolling && firstVisibleItem == 0 && !mIsLoading
                && mHasMoreData) {
            setFooterState(false, "");
        }
        // 触发用户自己的滚动事件
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    /**
     * 开始下拉刷新
     */
    private void startRefreshing() {
        // 每次只能做一件事,要么刷新,要么加载
        if (mIsLoading || mIsRefreshing) {
            return;
        }
        mIsRefreshing = true;

        // 刷新则默认还有数据可以加载
        mHasMoreData = true;

        if (mOnPullListener != null) {
            mOnPullListener.onRefresh();
        }
    }

    /**
     * 开始加载更多
     */
    private void startLoading() {
        // 每次只能做一件事,要么刷新,要么加载
        if (mIsLoading || mIsRefreshing) {
            return;
        }

        mIsLoading = true;
        setFooterState(true, "正在加载数据...");

        if (mOnPullListener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnPullListener.onLoad();
                }
            }, 100);
        }
    }

    /**
     * 设置底部信息
     *
     * @param isShowLoading 是否显示进度
     * @param msg           提示信息
     */
    private void setFooterState(boolean isShowLoading, String msg) {
        // 只有启用了上拉加载才会有底部信息
        if (mPullLoadEnable && mIsFooterInited) {
            pbLoading.setVisibility(isShowLoading ? View.VISIBLE : View.GONE);
            tvMsg.setText(msg);
        }
    }

    /**
     * 设置视图滚动监听监听器
     *
     * @param listener 滚动监听事件
     */
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    /**
     * 设置上拉刷新和下拉加载的事件监听器
     *
     * @param listener 事件监听器
     */
    public void setOnPullListener(OnPullListener listener) {
        mOnPullListener = listener;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        mHasMoreData = hasMoreData;

        if (!hasMoreData) {
            setFooterState(false, "");
        }
    }

    /**
     * 刷新完成
     */
    public void onRefreshComplete() {
        mIsRefreshing = false;
    }

    /**
     * 加载完成
     */
    public void onLoadComplete() {
        mIsLoading = false;
        setFooterState(false, "");
    }

    public void onComplete() {
        this.setRefreshing(false);
        this.onRefreshComplete();
        this.onLoadComplete();
    }

    /**
     * 下拉刷新是否可用
     *
     * @param pullRefreshEnable true下拉刷新可用,false不可用,默认true
     */
    public void setPullRefreshEnable(boolean pullRefreshEnable) {
        mPullRefreshEnable = pullRefreshEnable;

        setEnabled(pullRefreshEnable);
    }

    /**
     * 上拉加载是否可用
     *
     * @param pullLoadEnable true上拉加载可用,false不可用,默认true
     */
    public void setPullLoadEnabled(boolean pullLoadEnable) {
        mPullLoadEnable = pullLoadEnable;
    }

    /**
     * 获取列表视图
     *
     * @return 返回一个带下拉刷新和上拉加载功能的列表视图
     */
    public ListView getListView() {
        return mListView;
    }

    /**
     * 设置列表视图的数据
     *
     * @param adapter 数据适配器,承载着列表视图的数据
     */
    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    /**
     * 设置列表视图的点击某项item事件
     *
     * @param listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    /**
     * 获取数据适配器
     *
     * @return 当前列表视图所使用的数据适配器
     */
    public ListAdapter getAdapter() {
        return mListView.getAdapter();
    }

    /**
     * 下拉刷新和上拉加载的接口
     *
     * @author ChaiHongwei
     */
    public interface OnPullListener {
        /**
         * 下拉刷新时触发
         */
        void onRefresh();

        /**
         * 上拉加载时触发
         */
        void onLoad();
    }

    public void startFirst() {
        setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        setRefreshing(true);
    }

    public void addHeaderView(View v) {
        isAddHeader = true;
        mListView.addHeaderView(v);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mListView.setOnItemLongClickListener(listener);
    }

    public void onRefreshFinish() {
        setRefreshing(false);
        onRefreshComplete();
        onLoadComplete();
    }
}
