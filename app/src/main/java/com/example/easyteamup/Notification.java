package com.example.easyteamup;

import java.io.Serializable;

public class Notification implements Serializable {
    private int eventID;
    private int userID;
    private int type;

    public Notification() {

    }

    public Notification(int eventID, int userID, int type) {
        this.eventID = eventID;
        this.userID = userID;
        this.type = type;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
