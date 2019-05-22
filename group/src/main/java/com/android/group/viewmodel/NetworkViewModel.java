package com.android.group.viewmodel;

import com.android.group.model.Venue;
import com.android.group.model.VenueResponse;
import com.android.group.respository.Repository;

import java.util.List;

public class NetworkViewModel implements Repository.onDataReceivedListener {

    private static NetworkViewModel viewModel;
    private OnDataLoadedListener dataLoadedListener;
    private OnVenueSelectedListener venueSelectedListener;
    private Repository repository;


    private NetworkViewModel() {
        repository = new Repository();
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

    public void makeNetworkCall(String category) {
        repository.getApiData(this, category);
    }

    public void setUserSelectedVenue(Venue venue){
        venueSelectedListener.venueSelected(venue);

    }

    public void setVenueSelectedListener(OnVenueSelectedListener listener){
        this.venueSelectedListener = listener;
    }

    @Override
    public void dataReceived(VenueResponse venueResponse) {
        dataLoadedListener.dataLoaded(venueResponse.getVenues());
    }

    public interface OnDataLoadedListener {
        void dataLoaded(List<Venue> venues);
    }

    public interface OnVenueSelectedListener{
        void venueSelected(Venue venue);
    }
}
