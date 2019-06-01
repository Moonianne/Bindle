package org.pursuit.firebasetools.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.List;

public final class Group implements Serializable {
    private List<User> userList;
    private String description;
    private String category;
    private LatLng location;
    private String chatName;
    private String title;
    private int userCount;
    private String address;
    private String image_url;
    private String buiness_name;


    public Group() {
    }

    public Group(List<User> userList, String description, String category, LatLng location, String chatName, String title, int userCount, String address, String image_url, String buiness_name) {
        this.userList = userList;
        this.description = description;
        this.category = category;
        this.location = location;
        this.chatName = chatName;
        this.title = title;
        this.userCount = userCount;
        this.address = address;
        this.image_url = image_url;
        this.buiness_name = buiness_name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getAddress() {
        return address;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getBuiness_name() {
        return buiness_name;
    }
}
