/**
 * @file BaseConstants
 * @copyright (c) 2016 Macalline All Rights Reserved.
 * @author SongZheng
 * @date 2016/3/4
 */
package com.redstar.library.constants;

import com.redstar.library.ApplicationHelper;

/**
 * @author SongZheng
 * @description 常量
 * @date 2016/3/4
 */
public class BaseConstants {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/
    /*Debug版本标志*/
    public static boolean isTest = false;
    /*应用包名*/
    public final static String PACKAGE_NAME = ApplicationHelper.getApplication().getPackageName();
    /*应用版本code*/
    public static int APP_VERSION_CODE = 1;
    /** version name */
    public static String APP_VERSION_NAME = "1.0";
    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
