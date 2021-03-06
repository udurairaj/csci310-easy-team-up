package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.easyteamup.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class EventTable {
    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private int nextID;
    private Map<String, Event> map;
    private OnIntegerChangeListener listener;
    private Timer timer = null;

    public EventTable() {
        this.database = FirebaseDatabase.getInstance();
        this.rootRef = this.database.getReference().child("events");
        this.nextID = 1;
        this.map = new HashMap<String, Event>();

        this.rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int lastID = 0;
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        Event event = child.getValue(Event.class);
                        map.put(Integer.toString(event.getEventID()), event);
                        DatabaseReference listening = rootRef.child(Integer.toString(event.getEventID()));
                        listening.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Event event = snapshot.getValue(Event.class);
                                if (event == null) {
                                    return;
                                }
                                map.put(Integer.toString(event.getEventID()), event);
                                if (listener != null) {
                                    listener.onIntegerChanged(map.size());
                                }
                                if (event.getParticipants() != null) {
                                    if (event.getParticipants().contains(MainActivity.userID)) {
                                        if (!event.getNotificationHandler().getEditListener()) {
                                            event.getNotificationHandler().editListener(event);
                                        }
                                    }
                                }
                            }

                            public void onCancelled(DatabaseError dbError) {
                                Log.e("Error", dbError.toString());
                            }
                        });
                        if (event.getEventID() > lastID) {
                            lastID = event.getEventID();
                        }
                        if (listener != null) {
                            listener.onIntegerChanged(map.size());
                        }
                        if (event.getParticipants() != null) {
                            if (event.getParticipants().contains(MainActivity.userID)) {
                                if (!event.getNotificationHandler().getEditListener()) {
                                    event.getNotificationHandler().editListener(event);
                                }
                            }
                        }
                        if (event.getDueTime() != null) {
                            Date now = new Date();
                            if (!now.after(event.getDueTime().dateTimeAsDate())) {
                                if (timer == null) {
                                    timer = new Timer();
                                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                } else {
                                    timer.cancel();
                                    timer = new Timer();
                                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                }
                            }
                        }
                    }
                    nextID = lastID + 1;
                } else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

        this.rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Event event = child.getValue(Event.class);
                    if (!map.containsKey(Integer.toString(event.getEventID()))) {
                        map.put(Integer.toString(event.getEventID()), event);
                        if (listener != null) {
                            listener.onIntegerChanged(map.size());
                        }
                        if (event.getParticipants() != null) {
                            if (event.getParticipants().contains(MainActivity.userID)) {
                                if (!event.getNotificationHandler().getEditListener()) {
                                    event.getNotificationHandler().editListener(event);
                                }
                            }
                        }
                        if (event.getDueTime() != null) {
                            Date now = new Date();
                            if (!now.after(event.getDueTime().dateTimeAsDate())) {
                                if (timer == null) {
                                    timer = new Timer();
                                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                } else {
                                    timer.cancel();
                                    timer = new Timer();
                                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                }
                            }
                        }
                    }
                    DatabaseReference listening = rootRef.child(Integer.toString(event.getEventID()));
                    listening.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("INFO", "created event changed");
                            Event event = snapshot.getValue(Event.class);
                            if (event == null) {
                                return;
                            }
                            map.put(Integer.toString(event.getEventID()), event);
                            if (listener != null) {
                                listener.onIntegerChanged(map.size());
                            }
                            if (event.getDueTime() != null) {
                                Date now = new Date();
                                if (!now.after(event.getDueTime().dateTimeAsDate())) {
                                    if (timer == null) {
                                        timer = new Timer();
                                        timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                    } else {
                                        timer.cancel();
                                        timer = new Timer();
                                        timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                                    }
                                }
                            }
                        }

                        public void onCancelled(DatabaseError dbError) {
                            Log.e("Error", dbError.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public EventTable(boolean testing) {
        this.nextID = 1;
        this.map = new HashMap<String, Event>();
    }

    public void setOnIntegerChangeListener(OnIntegerChangeListener listener) {
        this.listener = listener;
    }

    public int addEvent(Event event) {
        if (event.getDueTime() != null) {
            Date now = new Date();
            if (!now.after(event.getDueTime().dateTimeAsDate())) {
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                } else {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                }
            }
        }
        DatabaseReference ref = rootRef.child(Integer.toString(this.nextID));
        event.setEventID(this.nextID);
        ref.setValue(event);
        map.put(Integer.toString(event.getEventID()), event);
        DatabaseReference listening = rootRef.child(Integer.toString(this.nextID));
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("INFO", "created event changed");
                Event event = snapshot.getValue(Event.class);
                if (event == null) {
                    return;
                }
                map.put(Integer.toString(event.getEventID()), event);
                if (listener != null) {
                    listener.onIntegerChanged(map.size());
                }
                if (event.getDueTime() != null) {
                    Date now = new Date();
                    if (!now.after(event.getDueTime().dateTimeAsDate())) {
                        if (timer == null) {
                            timer = new Timer();
                            timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                        } else {
                            timer.cancel();
                            timer = new Timer();
                            timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError dbError) {
                Log.e("Error", dbError.toString());
            }
        });
        if (listener != null) {
            listener.onIntegerChanged(map.size());
        }
        return nextID++;
    }

    public int addEvent(Event event, boolean testing) {
        event.setEventID(this.nextID);
        map.put(Integer.toString(event.getEventID()), event);
        return nextID++;
    }

    public void removeEvent(int ID) {
        DatabaseReference ref = rootRef.child(Integer.toString(ID));
        ref.removeValue();
        map.remove(Integer.toString(ID));
        if (listener != null) {
            listener.onIntegerChanged(map.size());
        }
    }

    public void removeEvent(String name) {
        this.rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        Event event = child.getValue(Event.class);
                        if (event.getEventName().compareTo(name) == 0) {
                            rootRef.child(Integer.toString(event.getEventID())).getRef().removeValue();
                        }
                    }
                } else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
        if (listener != null) {
            listener.onIntegerChanged(map.size());
        }
    }

    public void removeEvent(int ID, boolean testing) {
        map.remove(Integer.toString(ID));
    }

    public Event getEvent(int ID) {
        return map.get(Integer.toString(ID));
    }

    public Event getEvent(String name) {
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getEventName().compareTo(name) == 0) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void editEvent(Event event) {
        DatabaseReference ref = rootRef.child(Integer.toString(event.getEventID()));
        ref.setValue(event);
        if (event.getDueTime() != null) {
            Date now = new Date();
            if (!now.after(event.getDueTime().dateTimeAsDate())) {
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                } else {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimeGenerator(event), event.getDueTime().dateTimeAsDate());
                }
            }
        }
    }

    public void editEvent(Event event, boolean testing) {
        map.put(Integer.toString(event.getEventID()), event);
    }

    public int size() {
        return this.map.size();
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            events.add(entry.getValue());
        }
        return events;
    }

    public String[] getAllEventNames() {
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            namesList.add(entry.getValue().getEventName());
        }
        return namesList.toArray(names);
    }

    public String[] getOwnEvents() {
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getOwner() == MainActivity.userID) {
                TimeSlot finalTime = entry.getValue().getFinalTime();
                Date now = new Date();
                if (finalTime == null || finalTime.dateTimeAsDate() == null
                        || finalTime.dateTimeAsDate().after(now)) {
                    namesList.add(entry.getValue().getEventName());
                }
            }
        }
        return namesList.toArray(names);
    }

    public ArrayList<Integer> getOwnEventIDs() {
        ArrayList<Integer> IDList = new ArrayList<>();
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getOwner() == MainActivity.userID) {
                TimeSlot finalTime = entry.getValue().getFinalTime();
                Date now = new Date();
                if (finalTime == null || finalTime.dateTimeAsDate() == null
                        || finalTime.dateTimeAsDate().after(now)) {
                    IDList.add(entry.getValue().getEventID());
                }
            }
        }
        return IDList;
    }

    public String[] getInvitedEvents() {
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getInvitees() != null) {
                if (entry.getValue().getInvitees().contains(MainActivity.userID)) {
                    if (entry.getValue().getParticipants() == null ||
                            !entry.getValue().getParticipants().contains(MainActivity.userID)) {
                        TimeSlot finalTime = entry.getValue().getFinalTime();
                        Date now = new Date();
                        if (finalTime == null || finalTime.dateTimeAsDate() == null
                                || finalTime.dateTimeAsDate().after(now)) {
                            namesList.add(entry.getValue().getEventName());
                        }
                    }
                }
            }
        }
        return namesList.toArray(names);
    }

    public ArrayList<Integer> getInvitedEventIDs() {
        ArrayList<Integer> IDList = new ArrayList<>();
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getInvitees() != null) {
                if (entry.getValue().getInvitees().contains(MainActivity.userID)) {
                    if (entry.getValue().getParticipants() == null ||
                            !entry.getValue().getParticipants().contains(MainActivity.userID)) {
                        TimeSlot finalTime = entry.getValue().getFinalTime();
                        Date now = new Date();
                        if (finalTime == null || finalTime.dateTimeAsDate() == null
                                || finalTime.dateTimeAsDate().after(now)) {
                            IDList.add(entry.getValue().getEventID());
                        }
                    }
                }
            }
        }
        return IDList;
    }

    public String[] getJoinedEvents() {
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getParticipants() != null) {
                if (entry.getValue().getParticipants().contains(MainActivity.userID)) {
                    TimeSlot finalTime = entry.getValue().getFinalTime();
                    Date now = new Date();
                    if (finalTime == null || finalTime.dateTimeAsDate() == null
                            || finalTime.dateTimeAsDate().after(now)) {
                        namesList.add(entry.getValue().getEventName());
                    }
                }
            }
        }
        return namesList.toArray(names);
    }

    public ArrayList<Integer> getJoinedEventIDs() {
        ArrayList<Integer> IDList = new ArrayList<>();
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            if (entry.getValue().getParticipants() != null) {
                if (entry.getValue().getParticipants().contains(MainActivity.userID)) {
                    TimeSlot finalTime = entry.getValue().getFinalTime();
                    Date now = new Date();
                    if (finalTime == null || finalTime.dateTimeAsDate() == null
                            || finalTime.dateTimeAsDate().after(now)) {
                        IDList.add(entry.getValue().getEventID());
                    }
                }
            }
        }
        return IDList;
    }

    public String[] getPastEvents() {
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            TimeSlot finalTime = entry.getValue().getFinalTime();
            Date now = new Date();
            if (finalTime != null && finalTime.dateTimeAsDate() != null
                    && now.after(finalTime.dateTimeAsDate())) {
                if (entry.getValue().getOwner() == MainActivity.userID) {
                    namesList.add(entry.getValue().getEventName());
                }
                else if (entry.getValue().getInvitees() != null) {
                    if (entry.getValue().getInvitees().contains(MainActivity.userID)) {
                        namesList.add(entry.getValue().getEventName());
                    }
                }
                else if (entry.getValue().getParticipants() != null) {
                    if (entry.getValue().getParticipants().contains(MainActivity.userID)) {
                        namesList.add(entry.getValue().getEventName());
                    }
                }
            }
        }
        return namesList.toArray(names);
    }

    public ArrayList<Integer> getPastEventIDs() {
        ArrayList<Integer> IDList = new ArrayList<>();
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            TimeSlot finalTime = entry.getValue().getFinalTime();
            Date now = new Date();
            if (finalTime != null && finalTime.dateTimeAsDate() != null
                    && now.after(finalTime.dateTimeAsDate())) {
                if (entry.getValue().getOwner() == MainActivity.userID) {
                    IDList.add(entry.getValue().getEventID());
                }
                else if (entry.getValue().getInvitees() != null) {
                    if (entry.getValue().getInvitees().contains(MainActivity.userID)) {
                        IDList.add(entry.getValue().getEventID());
                    }
                }
                else if (entry.getValue().getParticipants() != null) {
                    if (entry.getValue().getParticipants().contains(MainActivity.userID)) {
                        IDList.add(entry.getValue().getEventID());
                    }
                }
            }
        }
        return IDList;
    }
}
