
package com.project.container.network.request;

import android.content.Context;

import com.project.container.network.http.HttpClient;
import com.project.container.network.parser.DownloadResponseParser;
import com.project.container.network.parser.ResponseParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Request;


public class HttpDownloadRequest<T> extends HttpRequest<T> {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/
    private DownloadResponseParser<T> mDownloadParser;
    private String mSavedFilePath;
    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    /**
     * constructor
     * */
    public HttpDownloadRequest(){
        super(null);
    }

    /**
     * constructor
     * */
    public HttpDownloadRequest(Context context){
        super(context);
    }


    public HttpDownloadRequest path(String filePath){
        mSavedFilePath = filePath;
        return this;
    }

    @Override
    public Request build() {
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
        HttpClient.instance().report("HttpJsonRequest", "=========get url:" + url);
        return new Request.Builder().url(url).cacheControl(getCacheControl()).build();
    }

    @Override
    public ResponseParser getParser() {
        if(mDownloadParser == null){
            mDownloadParser = new DownloadResponseParser<>(this,mSavedFilePath);
        }
        return mDownloadParser;
    }

    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
