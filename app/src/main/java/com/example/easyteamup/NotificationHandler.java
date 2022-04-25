package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationHandler {
    public NotificationHandler() {

    }

    public void editListener(Event event) {
        editEventName(event);
        editEventDescription(event);
        editDueTime(event);
        editLocation(event);
        editStatusPublic(event);
        editTimeOptions(event);
    }

    public void editEventName(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("eventName");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editEventDescription(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("description");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editDueTime(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("dueTime");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editLocation(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("location");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editStatusPublic(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("statusPublic");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void editTimeOptions(Event event) {
        DatabaseReference listening = FirebaseDatabase.getInstance().getReference().child("events")
                .child(Integer.toString(event.getEventID())).child("timeOptions");
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "name changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }
}
