package org.pursuit.zonechat.model;

public final class Message {
    private String name;
    private String message;
    private String id;
    private long timeStamp;

    public Message() {
    }

    public Message(String name, String message, String id, long timeStamp) {
        this.name = name;
        this.message = message;
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
