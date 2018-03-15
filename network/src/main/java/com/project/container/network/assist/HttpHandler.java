/**
 * @file HttpHandler
 * @copyright (c) 2016 Macalline All Rights Reserved.
 * @author SongZheng
 * @date 2016/5/8
 */
package com.project.container.network.assist;

import android.os.Handler;
import android.os.Looper;

/**
 * @author SongZheng
 * @description TODO
 * @date 2016/5/8
 */
public class HttpHandler {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     * Public/Protected Methods
     *******************************************************************************/
    private static HttpHandler delivery;
    private Handler mDeliveryHandler;

    private HttpHandler() {
        mDeliveryHandler = new Handler(Looper.getMainLooper());
        ;
    }

    public static HttpHandler get() {
        if (delivery == null) {
            synchronized (HttpHandler.class) {
                if (delivery == null) {
                    delivery = new HttpHandler();
                }
            }
        }
        return delivery;
    }

    /**
     * request start
     *
     * @param callback
     */
    public <T> void onStart(final UserCallback callback) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onStart();
                }
            }
        });
    }

    /**
     * request success
     *
     * @param result
     * @param callback
     */
    public <T> void onSuccess(final ResponseData result, final UserCallback callback) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    /**
     * request fail
     *
     * @param callback
     */
    public <T> void onFailed(final ResponseData result, final UserCallback callback) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(result);
                }
            }
        });
    }

    /**
     * process on upload or download.
     *
     *
     * @param callback
     */
    public <T> void onProcess(final long totalCount, final long currCount, final UserCallback callback) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoading(totalCount, currCount);
                }
            }
        });
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
