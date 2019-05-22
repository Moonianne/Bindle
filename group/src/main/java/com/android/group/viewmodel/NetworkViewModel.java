package com.android.group.viewmodel;

import com.android.group.model.Venue;
import com.android.group.model.VenueResponse;
import com.android.group.respository.FourSquareRepository;

import java.util.List;

public class NetworkViewModel implements FourSquareRepository.onDataReceivedListener {

    private static NetworkViewModel viewModel;
    private OnDataLoadedListener dataLoadedListener;
    private OnVenueSelectedListener venueSelectedListener;
    private FourSquareRepository fourSquareRepository;


    private NetworkViewModel() {
        fourSquareRepository = new FourSquareRepository();
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
        fourSquareRepository.getApiData(this, category);
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

    public interface OnDataLoadedListener {
        void dataLoaded(List<Venue> venues);
    }

    public interface OnVenueSelectedListener {
        void venueSelected(Venue venue);
    }
}
