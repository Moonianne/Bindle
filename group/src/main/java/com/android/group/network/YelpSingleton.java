package com.android.group.network;

import android.text.TextUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class YelpSingleton {
    private static final String BASE_URL = "https://api.yelp.com/";
    private static final String AUTH_TOKEN = "tPcRo3VZ6AO0qIxKNkWI2Fok77jRImOIJrl0rX15Or9k39vuC4QCLtT2BdD4cWjUgUw3flvp42af_UirQ00EYe69v7_S_yz_WNHGJLO5rlPnWIbt8QPM82j6EhrXXHYx";

    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
              .client(createClient(AUTH_TOKEN))
              .addConverterFactory(GsonConverterFactory.create())
              .baseUrl(BASE_URL)
              .build();
        }
        return instance;
    }

    private static OkHttpClient createClient(final String authToken) {
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (!TextUtils.isEmpty(authToken)) {
            Interceptor interceptor =
              chain -> chain.proceed(chain.request()
                .newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .build());
            if (!httpClientBuilder.interceptors().contains(interceptor)) {
                httpClientBuilder.addInterceptor(interceptor);
            }
        }
        return httpClientBuilder.build();
    }
}

