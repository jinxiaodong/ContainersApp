package com.project.container.containersapp.frame.model;

import com.project.container.containersapp.frame.base.BaseBean;

/**
 * Created by xiaodong.jin on 2018/3/22.
 * description：
 */

public class DXBoxDetailsBean extends BaseBean {


    /**
     * dxdtxzh : 4
     * dxdlsh : 0
     * dxdzydm : 2
     * dxdzygclxdm : 01
     * dxdjzxdm : TBJU1000044
     * dxdywzlx : 箱位
     * dxdywz : 010121
     * dxdmbwzlx : 集卡
     * dxdmbwz : tch0002
     * dxdwcsj : 1516807132
     * dxdczzh : 35
     * dxdzylb : 0
     */

    public String dxdtxzh;          //提箱组号
    public String dxdlsh;           //提箱流水号
    public String dxdzydm;          //作业代码
    public String dxdzygclxdm;      //过程ID  对于发送作业，过程ID为“03”或“07”，以区分吊空箱和重箱（2018-02-04注释）
    public String dxdjzxdm;         //集装箱代码（箱号）
    public String dxdywzlx;         //源位置类型
    public String dxdywz;           //源位置
    public String dxdmbwzlx;        //目标位置类型
    public String dxdmbwz;          //目标位置
    public String dxdwcsj;          //操作完成时间
    public String dxdczzh;          //DXDCZZH
    public String dxdzylb;          //对应的作业类别   0：发送：1：到达//2018-02-12更新

    public boolean isFinsh; //是否可点击，只有当前处于操作状态才可以点击

}
