package org.pursuit.zonechat.model;

public final class UserState {

    //TODO: I made this but i don't remember what we needed it for
    private String group;
    private String zone;

    public UserState() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
