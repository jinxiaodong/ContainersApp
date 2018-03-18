package com.project.container.containersapp.frame.presenter;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/16.
 * description：
 */

public interface IBaseListView<T> {

    void onSuccess(List<T> list);

    /*加载更多*/
    void onLoadMore(List<T> list);

    /*数据为空*/
    void onEmpty();

    void onError(String code, String msg);

    /*加载更多出错*/
    void  onLoadMoreError(String code,String msg);

    /*是否有下一页*/
    void onHasNext(boolean hasNext);
}
