package com.example.easyteamup;

import android.util.Log;

import androidx.annotation.NonNull;

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

    public void removeUser(int ID) {
        DatabaseReference ref = rootRef.child(Integer.toString(ID));
        ref.removeValue();
        map.remove(Integer.toString(ID));
    }

    public User getUser(int ID) {
        return map.get(Integer.toString(ID));
    }

    public void editUser(User user) {
        DatabaseReference ref = rootRef.child(Integer.toString(user.getUserID()));
        ref.setValue(user);
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (Map.Entry<String, User> entry : map.entrySet()) {
            users.add(entry.getValue());
        }
        return users;
    }
}
