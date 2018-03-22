package com.project.container.containersapp.business.checkbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.PoSunTypeBean;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.checkbox.BoxPoSunTypePresenter;
import com.project.container.containersapp.frame.presenter.checkbox.IGetPoSunTypeView;
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.view.NoScrollGridview;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CheckBoxDetailActivity extends JZXBaseActivity implements IGetPoSunTypeView, IBaseView<UpdateInfoBean> {

    public static final String ZYDM = "zydm";
    public static final String ZYJZXDM = "zyjzxdm";

    @BindView(R.id.tv_jzxdm)
    TextView mTvJzxdm;
    @BindView(R.id.btn_pass)
    TextView mBtnPass;
    @BindView(R.id.grid_view)
    NoScrollGridview mGridView;
    @BindView(R.id.tv_jzxdm_new)
    EditText mTvJzxdmNew;
    @BindView(R.id.btn_scan)
    TextView mBtnScan;
    @BindView(R.id.btn_commit)
    TextView mBtnCommit;

    private String mZydm;          //作业代码
    private String mZyjzxdm;       //作业集装箱代码


    /*x箱损类型获取*/
    private BoxPoSunTypePresenter mBoxPoSunTypePresenter;
    /*更新箱损信息*/
    private UpdateInfoPresenter mUpdateXiangSun;
    /*换箱*/
    private UpdateInfoPresenter mUpdateExchange;
    /*验箱通过*/
    private UpdateInfoPresenter CheckBoxPass;

    private XSGridViewAdapter mGridViewAdapter;
    private List<PoSunTypeBean> mList;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_box_detail;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        /*通知列表页返回时要刷新页面*/
        EventBus.getDefault().post(EventsKey.REFRESH_CHECKBOX);
        mZyjzxdm = getIntent().getStringExtra(ZYJZXDM);
        mZydm = getIntent().getStringExtra(ZYDM);

        initPresenter();
        mBoxPoSunTypePresenter = new BoxPoSunTypePresenter(this, this);
        mUpdateXiangSun = new UpdateInfoPresenter(this, this);
    }

    private void initPresenter() {

        //验箱通过回调
        CheckBoxPass = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                //弹出确认框，提示用户提交完成，是否返回列表
            }

            @Override
            public void onError(String code, String msg) {
                //弹出Toast，提示用户验箱失败原因
            }
        });

        //换箱
        mUpdateExchange = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                //换箱成功后直接调用验箱通过按钮，并且将
                CheckBoxPass.CheckBoxPass(mZydm);
            }

            @Override
            public void onError(String code, String msg) {
                //提示提交失败原因
            }
        });


    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("发箱验箱");
        mTvJzxdm.setText(mZyjzxdm);

    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);

        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = getXiangSunString();
               /*如果有箱损情况：更新箱损，然后换箱，最后验箱通过*/
                mUpdateXiangSun.CheckBoxUpdateXiangSun(mTvJzxdm.getText().toString().trim(), string);
            }
        });

        /*验箱通过点击事件*/
        mBtnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxPass.CheckBoxPass(mZydm);
            }
        });
    }

    /*获取箱损状态*/
    @NonNull
    private String getXiangSunString() {
        StringBuilder stringBuilder = new StringBuilder();
        int childCount = mGridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            CheckBox childAt = (CheckBox) mGridView.getChildAt(i);
            if (childAt.isChecked()) {
                stringBuilder.append(mList.get(i).name);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);
        showDialog();
        mBoxPoSunTypePresenter.getXiangSuntype();
    }

    @Override
    public void onPoSunTypeSuccess(List<PoSunTypeBean> list) {
        dismissDialog();
        mList = list;
        mGridViewAdapter = new XSGridViewAdapter(mContext, mList);
        mGridView.setAdapter(mGridViewAdapter);
    }

    @Override
    public void onPoSunError(String code, String msg) {
        dismissDialog();
        /*如果获取箱损失败，那么就按照目前的七种默认数据*/
        mList = getDefaultData();
        mGridViewAdapter = new XSGridViewAdapter(mContext, mList);
        mGridView.setAdapter(mGridViewAdapter);
    }

    private List<PoSunTypeBean> getDefaultData() {
        List<PoSunTypeBean> list = new ArrayList<>();
        String[] type = {"割伤", "擦伤", "破洞", "凹损", "破损", "污损", "部件缺失"};
        String[] name = {"A", "B", "C", "D", "E", "F", "G"};
        for (int i = 0; i < 7; i++) {
            PoSunTypeBean poSunTypeBean = new PoSunTypeBean();
            poSunTypeBean.name = name[i];
            poSunTypeBean.type = type[i];
            list.add(poSunTypeBean);
        }
        return list;
    }


    /***************更新箱损信息返回结果****************/
    @Override
    public void onSuccess(UpdateInfoBean data) {
        if (data.isupdate) {
            //更新成功
            ToastUtil.makeToast(mContext, "提交成功！！");

        } else {
            ToastUtil.makeToast(mContext, "提交失败！！");
        }
    }

    @Override
    public void onError(String code, String msg) {
        ToastUtil.makeToast(mContext, "提交失败！！");
    }
}
