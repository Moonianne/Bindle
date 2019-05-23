package com.android.group.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FourSquareRetrofit {
    private static Retrofit instance;

    private FourSquareRetrofit() {
    }

    public synchronized static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
              .baseUrl("https://api.foursquare.com/")
              .addConverterFactory(GsonConverterFactory.create())
              .build();
        }
        return instance;
    }
}