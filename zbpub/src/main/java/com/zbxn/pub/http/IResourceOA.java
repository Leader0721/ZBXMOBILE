package com.zbxn.pub.http;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by zbx on 16/1/11.
 */
public interface IResourceOA {

    /**
     * action 拼接的url地址   @Url动态的url地址请求;半静态的url地址请求 @GET("users/{user}/repos") @Path("id") String id
     * map 传入的参数
     */
    @GET
    Call<JsonObject> GetRequest(@Url String action, @QueryMap Map<String, String> map);

    /**
     * action 拼接的url地址   @Url动态的url地址请求;半静态的url地址请求 @GET("users/{user}/repos") @Path("id") String id
     * map 传入的参数
     */
    //@FormUrlEncoded
//    @POST
//    Call<JSONObject> GetRequest(@Url String action, @FieldMap Map<String, String> map);
}
