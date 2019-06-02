package org.pursuit.usolo.model;


import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

public final class MapFeature {
    public final String name;
    public final LatLng location;
    public final List<List<Point>> points;

    public MapFeature(String name, LatLng location, List<List<Point>> points) {
        this.name = name;
        this.location = location;
        this.points = points;
    }
}
