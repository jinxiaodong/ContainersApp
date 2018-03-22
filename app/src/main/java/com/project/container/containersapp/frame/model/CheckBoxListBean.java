package com.project.container.containersapp.frame.model;

import com.project.container.containersapp.frame.base.BaseListBean;

/**
 * Created by xiaodong.jin on 2018/3/16.
 * description：验箱列表数据bean类
 */

public class CheckBoxListBean extends BaseListBean<CheckBoxListBean> {

    public String zydm;         //作业代码
    public String zyydaid;      //运单号
    public String zyjzxdm;      //集装箱代码 (箱号)
    public String zydqzygcdm;   //当前作业过程DM
    public String xqdm;         //箱区代码
    public String xwdm;         //箱位代码
    public String xqmc;         //箱区名称
    public String zytype;       //1：表示发送作业，2表示到达作业。

}
