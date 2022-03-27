package com.example.easyteamup;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

    private int eventID;
    private int owner;
    private String eventName;
    private Boolean statusPublic;
    private String description;
    private ArrayList<Integer> participants;
    private ArrayList<Integer> invitees;
//    private ArrayList<TimeSlot> timeOptions;
//    private NotificationHandler notificationHandler;
//    private Location location;
//    private TimeSlot finalTime;
//    private TimeGenerator generator;

    public Event() {

    }

    public Event(int owner, String eventName, Boolean statusPublic) {
        this.owner = owner;
        this.eventName = eventName;
        this.statusPublic = statusPublic;
        this.description = null;
        this.invitees = new ArrayList<>();
        this.participants = new ArrayList<>();
//        this.timeOptions = new ArrayList<>();
//        this.notificationHandler = null;
//        this.location = null;
//        this.finalTime = null;
//        this.generator = new TimeGenerator(this);
    }

    public int getEventID() { return eventID; }
    public int getOwner() { return MainActivity.userTable.getUser(owner).getUserID(); }
    public String getEventName() { return eventName; }
    public Boolean getStatusPublic() { return statusPublic; }
    public String getDescription() { return description; }
    public ArrayList<Integer> getParticipants() { return participants; }
    public ArrayList<Integer> getInvitees() { return invitees; }
//    public ArrayList<TimeSlot> getTimeOptions() { return timeOptions; }
//    public NotificationHandler getNotificationHandler() { return notificationHandler; }
//    public Location getLocation() { return location; }
//    public TimeSlot getFinalTime() { return finalTime; }

    public void setEventID(int eventID) { this.eventID = eventID; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public void setStatusPublic(Boolean statusPublic) { this.statusPublic = statusPublic; }
    public void setDescription(String description) { this.description = description; }
//    public void setLocation(Location location) { this.location = location; }

    public void addParticipant(User user) { participants.add(user.getUserID()); }
//    public void addInvitee(int userID) { invitees.add(userID); }
//    public void addTimeSlot(TimeSlot t) { timeOptions.add(t); }
//    public void removeTimeSlot(TimeSlot t) { timeOptions.remove(t); }

    public void generateFinalTime() {
//        finalTime = generator.generate();
        // FEATURE 1: notification to participants
    }

    public void invite(User user) {
        // FEATURE 2
    }

}
