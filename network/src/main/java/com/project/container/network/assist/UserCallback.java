/**
 * @file UserCallback
 * @copyright (c) 2016 Macalline All Rights Reserved.
 * @author SongZheng
 * @date 2016/5/8
 */
package com.project.container.network.assist;

/**
 * @author SongZheng
 * @description
 * @date 2016/5/8
 */
public abstract class UserCallback {
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
     *	Public/Protected Methods
     *******************************************************************************/
    /**
     * request callback. use this in UI thread exp: dialog.
     */
    public void onStart() {

    }

    /**
     * UI thread
     */
    public abstract void onSuccess(ResponseData result);

    /**
     * UI thread
     * @param result
     */
    public abstract void onFailure(ResponseData result);

    /**
     * UI thread
     * @param count
     * @param current
     */
    public void onLoading(long count, long current) {

    }
    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
