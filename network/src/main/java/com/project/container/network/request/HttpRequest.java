
package com.project.container.network.request;

import android.app.Activity;
import android.content.Context;

import com.project.container.network.ApplicationHelper;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.assist.UserCallback;
import com.project.container.network.http.HttpClient;
import com.project.container.network.parser.ResponseParser;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Request;


public abstract class HttpRequest<T> {
    /*******************************************************************************
     * Public/Protected Variables
     *******************************************************************************/
    protected String TAG = getClass().getSimpleName();
    //
    protected String mUrl;
    //
    protected int mMethod;
    //
    protected HashMap<String, Object> mParams;
    //tag
    protected String mTag;
    //header
    protected Map<String, String> mHeaders;
    //
    protected Class clazz;
    //
    protected boolean secret;
    //
    protected boolean isListData = false;
    //
    protected boolean showDialog = true;
    //添加默认参数：暂不需要设置为false
    protected boolean appendDefaultParam = false;
    //添加默认herader：暂不需要设置为false
    protected boolean appendDefaultHeader = false;
    //
    protected boolean returnRawData = false;
    //
    protected CacheStrategy cacheStrategy = CacheStrategy.DEFAULT;
    //是否登录拦截：暂时不需要
    protected boolean isLoginIntercept = false;
    /**
     *
     */
    protected boolean mCallbackAnyway = false;
    //reset headers
    private boolean resetHeaders = true;

    /*******************************************************************************
     * Private Variables
     *******************************************************************************/
//    private Context mContext;
    private WeakReference<Context> mWRContext;
    //    private Call mRequestCall;
    private UserCallback mUserCallBack;
    /**
     * okhttp call
     */
    private Call mCall;
    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     * Public/Protected Methods
     *******************************************************************************/
    public HttpRequest(Context context) {
//        this.mContext = context;
        mWRContext = new WeakReference<Context>(context);
    }

    public Context getContext() {
        return mWRContext.get();
//        return mContext;
    }

    /**
     *
     */
    public boolean canCallback() {
        if (mCallbackAnyway) {
            return true;
        }
        Context context = getContext();
        if (context instanceof Activity) {
            if (context == null) {
                return false;
            }
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public abstract Request build();

    /**
     *
     */
    public abstract ResponseParser getParser();

    /**
     * url
     */
    public HttpRequest url(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     *
     */
    public HttpRequest method(int method) {
        this.mMethod = method;
        return this;
    }

    /**
     *
     */
    public HttpRequest get() {
        this.mMethod = Method.METHOD_GET;
        return this;
    }

    /**
     *
     */
    public HttpRequest post() {
        this.mMethod = Method.METHOD_POST;
        return this;
    }

    /**
     *
     */
    public HttpRequest put() {
        this.mMethod = Method.METHOD_PUT;
        return this;
    }

    /**
     *
     */
    public HttpRequest delete() {
        this.mMethod = Method.METHOD_DELETE;
        return this;
    }

    /**
     *
     */
    public HttpRequest params(Map<String, Object> params) {
        if (mParams == null) {
            mParams = new HashMap<String, Object>();
        }
        mParams.clear();
        mParams.putAll(params);
        return this;
    }

    /**
     *
     */
    public HttpRequest tag(String tag) {
        this.mTag = tag;
        return this;
    }

    /**
     *
     */
    public HttpRequest headers(Map<String, String> headers) {
        this.mHeaders = headers;
        return this;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     *
     */
    public HttpRequest clazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public Class getClazz() {
        return clazz;
    }

    public Boolean getLoginIntercept() {
        return isLoginIntercept;
    }

    public HttpRequest setLoginIntercept(Boolean isLoginIntercept) {
        this.isLoginIntercept = isLoginIntercept;
        return this;
    }

    /**
     *
     */
    public HttpRequest secret(boolean value) {
        this.secret = value;
        return this;
    }

    protected boolean getSecret() {
        return this.secret;
    }

    /**
     *
     */
    public HttpRequest callback(UserCallback callback) {
        this.mUserCallBack = callback;
        return this;
    }

    public UserCallback getUserCallBack() {
        return this.mUserCallBack;
    }


    /**
     *
     */
    public HttpRequest callbackAnyway(boolean callbackAnyway) {
        this.mCallbackAnyway = callbackAnyway;
        return this;
    }

    public boolean getCallbackAnyway() {
        return this.mCallbackAnyway;
    }
    /**
     *
     * */
//    public HttpRequest isList(boolean isList) {
//        this.isListData = isList;
//        return this;
//    }
//
//    public boolean getIsList(){
//        return isListData;
//    }

    /**
     *
     */
    public HttpRequest dialog(boolean show) {
        this.showDialog = show;
        return this;
    }

    /**
     *
     */
    public HttpRequest defaultParam(boolean defaultParam) {
        this.appendDefaultParam = defaultParam;
        return this;
    }

    /**
     *
     */
    public HttpRequest defaultHeader(boolean defaultHeader) {
        this.appendDefaultHeader = defaultHeader;
        return this;
    }


    public HttpRequest putHeaders(Map<String, String> headers) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.putAll(headers);
        return this;
    }

    /**
     *
     */
    public HttpRequest rawData(boolean rawData) {
        this.returnRawData = rawData;
        return this;
    }

    public HttpRequest cacheStrategy(CacheStrategy cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
        return this;
    }

    /**
     *
     */
    public void commitAsync() {
        HttpClient.instance().commitAsync(this);
    }

    /**
     *
     */
    public ResponseData commitSync() {
        return HttpClient.instance().commitSync(this);
    }

    public boolean getReturnRawData() {
        return this.returnRawData;
    }

    public boolean getShowDialog() {
        return this.showDialog;
    }

    /**
     *
     * @param result
     * @return
     */
//    protected Object dataParser(String result) {
//        if (TextUtils.isEmpty(result)) {
//            return null;
//        }
//        if (isListData) {
//            return JsonUtil.parseList(result, clazz);
//        } else {
//            return JsonUtil.parse(result, clazz);
//        }
//    }

    /**
     *
     * @return
     */
    protected CacheControl getCacheControl() {
        CacheControl cacheControl = null;
        switch (cacheStrategy) {
            case FORCE_NETWORK:
                cacheControl = CacheControl.FORCE_NETWORK;
                break;
            case FORCE_CACHE:
                cacheControl = CacheControl.FORCE_CACHE;
                break;
            default:
                cacheControl = new CacheControl.Builder().build();
                break;
        }
        return cacheControl;
    }

    /**
     *
     */
    protected void appendDefaultParams() {
        if (mParams == null) {
            mParams = new HashMap<String, Object>();
        }
        //TODO
        //
        //TODO
//        UserInfoBean user = LoginBlock.getUserInfo();
//        if(user != null){
//            mParams.put("access_token",user.accessToken);
//        }
        //mParams.put("mt_token", ApiTokenUtil.getEncryptedString(ApiTokenUtil.getTokenTime()));

    }

    public HttpRequest resetHeaders(boolean is) {
        this.resetHeaders = is;
        return this;
    }

    private void putHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        if (!resetHeaders && mHeaders.containsKey(key)) {
            return;
        }
        mHeaders.put(key, value);
    }

    protected void appendDefaultHeaders() {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        String authToken = HttpClient.getUseCaseProvider().token();
        if (authToken != null) {
            putHeader("x-auth-token", authToken);
            HttpClient.instance().report("token", authToken);
        }

        String locationCode = HttpClient.getUseCaseProvider().locationCode();
        putHeader("locationCode", locationCode == null ? "310100" : locationCode);
        HttpClient.instance().report("locationCode", mHeaders.get("locationCode"));

        putHeader("versionCode", String.valueOf(ApplicationHelper.getVersionCode()));
        putHeader("versionName", ApplicationHelper.getVersionName());
    }

    public void setCall(Call call) {
        this.mCall = call;
    }

    /**
     *
     */
    public void cancelRequest() {
        if (mCall != null && mCall.isCanceled() == false) {
            mCall.cancel();
        }
    }
    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
    /**
     * 缓存
     */
    public enum CacheStrategy {
        /**
         */
        DEFAULT,
        /**
         */
        FORCE_NETWORK,
        /**
         */
        FORCE_CACHE,
    }

    /**
     */
    public static class Method {
        public final static int METHOD_GET = 1;
        public final static int METHOD_POST = 2;
        public final static int METHOD_PUT = 3;
        public final static int METHOD_DELETE = 4;
        public final static int METHOD_MAX = 5;
    }
}
