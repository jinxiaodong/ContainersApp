package com.project.container.containersapp.business.boxing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.BoxingListBean;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.presenter.boxing.BoxingListPresenter;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.view.pulltorefresh.PullToRefreshFrameLayout;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;
import com.project.container.containersapp.frame.view.recycleview.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/*装箱列表页*/
public class BoxingListActivity extends JZXBaseActivity implements IBaseListView<BoxingListBean> {


    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.pullToRefresh)
    PullToRefreshFrameLayout mPullToRefresh;
    @BindView(R.id.rl_checkbox)
    RelativeLayout mRlCheckbox;
    private BoxingListAdapter mAdapter;

    private BoxingListPresenter mBoxingListPresenter;
    //更新信息
    private UpdateInfoPresenter mUpdateInfoPresenter;
    //更新作业过程代码
    private UpdateInfoPresenter mUpdateZygcdm;
    private String mZydm;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_boxing_list;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mBoxingListPresenter = new BoxingListPresenter(mContext, this);

        /*完成接口回调*/
        mUpdateInfoPresenter = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                //成功后直接调用更新作业代码接口
                if (data.isUpdated) {
                    mUpdateZygcdm.updateDQZYGCDM(mZydm, "05");
                }else {
                    ToastUtil.makeToast(mContext,"数据更新失败");
                }
            }

            @Override
            public void onError(String code, String msg) {
                dismissDialog();
                ToastUtil.makeToast(mContext, msg);
                //测试用
//                mUpdateZygcdm.updateDQZYGCDM(mZydm, "00");
            }
        });
        /*更新作业过程代码回调*/
        mUpdateZygcdm = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                /*刷新数据*/
                getListData();
            }

            @Override
            public void onError(String code, String msg) {
                dismissDialog();
                ToastUtil.makeToast(mContext, "更新作业代码失败");
            }
        });
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        //设置沉浸式
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);
        setTitle("装箱");

        mLoadMoreRecycleView.setNoLoadMoreHideViewFrist(true);
        mAdapter = new BoxingListAdapter(mContext, null);
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

        mAdapter.setOnFinishItemClickListener(new BoxingListAdapter.onFinishItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BoxingListBean boxingListBean = mAdapter.getData().get(position);
                mZydm = boxingListBean.zydm;
                showDialog();
                mUpdateInfoPresenter.updateBoxingFinish(mZydm);
            }
        });
    }


    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);


        showDialog();
        getListData();
    }


    private void getListData() {
        mBoxingListPresenter.getListData();
    }

    private void getDataMore() {
        mBoxingListPresenter.getListDataMore();
    }

    /**********列表数据返回结果*************/
    @Override
    public void onSuccess(List<BoxingListBean> list) {
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
    public void onLoadMore(List<BoxingListBean> list) {
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
