package org.pursuit.firebasetools.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class User {
    @NonNull
    private String displayName;
    @NonNull
    private String email;
    @Nullable
    private String aboutMe;
    @Nullable
    private String interests;
    @Nullable
    private String currentZone;
    @Nullable
    private String currentGroup;
    @Nullable
    private String currentLocation;
    @Nullable
    private String userProfilePhotoURL;
    @Nullable
    private String userId;

    public User() {
    }

    public User(@NonNull String displayName, @NonNull String email, @Nullable String aboutMe, @Nullable String interests, @Nullable String currentZone, @Nullable String currentGroup, @Nullable String currentLocation, @Nullable String userProfilePhotoURL, @Nullable String userId) {
        this.displayName = displayName;
        this.email = email;
        this.aboutMe = aboutMe;
        this.interests = interests;
        this.currentZone = currentZone;
        this.currentGroup = currentGroup;
        this.currentLocation = currentLocation;
        this.userProfilePhotoURL = userProfilePhotoURL;
        this.userId = userId;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getUserProfilePhotoURL() {
        return userProfilePhotoURL;
    }

    public void setUserProfilePhotoURL(String userProfilePhotoURL) {
        this.userProfilePhotoURL = userProfilePhotoURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(String currentZone) {
        this.currentZone = currentZone;
    }

    public String getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(String currentGroup) {
        this.currentGroup = currentGroup;
    }
}
