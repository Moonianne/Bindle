package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.android.group.model.Venue;
import com.android.group.model.VenueResponse;
import com.android.group.model.yelp.Business;
import com.android.group.model.yelp.YelpResponse;
import com.android.group.respository.ApiRepository;

import java.util.ArrayList;
import java.util.List;

public class NetworkViewModel extends ViewModel implements ApiRepository.onDataReceivedListener {

    private static NetworkViewModel viewModel;
    private OnDataLoadedListener dataLoadedListener;
    private OnInfoSelectedListener infoSelectedListener;
    private List<Venue> venues = new ArrayList<>();
    private List<Business> businesses = new ArrayList<>();
    private OnVenueSelectedListener venueSelectedListener;
    private ApiRepository apiRepository;

    private NetworkViewModel() {
        apiRepository = new ApiRepository();
    }

    public static NetworkViewModel getSingleInstance() {
        if (viewModel == null) {
            viewModel = new NetworkViewModel();
        }
        return viewModel;
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.dataLoadedListener = listener;
    }
    public void setInfoSelectedListener(OnInfoSelectedListener listener) {
        this.infoSelectedListener = listener;
    }

    public void makeFourSquareNetworkCall(String category) {
        apiRepository.getFourSquareApiData(this, category);
    }

    public void makeYelpNetworkCall(String businessName) {
        apiRepository.getYelpApiData(this, businessName);
    }

    public void setUserSelectedVenue(Venue venue) {
        venueSelectedListener.venueSelected(venue);
    }

    public void setVenueSelectedListener(OnVenueSelectedListener listener) {
        this.venueSelectedListener = listener;
    }

    @Override
    public void dataReceived(VenueResponse venueResponse) {
        dataLoadedListener.dataLoaded(venueResponse.getVenues());
    }

    @Override
    public void yelpDataReceived(YelpResponse yelpResponse) {
        infoSelectedListener.yelpDataLoaded(yelpResponse.getBusinesses());
    }

    public interface OnDataLoadedListener {
        void dataLoaded(List<Venue> venues);
    }

    public interface OnInfoSelectedListener{
        void yelpDataLoaded(List<Business> businesses);
    }

    public interface OnVenueSelectedListener {
        void venueSelected(Venue venue);
    }
}
