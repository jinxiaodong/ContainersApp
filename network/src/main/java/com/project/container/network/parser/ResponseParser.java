
package com.project.container.network.parser;


import com.project.container.network.assist.HttpHandler;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.http.HttpClient;
import com.project.container.network.request.HttpRequest;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;


public abstract class ResponseParser<T> {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/
    protected HttpRequest<T> request;
    /**
     *
     */
    protected long contentLength;
    /**
     *
     */
    protected String charsetName;
    /**
     *
     */
    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    public ResponseParser(HttpRequest<T> request) {
        this.request = request;
    }

    /**
     *
     * @param response
     */
    public abstract ResponseData parser(Response response);

    /**
     *
     * @param response
     */
    public final ResponseData parserSync(Response response) {
        if (response.isSuccessful()) {
            try {
                getResponseBodyInfo(response.body());
                return parser(response);
            } catch (Exception e) {
                return new ResponseData(false, "数据解析异常", null);
            }
        } else {
            return new ResponseData(false, "响应错误", null);
        }
    }

    /**
     *
     * @param response
     */
    public final void parserAsync(Response response) {
        if (response == null) {
            if (request.canCallback()) {
                ResponseData data = new ResponseData(false, "无返回数据", null);
                HttpHandler.get().onFailed(data, request.getUserCallBack());
            }
            return;
        }
        if (response.isSuccessful()) {
            try {
                getResponseBodyInfo(response.body());
                ResponseData data = parser(response);


                //
                if (request.getLoginIntercept()) {
                    HttpClient.getLoginInterceptor().intercept(data, request);
                }

                if (request.canCallback()) {
                    HttpHandler.get().onSuccess(data, request.getUserCallBack());
                }
//                if(data.retCode != null && data.retCode.equals("E00006")){
//                    HttpHandler.get().reLogin(request);
//                }
            } catch (IOException e) {
                if (request.canCallback()) {
                    ResponseData data = new ResponseData(false, "数据接收异常", null);
                    HttpHandler.get().onFailed(data, request.getUserCallBack());
                }
            } catch (Exception e) {
                if (request.canCallback()) {
                    ResponseData data = new ResponseData(false, "数据解析异常", null);
                    HttpHandler.get().onFailed(data, request.getUserCallBack());
                }
            }
        } else if (response.code() == 403) {
            if (request.canCallback()) {
                ResponseData data = new ResponseData(false, "您的请求不被允许", null);
                HttpHandler.get().onFailed(data, request.getUserCallBack());
            }
        } else {
            if (request.canCallback()) {
                ResponseData data = new ResponseData(false, "响应错误", null);
                HttpHandler.get().onFailed(data, request.getUserCallBack());
            }
        }
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/
    /**
     *
     * @param body
     * @throws IOException
     */
    private final void getResponseBodyInfo(ResponseBody body) throws IOException {
        MediaType contentType = body.contentType();
//        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
//        this.charsetName = charset.name();
        this.contentLength = body.contentLength();
    }
    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
