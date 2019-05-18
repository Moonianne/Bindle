package com.android.group.model;

public class Venue {
    private String id;
    private String name;
    private VenueLocation location;

    public Venue(final String id,
                 final String name,
                 final VenueLocation location) {
        this.id = id;
        this.name = name;
        this.location = location;
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
}
