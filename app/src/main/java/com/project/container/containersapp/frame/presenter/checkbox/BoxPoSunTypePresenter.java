package com.project.container.containersapp.frame.presenter.checkbox;

import android.content.Context;

import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.PoSunTypeBean;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;
import com.project.container.network.request.HttpRequest;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/19.
 * description：
 */

public class BoxPoSunTypePresenter extends Presenter<IGetPoSunTypeView> {


    HttpRequest mRequest;

    public BoxPoSunTypePresenter(Context context, IGetPoSunTypeView mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
    }

    public void getXiangSuntype() {

        mRequest.url(HttpContstants.TYPE_XIANGSUN)
                .clazz(PoSunTypeBean.class)
                .get()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            List<PoSunTypeBean> list = (List<PoSunTypeBean>) result.data;
                            if (list != null && list.size() > 0) {
                                mvpView.onPoSunTypeSuccess(list);
                                return;
                            }

                        }
                        mvpView.onPoSunError(result.code, result.message);
                    }

                    @Override
                    public void onFailure(ResponseData result) {
                        mvpView.onPoSunError(result.code, result.message);

                    }
                }).commitAsync();
    }
}
