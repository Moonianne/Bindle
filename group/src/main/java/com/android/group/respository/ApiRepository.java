package com.android.group.respository;

import android.util.Log;

import com.android.group.model.foursquare.FourSquareResponse;
import com.android.group.model.VenueResponse;
import com.android.group.model.yelp.Business;
import com.android.group.model.yelp.YelpResponse;
import com.android.group.network.BusinessService;
import com.android.group.network.FourSquareRetrofit;
import com.android.group.network.YelpService;
import com.android.group.network.YelpSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class ApiRepository {
    // TODO: 2019-05-23 Change name of this repository to something more general for yelp addition

    public void getFourSquareApiData(final onDataReceivedListener listener, String category) {
        FourSquareRetrofit.getInstance()
          .create(BusinessService.class)
          .getBusinesses(category)
          .enqueue(new Callback<FourSquareResponse>() {
              @Override
              public void onResponse(Call<FourSquareResponse> call, Response<FourSquareResponse> response) {
                  if (response.body() != null) {
                      listener.dataReceived(response.body().getResponse());
                  }
              }

              @Override
              public void onFailure(Call<FourSquareResponse> call, Throwable t) {
                  Log.d("retrofitResponse", "onFailure: " + t.getMessage());
              }
          });
    }

    public void getYelpApiData(final onDataReceivedListener listener, String businessName) {
        YelpSingleton.getInstance()
          .create(YelpService.class)
          .getBusinesses(businessName)
          .enqueue(new Callback<YelpResponse>() {
              @Override
              public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                  if (response.body() != null) {
                      listener.yelpDataReceived(response.body());
                  }
                  Log.d("joe's response", "onResponse: " + response.body().getBusinesses().get(0).getName());
              }

              @Override
              public void onFailure(Call<YelpResponse> call, Throwable t) {
                  Log.d("joe's response", "onResponse: " + t.getMessage());
              }
          });
    }

    public interface onDataReceivedListener {
        void dataReceived(VenueResponse venueResponse);

        void yelpDataReceived(YelpResponse yelpResponse);
    }
}
