package com.android.group.respository;

import android.util.Log;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.model.foursquare.FourSquareResponse;
import com.android.group.model.foursquare.Venue;
import com.android.group.model.yelp.YelpResponse;
import com.android.group.network.BusinessService;
import com.android.group.network.FourSquareRetrofit;
import com.android.group.network.YelpService;
import com.android.group.network.YelpSingleton;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class ApiRepository {
    // TODO: 2019-05-23 Change name of this repository to something more general for yelp addition
    private static final String TAG = "ApiRepository";

    public void getBindleBusinesses(String category, OnDataReceivedListener listener) {
        getFourSquareData(category)
                .subscribeOn(Schedulers.io())
                .flatMapIterable((Function<FourSquareResponse, Iterable<Venue>>) fourSquareResponse ->
                        fourSquareResponse.getResponse().getVenues())
                .map(venue -> {
                    try {
                        return new BindleBusiness(venue, ApiRepository.this.getYelpApiData(venue).blockingFirst().getBusinesses().get(0));
                    }catch (IndexOutOfBoundsException e){
                        Log.d(TAG, "This threw it: "+ e.toString());
                    }
                    return new BindleBusiness(venue, null);
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bindleBusinesses -> listener.dataReceived(bindleBusinesses),
                        throwable -> Log.d(TAG, "accept: " + throwable.toString()));
    }

    private Observable<FourSquareResponse> getFourSquareData(String category) {
        return FourSquareRetrofit.getInstance()
                .create(BusinessService.class)
                .getBusinesses(category);
    }

    private Observable<YelpResponse> getYelpApiData(Venue venue) {
        return YelpSingleton.getInstance()
                .create(YelpService.class)
                .getBusinesses(venue.getName());
    }

    public interface OnDataReceivedListener {
        void dataReceived(List<BindleBusiness> bindleBusinesses);
    }
}
