package com.zbxn.pub.http;


import com.zbxn.pub.utils.ConfigUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhkj on 16/3/11.
 */
public class UtilHttpRequest {

    private static IResourceOA newsIResource;
    private static Retrofit retrofitInstance;

    public static Retrofit GetRetrofitInstance() {
        if (retrofitInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

            String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
            retrofitInstance = new Retrofit.Builder().baseUrl(server).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofitInstance;
    }

    public static IResourceOA getIResource() {
        if (newsIResource == null) {
            newsIResource = GetRetrofitInstance().create(IResourceOA.class);
        }
        return newsIResource;
    }

}
