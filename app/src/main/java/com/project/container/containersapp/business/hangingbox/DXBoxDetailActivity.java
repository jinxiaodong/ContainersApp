package com.project.container.containersapp.business.hangingbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.DXBoxDetailsBean;
import com.project.container.containersapp.frame.presenter.IListView;
import com.project.container.containersapp.frame.presenter.hangingbox.DXBoxDetailPresenter;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;

/*
* 吊箱流程页：
* */
public class DXBoxDetailActivity extends JZXBaseActivity {

    public static final String DX_ZYDM = "dx_zydm";
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;

    private DXBoxDetailPresenter mDXBoxDetailPresenter;
    private DxBoxDetailAdapter mAdapter;
    private String mZydm;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_hanging_box_deatil;
    }


    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mZydm = getIntent().getStringExtra(DX_ZYDM);
        mDXBoxDetailPresenter = new DXBoxDetailPresenter(mContext, new IListView<DXBoxDetailsBean>() {
            @Override
            public void onSuccess(List<DXBoxDetailsBean> data) {
//                data.get(0).isFinsh = true;
                mAdapter.getData().clear();
                mAdapter.getData().addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEmpty() {
                mAdapter.showNothing();
            }

            @Override
            public void onError(String code, String msg) {

            }
        });
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("吊箱流程");

        /*沉浸式*/
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.setNoLoadMoreHideView(true);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DxBoxDetailAdapter(mContext, null);
        mLoadMoreRecycleView.setAdapter(mAdapter);
        mDXBoxDetailPresenter.getDetailList(mZydm);

    }

}
