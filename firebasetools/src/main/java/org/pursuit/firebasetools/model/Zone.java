package org.pursuit.firebasetools.model;

import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Zone {
    private LatLng location;
    private String chatName;
    private String name;
    private int userCount;
    @Nullable
    private List<String> currentUserIds;

    public Zone() {
    }

    public Zone(@NotNull LatLng location, @NotNull String chatName, @NotNull String name, int userCount) {
        this.location = location;
        this.chatName = chatName;
        this.name = name;
        this.userCount = userCount;
    }

    @NotNull
    public LatLng getLocation() {
        return location;
    }

    public void setLocation(@NotNull LatLng location) {
        this.location = location;
    }

    @NotNull
    public String getChatName() {
        return chatName;
    }

    public void setChatName(@NotNull String chatName) {
        this.chatName = chatName;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    @Nullable
    public List<String> getCurrentUserIds() {
        return currentUserIds;
    }

    public void setCurrentUserIds(@Nullable List<String> currentUserIds) {
        this.currentUserIds = currentUserIds;
    }
}
