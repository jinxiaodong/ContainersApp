package com.project.container.containersapp.frame.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;
import com.project.container.containersapp.frame.constants.DeviceInfo;
import com.project.container.containersapp.frame.utils.LogUtil;
import com.project.container.network.http.HttpClient;
import com.project.container.network.http.HttpConfig;
import com.project.container.network.report.LogReporter;
import com.redstar.library.BuildConfig;
import com.redstar.library.task.TaskHelper;

/**
 * Created by xiaodong.jin on 2018/3/14.
 * description：
 */

public class JZXApplication extends Application {

    static Context context;
    public static JZXApplication instance;

    public final String TAG = getClass().getSimpleName();

    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        //task init
        TaskHelper.getInstance().taskInit();
        //初始化内存监控：暂时不用
//        LeakCanaryUtil.initLeakCanary(this);

        appInit();
        //初始化网络请求框架
        initOkhttp();

    }

    private void appInit() {

        //初始化Logger tag
        Logger.init("JXZ");
        //屏幕信息\
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.makeLog(TAG, "============appInit, orientation:" + "横屏状态");
            DeviceInfo.WIDTHPIXELS = displayMetrics.heightPixels;
            DeviceInfo.HEIGHTPIXELS = displayMetrics.widthPixels;
        } else {
            LogUtil.makeLog(TAG, "============appInit, orientation:" + "竖屏状态");
            DeviceInfo.WIDTHPIXELS = displayMetrics.widthPixels;
            DeviceInfo.HEIGHTPIXELS = displayMetrics.heightPixels;
        }
        DeviceInfo.DENSITYDPI = displayMetrics.densityDpi;
        DeviceInfo.DENSITY = displayMetrics.density;
    }


    public static JZXApplication getInstance() {
        return instance;
    }

    private void initOkhttp() {

        HttpConfig httpConfig = new HttpConfig(this);
        httpConfig.supportCookie = true;
        httpConfig.logDebug = BuildConfig.DEBUG;
        HttpClient.Builder builder = new HttpClient.Builder();
        builder.config(httpConfig).addReporter(new LogReporter() {
            @Override
            public void makeLog(String tag, String msg) {
                LogUtil.makeLog(tag, msg);
            }

            @Override
            public void d(String msg, Object... args) {
                LogUtil.d(msg, args);
            }
        }).build();

//             .loginInterceptor(new LoginInterceptor() {
//            /*登录拦截：暂时不做什么处理，后面如果需要有自动登录过期那么在这进行处理*/
//            @Override
//            public void intercept(ResponseData data, HttpRequest request) {
//                if (TextUtils.isEmpty(data.code)) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(data.data.toString());
//                        String code = jsonObject.getString("code");
//                        if (code.equals("-401")) {
//                            reLogin();
//                            if (request.canCallback()) {
//                                HttpHandler.get().onFailed(data, request.getUserCallBack());
//                            }
//                            LoginBlock.setUserInfo(null);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (data.code.equals("-401")) {
//                        reLogin();
//                        if (request.canCallback()) {
//                            HttpHandler.get().onFailed(data, request.getUserCallBack());
//                        }
//                        LoginBlock.setUserInfo(null);
//                    }
//                }
//            }
//        }).userCaseProvider(new UserCaseProvider() {
//            @Override
//            public String token() {
//                return LoginBlock.getAuthToken();
//            }
//
//            @Override
//            public String locationCode() {
//                return LoginBlock.getLocationCode();
//            }
//
//            @Override
//            public void storageToken(String token) {
//                LoginBlock.setAuthToken(token);
//            }
//        })

    }

}
