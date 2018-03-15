
package com.project.container.containersapp.frame.presenter;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description： MVP架构 P层: 数据提供者基类
 */
public class Presenter<T> {


    public HashMap<String, Object> getmParams() {
        if (mParams==null){
            mParams = new HashMap<>();
        }
        return mParams;
    }


    protected HashMap<String, Object> mParams;
    protected T mvpView;
    protected Context mContext;

    public Presenter(Context context, T mvpView){
        this.mContext = context;
        this.mvpView = mvpView;
    }

    public HashMap<String,Object> put(String key, Object value){
        if(mParams == null){
            mParams = new HashMap<String, Object>();
        }
        mParams.put(key,value);
        return mParams;
    }

}
