package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventTable {
    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private int nextID;
    private Map<String, Event> map;

    public EventTable() {
        this.database = FirebaseDatabase.getInstance();
        this.rootRef = this.database.getReference().child("events");
        this.nextID = 1;
        this.map = new HashMap<String, Event>();

        this.rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        Log.i("DATA", "ADDING");
                        Event event = child.getValue(Event.class);
                        map.put(Integer.toString(event.getEventID()), event);
                    }
                }
                else {
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public int addEvent(Event event) {
        DatabaseReference ref = rootRef.child(Integer.toString(this.nextID));
        event.setEventID(this.nextID);
        ref.setValue(event);
        map.put(Integer.toString(event.getEventID()), event);
        DatabaseReference listening = rootRef.child(Integer.toString(this.nextID));
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                map.put(Integer.toString(event.getEventID()), event);
            }
            public void onCancelled(DatabaseError dbError) {
                Log.e("Error", dbError.toString());
            }
        });
        return nextID++;
    }

    public void removeEvent(int ID) {
        DatabaseReference ref = rootRef.child(Integer.toString(ID));
        ref.removeValue();
        map.remove(Integer.toString(ID));
    }

    public Event getEvent(int ID) {
        return map.get(Integer.toString(ID));
    }

    public void editEvent(Event event) {
        DatabaseReference ref = rootRef.child(Integer.toString(event.getEventID()));
        ref.setValue(event);
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
        Log.i("DATA", "Events: " + map.size());
        ArrayList<String> namesList = new ArrayList<>();
        String[] names = {};
        for (Map.Entry<String, Event> entry : map.entrySet()) {
            namesList.add(entry.getValue().getEventName());
        }
        return namesList.toArray(names);
    }
}
