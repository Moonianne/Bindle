package org.pursuit.firebasetools.model;

public final class User {
    private String displayName;
    private String email;
    private String aboutMe;
    private String interests;
    private String currentZone;
    private String currentGroup;

    public User() {
    }

    public User(String displayName, String email, String aboutMe, String interests, String currentZone, String currentGroup) {
        this.displayName = displayName;
        this.email = email;
        this.aboutMe = aboutMe;
        this.interests = interests;
        this.currentZone = currentZone;
        this.currentGroup = currentGroup;
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
