package com.project.container.network.report;



public interface Reporter {
    void report(boolean debug, String msg, Object... args);
}
