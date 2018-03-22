package com.project.container.containersapp.frame.presenter.hangingbox;

import android.content.Context;

import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.DXBoxListBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;
import com.project.container.network.request.HttpRequest;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/19.
 * description：吊箱数据presenter；
 */

public class DXBoxListPresenter extends Presenter<IBaseListView<DXBoxListBean>> {

    HttpRequest mHttpRequest;

    private int pagesize = 15;
    private int page = 1;

    private boolean isLoadMore = false;

    public DXBoxListPresenter(Context context, IBaseListView<DXBoxListBean> mvpView) {
        super(context, mvpView);
        mHttpRequest = new HttpJsonRequest(mContext);
    }

    public void getListData() {
        page = 1;
        isLoadMore = false;
        requestData();

    }

    public void getListDataMore() {
        page++;
        isLoadMore = true;
        requestData();
    }

    private void requestData() {
        /*获取登录的auth*/
        String auth = LoginBlock.getmAuth();
        /*清除参数*/
        getmParams().clear();
        /*添加参数*/
        put("auth", auth);
        put("page", page);
        put("pagesize", pagesize);

        mHttpRequest.url(HttpContstants.DX_LIST)
                .params(mParams)
                .clazz(DXBoxListBean.class)
                .get()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            DXBoxListBean dxBoxListBean = (DXBoxListBean) result.data;
                            List<DXBoxListBean> list = dxBoxListBean.list;
                            if (list != null && list.size() > 0) {
                                mvpView.onHasNext(dxBoxListBean.isHasNext());
                                if (isLoadMore) {
                                    mvpView.onLoadMore(list);
                                    return;
                                }
                                mvpView.onSuccess(list);
                            } else {
                                mvpView.onEmpty();
                            }
                        } else {
                            if (isLoadMore) {
                                mvpView.onLoadMoreError(result.code, result.message);
                                return;
                            }
                            mvpView.onError(result.code, result.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseData result) {
                        if (isLoadMore) {
                            mvpView.onLoadMoreError(result.code, result.message);
                            return;
                        }
                        mvpView.onError(result.code, result.message);
                    }
                }).commitAsync();
    }
}
