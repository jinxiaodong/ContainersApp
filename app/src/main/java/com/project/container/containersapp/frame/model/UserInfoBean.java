package com.project.container.containersapp.frame.model;

import com.project.container.containersapp.frame.base.BaseBean;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：用户信息model
 */

public class UserInfoBean extends BaseBean {

    /**
     * id : 704
     * auth : fD00zLR5M6li3Emrg-ngUnuLW_nbk-Zt
     * username : 张三
     * roles : ["吊箱","施封","门卫","验箱"]    权限列表
     * isSuper : 0  是否是超级用户
     */

    public String id;
    public String auth;
    public String username;
    public int isSuper;
    public List<String> roles;

}
