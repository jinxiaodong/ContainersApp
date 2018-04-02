package com.project.container.containersapp.business.hangingbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.DXBoxDetailsBean;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.IListView;
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.presenter.hangingbox.DXBoxDetailPresenter;
import com.project.container.containersapp.frame.utils.LogUtil;
import com.project.container.containersapp.frame.utils.SelfDialogUtils;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.view.SelfDialog;
import com.project.container.containersapp.frame.view.recycleview.LoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/*
* 吊箱流程页：
* */
public class DXBoxDetailActivity extends JZXBaseActivity implements IListView<DXBoxDetailsBean> {

    public static final String DX_ZYDM = "dx_zydm";
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.loadMoreRecycleView)
    LoadMoreRecyclerView mLoadMoreRecycleView;
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;

    private DXBoxDetailPresenter mDXBoxDetailPresenter;
    private DxBoxDetailAdapter mAdapter;
    private String mZydm;
    /*点击完成，更新信息*/
    private UpdateInfoPresenter mInfoPresenter;
    private UpdateInfoPresenter mUpdateZygcdm;

    /*保存点击的位置*/
    private int clickPosition;
    private SelfDialog selfDialog;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_hanging_box_deatil;
    }


    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mZydm = getIntent().getStringExtra(DX_ZYDM);

    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("吊箱流程");

        /*沉浸式*/
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        mLoadMoreRecycleView.setHasLoadMore(false);
        mLoadMoreRecycleView.setNoLoadMoreHideView(true);
        mLoadMoreRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DxBoxDetailAdapter(mContext, null);
        mLoadMoreRecycleView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mDXBoxDetailPresenter = new DXBoxDetailPresenter(mContext, this);

        mInfoPresenter = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                dismissDialog();
                if (data.isUpdated) {
                    if (clickPosition == mAdapter.getData().size() - 1) {
                        //如果是点击的最后一条成功，更新一下作业状态
                        String dxdzygclxdm = mAdapter.getData().get(clickPosition).dxdzygclxdm;
                        //"03":吊空箱、"07"：吊重箱
                        if ("03".equals(dxdzygclxdm) || "07".equals(dxdzygclxdm)) {
                            mUpdateZygcdm.updateDQZYGCDM(mZydm, dxdzygclxdm);
                        }
                        /*测试用 ，发布删除*/
                        mUpdateZygcdm.updateDQZYGCDM(mZydm, "03");
                    }
                    mAdapter.getData().get(clickPosition).dxdwcsj = System.currentTimeMillis() + "";
                    mAdapter.notifyDataSetChanged();
//                    mDXBoxDetailPresenter.getDetailList(mZydm);
                } else {
                    ToastUtil.makeToast(mContext, "更新数据失败");
                }
            }

            @Override
            public void onError(String code, String msg) {
                dismissDialog();
                LogUtil.e("吊箱更新数据失败", "dx");
                ToastUtil.makeToast(mContext, msg);
            }
        });

        mUpdateZygcdm = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                dismissDialog();
                EventBus.getDefault().post(EventsKey.REFRESH_HANGINGBOX);
                //更新状态成功，吊箱通过提示是否返回吊箱列表页
                selfDialog = SelfDialogUtils.showSelfDialog(selfDialog, mContext,
                        getResString(R.string.dx_success), new SelfDialog.onClickListener() {
                            @Override
                            public void onYesClick() {
                                selfDialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onNoClick() {
                                selfDialog.dismiss();
                            }
                        });
            }

            @Override
            public void onError(String code, String msg) {
                dismissDialog();
                //让最后一步可以点击
                mAdapter.isLastClick = false;
                mAdapter.notifyDataSetChanged();
                ToastUtil.makeToast(mContext, "更新状态失败，请重新提交！");
            }
        });

        //complete点击事件
        mAdapter.setOnCompleteClick(new DxBoxDetailAdapter.onCompleteClick() {
            @Override
            public void completeClick(int position) {
                showDialog();
                clickPosition = position;
                DXBoxDetailsBean mDxBoxDetailsBean = mAdapter.getData().get(position);
                mInfoPresenter.updateDxComplete(mDxBoxDetailsBean.dxdtxzh, mDxBoxDetailsBean.dxdlsh);
            }
        });
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);

        showDialog();
        mDXBoxDetailPresenter.getDetailList(mZydm);

    }


    /***********操作流程列表数据返回结果******/
    @Override
    public void onSuccess(List<DXBoxDetailsBean> data) {
        dismissDialog();
//                data.get(0).isFinsh = true;
        mAdapter.getData().clear();
        mAdapter.getData().addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmpty() {
        dismissDialog();
        //显示空布局
        showNoDataNoti(mRlContent, R.layout.default_page_no_content);
    }

    @Override
    public void onError(String code, String msg) {
        dismissDialog();
        //显示出错布局
        showNoDataNoti(mRlContent, R.layout.default_page_failed);
    }

}
