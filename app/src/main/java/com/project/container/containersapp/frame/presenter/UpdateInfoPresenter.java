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

    private HttpJsonRequest mRequest;
    private final String mAuth;

    public UpdateInfoPresenter(Context context, IBaseView<UpdateInfoBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
        mAuth = LoginBlock.getmAuth();
    }

    /*更新当前作业过程代码*/
    public void updateDQZYGCDM(String zydm, String zygcdm) {
        getmParams().clear();
        String url = HttpContstants.UPDATE_ZY_STATUS;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.UPDATE_ZY_STATUS + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        put("zydm", zydm);
        put("zydqzygcdm", zygcdm);

        request(url);
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
        String url = HttpContstants.CHECKBOX_PASS;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.CHECKBOX_PASS + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getmParams().clear();
        put("zydm", zydm);
        request(url);
    }

    /*吊箱更新接口*/
    public void updateDxComplete(String txzh, String txlsh) {
        getmParams().clear();
        String url = HttpContstants.DX_UPDATE_COMPLETE;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.DX_UPDATE_COMPLETE + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        put("txzh", txzh);
        put("txlsh", txlsh);

        request(url);
    }


    /*装箱完成*/
    public void updateBoxingFinish(String zydm) {
        getmParams().clear();
        String url = HttpContstants.ZX_FINISH;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.ZX_FINISH + "?auth=" + auth;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        put("zydm", zydm);
        request(url);
    }

    /*检斤验货*/
    public void updateJianjinCommit(String zydm,String zytyrzl, String zycyrzl,
                                    String jzxzg,String zysfh){
        getmParams().clear();
        String url = HttpContstants.CHECK_GOODS_DONE;
        try {
            String auth = URLEncoder.encode(mAuth, "utf-8");
            url = HttpContstants.CHECK_GOODS_DONE + "?auth=" + auth;//+"&zysfh="+zysfh
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //作业代码
        put("zydm", zydm);
        //托运人重量
//        put("zytyrzl", zytyrzl);
        //承运人重量
        put("zycyrzl", " "+zycyrzl+" ");
        //集装箱自重
//        put("jzxzg", jzxzg);
        //施封号
        put("zysfh", " "+zysfh+" ");
        request(url);
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
