package com.netease.core.network;

import com.netease.core.app.ProjectInit;
import com.netease.core.network.rx.RxRetrofitService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

// TODO 此类是 Retrofit创造者
public class RetrofitCreateor {

    // 定义一个全局的Retrofit的客户端
    private final static class RetrofitHolder {

        private final static String BASE_URL = ProjectInit.API_HOST;

        private final static Retrofit RETROFIT_CLITENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    // OKHttp 客户端
    private final static class OKHttpHolder {

        private final static int TIME_OUT = 60;

        private final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    // 对外提供 RetrofitService
    public static RetrofitService getRetrofitService() {
        return RetrofitHolder.RETROFIT_CLITENT.create(RetrofitService.class);
    }

    // 对外提供 RxRetrofitService
    public static RxRetrofitService getRxRetrofitService() {
        return RetrofitHolder.RETROFIT_CLITENT.create(RxRetrofitService.class);
    }
}
