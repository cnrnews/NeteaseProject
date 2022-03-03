package com.netease.core;

import com.netease.core.network.callback.IError;
import com.netease.core.network.callback.IFailure;
import com.netease.core.network.callback.IRequest;
import com.netease.core.network.callback.ISuccess;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;

public class RequestClientBuilder {

    // 链式调用（调用方式） 和 建造者（模式）没有任何关系

    private HashMap<String, Object> param;
    private String url;
    private IError iError;
    private IFailure iFailure;
    private IRequest iRequest;
    private ISuccess iSuccess;
    private RequestBody requestBody;

    // 上传 下载
    private File file;
    private String downloadDir;
    private String extension;
    private String fileName;

    public RequestClientBuilder param(HashMap<String, Object> param) {
        this.param = param;
        return this; // 链式调用（调用方式）
    }

    public RequestClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestClientBuilder error(IError iError) {
        this.iError = iError;
        return this;
    }

    public RequestClientBuilder failure(IFailure iFailure) {
        this.iFailure = iFailure;
        return this;
    }

    public RequestClientBuilder request(IRequest iRequest) {
        this.iRequest = iRequest;
        return this;
    }

    public RequestClientBuilder success(ISuccess iSuccess) {
        this.iSuccess = iSuccess;
        return this;
    }

    public RequestClientBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    // 最终一定会建筑结果 build
    public RequestClient build() {
        return new RequestClient(param, url, iError, iFailure, iRequest, iSuccess, requestBody, file, downloadDir, extension, fileName);
    }

}
