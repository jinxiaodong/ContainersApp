package com.project.container.containersapp.frame.view.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.project.container.containersapp.frame.constants.DeviceInfo;

import java.lang.reflect.Constructor;


public class LoadMoreRecyclerView extends RecyclerView implements OnScrollBottomListener {


    /**
     * 加载更多UI
     */
    ILoadMoreView mLoadMoreView;

    /**
     * 加载更多方式，默认滑动到底部加载更多
     */
    LoadMoreMode mLoadMoreMode = LoadMoreMode.SCROLL;
    /**
     * 加载更多lock
     */
    private boolean mLoadMoreLock;
    /**
     * 是否可以加载跟多
     */
    boolean mHasLoadMore;
    /**
     * 是否加载失败
     */
    private boolean mHasLoadFail;

    /**
     * 加载更多事件回调
     */
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * emptyview
     */
    private View mEmptyView;

    /**
     * 设置第一次就没有更多数据了，是否要显示底部
     */
    private boolean mFristNoLoadMoreHideView;


    /**
     * 没有更多了时,是否隐藏加载更多的view
     */
    private boolean mNoLoadMoreThenHideView;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private boolean mAddLoadMoreFooterFlag;

    private ScrollDistanceListener scrollDistanceListener;

    //标题栏渐变颜色
    private int basecolor;
    /**滚动状态变化监听*/
    private OnScrollStateChangedListener mOnScrollStateChangedListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter();
        super.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreView);

        if (a.hasValue(R.styleable.LoadMoreView_loadMoreMode)) {
            mLoadMoreMode = LoadMoreMode.mapIntToValue(a.getInt(R.styleable.LoadMoreView_loadMoreMode, 0x01));
        } else {
            mLoadMoreMode = LoadMoreMode.SCROLL;
        }

        if (a.hasValue(R.styleable.LoadMoreView_noLoadMoreHideView)) {
            mNoLoadMoreThenHideView = a.getBoolean(R.styleable.LoadMoreView_noLoadMoreHideView, false);
        } else {
            mNoLoadMoreThenHideView = true;
        }

        if (a.hasValue(R.styleable.LoadMoreView_loadMoreView)) {
            try {
                String loadMoreViewName = a.getString(R.styleable.LoadMoreView_loadMoreView);
                Class clazz = Class.forName(loadMoreViewName);
                Constructor c = clazz.getConstructor(Context.class);
                ILoadMoreView loadMoreView = (ILoadMoreView) c.newInstance(context);
                mLoadMoreView = loadMoreView;
            } catch (Exception e) {
                e.printStackTrace();
                mLoadMoreView = new DefaultLoadMoreView(context);
            }
        } else {
            mLoadMoreView = new DefaultLoadMoreView(context);
        }

        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());

        setHasLoadMore(false);
        a.recycle();
        addOnScrollListener(new RecyclerViewOnScrollListener());
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
//        super.setAdapter(adapter);
        try {
            adapter.unregisterAdapterDataObserver(mDataObserver);

        } catch (Exception e) {
        } finally {
            adapter.registerAdapterDataObserver(mDataObserver);
            mHeaderAndFooterRecyclerViewAdapter.setAdapter(adapter);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        mHeaderAndFooterRecyclerViewAdapter.putLayoutManager(layout);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (null == scrollDistanceListener) {
            return;
        }

/*        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        //fix bug item高度不等高时computeVerticalScrollOffset超过一屏得到数值变小
        //目前使用与头图或第一个item进行滑动的距离显示title透明度
        if (0 == layoutManager.findFirstVisibleItemPosition()) {
            getTitleScrolledHeight();
            int iScrollDistance = computeVerticalScrollOffset();
            iScrollDistance = Math.min(iScrollDistance, MaxScrollDistance);
            float progress = (float) iScrollDistance / MaxScrollDistance;
            int a = 0xFF;
            a = (int) (a * progress);
            int color = a << 24 | 0x000000;
            scrollDistanceListener.onScrollDistanceSetColor(color);
        }*/


        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
            //fix bug item高度不等高时computeVerticalScrollOffset超过一屏得到数值变小
            //目前使用与头图或第一个item进行滑动的距离显示title透明度
            if (0 == layoutManager.findFirstVisibleItemPosition()) {
                getTitleScrolledHeight();
                int iScrollDistance = computeVerticalScrollOffset();
                iScrollDistance = Math.min(iScrollDistance, MaxScrollDistance);
                float progress = (float) iScrollDistance / MaxScrollDistance;
                float alpha = 0;
                if (MaxScrollDistance >= DeviceInfo.HEIGHTPIXELS / 2) {
                    if (progress > 0.05f) {
                        alpha = Math.min(1, progress + 0.1f);
                    } else {
                        alpha = Math.min(1, progress);
                    }
                } else {
                    if (progress > 0.08f) {
                        alpha = Math.min(1, progress + 0.1f);
                    } else {
                        alpha = Math.min(1, progress);
                    }
                }

                int color = getColorWithAlpha(alpha, basecolor);
                scrollDistanceListener.onScrollDistanceSetColor(color, alpha);
            } else {
                scrollDistanceListener.onScrollDistanceSetColor(basecolor, 1.0f);
            }
        }


    }

    @Override
    public void onScorllBootom() {
        if (mHasLoadMore && mLoadMoreMode == LoadMoreMode.SCROLL) {
            executeLoadMore();
        }
    }

    /**
     * 设置滚动状态监听
     * */
    public void setOnScrollStateChangedListener(OnScrollStateChangedListener listener){
        mOnScrollStateChangedListener = listener;
    }

    public void setScrolledDistanceListener(int baseColor, ScrollDistanceListener listener) {
        this.scrollDistanceListener = listener;
        this.basecolor = baseColor;
    }

    public void setScrolledDistanceListener(ScrollDistanceListener listener) {
        this.scrollDistanceListener = listener;
        this.basecolor = getResources().getColor(R.color.header_bg_color);
    }

    /**
     * 设置OnItemClickListener
     *
     * @param listener
     */
    public void setOnItemClickListener(HeaderAndFooterRecyclerViewAdapter.OnItemClickListener listener) {
        mHeaderAndFooterRecyclerViewAdapter.setOnItemClickListener(listener);
    }

    /**
     * 设置OnItemLongClickListener
     *
     * @param listener
     */
    public void setOnItemLongClickListener(HeaderAndFooterRecyclerViewAdapter.OnItemLongClickListener listener) {
        mHeaderAndFooterRecyclerViewAdapter.setOnItemLongClickListener(listener);
    }

    /**
     * 设置加载更多事件回调
     *
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    /**
     * 获取底部加载view
     *
     * @return
     */
    public ILoadMoreView getmLoadMoreView() {
        return mLoadMoreView;
    }

    /**
     * 设置recyclerview emptyview
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 设置LoadMoreView
     *
     * @param loadMoreView
     */
    public void setLoadMoreView(ILoadMoreView loadMoreView) {
        if (mLoadMoreView != null) {
            try {
                removeFooterView(mLoadMoreView.getFooterView());
                mAddLoadMoreFooterFlag = false;
            } catch (Exception e) {
            }
        }
        mLoadMoreView = loadMoreView;
        mLoadMoreView.getFooterView().setOnClickListener(new OnMoreViewClickListener());
    }

    /**
     * 设置加载更多模式,点击加载/滚动到底加载
     *
     * @param mode
     */
    public void setLoadMoreMode(LoadMoreMode mode) {
        mLoadMoreMode = mode;
    }

    /**
     *
     */
    public void setNoLoadMoreHideViewFrist(boolean isShow){
        this.mFristNoLoadMoreHideView = isShow;

    }

    /**
     * 设置没有更多数据了，是否隐藏fooler view
     *
     * @param hide
     */
    public void setNoLoadMoreHideView(boolean hide) {
        this.mNoLoadMoreThenHideView = hide;
    }

    /**
     * 没有很多了
     */
    public void showNoMoreUI() {
        mHasLoadMore = false;
        mLoadMoreLock = false;
        mLoadMoreView.showNoMore();
    }

    /**
     * 显示失败UI
     */
    public void showFailUI() {
        mHasLoadFail = true;
        mLoadMoreLock = false;
        mLoadMoreView.showFail();
    }

    /**
     * 显示默认UI
     */
    private void showNormalUI() {
        mLoadMoreLock = false;
        mLoadMoreView.showNormal();
    }

    /**
     * 显示加载中UI
     */
    private void showLoadingUI() {
        mHasLoadFail = false;
        mLoadMoreView.showLoading();
    }

    /**
     * 是否有更多
     *
     * @param hasLoadMore
     */
    public void setHasLoadMore(boolean hasLoadMore) {
        mHasLoadMore = hasLoadMore;
        if (!mHasLoadMore) {
            showNoMoreUI();
            if (mNoLoadMoreThenHideView) {
                removeFooterView(mLoadMoreView.getFooterView());
                mAddLoadMoreFooterFlag = false;
            }else{
                if (!mAddLoadMoreFooterFlag && mFristNoLoadMoreHideView) {
                    mAddLoadMoreFooterFlag = true;
                    addFooterView(mLoadMoreView.getFooterView());
                }
            }
        } else {
            if (!mAddLoadMoreFooterFlag) {
                mAddLoadMoreFooterFlag = true;
                addFooterView(mLoadMoreView.getFooterView());
            }
            showNormalUI();
        }
    }

    /**
     * 完成加载更多
     */
    public void onLoadMoreComplete() {
        if (mHasLoadFail) {
            showFailUI();
        } else if (mHasLoadMore) {
            showNormalUI();
        }
    }

    /**
     * 添加footer view
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        mHeaderAndFooterRecyclerViewAdapter.addFooterView(footerView);
    }

    /**
     * 添加header view
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaderAndFooterRecyclerViewAdapter.addHeaderView(headerView);
    }

    public void removeFooterView(View footerView) {
        mHeaderAndFooterRecyclerViewAdapter.removeFooter(footerView);
    }

    public void removeHeaderView(View headerView) {
        mHeaderAndFooterRecyclerViewAdapter.removeHeader(headerView);
    }

    public void setTitleScrolledHeight(int height) {
        MaxScrollDistance = height;
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    private int MaxScrollDistance;

    private int getTitleScrolledHeight() {
        if (MaxScrollDistance == 0) {
//            MaxScrollDistance = (int) (getResources().getDisplayMetrics().widthPixels * 1.21);
            MaxScrollDistance = this.getChildAt(0).getHeight();
        }
        return MaxScrollDistance;
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/
    /**
     * 加载更多
     */
    private void executeLoadMore() {
        if (!mLoadMoreLock && mHasLoadMore) {
            mLoadMoreLock = true;//上锁
            showLoadingUI();
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
        }
    }

    /**
     * recyclerview滚动状态变化
     * */
    private void onViewScrollStateChanged(RecyclerView recyclerView, int newState){
        if(mOnScrollStateChangedListener != null){
            mOnScrollStateChangedListener.onScrollStateChanged(recyclerView,newState);
        }
    }
    /*******************************************************************************
     * Internal Class,Interface
     *******************************************************************************/

    /**
     * 滚动距离监听
     * */
    public interface ScrollDistanceListener {
        void onScrollDistanceSetColor(int color, float alpha);
    }

    /**
     * recyclerview 滚动状态监听
     * */
    public interface OnScrollStateChangedListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    /**
     * 点击more view加载更多
     */
    private class OnMoreViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mHasLoadMore) {
                executeLoadMore();
            }
        }
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;

        /**
         * 当前滑动的状态
         */
        private int currentScrollState = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                onScorllBootom();
            }
            onViewScrollStateChanged(recyclerView, newState);
        }

        /**
         * 取数组中最大值
         *
         * @param lastPositions
         * @return
         */
        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }

            return max;
        }
    }

    /**
     * 刷新数据时停止滑动,避免出现数组下标越界问题
     */
    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }

            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
    };
}
