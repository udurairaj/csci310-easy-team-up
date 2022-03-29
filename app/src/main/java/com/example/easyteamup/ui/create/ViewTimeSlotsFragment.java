package com.example.easyteamup.ui.create;

import android.os.Bundle;

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

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;

import java.util.ArrayList;

public class ViewTimeSlotsFragment extends Fragment {

    ListView timeSlotsList = null;
    ArrayAdapter listAdapter = null;
    ArrayAdapter listAdapterSub = null;

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
                    Fragment createFrag = new CreateFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
                Fragment setTimeFrag = new SetTimeSlotFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, setTimeFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button backButton = (Button)root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment createFrag = new CreateFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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