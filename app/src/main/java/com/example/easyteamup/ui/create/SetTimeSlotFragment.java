package com.example.easyteamup.ui.create;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;
import com.example.easyteamup.ui.create.CreateFragment;

import java.sql.Time;
import java.util.Date;

public class SetTimeSlotFragment extends Fragment {

    DatePicker datePicker = null;
    TimePicker timePicker = null;
    EditText duration = null;
    Button saveButton = null;

    public SetTimeSlotFragment() {
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
        View root = inflater.inflate(R.layout.fragment_set_time_slot, container, false);

        datePicker = (DatePicker) root.findViewById(R.id.datePicker);
        timePicker = (TimePicker) root.findViewById(R.id.timePicker);
        duration = (EditText)root.findViewById(R.id.timeSlotDurationView);
        saveButton = (Button) root.findViewById(R.id.dateTimeSet);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetTime(view);
            }
        });

        if (MainActivity.infoBundle.containsKey("clicked_slot")) {
            TimeSlot clickedSlot = (TimeSlot)MainActivity.infoBundle.getSerializable("clicked_slot");
            datePicker.updateDate(clickedSlot.getYear(), clickedSlot.getMonth(), clickedSlot.getDay());
            timePicker.setHour(clickedSlot.getHour());
            timePicker.setMinute(clickedSlot.getMinute());
            duration.setText(String.valueOf(clickedSlot.getDuration()));
            MainActivity.infoBundle.remove("clicked_slot");
        }

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClickSetTime(View view) {
        Boolean success = true;
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        String dur = duration.getText().toString();

        String date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        String time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        String datetime = date + " " + time;

        TimeSlot temp = new TimeSlot(datetime);
        Date current = new Date();
        if (current.after(temp.dateTimeAsDate())) {
            AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
            fail.setMessage("Time slot must be in future. Try again.");
            fail.setTitle("Error");
            fail.setPositiveButton("Close", null);
            fail.create().show();
            success = false;
        }
        if (dur.length() == 0) {
            duration.setError("Please enter the duration of your availability time slot.");
            success = false;
        }

        if (success) {
            TimeSlot timeslot = new TimeSlot(datetime, Integer.parseInt(dur));
            MainActivity.infoBundle.putSerializable("timeslot", timeslot);
            Fragment createFrag = new CreateFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, createFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else {
            AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
            fail.setMessage("Please fix errors and try again.");
            fail.setTitle("Error");
            fail.setPositiveButton("Close", null);
            fail.create().show();
        }
    }
}