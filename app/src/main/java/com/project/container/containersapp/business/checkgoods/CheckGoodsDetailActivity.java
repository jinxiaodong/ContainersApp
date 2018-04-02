package com.project.container.containersapp.business.checkgoods;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.business.EventsKey;
import com.project.container.containersapp.frame.base.JZXBaseActivity;
import com.project.container.containersapp.frame.model.CheckGoodsListBean;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.UpdateInfoPresenter;
import com.project.container.containersapp.frame.utils.SelfDialogUtils;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.utils.WeightInputFilter;
import com.project.container.containersapp.frame.view.SelfDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class CheckGoodsDetailActivity extends JZXBaseActivity implements IBaseView<UpdateInfoBean> {

    public static final String GOODS_DATA = "goods_data";

    @BindView(R.id.tv_jzxdm)
    TextView mTvJzxdm;
    @BindView(R.id.et_all_kg)
    EditText mEtAllKg;
    @BindView(R.id.et_cyr_kg)
    EditText mEtCyrKg;
    @BindView(R.id.et_tyr_kg)
    TextView mEtTyrKg;
    @BindView(R.id.et_sfh)
    EditText mEtSfh;
    @BindView(R.id.btn_commit)
    TextView mBtnCommit;
    private CheckGoodsListBean mGoodsData;

    private SelfDialog selfDialog;

    private UpdateInfoPresenter mCommitPresenter;
//    private UpdateInfoPresenter mUpdateZygcdm;

    /*解决两个TextView互相监听造成的死循环*/
    private boolean flag = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_check_goods_detail;
    }

    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mGoodsData = (CheckGoodsListBean) getIntent().getSerializableExtra(GOODS_DATA);
        if (mGoodsData == null) {
            showDataErrorDialog();
        }

        mCommitPresenter = new UpdateInfoPresenter(mContext, this);
//        mUpdateZygcdm = new UpdateInfoPresenter(mContext, new IBaseView<UpdateInfoBean>() {
//            @Override
//            public void onSuccess(UpdateInfoBean data) {
//                //更新作业状态成功
//            }
//
//            @Override
//            public void onError(String code, String msg) {
//
//            }
//        });
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("货物详情");
        mEtAllKg.setKeyListener(new DigitsKeyListener(false, true));
        mEtAllKg.setFilters(new InputFilter[]{new WeightInputFilter()});

        mEtCyrKg.setKeyListener(new DigitsKeyListener(false, true));
        mEtCyrKg.setFilters(new InputFilter[]{new WeightInputFilter()});

        mEtTyrKg.setKeyListener(new DigitsKeyListener(false, true));
        mEtTyrKg.setFilters(new InputFilter[]{new WeightInputFilter()});

        mTvJzxdm.setText(mGoodsData.zyjzxdm);
        //施封号
        mEtSfh.setText(mGoodsData.zysfh);
        //承运人重量
        mEtCyrKg.setText(mGoodsData.zycyrzl);
        //托运人重量
        mEtTyrKg.setText(mGoodsData.zytyrzl);
    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tyrkg = mEtTyrKg.getText().toString().trim();
                String allkg = mEtAllKg.getText().toString().trim();
                String cyrkg = mEtCyrKg.getText().toString().trim();
                String sfh = mEtSfh.getText().toString().trim();


                if (TextUtils.isEmpty(allkg)) {
                    ToastUtil.makeToast(mContext, "请输入箱货总重");
                    return;
                }
                if (TextUtils.isEmpty(cyrkg)) {
                    ToastUtil.makeToast(mContext, "箱货总重应该大于集装箱自重！");
                    return;
                }

//                if (TextUtils.isEmpty(tyrkg)) {
//                    ToastUtil.makeToast(mContext, "请输入托运人重量");
//                    return;
//                }
                if (TextUtils.isEmpty(sfh)) {
                    ToastUtil.makeToast(mContext, "请输入施封号");
                    return;
                }
                mCommitPresenter.updateJianjinCommit(mGoodsData.zydm, tyrkg, cyrkg,
                        mGoodsData.jzxzg, mGoodsData.zysfh);
            }
        });


        /*EditText 互斥监听*/
        mEtAllKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String allkg;
                if (!TextUtils.isEmpty(mEtAllKg.getText().toString())) {
                    allkg = mEtAllKg.getText().toString().trim();
                } else {
                    allkg = "0";
                }
                if (flag) {
                    flag = false;
                    double cyrkg = Double.parseDouble(allkg) - Double.parseDouble(mGoodsData.jzxzg);
                    mEtCyrKg.setText(cyrkg + "");
                } else {
                    flag = true;
                }
            }
        });
        mEtCyrKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cyrkg;
                if (!TextUtils.isEmpty(mEtCyrKg.getText().toString())) {
                    cyrkg = mEtCyrKg.getText().toString().trim();
                } else {
                    cyrkg = "0";
                }
                if (flag) {
                    flag = false;
                    double allkg = Double.parseDouble(cyrkg) + Double.parseDouble(mGoodsData.jzxzg);
                    mEtAllKg.setText(allkg + "");
                } else {
                    flag = true;
                }
            }
        });
    }

    /*数据有误确认框*/
    private void showDataErrorDialog() {

        selfDialog = SelfDialogUtils.showSelfDialog(selfDialog, mContext, getResString(R.string.data_error), new SelfDialog.onClickListener() {
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


    /********检斤验货提交成功**************/
    @Override
    public void onSuccess(UpdateInfoBean data) {

        EventBus.getDefault().post(EventsKey.REFRESH_CHECKGOODS);
        selfDialog = SelfDialogUtils.showSelfDialog(selfDialog, mContext, getResString(R.string.check_goods_success), new SelfDialog.onClickListener() {
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
        //        ToastUtil.makeToast(mContext,"提交成功");
    }

    @Override
    public void onError(String code, String msg) {
        ToastUtil.makeToast(mContext, "提交失败");
    }
}
