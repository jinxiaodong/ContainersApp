package com.project.container.containersapp.business.hangingbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.DXBoxListBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.hangingbox.DXBoxListPresenter;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.view.pulltorefresh.PullToRefreshFrameLayout;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;
import com.project.container.containersapp.frame.view.recycleview.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class HangingBoxActivity extends JZXBaseActivity implements IBaseListView<DXBoxListBean> {

    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.pullToRefresh)
    PullToRefreshFrameLayout mPullToRefresh;
    @BindView(R.id.rl_hanging_box)
    RelativeLayout mRlHangingBox;

    private DXBoxListPresenter mDXBoxListPresenter;
    private HangingBoxListAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_hanging_box;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        EventBus.getDefault().register(this);
        mDXBoxListPresenter = new DXBoxListPresenter(this, this);
    }


    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("吊箱");
        //设置沉浸式
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        mLoadMoreRecycleView.setHasLoadMore(false);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mLoadMoreRecycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                getListDataMore();
            }
        });

        /*下拉刷新监控*/
        mPullToRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mLoadMoreRecycleView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                showDialog();
                getListData();
            }
        });

    }


    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);

        mAdapter = new HangingBoxListAdapter(mContext, null);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadMoreRecycleView.setAdapter(mAdapter);

        showDialog();
        getListData();
    }

    /*出错后点击屏幕重新请求数据*/
    @Override
    public void reRequestData() {
        super.reRequestData();
        showDialog();
        getListData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventsKey event) {
        if (event == EventsKey.REFRESH_HANGINGBOX) {
            ToastUtil.makeToast(mContext, "刷新吊箱");
            getListData();
        }
    }

    public void getListData() {
        if (mDXBoxListPresenter != null) {
            mDXBoxListPresenter.getListData();
        }
    }

    public void getListDataMore() {
        if (mDXBoxListPresenter != null) {
            mDXBoxListPresenter.getListDataMore();
        }
    }


    /**********获取数据后的处理************/
    @Override
    public void onSuccess(List<DXBoxListBean> list) {
        /*UI部分*/
        dismissDialog();
        hideNoDataNoti();
        mPullToRefresh.refreshComplete();
        mLlTitle.setVisibility(View.VISIBLE);

        /*数据部分*/
        mAdapter.getData().clear();
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(List<DXBoxListBean> list) {
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmpty() {
        dismissDialog();
        //显示空布局
        showNoDataNoti(mRlHangingBox, R.layout.default_page_no_content);
    }

    @Override
    public void onError(String code, String msg) {
        dismissDialog();
        //显示出错布局
        showNoDataNoti(mRlHangingBox, R.layout.default_page_failed);
    }

    @Override
    public void onLoadMoreError(String code, String msg) {
        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.showFailUI();
    }

    @Override
    public void onHasNext(boolean hasNext) {
        mLoadMoreRecycleView.setHasLoadMore(hasNext);
        if (!hasNext) {
            mLoadMoreRecycleView.setHasLoadMore(true);
            mLoadMoreRecycleView.showNoMoreUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
