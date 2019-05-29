package com.android.group.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FourSquareRetrofit {
    private static Retrofit instance;
    // TODO: 2019-05-23 Change name of this class to something more general

    private FourSquareRetrofit() {
    }

    public synchronized static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl("https://api.foursquare.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}
