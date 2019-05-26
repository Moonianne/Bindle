package org.pursuit.firebasetools.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

public final class Zone {
    private LatLng location;
    private String chatName;
    private String name;
    private int userCount;

    public Zone() {
    }

    public Zone(LatLng location, String chatName, String name, int userCount) {
        this.location = location;
        this.chatName = chatName;
        this.name = name;
        this.userCount = userCount;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
