package com.android.group.network;

import com.android.group.model.yelp.YelpResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpService {
    String ENDPOINT = "v3/businesses/search?&location=nyc";

    @GET(ENDPOINT)
    Call<YelpResponse> getBusinesses(@Query("term") String business);
}

