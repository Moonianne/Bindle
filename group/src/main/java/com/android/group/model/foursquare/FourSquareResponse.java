package com.android.group.model.foursquare;

import com.android.group.model.VenueResponse;

public class FourSquareResponse {
    private VenueResponse response;

    public FourSquareResponse(final VenueResponse response) {
        this.response = response;
    }

    public VenueResponse getResponse() {
        return response;
    }
}
