package com.android.group.model;

import com.android.group.model.foursquare.VenueCategory;

public class Venue {
    private String id;
    private String name;
    private VenueLocation location;
    private VenueCategory[] categories;

    public Venue(final String id,
                 final String name,
                 final VenueLocation location,
                 VenueCategory[] categories) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VenueLocation getLocation() {
        return location;
    }

    public VenueCategory[] getCategories() {
        return categories;
    }
}
