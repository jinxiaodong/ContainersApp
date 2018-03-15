package com.project.container.containersapp.frame.presenter;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：MVP 提供给V层的回调接口
 */

public interface IBaseView<T> {

    void onSuccess(T data);

    void onError(String code, String msg);
}
