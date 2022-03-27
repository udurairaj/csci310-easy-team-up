package com.example.easyteamup.ui.shared;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.ui.create.CreateFragment;

public class SetDueFragment extends Fragment {

    DatePicker datePicker = null;
    TimePicker timePicker = null;
    Button saveButton = null;

    public SetDueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_set_due, container, false);

        datePicker = (DatePicker) root.findViewById(R.id.datePicker);
        timePicker = (TimePicker) root.findViewById(R.id.timePicker);
        saveButton = (Button) root.findViewById(R.id.dateTimeSet);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetDueTime(view);
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClickSetDueTime(View view) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        String date = month + "/" + day + "/" + year;
        String time = "";
        if (hour < 12) {
            time = hour + ":" + min + "AM";
        }
        else if (hour == 12) {
            time = hour + ":" + min + "PM";
        }
        else {
            time = (hour-12) + ":" + min + "PM";
        }
        String datetime = date + " " + time;

        MainActivity.infoBundle.putString("duetime", datetime);
        Fragment createFrag = new CreateFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}