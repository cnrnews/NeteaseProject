package com.netease.core.network.rx;

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

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

// TODO 请求的客户端
public class RxRequestClient {

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

    public RxRequestClient(HashMap<String, Object> param, String url, IError iError,
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
    public static RxRequestClientBuilder create() {
        return new RxRequestClientBuilder();
    }

    // 内部真正完成请求操作 的处理方法
    private Observable<String> requestAction(HttpMethod httpMethod) {

        RxRetrofitService retrofitService = RetrofitCreateor.getRxRetrofitService();

        // 标记一个起点
        if (iRequest != null) {
            iRequest.onRequestStart();
        }

        // 接收
        Observable<String> observableResult = null;

        switch (httpMethod) {
            case GET:
                observableResult = retrofitService.get(url, param);
                break;
            case PUT:
                observableResult = retrofitService.put(url, param);
                break;
            case POST:
                observableResult = retrofitService.post(url, param);
                break;
            case DELETE:
                observableResult = retrofitService.delete(url, param);
                break;
            case DOWNLOAD:
                observableResult = retrofitService.download(url, param);
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

        return observableResult;
    }

    // 暴露具体
    public Observable<String> get() {
        return requestAction(HttpMethod.GET);
    }
    public Observable<String> post() {
        return requestAction(HttpMethod.POST);
    }
    public Observable<String> put() {
        return requestAction(HttpMethod.PUT);
    }
    public Observable<String> delete() {
        return requestAction(HttpMethod.DELETE);
    }
    public Observable<String> download() {
        return requestAction(HttpMethod.DOWNLOAD);
    }
    public Observable<String> upload(File file) {
        return requestAction(HttpMethod.UPLOAD);
    }
}
