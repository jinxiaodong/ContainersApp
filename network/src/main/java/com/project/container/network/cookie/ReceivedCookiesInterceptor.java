package com.project.container.network.cookie;


import com.project.container.network.ApplicationHelper;
import com.project.container.network.utils.PreferencesUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by wulin on 2016/9/5.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            PreferencesUtils.putStringSet(ApplicationHelper.getApplication(),cookies);
//            PreferenceHelper.getDefaultPreferences().edit()
//                    .putStringSet(PreferenceHelper.PREF_COOKIES, cookies)
//                    .apply();

        }

        return originalResponse;
    }
}
