package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.respository.ApiRepository;

import java.util.List;

public class NetworkViewModel extends ViewModel implements ApiRepository.OnDataReceivedListener {

    private static NetworkViewModel viewModel;
    private OnDataLoadedListener dataLoadedListener;
    private OnVenueSelectedListener venueSelectedListener;
    private ApiRepository apiRepository;
    private List<BindleBusiness> bindleBusinesses;

    private NetworkViewModel() {
        apiRepository = new ApiRepository();
    }

    public static NetworkViewModel getSingleInstance() {
        if (viewModel == null) {
            viewModel = new NetworkViewModel();
        }
        return viewModel;
    }

    @Override
    public void dataReceived(List<BindleBusiness> bindleBusinesses) {
        this.bindleBusinesses = bindleBusinesses;
        dataLoadedListener.dataLoaded(bindleBusinesses);
    }

    public List<BindleBusiness> getBindleBusinesses() {
        return bindleBusinesses;
    }

    public void setOnDataLoadedListener(OnDataLoadedListener listener) {
        this.dataLoadedListener = listener;
    }

    public void makeBindleBusinessNetworkCall(String category) {
        apiRepository.getBindleBusinesses(category, this);
    }

    public void setUserSelectedVenue(BindleBusiness venue) {
        venueSelectedListener.bindleBusinessSelected(venue);
    }

    public void setVenueSelectedListener(OnVenueSelectedListener listener) {
        this.venueSelectedListener = listener;
    }

    public interface OnDataLoadedListener {
        void dataLoaded(List<BindleBusiness> bindleBusinesses);
    }

    public interface OnVenueSelectedListener {
        void bindleBusinessSelected(BindleBusiness bindleBusiness);
    }
}
