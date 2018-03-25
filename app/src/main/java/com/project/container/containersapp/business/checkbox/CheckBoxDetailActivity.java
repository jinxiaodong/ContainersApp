package com.project.container.containersapp.business.checkbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.presenter.checkbox.BoxPoSunTypePresenter;
import com.project.container.containersapp.frame.presenter.checkbox.IGetPoSunTypeView;
import com.project.container.containersapp.frame.utils.LogUtil;
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
    /*是否换箱*/
    private boolean isExchange;

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

    }

    private void initPresenter() {
        mBoxPoSunTypePresenter = new BoxPoSunTypePresenter(this, this);
        mUpdateXiangSun = new UpdateInfoPresenter(this, this);
        //验箱通过回调
        CheckBoxPass = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                //更新作业状态
                ToastUtil.makeToast(mContext,"验箱成功");
            }

            @Override
            public void onError(String code, String msg) {
                //弹出Toast，提示用户验箱失败原因
                ToastUtil.makeToast(mContext,"验箱失败");
            }
        });

        //换箱
        mUpdateExchange = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
            @Override
            public void onSuccess(UpdateInfoBean data) {
                //换箱成功后直接调用验箱通过按钮，并且将
                LogUtil.e("check_box==","==>换箱成功");
                CheckBoxPass.CheckBoxPass(mZydm);
            }

            @Override
            public void onError(String code, String msg) {
                //提示提交失败原因
                LogUtil.e("check_box==","==>换箱失败");
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
                /*
                * 第一步：箱号是否为空
                * 第二步：不为空，箱损情况是否为空，不为空，更新箱损，为空直接调用换箱
                * 第三步：换箱
                * 第四步：换箱成功、调用验箱通过。
                * */
                if (TextUtils.isEmpty(mTvJzxdmNew.getText().toString().trim())) {
                    ToastUtil.makeToast(mContext, "请输入新箱号");
                } else {
                    isExchange = true;
                    String string = getXiangSunString();
                    mUpdateXiangSun.CheckBoxUpdateXiangSun(mTvJzxdm.getText().toString().trim(), string);
                }


            }
        });

        /*验箱通过点击事件*/
        mBtnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xiangSunString = getXiangSunString();
                if (TextUtils.isEmpty(xiangSunString)) {
                    /*无箱损，直接验箱通过*/
                    LogUtil.e("check_box==", "btn_pass_wusun");
                    CheckBoxPass.CheckBoxPass(mZydm);
                } else {
                    /*有箱损先更新箱损：主要是给到达作业使用。发送作业需要换箱*/
                    isExchange = false;
                    mUpdateXiangSun.CheckBoxUpdateXiangSun(mTvJzxdm.getText().toString().trim(), xiangSunString);
                }
            }
        });
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


    /***************更新箱损信息返回结果****************/
    @Override
    public void onSuccess(UpdateInfoBean data) {
        if (data.isupdate) {
            //更新成功: 有两种情况：换箱、直接通过。
            if (isExchange) {
                LogUtil.e("check_box==", "更新箱损成功===>换箱");
                String jzxdmNew = mTvJzxdmNew.getText().toString().trim();
                String jzxdmOld = mTvJzxdm.getText().toString().trim();
                mUpdateExchange.ExchangeBox(mZydm, jzxdmNew, jzxdmOld);
            }else {
                LogUtil.e("check_box==", "更新箱损成功===>验箱通过");
                CheckBoxPass.CheckBoxPass(mZydm);
            }

        } else {
            ToastUtil.makeToast(mContext, "提交失败！！");
        }
    }

    @Override
    public void onError(String code, String msg) {
        ToastUtil.makeToast(mContext, "提交失败！！");
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
}
