package com.netease.core.network.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO 对回调处理类对象
public class RequestCallbacks implements Callback {

    private final IError iError;
    private final IFailure iFailure;
    private final IRequest iRequest;
    private final ISuccess iSuccess;

    public RequestCallbacks(IError iError, IFailure iFailure, IRequest iRequest, ISuccess iSuccess) {
        this.iError = iError;
        this.iFailure = iFailure;
        this.iRequest = iRequest;
        this.iSuccess = iSuccess;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful() && call.isExecuted() && iSuccess != null) {
            iSuccess.onSuccess(response.message());
        } else if (iError != null) {
            iError.onError(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (iFailure != null) {
            iFailure.onFailure();
        }

        if (iRequest != null) {
            iRequest.onRequestEnd(); // 最后纪录
        }
    }
}
