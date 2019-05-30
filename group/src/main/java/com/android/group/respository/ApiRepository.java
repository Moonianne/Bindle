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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.observable.ObservableCombineLatest;
import io.reactivex.schedulers.Schedulers;

public final class ApiRepository {
    // TODO: 2019-05-23 Change name of this repository to something more general for yelp addition
    private static final String TAG = "ApiRepository";
    private YelpService yelpService;
    private BusinessService businessService;

    public ApiRepository() {
        yelpService = YelpSingleton.getInstance()
                .create(YelpService.class);
        businessService = FourSquareRetrofit.getInstance()
                .create(BusinessService.class);
    }

    public Observable<BindleBusiness> getBindleBusinesses(String category) {
        return getFourSquareData(category)
                .subscribeOn(Schedulers.io())
                .flatMapIterable((Function<FourSquareResponse, Iterable<Venue>>) fourSquareResponse ->
                        fourSquareResponse.getResponse().getVenues())
                .map(venue -> {
                    try {
                        return new BindleBusiness(venue, ApiRepository.this.getYelpApiData(venue).blockingFirst().getBusinesses().get(0));
                    } catch (IndexOutOfBoundsException e) {
                        Log.d(TAG, "This threw it: " + e.toString());
                    }
                    return new BindleBusiness(venue, null);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<FourSquareResponse> getFourSquareData(String category) {
        return businessService.getBusinesses(category);
    }

    private Observable<YelpResponse> getYelpApiData(Venue venue) {
        return yelpService.getBusinesses(venue.getName());
    }

}
