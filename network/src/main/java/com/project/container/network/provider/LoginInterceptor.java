package com.project.container.network.provider;


import com.project.container.network.assist.ResponseData;
import com.project.container.network.request.HttpRequest;

public interface LoginInterceptor extends Provider {
    void intercept(ResponseData data, HttpRequest request);
}
