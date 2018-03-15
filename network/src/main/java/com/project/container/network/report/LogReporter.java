package com.project.container.network.report;


public abstract class LogReporter implements Reporter {
    abstract public void makeLog(String tag, String msg);

    abstract public void d(String msg, Object... args);

    @Override
    public void report(boolean debug, String msg, Object... args) {
        if (args == null || args.length == 0) {
            return;
        }
        if (debug) {
            d(msg, args);
            return;
        }
        makeLog(msg, args[0].toString());
    }
}
