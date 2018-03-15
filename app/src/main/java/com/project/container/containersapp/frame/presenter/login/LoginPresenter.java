package com.project.container.containersapp.frame.presenter.login;

import android.content.Context;

import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.UserInfoBean;
import com.project.container.containersapp.frame.presenter.IBaseView;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：
 */

public class LoginPresenter extends Presenter<IBaseView<UserInfoBean>> {

    HttpJsonRequest mRequest;

    public LoginPresenter(Context context, IBaseView<UserInfoBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
    }


    public void Login(String username, String password) {

        put("username", username);
        put("password", password);

        mRequest.url(HttpContstants.LOGIN)
                .post()
                .params(mParams)
                .clazz(UserInfoBean.class)
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            UserInfoBean userInfoBean = (UserInfoBean) result.data;
                            if (userInfoBean != null) {
                                mvpView.onSuccess(userInfoBean);
                            } else {
                                mvpView.onError(result.code, result.message);
                            }
                        }
                    }

                    @Override
                    public void onFailure(ResponseData result) {
                        mvpView.onError(result.code, result.message);
                    }
                }).commitAsync();


    }
}
