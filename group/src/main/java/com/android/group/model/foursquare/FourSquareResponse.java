package com.android.group.model.foursquare;

public class FourSquareResponse {
    private VenueResponse response;

    public FourSquareResponse(final VenueResponse response) {
        this.response = response;
    }

    public VenueResponse getResponse() {
        return response;
    }
}
