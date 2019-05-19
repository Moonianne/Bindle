package com.android.group.viewmodel;

import com.android.group.model.VenueResponse;
import com.android.group.respository.Repository;

public class NetworkViewModel implements Repository.onDataReceivedListener {

    private Repository repository;

    public NetworkViewModel() {
        repository = new Repository();
    }

    public void makeNetworkCall(String query) {
        repository.getApiData(this);
    }

    @Override
    public void dataReceived(VenueResponse venueResponse) {
        // TODO: 2019-05-18 do something with response
    }
}
