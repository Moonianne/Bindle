package com.android.group.model;

public class FourSquareResponse {
    private VenueResponse response;

    public FourSquareResponse(final VenueResponse response) {
        this.response = response;
    }

    public VenueResponse getResponse() {
        return response;
    }
}
