package com.example.easyteamup;

import com.example.easyteamup.ui.MainActivity;

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

    public String getMessage () {
        if (MainActivity.eventTable.getEvent(eventID) == null) {
            return "";
        }
        String message = "";
        if (type == 1) {
            message = "\"" + MainActivity.eventTable.getEvent(eventID).getEventName() + "\" has been edited.";
        }
        else if (type == 2) {
            message = "A participant has withdrawn from your event \"" + MainActivity.eventTable.getEvent(eventID).getEventName() + "\"";
        }
        else if (type == 3) {
            message = "A final event time has been generated for \"" + MainActivity.eventTable.getEvent(eventID).getEventName() + "\"";
        }
        return message;
    }
}
