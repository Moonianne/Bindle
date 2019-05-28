package org.pursuit.userprofile.model;

import android.location.Location;

public class UserProfile {

    private String displayName, aboutMeText, interestsText, profilePhotoUrl;
    private Location currentLocation;

    public UserProfile(String displayName, Location currentLocation, String aboutMeText, String interestsText, String profilePhotoUrl) {
        this.currentLocation = currentLocation;
        this.displayName = displayName;
        this.aboutMeText = aboutMeText;
        this.interestsText = interestsText;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getAboutMeText() {
        return aboutMeText;
    }

    public void setAboutMeText(String aboutMeText) {
        this.aboutMeText = aboutMeText;
    }

    public String getInterestsText() {
        return interestsText;
    }

    public void setInterestsText(String interestsText) {
        this.interestsText = interestsText;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
