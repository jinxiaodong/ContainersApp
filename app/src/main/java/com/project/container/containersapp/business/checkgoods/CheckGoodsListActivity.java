package com.project.container.containersapp.business.checkgoods;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.view.pulltorefresh.PullToRefreshFrameLayout;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;
import com.project.container.containersapp.frame.view.recycleview.OnLoadMoreListener;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CheckGoodsListActivity extends JZXBaseActivity {

    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.pullToRefresh)
    PullToRefreshFrameLayout mPullToRefresh;
    @BindView(R.id.rl_checkbox)
    RelativeLayout mRlCheckbox;

    private CheckGoodsListAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_goods_list;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);
        setTitle("检斤验货");

        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.setNoLoadMoreHideView(false);
        mLoadMoreRecycleView.showNoMoreUI();
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

    }


    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);
        mAdapter = new CheckGoodsListAdapter(mContext, null);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mLoadMoreRecycleView.setAdapter(mAdapter);

    }


    private void getListData() {

    }

    private void getDataMore() {

    }
}