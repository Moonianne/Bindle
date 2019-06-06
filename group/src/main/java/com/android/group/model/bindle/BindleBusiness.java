package com.android.group.model.bindle;

import com.android.group.model.foursquare.Venue;
import com.android.group.model.yelp.Business;

import io.reactivex.annotations.NonNull;

public final class BindleBusiness {
    public static final String NIGHTLIFE = "Nightlife";
    public static final String EAT_AND_DRINK = "Eat & Drink";
    public static final String SIGHTSEEING = "Sight-Seeing";

    private final Venue venue;
    private final Business business;

    public BindleBusiness(@NonNull final Venue venue,
                          @NonNull final Business business) {
        this.venue = venue;
        this.business = business;
    }

    public Venue getVenue() {
        return venue;
    }

    public Business getBusiness() {
        return business;
    }
}
