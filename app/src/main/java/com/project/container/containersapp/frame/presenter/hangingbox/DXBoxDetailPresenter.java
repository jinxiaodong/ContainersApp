package com.project.container.containersapp.frame.presenter.hangingbox;

import android.content.Context;

import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.DXBoxDetailsBean;
import com.project.container.containersapp.frame.presenter.IListView;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;
import com.project.container.network.request.HttpRequest;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/22.
 * description：吊箱流程列表数据
 */

public class DXBoxDetailPresenter extends Presenter<IListView<DXBoxDetailsBean>> {

    HttpRequest mRequest;

    public DXBoxDetailPresenter(Context context, IListView<DXBoxDetailsBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
    }

    public void getDetailList(String zydm){
        getmParams().clear();
        String auth = LoginBlock.getmAuth();
        put("auth", auth);
        put("zydm", zydm);
        mRequest.url(HttpContstants.DX_PROCESS_LIST)
                .clazz(DXBoxDetailsBean.class)
                .params(mParams)
                .get()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            List<DXBoxDetailsBean> data = (List<DXBoxDetailsBean>) result.data;
                            if (data != null && data.size() > 0) {
                                mvpView.onSuccess(data);
                                return;
                            }
                            mvpView.onEmpty();
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
