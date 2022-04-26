package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.easyteamup.ui.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationHandler {
    private boolean edit;
    private boolean constructing;
    private int editCounter;
    private Event event;
    private User user;

    public NotificationHandler() {
        this.edit = false;
        this.constructing = true;
        editCounter = 0;
        this.user = MainActivity.userTable.getUser(MainActivity.userID);
    }

    public void editListener(Event event) {
        this.event = event;
        this.edit = true;
        this.constructing = true;
        editEventName();
        editEventDescription();
        editDueTime();
        editLocation();
        editStatusPublic();
        editTimeOptions();
    }

    public boolean getEditListener() {
        return this.edit;
    }

    public void sendEditNotif() {
        Notification notification = new Notification(event.getEventID(), MainActivity.userID, 1);
        ArrayList<Notification> notifications = user.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        user.setNotifications(notifications);
        MainActivity.userTable.editUser(user);
    }

    public void sendWithdrawNotif() {
        User owner = MainActivity.userTable.getUser(event.getOwner());
        Notification notification = new Notification(event.getEventID(), event.getOwner(), 2);
        ArrayList<Notification> notifications = owner.getNotifications();
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        owner.setNotifications(notifications);
        MainActivity.userTable.editUser(owner);
    }

    public void editEventName() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("eventName");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editEventDescription() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("description");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editDueTime() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("dueTime");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editLocation() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("location");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editStatusPublic() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("statusPublic");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editTimeOptions() {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("timeOptions");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!constructing) {
                    sendEditNotif();
                }
                else {
                    editCounter++;
                    if (editCounter == 6) {
                        constructing = false;
                        editCounter = 0;
                    }
                    else {
                        constructing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }
}
