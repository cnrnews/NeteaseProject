package com.netease.core;

import com.netease.core.network.HttpMethod;
import com.netease.core.network.RetrofitCreateor;
import com.netease.core.network.RetrofitService;
import com.netease.core.network.callback.IError;
import com.netease.core.network.callback.IFailure;
import com.netease.core.network.callback.IRequest;
import com.netease.core.network.callback.ISuccess;
import com.netease.core.network.callback.RequestCallbacks;

import java.io.File;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

// TODO 请求的客户端
public class RequestClient {

    private final HashMap<String, Object> param;
    private final String url;
    private final IError iError;
    private final IFailure iFailure;
    private final IRequest iRequest;
    private final ISuccess iSuccess;
    private final RequestBody requestBody;

    // 上传 下载
    private final File file;
    private final String downloadDir;
    private final String extension;
    private final String fileName;

    public RequestClient(HashMap<String, Object> param, String url, IError iError,
                         IFailure iFailure, IRequest iRequest, ISuccess iSuccess, RequestBody requestBody,
                         File file, String downloadDir, String extension, String fileName) {
        this.param = param;
        this.url = url;
        this.iError = iError;
        this.iFailure = iFailure;
        this.iRequest = iRequest;
        this.iSuccess = iSuccess;
        this.requestBody = requestBody;
        this.file = file;
        this.downloadDir = downloadDir;
        this.extension = extension;
        this.fileName = fileName;
    }

    // 为了配合 建造者模式
    public static RequestClientBuilder create() {
        return new RequestClientBuilder();
    }

    // 内部真正完成请求操作 的处理方法
    private void requestAction(HttpMethod httpMethod) {

        RetrofitService retrofitService = RetrofitCreateor.getRetrofitService();

        // 标记一个起点
        if (iRequest != null) {
            iRequest.onRequestStart();
        }

        // 接收
        Call<String> callResult = null;

        switch (httpMethod) {
            case GET:
                callResult = retrofitService.get(url, param);
                break;
            case PUT:
                callResult = retrofitService.put(url, param);
                break;
            case POST:
                callResult = retrofitService.post(url, param);
                break;
            case DELETE:
                callResult = retrofitService.delete(url, param);
                break;
            case DOWNLOAD:
                callResult = retrofitService.download(url, param);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                final MultipartBody.Part body =MultipartBody.Part.createFormData("file", file.getName());
                retrofitService.upload(url, body);
                break;
            case PUT_RAW:
                break;
            case POST_RAW:
                break;
           default:
               break;
        }

        // OK HTTP 异步执行
        if (callResult != null) {
            callResult.enqueue(new RequestCallbacks(iError, iFailure, iRequest, iSuccess));
        }

    }
}
