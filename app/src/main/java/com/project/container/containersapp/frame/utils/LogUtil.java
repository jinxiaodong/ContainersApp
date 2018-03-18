
package com.project.container.containersapp.frame.utils;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;


public class LogUtil {

    //log config
    public static boolean logDebug = true;


    /**
     * LOG显示
     *
     * @param tag
     * @param msg
     */
    public static void makeLog(String tag, String msg) {
        if (logDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * LOG显示
     *
     * @param tag
     * @param msg
     */
    public static void makeLog(Context context, String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (logDebug) {
            try {
                Log.i(context.getClass().getSimpleName() + "_" + tag, msg);
            } catch (Exception e) {
                Log.i("SimpleName Not Found" + tag, msg);
            }
        }

    }


    public static void i(String msg, Object... args) {
        if (logDebug)
            Logger.i(msg, args);
    }

    public static void e(String msg, Object... args) {
        if (logDebug)
            Logger.e(msg, args);
    }

    public static void d(String msg, Object... args) {
        if (logDebug)
            Logger.d(msg, args);
    }

    public static void v(String msg, Object... args) {
        if (logDebug)
            Logger.v(msg, args);
    }

    public static void w(String msg, Object... args) {
        if (logDebug)
            Logger.w(msg, args);
    }

    public static void json(String json) {
        if (logDebug)
            Logger.json(json);
    }

    public static void xml(String xml) {
        if (logDebug)
            Logger.xml(xml);
    }

}
