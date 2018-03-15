
package com.project.container.network.parser;


import com.project.container.network.assist.ResponseData;
import com.project.container.network.http.HttpClient;
import com.project.container.network.request.HttpRequest;
import com.project.container.network.utils.JsonUtil;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * @description TODO
 */
public class JsonResponseParser<T> extends ResponseParser<T> {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/

    /*******************************************************************************
     * Overrides From Base
     *******************************************************************************/
    @Override
    public ResponseData parser(Response response) {
        ResponseData data = new ResponseData();
        try {
            data.status = true;
            if (response == null) {
                return data;
            }

            String content = response.body().string();
            if (content == null) {
                data.message = "内容为空";
                return data;
            }

            HttpClient.instance().report("JsonResponseParser", "====json:" + content);
            Class clazz = request.getClazz();

            if (request.getReturnRawData() || clazz == null) {
                data.data = content;
                data.message = "请求成功";
                return data;
            } else {
                JSONObject jo = new JSONObject(content);
                if (jo.has("code")) {
                    Integer isOk = jo.getInt("code");
                    String strOk = jo.getString("code");
                    if (isOk != null) {
                        data.status = true;
                        data.code = String.valueOf(isOk);
                    } else if (strOk != null) {
                        data.status = true;
                        data.code = strOk;
                    } else {
                        data.status = false;
                    }
                } else if (jo.has("errorCode")) {
                    int isOk = jo.getInt("errorCode");
                    if (isOk == 0 || isOk == 200) {
                        data.status = true;
                    } else {
                        data.status = false;
                    }
                }


                if (!jo.isNull("message")) {
                    data.message = jo.getString("message");
                } else if (!jo.isNull("errorMsg")) {
                    data.message = jo.getString("errorMsg");
                }

                if (jo.isNull("dataMap")) {
                    return data;
                }

                if (!jo.isNull("hasNextPage")) {
                    data.hasNextPage = jo.getBoolean("hasNextPage");
                }

                String dataString = jo.getString("dataMap");
                if (dataString.startsWith("[")) {
                    data.data = JsonUtil.parseList(dataString, clazz);
                } else if (dataString.startsWith("{")) {
                    data.data = JsonUtil.parse(dataString, clazz);
                } else {
                    data.data = jo.get("dataMap");
                }

                return data;
            }
        } catch (Exception e) {
            data.status = false;
            data.message = "解析错误";
        }
        return data;
    }

    /*******************************************************************************
     * Public/Protected Methods
     *******************************************************************************/
    public JsonResponseParser(HttpRequest<T> request) {
        super(request);
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
