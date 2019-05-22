package com.android.group.model.firebase;

import java.util.List;

public final class Group {
    private final String groupName;
    private final String category;
    private final String iD;
    private final List<Object> userList;
    private final String description;
    private final int users;
    private final double lat;
    private final double lng;

    private Group(Builder builder) {
        this.groupName = builder.groupName;
        this.category = builder.category;
        this.iD = builder.iD;
        this.userList = builder.userList;
        this.description = builder.description;
        this.users = builder.users;
        this.lat = builder.lat;
        this.lng = builder.lng;
    }


    public static class Builder{
        //required params from user
        private final String groupName;
        private final String category;
        private final String description;

        //required internal params
        private int users = 0;
        private String iD = "Some Id";
        private double lat = 0;
        private double lng = 0;
        private List<Object> userList = null;

        //builder constructor
        public Builder(String groupName, String category, String description) {
            this.groupName = groupName;
            this.category = category;
            this.description = description;
        }

        public Builder setUsers(int users){
            this.users = users;
            return this;
        }

        public Builder setiD(String iD) {
            this.iD = iD;
            return this;
        }

        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLng(double lng) {
            this.lng = lng;
            return this;
        }

        public Builder setUserList(List<Object> userList) {
            this.userList = userList;
            return this;
        }

        public Group build(){
            return new Group(this);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCategory() {
        return category;
    }

    public String getiD() {
        return iD;
    }

    public List<Object> getUserList() {
        return userList;
    }

    public String getDescription() {
        return description;
    }

    public int getUsers() {
        return users;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", category='" + category + '\'' +
                ", iD='" + iD + '\'' +
                ", userList=" + userList +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
