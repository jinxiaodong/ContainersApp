package com.project.container.containersapp.frame.presenter;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/21.
 * description：
 */

public interface IListView<T> {

    void onSuccess(List<T> data);

    void onEmpty();

    void onError(String code, String msg);
}
