/**
 * @file ResponseData
 * @copyright (c) 2016 Macalline All Rights Reserved.
 * @author SongZheng
 * @date 2016/5/5
 */
package com.project.container.network.assist;

/**
 * @author SongZheng
 * @description TODO
 * @date 2016/5/5
 */
public class ResponseData {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/
    /**error code*/
    public String code;
    /**message from server*/
    public String message;
    /**data object*/
    public Object data;
    /**request status*/
    public boolean status;
//    public int countRecord;
    public boolean hasNextPage;
    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    public ResponseData(){
    }

    public ResponseData(boolean status, String errorMsg, Object data){
        this.status = status;
        this.message = errorMsg;
        this.data = data;
    }
    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
