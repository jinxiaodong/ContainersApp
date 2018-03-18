package com.project.container.containersapp.frame.base;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/18.
 * description：列表数据统一的bean
 */

public class BaseListBean<T> extends BaseBean {

    public String currentPage;     //当前页数
    public String total;        //数据总数
    public String pageSize;     //一页数据的size
    public String totalPage;       //总页数
    public List<T> list;


    /*
    * 是否有下一页
    * */
    public boolean isHasNext() {
        return Integer.parseInt(currentPage) < Integer.parseInt(totalPage);
    }
}
