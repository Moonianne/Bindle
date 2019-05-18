package org.pursuit.zonechat.model;

public final class ZoneChatRoom {
    private String name;
    private String lastMessage;
    private long timeStamp;

    public ZoneChatRoom() {
    }

    public ZoneChatRoom(String name, String lastMessage, long timeStamp) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
