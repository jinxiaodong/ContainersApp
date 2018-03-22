package com.project.container.containersapp.frame.presenter.checkbox;

import com.project.container.containersapp.frame.model.PoSunTypeBean;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/19.
 * description：
 */

public interface IGetPoSunTypeView {

    void onPoSunTypeSuccess(List<PoSunTypeBean> list);

    void onPoSunError(String code, String msg);

}
