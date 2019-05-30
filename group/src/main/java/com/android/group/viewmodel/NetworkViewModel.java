package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.respository.ApiRepository;

import io.reactivex.Observable;

public class NetworkViewModel extends ViewModel{

    private static NetworkViewModel viewModel;
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

    public Observable<BindleBusiness> makeBindleBusinessNetworkCall(String category) {
        return apiRepository.getBindleBusinesses(category);
    }

    public void setUserSelectedVenue(BindleBusiness venue) {
        venueSelectedListener.bindleBusinessSelected(venue);
    }

    public void setVenueSelectedListener(OnVenueSelectedListener listener) {
        this.venueSelectedListener = listener;
    }

    public interface OnVenueSelectedListener {
        void bindleBusinessSelected(BindleBusiness bindleBusiness);
    }
}
