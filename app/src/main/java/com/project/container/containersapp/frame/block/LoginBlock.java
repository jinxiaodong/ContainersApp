package com.project.container.containersapp.frame.block;

import android.text.TextUtils;

import com.project.container.containersapp.frame.application.JZXApplication;
import com.project.container.containersapp.frame.constants.PreferencesKeys;
import com.project.container.containersapp.frame.model.UserInfoBean;
import com.redstar.library.utils.JsonUtil;
import com.redstar.library.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：登录模块 信息处理
 */

public class LoginBlock {

    /**
     * 用户登录auth
     */
    private static String mAuthToken;

    /*
    * 用户登录名
    */

    private static String username;

    /*
    * 用户id
    */

    private static String id;

    /*
    * 是否超级用户
    * */
    private static String isSuper;


    /*
    * 功能权限
    * */

    private static List<String> mRoles;


    /*设置auth*/
    public static void setmAuth(String auth) {
        mAuthToken = auth;
        if (mAuthToken != null) {
            PreferencesUtils.putString(JZXApplication.getInstance(), PreferencesKeys.USER_AUTH, mAuthToken);
        }
    }
    /*获取auth*/
    public static String getmAuth() {
        if (mAuthToken == null) {
            String string = PreferencesUtils.getString(JZXApplication.getInstance(), PreferencesKeys.USER_AUTH, "");
        }
        return mAuthToken;
    }

    /*设置角色*/
    public static void setRoles(List<String> roles) {
        mRoles = roles;
        if (mRoles != null) {
            String role = JsonUtil.objectToString(roles);
            PreferencesUtils.putString(JZXApplication.getInstance(), PreferencesKeys.USER_ROLES, role);
        }
    }

    /*获取角色*/
    public static List<String> getRoles() {
        if (mRoles == null) {
            String string = PreferencesUtils.getString(JZXApplication.getInstance(), PreferencesKeys.USER_ROLES, "");
            List<String> roles = JsonUtil.parseList(string, String.class);
            return roles;
        }
        return mRoles;
    }

    /*设置用户信息*/
    public static void setUserInfo(UserInfoBean data) {
        if (data != null) {
            //转为JSON字符串存储
            String userInfo = JsonUtil.objectToString(data);
            PreferencesUtils.putString(JZXApplication.getInstance(), PreferencesKeys.USER_INFO, userInfo);
        }
    }

    /*
    * 获取用户信息：
    * 使用时要判断是否为空，为空则需要重新登录。
    * */
    public static UserInfoBean getUSerInfo() {

        String string = PreferencesUtils.getString(JZXApplication.getInstance(), PreferencesKeys.USER_INFO, "");
        if (!TextUtils.isEmpty(string)) {
            UserInfoBean parse = JsonUtil.parse(string, UserInfoBean.class);
            return parse;
        }
        return null;
    }


}
