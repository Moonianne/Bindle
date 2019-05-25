package org.pursuit.firebasetools.model;

public final class Message {
    private long timeStamp;
    private String userName;
    private String messageText;

    public Message() {
    }

    public Message(long timeStamp, String userName, String messageText) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.messageText = messageText;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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
