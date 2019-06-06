package org.pursuit.firebasetools.model;

public final class Message {
    private long timeStamp;
    private String iD;
    private String userName;
    private String messageText;
    private String photoUrl;

    public Message() {
    }

    public Message(long timeStamp, String userName, String messageText, String photoUrl) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.messageText = messageText;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
