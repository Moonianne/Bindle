package com.android.group.respository;

import android.util.Log;

import com.android.group.model.FourSquareResponse;
import com.android.group.model.VenueResponse;
import com.android.group.network.BusinessService;
import com.android.group.network.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    public void getApiData(final onDataReceivedListener listener) {
        RetrofitSingleton.getInstance()
          .create(BusinessService.class)
          .getBusinesses()
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

    public interface onDataReceivedListener {
        void dataReceived(VenueResponse venueResponse);
    }
}
