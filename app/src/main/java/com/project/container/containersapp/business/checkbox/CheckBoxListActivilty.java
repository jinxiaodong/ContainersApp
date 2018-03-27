package com.project.container.containersapp.business.checkbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.CheckBoxListBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.checkbox.CheckBoxListPresenter;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
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

/*
* 验箱列表页面
* */
public class CheckBoxListActivilty extends JZXBaseActivity implements IBaseListView<CheckBoxListBean> {

    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.pullToRefresh)
    PullToRefreshFrameLayout mPullToRefresh;
    @BindView(R.id.rl_checkbox)
    RelativeLayout mRlCheckbox;

    /*recycleView 数据适配器*/
    private CheckBoxListAdapter mAdapter;
    /*数据提供者*/
    private CheckBoxListPresenter mCheckBoxListPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_box_list_activilty;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        EventBus.getDefault().register(this);
        mCheckBoxListPresenter = new CheckBoxListPresenter(this, this);
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("验箱");

        //设置沉浸式
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        mLoadMoreRecycleView.setHasLoadMore(false);
        //第一次没有数据是否隐藏底部“没有更多了”view
//        mLoadMoreRecycleView.setNoLoadMoreHideView(true);
//        mLoadMoreRecycleView.setNoLoadMoreHideViewFrist(false);


    }


    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mLoadMoreRecycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (mCheckBoxListPresenter != null) {

                    mCheckBoxListPresenter.getListDataMore();
                }
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
                if (mCheckBoxListPresenter != null) {
                    mCheckBoxListPresenter.getListData();
                }
            }
        });
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);

        mAdapter = new CheckBoxListAdapter(mContext, null);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadMoreRecycleView.setAdapter(mAdapter);

        showDialog();
        mCheckBoxListPresenter.getListData();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventsKey event) {
        if (event == EventsKey.REFRESH_CHECKBOX) {
//            ToastUtil.makeToast(mContext, "lalal");
            mCheckBoxListPresenter.getListData();
        }
    }

    /*出错后点击屏幕重新请求数据*/
    @Override
    public void reRequestData() {
        super.reRequestData();
        showDialog();
        mCheckBoxListPresenter.getListData();
    }

    /*
            * 首次加载成功
            * */
    @Override
    public void onSuccess(List<CheckBoxListBean> list) {
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

    /*
    * 加载更多成功
    * */
    @Override
    public void onLoadMore(List<CheckBoxListBean> list) {
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /*
    * 返回数据为空
    * */
    @Override
    public void onEmpty() {
        dismissDialog();
        //显示空布局
        showNoDataNoti(mRlCheckbox, R.layout.default_page_no_content);
    }

    /*
    * 首次加载失败
    * */
    @Override
    public void onError(String code, String msg) {
        dismissDialog();
        //显示出错布局
        showNoDataNoti(mRlCheckbox, R.layout.default_page_failed);
    }

    /*
    * 加载更多失败
    * */
    @Override
    public void onLoadMoreError(String code, String msg) {
        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.showFailUI();
    }

    /*是否有下一页*/
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
