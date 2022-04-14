package com.example.easyteamup.ui.shared;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.easyteamup.ui.EditEvent;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;
import com.example.easyteamup.ui.create.CreateFragment;

import java.util.Date;

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
        container.removeAllViews();
        View root = inflater.inflate(R.layout.fragment_set_due, container, false);

        datePicker = (DatePicker) root.findViewById(R.id.dateDuePicker);
        timePicker = (TimePicker) root.findViewById(R.id.timeDuePicker);
        saveButton = (Button) root.findViewById(R.id.dateTimeDueSet);
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
        Boolean success = true;
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        String date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        String time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        String datetime = date + " " + time;

        TimeSlot duetime = new TimeSlot(datetime);

        Date current = new Date();
        if (current.after(duetime.dateTimeAsDate())) {
            AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
            fail.setMessage("Due time must be in future. Try again.");
            fail.setTitle("Error");
            fail.setPositiveButton("Close", null);
            fail.create().show();
            success = false;
        }

        if (success) {
            MainActivity.infoBundle.putSerializable("duetime", duetime);
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
            AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
            fail.setMessage("Fix errors and try again.");
            fail.setTitle("Error");
            fail.setPositiveButton("Close", null);
            fail.create().show();
        }
    }
}