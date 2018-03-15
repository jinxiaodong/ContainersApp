package com.project.container.network;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;



public class ApplicationHelper {
    private static String VERSION_NAME;
    private static int VERSION_CODE;

    public static Application getApplication() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Method method = clazz.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode() {
        if (VERSION_CODE > 0) {
            return VERSION_CODE;
        }
        Context context = getApplication();
        int version = 1;
        if (context == null) {
            return 1;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        VERSION_CODE = version;
        return VERSION_CODE;
    }

    public static String getVersionName() {
        if (VERSION_NAME != null) {
            return VERSION_NAME;
        }
        Context context = getApplication();
        String version = "0.0.0";
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        VERSION_NAME = version;
        return VERSION_NAME;
    }
}
