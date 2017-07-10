package com.zbxn.pub.utils;

import com.zbxn.pub.R;

import widget.pulltorefresh.PtrDefaultHandler;
import widget.pulltorefresh.PtrFrameLayout;
import widget.pulltorefresh.PtrHandler;
import widget.pulltorefresh.header.MaterialHeader;
import widget.pulltorefresh.loadmore.LoadMoreContainer;
import widget.pulltorefresh.loadmore.LoadMoreHandler;
import widget.pulltorefresh.loadmore.LoadMoreListViewContainer;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Listview下拉刷新、上拉加载更多的实现
 *
 * @author GISirFive
 */
public class ListviewUpDownHelper implements LoadMoreHandler {

    public PtrFrameLayout mLayoutContainer;
    public MaterialHeader mLayoutHeader;
    public LoadMoreListViewContainer mLayoutFooter;
    public ListView mListView;

    private OnRequestDataListener mListener;

    public ListviewUpDownHelper(OnRequestDataListener listener) {
        mListener = listener;
    }

    public void bind(Activity activity) {
        init(activity.getWindow().getDecorView());
    }

    public void bind(View root) {
        init(root);
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        if (mListener != null)
            mListener.onLoadMore();
    }

    /***
     * 自动刷新记录
     *
     * @author GISirFive
     */
    public void autoRefresh() {
        mLayoutContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutContainer.autoRefresh(true);
            }
        }, 300);
    }

    /**
     * 延迟delayMillis毫秒后，自动刷新记录
     *
     * @param delayMillis 延迟毫秒数
     * @author GISirFive
     */
    public void autoRefresh(long delayMillis) {
        mLayoutContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutContainer.autoRefresh(true);
            }
        }, delayMillis);
    }

    public void refreshComplete() {
        mLayoutContainer.refreshComplete();
        // 设置可以上拉加载更多
        mLayoutFooter.setHasMore(true);
    }

    public void loadMoreComplete(boolean emptyResult, boolean hasMore) {
        mLayoutFooter.loadMoreFinish(emptyResult, hasMore);
    }

    /**
     * 是否正在加载
     */
    public boolean isLoading() {
        return mLayoutContainer.isRefreshing();
    }

    private void init(View root) {
        mLayoutContainer = (PtrFrameLayout) root
                .findViewById(R.id.layout_container);
        mLayoutHeader = (MaterialHeader) root.findViewById(R.id.layout_header);
        mLayoutFooter = (LoadMoreListViewContainer) root
                .findViewById(R.id.layout_footer);
        mListView = (ListView) root.findViewById(R.id.listview_content);

        mLayoutContainer.addPtrUIHandler(mLayoutHeader);
        mLayoutContainer.setLoadingMinTime(1500);
        mLayoutContainer.setPinContent(true);

        mLayoutContainer.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mListener != null)
                    mListener.onRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                boolean b = PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        mListView, header);
                return b;
            }
        });
        mLayoutFooter.useDefaultFooter();
        mLayoutFooter.setLoadMoreHandler(this);
    }

    /**
     * 请求数据
     *
     * @author GISirFive
     */
    public interface OnRequestDataListener {
        /**
         * 下拉刷新
         */
        void onRefresh();

        /**
         * 上拉加载更多
         */
        void onLoadMore();
    }
}
