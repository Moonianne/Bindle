package org.pursuit.usolo.map.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.zonechat.model.Message;

import java.util.Map;

public final class Zone {
    public String name;
    public Map<String, Message> zoneChatKey;
    public LatLng location;
    public int users;

    public Zone() {
    }

    public Zone(String name, Map<String, Message> zoneChatKey, LatLng location, int users) {
        this.name = name;
        this.zoneChatKey = zoneChatKey;
        this.location = location;
        this.users = users;
    }
}
