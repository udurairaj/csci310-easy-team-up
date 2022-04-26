package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.easyteamup.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;

public class Event implements Serializable {

    private int eventID;
    private int owner;
    private String eventName;
    private Boolean statusPublic;
    private String description;
    private ArrayList<Integer> participants;
    private TimeSlot dueTime;
    private ArrayList<Integer> invitees;
    private ArrayList<TimeSlot> timeOptions;
    private Location location;
    private TimeSlot finalTime;
    private NotificationHandler notificationHandler;
    private Timer timer = new Timer();
//    private TimeGenerator generator;

    public Event() {
        if (this.notificationHandler == null) {
            this.notificationHandler = new NotificationHandler();
        }
    }

    public Event(int owner, String eventName, Boolean statusPublic) {
        this.owner = owner;
        this.eventName = eventName;
        this.statusPublic = statusPublic;
        this.description = null;
        this.invitees = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.dueTime = null;
        this.timeOptions = new ArrayList<>();
        this.notificationHandler = new NotificationHandler();
        this.location = null;
        this.finalTime = null;
//        this.generator = new TimeGenerator(this);
    }

    public int getEventID() { return eventID; }
    public int getOwner() { return owner; }
    public String getEventName() { return eventName; }
    public Boolean getStatusPublic() { return statusPublic; }
    public String getDescription() { return description; }
    public ArrayList<Integer> getParticipants() { return participants; }
    public TimeSlot getDueTime() { return dueTime; }
    public ArrayList<Integer> getInvitees() { return invitees; }
    public ArrayList<TimeSlot> getTimeOptions() { return timeOptions; }
    public NotificationHandler getNotificationHandler() { return notificationHandler; }
    public Location getLocation() { return location; }
    public TimeSlot getFinalTime() { return finalTime; }

    public void setEventID(int eventID) { this.eventID = eventID; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public void setStatusPublic(Boolean statusPublic) { this.statusPublic = statusPublic; }
    public void setDescription(String description) { this.description = description; }
    public void setDueTime(TimeSlot dueTime) {
        this.dueTime = dueTime;
        timer.schedule(new TimeGenerator(this), dueTime.dateTimeAsDate());
    }
    public void setInvitees(ArrayList<Integer> list) { this.invitees = list; }
    public void setLocation(Location location) { this.location = location; }

    public void addParticipant(User user) {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        participants.add(user.getUserID());

        notificationHandler.editListener(this);
    }

    public void removeParticipant(User user) {
        if (participants == null) {
            return;
        }
        if (!participants.contains(user.getUserID())) {
            return;
        }
        participants.remove(participants.indexOf(user.getUserID()));

        notificationHandler.sendWithdrawNotif();
    }

    public void removeInvitee(User user) {
        if (invitees == null) {
            return;
        }
        if (!invitees.contains(user.getUserID())) {
            return;
        }
        invitees.remove(invitees.indexOf(user.getUserID()));
    }

    public void invite(User user) {
        if (invitees == null) {
            invitees = new ArrayList<>();
        }
        if (invitees.contains(user.getUserID())) {
            return;
        }
        invitees.add(user.getUserID());
    }

    public void setTimeOptions(ArrayList<TimeSlot> list) { this.timeOptions = list; }


    public void generateFinalTime() {
//        finalTime = generator.generate();
        // FEATURE 1: notification to participants
    }

}
