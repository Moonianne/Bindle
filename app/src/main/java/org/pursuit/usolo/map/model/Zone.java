package org.pursuit.usolo.map.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

public final class Zone {
    public String name;
    public LatLng location;
    public int users;

    public Zone() {
    }

    public Zone(String name, LatLng location, int users) {
        this.name = name;
        this.location = location;
        this.users = users;
    }
}
