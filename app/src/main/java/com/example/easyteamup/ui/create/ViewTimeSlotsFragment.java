package com.example.easyteamup.ui.create;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.easyteamup.ui.EditEvent;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;

import java.util.ArrayList;

public class ViewTimeSlotsFragment extends Fragment {

    ListView timeSlotsList = null;
    ArrayAdapter listAdapter = null;

    public ViewTimeSlotsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View root = inflater.inflate(R.layout.fragment_view_time_slots, container, false);

        String[] timeSlotsStringArray = MainActivity.infoBundle.getStringArray("timeOptionsArray");

        timeSlotsList = (ListView)root.findViewById(R.id.timeSlotsList);
        listAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, timeSlotsStringArray);
        timeSlotsList.setAdapter(listAdapter);
        timeSlotsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                ArrayList<TimeSlot> slots = (ArrayList<TimeSlot>) MainActivity.infoBundle.getSerializable("temp_time_options");
                if (slots.size() > 0) {
                    TimeSlot deleteSlot = slots.get(position);
                    MainActivity.infoBundle.putSerializable("delete_slot", deleteSlot);

                    if (MainActivity.infoBundle.getBoolean("editing")) {
                        Fragment editFrag = new EditEvent();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    else {
                        Fragment createFrag = new CreateFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else {
                    Toast.makeText(getContext(), "An error occurred. Please try again later.", Toast.LENGTH_LONG);
                }
            }
        });

        Button addButton = (Button)root.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotsList.getAdapter().getCount() < 3) {
                    Fragment setTimeFrag = new SetTimeSlotFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, setTimeFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
                    fail.setMessage("Only 3 time slots allowed. Please delete one time slot to create a new availability.");
                    fail.setTitle("Error");
                    fail.setPositiveButton("Close", null);
                    fail.create().show();
                }
            }
        });

        Button backButton = (Button)root.findViewById(R.id.backTimeSlotViewButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.infoBundle.getBoolean("editing")) {
                    Fragment editFrag = new EditEvent();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Fragment createFrag = new CreateFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}