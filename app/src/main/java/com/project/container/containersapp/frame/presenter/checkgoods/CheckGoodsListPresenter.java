package com.project.container.containersapp.frame.presenter.checkgoods;

import android.content.Context;

import com.project.container.containersapp.frame.block.LoginBlock;
import com.project.container.containersapp.frame.constants.HttpContstants;
import com.project.container.containersapp.frame.model.CheckGoodsListBean;
import com.project.container.containersapp.frame.presenter.IBaseListView;
import com.project.container.containersapp.frame.presenter.Presenter;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.request.HttpJsonRequest;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/29.
 * description：
 */

public class CheckGoodsListPresenter extends Presenter<IBaseListView<CheckGoodsListBean>> {

    HttpJsonRequest mRequest;
    private int pagesize = 15;
    private int page = 1;

    private boolean isLoadMore = false;

    public CheckGoodsListPresenter(Context context, IBaseListView<CheckGoodsListBean> mvpView) {
        super(context, mvpView);
        mRequest = new HttpJsonRequest(mContext);
    }

    public void getListData() {
        isLoadMore = false;
        page = 1;
        requestData();
    }

    public void getListDataMore() {
        isLoadMore = true;
        page++;
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

        mRequest.url(HttpContstants.CHECK_GOODS_LIST)
                .params(mParams)
                .clazz(CheckGoodsListBean.class)
                .get()
                .callback(new UserCallback() {
                    @Override
                    public void onSuccess(ResponseData result) {
                        if ("200".equals(result.code)) {
                            CheckGoodsListBean checkGoodsListBean = (CheckGoodsListBean) result.data;
                            if (checkGoodsListBean == null) {
                                if (isLoadMore) {
                                    mvpView.onLoadMoreError(result.code, result.message);
                                    return;
                                }
                                mvpView.onEmpty();
                                return;
                            }
                            List<CheckGoodsListBean> list = checkGoodsListBean.list;
                            if (list != null && list.size() > 0) {
                                mvpView.onHasNext(checkGoodsListBean.isHasNext());
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
