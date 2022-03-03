package com.netease.core.network.rx;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

// TODO Retrofit 接口
public interface RxRetrofitService {

    @GET // www.baidu.com/参数1，参数2
    Observable<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @PUT
    Observable<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @DELETE
    Observable<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    // 下载 直接下载到内存中的
    @Streaming
    @GET
    Observable<String> download(@Url String url, @QueryMap Map<String, Object> params);

    // 上传
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part file);

    // 原始数据
    @POST
    Observable<String> postRaw(@Url String url, @Body RequestBody requestBody);

    // 原始数据
    @PUT
    Observable<String> putRaw(@Url String url, @Body RequestBody requestBody);
}
