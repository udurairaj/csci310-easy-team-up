package com.example.easyteamup.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.easyteamup.EventTable;
import com.example.easyteamup.NotificationHandler;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.UserTable;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.navigation.NavigationView;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static int userID = 0;
    public static Bundle infoBundle = new Bundle();
    public static UserTable userTable = new UserTable();
    public static EventTable eventTable = new EventTable();
    public static NotificationHandler handler = new NotificationHandler();
    private NotificationChannel channel = null;
    private NotificationManager manager = null;
    private int notifCount = 0;

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

        channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID,
                "notifications", NotificationManager.IMPORTANCE_DEFAULT);
        manager = (NotificationManager) getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
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

    public void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Easy Team Up")
                .setContentText("TEST NOTIFICATION")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(notifCount++, builder.build());
    }

}