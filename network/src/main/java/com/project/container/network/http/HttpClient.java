
package com.project.container.network.http;


import com.project.container.network.ApplicationHelper;
import com.project.container.network.assist.HttpHandler;
import com.project.container.network.assist.ResponseData;
import com.project.container.network.cookie.AddCookiesInterceptor;
import com.project.container.network.cookie.CookieJarManager;
import com.project.container.network.cookie.ReceivedCookiesInterceptor;
import com.project.container.network.interceptor.LoggerInterceptor;
import com.project.container.network.parser.ResponseParser;
import com.project.container.network.provider.LoginInterceptor;
import com.project.container.network.provider.Provider;
import com.project.container.network.provider.UserCaseProvider;
import com.project.container.network.report.DefaultLogReporter;
import com.project.container.network.report.LogReporter;
import com.project.container.network.report.Reporter;
import com.project.container.network.request.HttpRequest;
import com.project.container.network.utils.PreferencesUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @Description OkHttpRequest
 */
public class HttpClient {
    private volatile static HttpClient uniqueInstance = null;
    //ok http client
    private OkHttpClient okClient;
    private Call call;

    private static List<Reporter> reporters;
    private static UserCaseProvider userCaseProvider;
    private static LoginInterceptor loginInterceptor;

    /**
     * constructor
     */
    private HttpClient() {
        this(new Builder());
        okClient.interceptors().add(new AddCookiesInterceptor());
        okClient.interceptors().add(new ReceivedCookiesInterceptor());
    }

    /**
     * constructor
     */
    private HttpClient(HttpConfig config) {
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        build.connectTimeout(HttpConfig.connectTimeOut, TimeUnit.SECONDS);
        build.readTimeout(HttpConfig.readTimeOut, TimeUnit.SECONDS);
        build.writeTimeout(HttpConfig.writeTimeOut, TimeUnit.SECONDS);
        if (config != null) {
            if (config.supportCookie) {
//                build.setCookieHandler(config.cookieHandler);
                build.cookieJar(config.cookieJarManager);
            }
            if (config.isSetCacheSize) {
                build.cache(new Cache(new File(config.cacheDirectory), config.cacheSize));
            }
            if (config.logDebug) {
                build.addInterceptor(new LoggerInterceptor());
            }
        }
        okClient = build.build();
    }

    private HttpClient(Builder builder) {
        reporters = builder.reporters;
        userCaseProvider = builder.userCaseProvider;
        loginInterceptor = builder.loginInterceptor;
        init(builder.config);
    }

    /**
     * init
     */
    private void initInner(HttpConfig config) {
        if (okClient != null) {
            OkHttpClient.Builder build = new OkHttpClient.Builder();
            build.connectTimeout(HttpConfig.connectTimeOut, TimeUnit.SECONDS);
            build.readTimeout(HttpConfig.readTimeOut, TimeUnit.SECONDS);
            build.writeTimeout(HttpConfig.writeTimeOut, TimeUnit.SECONDS);
            if (config != null) {
                if (config.supportCookie) {
//                    okClient.setCookieHandler(config.cookieHandler);
                    build.cookieJar(config.cookieJarManager);
                }
                if (config.isSetCacheSize) {
                    build.cache(new Cache(new File(config.cacheDirectory), config.cacheSize));
                }
                if (config.logDebug) {
                    build.addInterceptor(new LoggerInterceptor());
                }
            }
            okClient = build.build();
        }
    }

    /**
     * init
     */
    private static void init(HttpConfig config) {
        if (uniqueInstance == null) {
            synchronized (HttpClient.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new HttpClient(config);
                }
            }
        } else {
            uniqueInstance.initInner(config);
        }
    }

    /**
     * init the object
     *
     * @return
     */
    public static HttpClient instance() {
        if (uniqueInstance != null)
            return uniqueInstance;
        synchronized (HttpClient.class) {
            if (uniqueInstance == null) {
                uniqueInstance = new HttpClient();
            }
        }
        return uniqueInstance;
    }

    /**
     * async http request
     *
     * @param httpRequest
     * @return
     */
    public <T> Call commitAsync(final HttpRequest<T> httpRequest) {
        call = null;
        try {
            Request okRequest = httpRequest.build();
            if (okRequest == null) {
                ResponseData data = new ResponseData(false, "请求参数错误", null);
                HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
                return null;
            }
            HttpHandler.get().onStart(httpRequest.getUserCallBack());
            call = okClient.newCall(okRequest);
            httpRequest.setCall(call);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (httpRequest.canCallback()) {
                        ResponseData data = new ResponseData(false, "网络不给力，请再试一次", null);
                        HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response == null) {
                        if (httpRequest.canCallback()) {
                            ResponseData data = new ResponseData(false, "响应为空", null);
                            HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
                        }
                        return;
                    }
                    if (!response.isSuccessful()) {
                        if (httpRequest.canCallback()) {
                            ResponseData data = new ResponseData(false, "响应错误", null);
                            HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
                        }

                    } else {
                        String authToken = response.header("x-auth-token");
                        if (authToken != null) {
                            getUseCaseProvider().storageToken(authToken);
                        }

                        if (httpRequest.getParser() != null) {
                            httpRequest.getParser().parserAsync(response);
                        } else {
                            if (httpRequest.canCallback()) {
                                ResponseData data = new ResponseData(false, "无法解析数据", null);
                                HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (httpRequest.canCallback()) {
                ResponseData data = new ResponseData(false, "请求异常", null);
                HttpHandler.get().onFailed(data, httpRequest.getUserCallBack());
            }
        }
        return call;
    }

    /**
     * sync http request
     *
     * @param request
     * @return
     */
    public <T> ResponseData commitSync(HttpRequest<T> request) {
        ResponseData data = new ResponseData();
        try {
            final ResponseParser<T> parser = request.getParser();
            Request okRequest = request.build();
            if (okRequest == null) {
                data.status = false;
                data.message = "参数错误";
                return data;
            }
            call = okClient.newCall(okRequest);
            Response response = call.execute();
            if (response.isSuccessful()) {
                return parser.parserSync(response);
//                return response.body().string();
            }
            data.status = false;
            data.message = "请求失败";
        } catch (Exception e) {
            e.printStackTrace();
            data.status = false;
            data.message = "请求异常";
        }
        return data;
    }

    /**
     * cancel request immediately
     */
    public void cancelRequestNow() {
        if (call != null) {
            call.cancel();
        }
    }

    public OkHttpClient getOkClient() {
        return okClient;
    }

    /**
     * eliminate cookie
     */
    public void clearCookies() {
        if (okClient.cookieJar() != null) {
            CookieJarManager jarManager = (CookieJarManager) okClient.cookieJar();
            jarManager.clearCookies();
        }
    }

    public void report(String msg, Object... args) {
        this.report(false, msg, args);
    }

    public void reportDebug(String msg, Object... args) {
        this.report(true, msg, args);
    }

    private void report(boolean debug, String msg, Object... args) {
        if (reporters == null || reporters.isEmpty()) {
            return;
        }
        checkLogReport();
        for (Reporter reporter : reporters) {
            reporter.report(debug, msg, args);
        }
    }

    private void checkLogReport() {
        for (Reporter reporter : reporters) {
            if (reporter instanceof LogReporter) {
                return;
            }
        }
        reporters.add(new DefaultLogReporter());
    }

    public static UserCaseProvider getUseCaseProvider() {
        createIfNeed(userCaseProvider, PreferencesUtils.USER_CASE_PROVIDER);
        return userCaseProvider;
    }

    public static LoginInterceptor getLoginInterceptor() {
        createIfNeed(loginInterceptor, PreferencesUtils.LOGIN_INTERCEPTOR);
        return loginInterceptor;
    }

    private static void createIfNeed(Provider provider, String tag) {
        if (provider != null) {
            return;
        }
        String name = PreferencesUtils.getString(ApplicationHelper.getApplication(), tag);
        try {
            provider = (UserCaseProvider) Class.forName(name).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static final class Builder {
        private List<Reporter> reporters;
        private UserCaseProvider userCaseProvider;
        private LoginInterceptor loginInterceptor;
        private HttpConfig config;

        public Builder() {
        }

        public Builder addReporter(Reporter reporter) {
            if (this.reporters == null) {
                this.reporters = new ArrayList<>();
            }
            if (this.reporters.contains(reporter)) {
                return this;
            }
            this.reporters.add(reporter);
            return this;
        }

        public Builder userCaseProvider(UserCaseProvider provider) {
            if (provider == null) {
                return this;
            }
            this.userCaseProvider = provider;
            PreferencesUtils.putString(ApplicationHelper.getApplication(),
                    PreferencesUtils.USER_CASE_PROVIDER, provider.getClass().getName());
            return this;
        }

        public Builder loginInterceptor(LoginInterceptor loginInterceptor) {
            if (loginInterceptor == null) {
                return this;
            }
            this.loginInterceptor = loginInterceptor;
            PreferencesUtils.putString(ApplicationHelper.getApplication(),
                    PreferencesUtils.LOGIN_INTERCEPTOR, loginInterceptor.getClass().getName());
            return this;
        }

        public Builder config(HttpConfig config) {
            this.config = config;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}
