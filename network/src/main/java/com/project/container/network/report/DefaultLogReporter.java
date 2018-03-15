package com.project.container.network.report;

import android.util.Log;


public final class DefaultLogReporter extends LogReporter {
    @Override
    public void makeLog(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void d(String msg, Object... args) {
        for (Object arg : args) {
            Log.d(msg, arg.toString());
        }
    }
}
