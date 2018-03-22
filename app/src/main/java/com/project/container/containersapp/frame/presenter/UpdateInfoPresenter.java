package com.project.container.containersapp.frame.presenter;

import android.content.Context;

import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.UpdateInfoBean;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by xiaodong.jin on 2018/3/19.
 * description：通用-更新信息请求
 */

public class UpdateInfoPresenter extends Presenter<IBaseView<UpdateInfoBean>> {

    HttpJsonRequest mRequest;
    private final String mAuth;

    public UpdateInfoPresenter(Context context, IBaseView<UpdateInfoBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
        mAuth = LoginBlock.getmAuth();
    }


    /*验箱更新集装箱箱损*/
    public void CheckBoxUpdateXiangSun(String zyjzxdm, String pszt) {
        getmParams().clear();
        String url = HttpContstants.UPDATE_XIANGSUN;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.UPDATE_XIANGSUN + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        put("zyjzxdm", zyjzxdm);
        put("pszt", pszt);

        request(url);
    }


    /*
    * 换箱
    * */
    public void ExchangeBox(String zydm, String newjzxdm, String oldjzxdm) {
        getmParams().clear();
        String url = HttpContstants.CHECKBOX_EXCHANGE;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.CHECKBOX_EXCHANGE + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        put("zydm", zydm);
        put("newjzxdm", newjzxdm);
        put("oldjzxdm", oldjzxdm);

        request(url);
    }

    /*验箱通过调用*/
    public void CheckBoxPass(String zydm) {
        getmParams().clear();
        put("auth", mAuth);
        put("zydm", zydm);
        request(HttpContstants.CHECKBOX_PASS);
    }


    /*请求*/
    private void request(String url) {
        mRequest.url(url)
                .params(mParams)
                .clazz(UpdateInfoBean.class)
                .post()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            UpdateInfoBean updateBean = (UpdateInfoBean) result.data;
                            if (updateBean != null) {
                                mvpView.onSuccess(updateBean);
                                return;
                            }
                        }
                        mvpView.onError(result.code, result.message);
                    }

                    @Override
                    public void onFailure(ResponseData result) {
                        mvpView.onError(result.code, result.message);
                    }
                }).commitAsync();
    }


}
