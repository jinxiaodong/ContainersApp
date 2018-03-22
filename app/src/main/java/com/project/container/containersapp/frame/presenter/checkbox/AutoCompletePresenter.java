package com.project.container.containersapp.frame.presenter.checkbox;

import android.content.Context;

import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.JZXAutoCompleteBean;
import com.project.container.containersapp.frame.presenter.IListView;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/21.
 * description：换箱：获取包含输入关键字的所有可用集装箱。
 */

public class AutoCompletePresenter extends Presenter<IListView<JZXAutoCompleteBean>> {

    HttpJsonRequest mRequest;

    public AutoCompletePresenter(Context context, IListView<JZXAutoCompleteBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
    }

    public void getAutoList(String keys) {
        getmParams().clear();
        String auth = LoginBlock.getmAuth();
        put("auth", auth);
        put("term", keys);
        mRequest.url(HttpContstants.AUTO_COMPLETE_BOX)
                .clazz(JZXAutoCompleteBean.class)
                .params(mParams)
                .get()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            List<JZXAutoCompleteBean> data = (List<JZXAutoCompleteBean>) result.data;
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
