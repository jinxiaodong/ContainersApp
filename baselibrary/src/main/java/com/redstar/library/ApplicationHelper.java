package com.redstar.library;

import android.app.Application;

import java.lang.reflect.Method;

/**
 * Created by jiangdylan on 2017/6/20.
 */

public class ApplicationHelper {
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
}
