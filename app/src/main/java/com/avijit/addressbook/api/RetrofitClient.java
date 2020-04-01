package com.avijit.addressbook.api;

import com.avijit.addressbook.common.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    private RetrofitClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null)
            retrofitClient = new RetrofitClient();

        return retrofitClient;
    }

    public ContactApi getApi() {
        return retrofit.create(ContactApi.class);
    }
}