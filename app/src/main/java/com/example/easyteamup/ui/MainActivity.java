package com.example.easyteamup.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.easyteamup.EventTable;
import com.example.easyteamup.Notification;
import com.example.easyteamup.NotificationHandler;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.UserTable;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyteamup.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static int userID = 0;
    public static Bundle infoBundle = new Bundle();
    public static UserTable userTable = new UserTable();
    public static EventTable eventTable = null;
    private NotificationChannel channel = null;
    private NotificationManager manager = null;
    private int notifCount = 0;
    private String lastNotif = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        User user = (User) i.getSerializableExtra("user");
        infoBundle.putSerializable("user", user);
        userID = user.getUserID();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Easy Team Up");

        //Fragment fragment = new MapFragment();
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_create, R.id.nav_userEventDisplay)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        channel = new NotificationChannel("channel",
                "notifications", NotificationManager.IMPORTANCE_DEFAULT);
        manager = (NotificationManager) getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        eventTable = new EventTable();

        FirebaseDatabase.getInstance().getReference().child("users").child(Integer.toString(userID))
                .child("notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("NOTIFY", "data change");
                ArrayList<Notification> notifications = new ArrayList<>();
                for (DataSnapshot notif : snapshot.getChildren()) {
                    Notification notification = notif.getValue(Notification.class);
                    notifications.add(notification);
                }
                User tempUser = userTable.getUser(userID);
                tempUser.setNotifications(notifications);
                if (tempUser.getNotifications() != null) {
                    Log.i("NOTIFY", "user notifs not null");
                    while (tempUser.getNotifications().size() > 0) {
                        Log.i("NOTIFY", "notifs size > 0");
                        if (tempUser.getNotifications().get(0).getMessage() != lastNotif) {
                            Notification notification = tempUser.getNotifications().get(0);
                            tempUser.getNotifications().remove(0);
                            if (notification.getMessage() != lastNotif) {
                                sendNotification(notification);
                                lastNotif = notification.getMessage();
                            }
                            Log.i("NOTIFY", "sent");
                        }
                        else {
                            Notification notification = tempUser.getNotifications().get(0);
                            tempUser.getNotifications().remove(0);
                            Log.i("NOTIFY", "duplicate");
                        }
                    }
                }
                userTable.editUser(tempUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void sendNotification(Notification notification) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        if (notification.getMessage() == "") {
            return;
        }

        NotificationCompat.Builder notif = new NotificationCompat.Builder
                (this, "channel")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("Easy Team Up")
                .setContentText(notification.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        nm.notify(notifCount++, notif.build());
    }

}