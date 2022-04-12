package com.example.easyteamup.ui.shared;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.easyteamup.Event;
import com.example.easyteamup.EventTable;
import com.example.easyteamup.Location;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.OnIntegerChangeListener;
import com.example.easyteamup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

// working as of night 3/27
public class MapsFragment extends Fragment{

    private GoogleMap map;
    String[] eventNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // init view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // init map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // async map
        EventTable et = new EventTable();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                ArrayList<Event> allEvents;
                allEvents = et.getAllEvents();
                for (int i = 0; i < allEvents.size(); i++)
                {
                    Location loc = allEvents.get(i).getLocation();
                    if (loc != null) {
                        String title = allEvents.get(i).getEventName();
                        double longitude = loc.getLongitude();
                        double latitude = loc.getLatitude();
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
                        marker.setTag(marker.getPosition());
                    }
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            LatLng position = (LatLng) marker.getTag();
                            eventNames = et.getAllEventNames();
                            int pos = 0;
                            for (int i = 0; i < allEvents.size(); i++)
                            {
                                if (allEvents.get(i).getLocation() != null)
                                {
                                    if (position.latitude == allEvents.get(i).getLocation().getLatitude())
                                    {
                                        pos = i;
                                    }
                                }
                            }
                            MainActivity.infoBundle.putInt("eventID", allEvents.get(pos).getEventID());

                            Fragment editFrag = new DetailsFragment();
                            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            //Using position get Value from arraylist
                            return false;
                        }
                    });
                }
            }
        });
        et.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                System.out.println("new value " + newValue);
                EventTable et = new EventTable();
                ArrayList<Event> allEvents;
                allEvents = et.getAllEvents();
                System.out.println(et.size());
                SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        EventTable et = new EventTable();
                        ArrayList<Event> allEvents;
                        allEvents = et.getAllEvents();
                        for (int i = 0; i < allEvents.size(); i++)
                        {
                            Location loc = allEvents.get(i).getLocation();
                            if (loc != null) {
                                String title = allEvents.get(i).getEventName();
                                double longitude = loc.getLongitude();
                                double latitude = loc.getLatitude();
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
                                marker.setTag(marker.getPosition());
                            }
                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    LatLng position = (LatLng) marker.getTag();
                                    eventNames = et.getAllEventNames();
                                    int pos = 0;
                                    for (int i = 0; i < allEvents.size(); i++)
                                    {
                                        if (allEvents.get(i).getLocation() != null)
                                        {
                                            if (position.latitude == allEvents.get(i).getLocation().getLatitude())
                                            {
                                                pos = i;
                                            }
                                        }
                                    }
                                    MainActivity.infoBundle.putInt("eventID", allEvents.get(pos).getEventID());

                                    Fragment editFrag = new DetailsFragment();
                                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                    //Using position get Value from arraylist
                                    return false;
                                }
                            });
                        }
                    }
                });
            }
        });
        return view;
    }

}
