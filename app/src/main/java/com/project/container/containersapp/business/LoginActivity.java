package com.project.container.containersapp.business;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.container.containersapp.R;
import com.project.container.containersapp.frame.base.CABaseActivity;
import com.project.container.containersapp.frame.model.UserInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.login.LoginPresenter;
import com.project.container.containersapp.frame.utils.EtTextWitcher;
import com.project.container.containersapp.frame.utils.SystemBarUtil;
import com.project.container.containersapp.frame.utils.ToastUtil;
import com.project.container.containersapp.frame.view.ClearEditText;
import com.project.container.containersapp.frame.view.PwdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class LoginActivity extends CABaseActivity implements View.OnClickListener, IBaseView<UserInfoBean> {


    @BindView(R.id.login_et_name)
    ClearEditText mLoginEtName;     //登录名

    @BindView(R.id.divider_id_login)
    TextView mDividerIdLogin;       //分割线

    @BindView(R.id.login_pwd)
    PwdView mLoginPwd;              //登录密码

    @BindView(R.id.divider_psw_login)
    TextView mDividerPswLogin;      //分割线

    @BindView(R.id.login_sure)
    Button mLoginSure;              //登录按钮

    /*登录请求*/
    LoginPresenter mLoginPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initValue(Bundle onSavedInstance) {
        super.initValue(onSavedInstance);
        mLoginPresenter = new LoginPresenter(this, this);
    }

    @Override
    protected void initWidget(Bundle onSavedInstance) {
        super.initWidget(onSavedInstance);
        setTitle("登录");
        //隐藏返回按钮
        showOrHideBackButton(View.INVISIBLE);
        //设置沉浸式
        SystemBarUtil.tintStatusBar(this, Color.parseColor(getResString(R.color.main_blue)), 0);

        //绑定分割线（根据输入状态调整分割线颜色）
        mLoginEtName.bindTextView(mDividerIdLogin);
        mLoginPwd.et_pwd.bindTextView(mDividerPswLogin);

        //添加编辑框监听事件
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(mLoginEtName);
        editTexts.add(mLoginPwd.et_pwd);
        EtTextWitcher etTextWitcher = new EtTextWitcher(editTexts, mLoginSure);
        mLoginEtName.addTextChangedListener(etTextWitcher);
        mLoginPwd.et_pwd.addTextChangedListener(etTextWitcher);

    }

    @Override
    protected void initListener(Bundle onSavedInstance) {
        super.initListener(onSavedInstance);
        mLoginSure.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle onSavedInstance) {
        super.initData(onSavedInstance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_sure:
                //请求登录接口
                String name = mLoginEtName.getText().toString().trim();
                String password = mLoginPwd.getText().toString().trim();
                if (name != null) {
                    name = name.replace(" ", "");
                } else {
                    ToastUtil.makeToast(mContext, "请输入用户名或邮箱账号！");
                    mLoginEtName.setSelected(true);
                    return;
                }
                if (password != null) {
                    password = password.replace(" ", "");
                } else {
                    ToastUtil.makeToast(mContext, "请输入密码！");
                    return;
                }

                showDialog();
                mLoginPresenter.Login(name, password);
                break;
        }
    }

    @Override
    public void onSuccess(UserInfoBean data) {
        dismissDialog();
        //请求成功
        ToastUtil.makeToast(mContext, data.toString());
    }

    @Override
    public void onError(String code, String msg) {
        //请求失败
        ToastUtil.makeToast(mContext, msg);
    }
}
