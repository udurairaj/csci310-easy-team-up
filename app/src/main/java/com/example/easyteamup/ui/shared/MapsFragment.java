package com.example.easyteamup.ui.shared;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.easyteamup.Event;
import com.example.easyteamup.EventTable;
import com.example.easyteamup.Location;
import com.example.easyteamup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment{

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // init view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // init map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                // when map is loaded
//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        // when clicked on map, init marker options
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        // set pos of marker
//                        markerOptions.position(latLng);
//                        // set title of marker
//                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//                        // remove all markers
//                        googleMap.clear();
//                        // animating to zoom the marker
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                        // add marker on map
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
                EventTable et = new EventTable();
                ArrayList<Event> allEvents;
                allEvents = et.getAllEvents();
//                for (int i = 0; i < allEvents.size(); i++)
//                {
//                    Location loc = allEvents.get(i).getLocation();
//                    String title = allEvents.get(i).getEventName();
//                    double longitude = loc.getLongitude();
//                    double latitude = loc.getLatitude();
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
//                }
            }
        });

        return view;
    }

    // Get a handle to the GoogleMap object and display marker.
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));
//    }
}