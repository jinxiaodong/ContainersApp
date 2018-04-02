package com.project.container.containersapp.business.checkgoods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.CheckGoodsListBean;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.presenter.checkgoods.CheckGoodsListPresenter;
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

public class CheckGoodsListActivity extends JZXBaseActivity implements IBaseListView<CheckGoodsListBean> {

    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.pullToRefresh)
    PullToRefreshFrameLayout mPullToRefresh;
    @BindView(R.id.rl_checkbox)
    RelativeLayout mRlCheckbox;

    private CheckGoodsListAdapter mAdapter;

    private CheckGoodsListPresenter mCheckGoodsListPresenter;

    private UpdateInfoPresenter mUpdateZygcdm;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_goods_list;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        EventBus.getDefault().register(this);
        mCheckGoodsListPresenter = new CheckGoodsListPresenter(mContext, this);
        mUpdateZygcdm = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                getListData();
            }

            @Override
            public void onError(String code, String msg) {
                dismissDialog();
                ToastUtil.makeToast(mContext, msg);
            }
        });
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);
        setTitle("检斤验货");

        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.setNoLoadMoreHideView(false);
        mLoadMoreRecycleView.showNoMoreUI();

        mAdapter = new CheckGoodsListAdapter(mContext, null);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadMoreRecycleView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mLoadMoreRecycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                getDataMore();
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

        mAdapter.setOnFinishItemClickListener(new CheckGoodsListAdapter.onFinishItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CheckGoodsListBean checkGoodsListBean = mAdapter.getData().get(position);
                if (checkGoodsListBean != null) {
                    Intent intent = new Intent(mContext, CheckGoodsDetailActivity.class);
                    intent.putExtra(CheckGoodsDetailActivity.GOODS_DATA, checkGoodsListBean);
                    startActivity(intent);
                } else {
                    ToastUtil.makeToast(mContext, "该条数据无效，请刷新列表");
                }
                //                showDialog();
//                mUpdateZygcdm.updateDQZYGCDM(checkGoodsListBean.zydm, "00");
            }
        });
    }


    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);

        showDialog();
        getListData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventsKey event) {
        if (event == EventsKey.REFRESH_CHECKGOODS) {
//            ToastUtil.makeToast(mContext, "lalal");
            getListData();
        }
    }

    private void getListData() {
        mCheckGoodsListPresenter.getListData();
    }

    private void getDataMore() {
        mCheckGoodsListPresenter.getListDataMore();
    }


    /*************列表接口数据返回********************/
    @Override
    public void onSuccess(List<CheckGoodsListBean> list) {
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
    public void onLoadMore(List<CheckGoodsListBean> list) {
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmpty() {
        dismissDialog();
        //显示空布局
        showNoDataNoti(mRlCheckbox, R.layout.default_page_no_content);
    }

    @Override
    public void onError(String code, String msg) {
        dismissDialog();
        //显示出错布局
        showNoDataNoti(mRlCheckbox, R.layout.default_page_failed);
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
}