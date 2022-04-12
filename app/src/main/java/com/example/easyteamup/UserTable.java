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

public class UserTable {
    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private int nextID;
    private Map<String, User> map;

    public UserTable() {
        this.database = FirebaseDatabase.getInstance();
        this.rootRef = this.database.getReference().child("users");
        this.nextID = 1;
        this.map = new HashMap<String, User>();

        this.rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int lastID = 0;
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        User user = child.getValue(User.class);
                        map.put(Integer.toString(user.getUserID()), user);
                        if (user.getUserID() > lastID) {
                            lastID = user.getUserID();
                        }
                    }
                    nextID = lastID + 1;
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
                    User user = child.getValue(User.class);
                    if (!map.containsKey(Integer.toString(user.getUserID()))) {
                        map.put(Integer.toString(user.getUserID()), user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public UserTable(boolean testing) {
        this.nextID = 1;
        this.map = new HashMap<String, User>();
    }

    public int addUser(User user) {
        DatabaseReference ref = rootRef.child(Integer.toString(this.nextID));
        user.setUserID(this.nextID);
        ref.setValue(user);
        map.put(Integer.toString(user.getUserID()), user);
        DatabaseReference listening = rootRef.child(Integer.toString(this.nextID));
        listening.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                map.put(Integer.toString(user.getUserID()), user);
            }
            public void onCancelled(DatabaseError dbError) {
                Log.e("Error", dbError.toString());
            }
        });
        return nextID++;
    }

    public int addUser(User user, boolean testing) {
        user.setUserID(this.nextID);
        map.put(Integer.toString(user.getUserID()), user);
        return nextID++;
    }

    public void removeUser(String username) {
        this.rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        User user = child.getValue(User.class);
                        if (user.getUsername().compareTo(username) == 0) {
                            rootRef.child(Integer.toString(user.getUserID())).getRef().removeValue();
                        }
                    }
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    public User getUser(int ID) {
        return map.get(Integer.toString(ID));
    }

    public void editUser(User user) {
        DatabaseReference ref = rootRef.child(Integer.toString(user.getUserID()));
        ref.setValue(user);
    }

    public void editUser(User user, boolean testing) {
        map.put(Integer.toString(user.getUserID()), user);
    }

    public User contains(String username) {
        for (Map.Entry<String, User> entry : map.entrySet()) {
            if (entry.getValue().getUsername().compareTo(username) == 0) {
                return entry.getValue();
            }
        }
        return null;
    }

    public User containsEmail(String email) {
        for (Map.Entry<String, User> entry : map.entrySet()) {
            if (entry.getValue().getEmail().compareTo(email) == 0) {
                return entry.getValue();
            }
        }
        return null;
    }
}
