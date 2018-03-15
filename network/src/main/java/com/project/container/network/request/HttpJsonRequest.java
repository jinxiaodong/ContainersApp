
package com.project.container.network.request;

import android.content.Context;

import com.project.container.network.http.HttpClient;
import com.project.container.network.parser.JsonResponseParser;
import com.project.container.network.parser.ResponseParser;
import com.project.container.network.utils.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;



public class HttpJsonRequest<T> extends HttpRequest<T> {
    //
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //
    private boolean isPostJson = false;

    private ResponseParser mParser;
    /**
     * constructor
     * */
    private HttpJsonRequest(){
        super(null);
    }

    /**
     * constructor
     * */
    public HttpJsonRequest(Context context){
        super(context);
    }

    @Override
    public Request build() {
        Request request = null;
        if(mUrl == null){
            return null;
        }
        if(appendDefaultParam){
            appendDefaultParams();
        }
        if(appendDefaultHeader){
            appendDefaultHeaders();
        }


        switch(mMethod){
            case Method.METHOD_GET:
                request = requestGet();
                break;
            case Method.METHOD_POST:
                request = requestPost();
                break;
            case Method.METHOD_DELETE:
                request = requestDelete();
                break;
            case Method.METHOD_PUT:
                request = requestPut();
                break;
            default:
                break;
        }
        return request;
    }

    @Override
    public ResponseParser getParser() {
        if(mParser == null){
            mParser = new JsonResponseParser<T>(this);
        }
        return mParser;
    }

    /**
     *
     * */
    public HttpJsonRequest postWithJsonFormat(boolean value){
        this.isPostJson = value;
        return this;
    }

    /**
     *
     */
    private Request requestGet() {
        StringBuilder sb = new StringBuilder(mUrl);
        if (mParams != null && mParams.size() > 0) {
            HashMap<String, String> map = new HashMap<>();
            for(String key : mParams.keySet()){
                if(key == null || mParams.get(key) == null){
                    continue;
                }
                String value = mParams.get(key).toString();
                try {
                    value = URLEncoder.encode(value, "utf-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                map.put(key,value);
            }
            if(sb.toString().contains("?") == false){
                sb.append("?");
            }
            if(sb.toString().endsWith("?") == false){
                sb.append("&");
            }
            if(getSecret()) {
//                SignUtil.createStringSignMap(map);
            }
            int position = 0;
            for (String key : map.keySet()) {
                sb.append(key + "=" + map.get(key));
                if(position < map.size()-1){
                    sb.append("&");
                }
                position++;
            }
        }
        String url = sb.toString();
         HttpClient.instance().report("HttpJsonRequest","=========get url:"+url);
        Request.Builder builder = new Request.Builder();
        if(mHeaders != null){
            for(String key : mHeaders.keySet()){
                builder.addHeader(key,mHeaders.get(key));
            }
        }
        return builder.url(url).cacheControl(getCacheControl()).build();
    }

    /**
     *
     */
    private Request requestPost(){
        RequestBody body = null;
        if(isPostJson){
            body = buildJsonRequestBody();
        }else{
            body = buildFormRequestBody();
        }
        Request.Builder builder = new Request.Builder();
        if(mHeaders != null){
            for(String key : mHeaders.keySet()){
                builder.addHeader(key,mHeaders.get(key));
            }
        }
        return builder.url(mUrl).post(body).cacheControl(getCacheControl()).build();
    }

    /**
     *
     */
    private Request requestPut() {
        RequestBody body = null;
        if(isPostJson){
            body = buildJsonRequestBody();
        }else{
            body = buildFormRequestBody();
        }
        Request.Builder builder = new Request.Builder();
        if(mHeaders != null){
            for(String key : mHeaders.keySet()){
                builder.addHeader(key,mHeaders.get(key));
            }
        }
        return builder.url(mUrl).put(body).build();
    }

    /**
     *
     */
    private Request requestDelete() {
        RequestBody body = null;
        if(isPostJson){
            body = buildJsonRequestBody();
        }else{
            body = buildFormRequestBody();
        }
        Request.Builder builder = new Request.Builder();
        if(mHeaders != null){
            for(String key : mHeaders.keySet()){
                builder.addHeader(key,mHeaders.get(key));
            }
        }
        return builder.url(mUrl).delete(body).build();
    }

    /**
     *
     * */
    private RequestBody buildFormRequestBody(){
        RequestBody body = null;
        if(getSecret()) {
//            SignUtil.createSignMap(mParams);
        }
        if(mParams != null && mParams.size() > 0){
//            MultipartBody.Builder formBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
//            for(String key : mParams.keySet()){
//                 HttpClient.instance().report("HttpJsonRequest","========= params:key:"+key+" value:"+mParams.get(key));
//                formBuild.addFormDataPart(key, mParams.get(key).toString());
//            }
//            body = formBuild.build();

            FormBody.Builder formBuild = new FormBody.Builder();
            for(String key : mParams.keySet()){
                formBuild.add(key, String.valueOf(mParams.get(key)));
                 HttpClient.instance().report("HttpJsonRequest","========= params:key:"+key+" value:"+mParams.get(key));
            }
            body = formBuild.build();
        } else {
            body = new FormBody.Builder().build();
        }
        return body;
    }

    /**
     *
     * */
    private RequestBody buildJsonRequestBody(){
        RequestBody body = null;
        if(getSecret()) {
//            SignUtil.createSignMap(mParams);
        }
        if(mParams != null && mParams.size()>0){
            String json = JsonUtil.objectToString(mParams);
             HttpClient.instance().report("HttpJsonRequest", "======post json:" + json);
            body = RequestBody.create(JSON, json);
        } else {
            body = RequestBody.create(null, "");
        }
        return body;
    }


}
