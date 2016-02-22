package com.techhaven.schooltweets.dataobjects;

/**
 * Created by Oluwayomi on 2/13/2016.
 */
public class ForumObject {
    private int id;
    private String roomName;
    private String datePosted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
