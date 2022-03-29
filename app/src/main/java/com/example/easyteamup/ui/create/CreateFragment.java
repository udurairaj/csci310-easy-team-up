package com.example.easyteamup.ui.create;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.Event;
import com.example.easyteamup.Location;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentCreateBinding;
import com.example.easyteamup.ui.shared.DetailsFragment;
import com.example.easyteamup.ui.shared.SetDueFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private User user = null;
    private User searchedInviteUser = null;
    private Event eventInProgress = null;
    Boolean createSuccess = true;

    EditText nameCreate = null;
    EditText descriptionCreate = null;
    Spinner statusPublicSpinner = null;
    androidx.appcompat.widget.SearchView locationCreate = null;
    TextView dueTimeTextView = null;
    androidx.appcompat.widget.SearchView inviteCreate = null;
    ArrayList<Integer> invitedUsersTemp = new ArrayList<>();
    TextView displayInvitedUserButton = null;
    Button viewInvitedUsersButton = null;
    TimeSlot duetime = null;
    Location location = null;
    Button timeSlotsButton = null;
    ArrayList<TimeSlot> timeOptions = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = MainActivity.userTable.getUser(MainActivity.userID);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        statusPublicSpinner = (Spinner)root.findViewById(R.id.statusPublicSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_selected, getResources().getStringArray(R.array.eventStatusArray));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusPublicSpinner.setAdapter(adapter);

        nameCreate = (EditText)root.findViewById(R.id.nameEventView);
        descriptionCreate = (EditText)root.findViewById(R.id.descriptionEventView);
        locationCreate = (androidx.appcompat.widget.SearchView)root.findViewById(R.id.locationEventSearch);
        dueTimeTextView = (TextView) root.findViewById(R.id.dueTimeEventView);
        inviteCreate = (androidx.appcompat.widget.SearchView) root.findViewById(R.id.inviteEventSearch);
        displayInvitedUserButton = (TextView)root.findViewById(R.id.inviteSearchDisplayButton);
        displayInvitedUserButton.setVisibility(View.GONE);

        restorePageEntries();

        Button createButton = (Button)root.findViewById(R.id.saveEventButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreateEvent(view);
            }
        });
        Button setButton = (Button)root.findViewById(R.id.dateTimeSet);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetTime(view);
            }
        });
        viewInvitedUsersButton = (Button)root.findViewById(R.id.viewInvitedUsersButton);
        viewInvitedUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickViewInvitedUsers(view);
            }
        });
        timeSlotsButton = (Button)root.findViewById(R.id.timeSlotsEventButton);
        timeSlotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTimeSlots(view);
            }
        });

        locationCreate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Address> addresses = null;
                if (s != null || !s.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addresses = geocoder.getFromLocationName(s, 1);
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Issue finding location. Please try again.", Toast.LENGTH_LONG);
                    }
                    if (addresses.size() == 1) {
                        Address addr = addresses.get(0);
                        location = new Location(s, (float)addr.getLatitude(), (float)addr.getLongitude());
                        locationCreate.setQueryHint(s);
                        locationCreate.setQuery("", false);
                        AlertDialog.Builder locationsuccess = new AlertDialog.Builder(getContext());
                        locationsuccess.setMessage("Event location set to " + s + ".");
                        locationsuccess.setTitle("Success");
                        locationsuccess.setPositiveButton("Close", null);
                        locationsuccess.create().show();
                    }
                    else {
                        AlertDialog.Builder locationfail = new AlertDialog.Builder(getContext());
                        locationfail.setMessage("Location not found. Please select a more specific location or address.");
                        locationfail.setTitle("Error");
                        locationfail.setPositiveButton("Close", null);
                        locationfail.create().show();                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        inviteCreate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (Integer.parseInt(s) == user.getUserID()) {
                    AlertDialog.Builder inviteFail = new AlertDialog.Builder(getContext());
                    inviteFail.setMessage("You cannot invite yourself to your event.");
                    inviteFail.setTitle("Error");
                    inviteFail.setPositiveButton("Close", null);
                    inviteFail.create().show();
                    inviteCreate.setQuery("", false);
                }
                else {
                    searchedInviteUser = MainActivity.userTable.getUser(Integer.parseInt(s));

                    displayInvitedUserButton.setVisibility(View.VISIBLE);
                    viewInvitedUsersButton.setVisibility(View.INVISIBLE);
                    if (searchedInviteUser != null) {
                        displayInvitedUserButton.setText(searchedInviteUser.getName());
                    }
                    else {
                        displayInvitedUserButton.setText("User not found. Click to try again.");
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        displayInvitedUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInvitedUserButton.setVisibility(View.GONE);
                inviteCreate.setQuery("", false);
                if (searchedInviteUser != null) {
                    invitedUsersTemp.add(searchedInviteUser.getUserID());
                }
                viewInvitedUsersButton.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClickCreateEvent(View view) {
        String createdName = nameCreate.getText().toString();
        if (createdName.length() == 0) {
            nameCreate.setError("Title is required.");
            createSuccess = false;
        }
        String createdStatusPublic = statusPublicSpinner.getSelectedItem().toString();
        Boolean status;
        if (createdStatusPublic.equals("Public")) {
            status = true;
        }
        else {
            status = false;
        }
        String description = descriptionCreate.getText().toString();
        // READ TIME SLOTS HERE
        duetime = (TimeSlot)MainActivity.infoBundle.getSerializable("duetime");
        if (duetime == null) {
            AlertDialog.Builder missingDue = new AlertDialog.Builder(getContext());
            missingDue.setMessage("Event due time is required.");
            missingDue.setTitle("Error");
            missingDue.setPositiveButton("Close", null);
            missingDue.create().show();
            createSuccess = false;
        }

        // CREATE EVENT WITH ALL INFO
        eventInProgress = new Event(user.getUserID(), createdName, status);
        int eventID = MainActivity.eventTable.addEvent(eventInProgress);
        Event event = MainActivity.eventTable.getEvent(eventID);
        event.setDescription(description);
        event.setDueTime(duetime);
        event.setInvitees(invitedUsersTemp);
        if (location != null) {
            event.setLocation(location);
        }
        event.setTimeOptions(timeOptions);
        MainActivity.eventTable.editEvent(event);

        // delete temps
        MainActivity.infoBundle.remove("temp_event_name");
        MainActivity.infoBundle.remove("temp_event_statuspublic");
        MainActivity.infoBundle.remove("temp_event_otherinfo");
        MainActivity.infoBundle.remove("duetime");
        MainActivity.infoBundle.remove("temp_invited_users");
        MainActivity.infoBundle.remove("temp_event_location");
        MainActivity.infoBundle.remove("invitedUsersArray");
        MainActivity.infoBundle.remove("timeOptionsArray");

        if (createSuccess) {
            // SAVE EVENT LOCALLY
            MainActivity.infoBundle.putInt("eventID", eventID);
            MainActivity.infoBundle.putSerializable("event", event);
            // SWITCH FRAGMENTS
            Fragment detailsFrag = new DetailsFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, detailsFrag);
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
        createSuccess = true;
    }

    public void onClickSetTime(View view) {
        savePageEntries();

        MainActivity.infoBundle.putBoolean("editing", false);
        Fragment setDueFrag = new SetDueFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, setDueFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickViewInvitedUsers(View view) {
        if (invitedUsersTemp.size() == 0) {
            AlertDialog.Builder noInvites = new AlertDialog.Builder(getContext());
            noInvites.setMessage("No users have been invited to this event yet.");
            noInvites.setTitle("Invited Users");
            noInvites.setPositiveButton("Close", null);
            noInvites.create().show();
        }
        else {
            savePageEntries();

            String[] invitedUsersStringArray = new String[invitedUsersTemp.size()];
            for (int i = 0; i < invitedUsersTemp.size(); i++) {
                User u = MainActivity.userTable.getUser(invitedUsersTemp.get(i));
                String str = u.getName() + " (" + invitedUsersTemp.get(i) + ")";
                invitedUsersStringArray[i] = str;
            }
            MainActivity.infoBundle.putIntegerArrayList("invitedUserIDs", invitedUsersTemp);
            MainActivity.infoBundle.putStringArray("invitedUsersArray", invitedUsersStringArray);

            MainActivity.infoBundle.putBoolean("editing", false);
            Fragment invitedFrag = new InvitedUsersFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, invitedFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void onClickTimeSlots(View view) {
        savePageEntries();
        String[] timeOptionsStringArray;
        if (timeOptions.size() > 0) {
             timeOptionsStringArray = new String[timeOptions.size()];
            for (int i = 0; i < timeOptions.size(); i++) {
                String str = timeOptions.get(i).toStringDateTime() + " (" + timeOptions.get(i).getDuration() + " min)";
                timeOptionsStringArray[i] = str;
            }
        }
        else {
            timeOptionsStringArray = new String[]{};
        }
        MainActivity.infoBundle.putStringArray("timeOptionsArray", timeOptionsStringArray);
        MainActivity.infoBundle.putBoolean("editing", false);

        Fragment timeSlotFrag = new ViewTimeSlotsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, timeSlotFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void savePageEntries() {
        String createdName = nameCreate.getText().toString();
        String createdStatusPublic = statusPublicSpinner.getSelectedItem().toString();
        String createdDescription = descriptionCreate.getText().toString();

        MainActivity.infoBundle.putString("temp_event_name", createdName);
        MainActivity.infoBundle.putString("temp_event_statuspublic", createdStatusPublic);
        MainActivity.infoBundle.putString("temp_event_otherinfo", createdDescription);
        MainActivity.infoBundle.putIntegerArrayList("temp_invited_users", invitedUsersTemp);
        if (duetime != null) {
            MainActivity.infoBundle.putSerializable("temp_event_duetime", duetime);
        }
        if (location != null) {
            MainActivity.infoBundle.putSerializable("temp_event_location", location);
        }
        MainActivity.infoBundle.putSerializable("temp_time_options", timeOptions);
    }

    public void restorePageEntries() {

        if (MainActivity.infoBundle.containsKey("temp_event_name")) {
            String tempName = MainActivity.infoBundle.getString("temp_event_name");
            nameCreate.setText(tempName);
        }
        if (MainActivity.infoBundle.containsKey("temp_event_statuspublic")) {
            String tempStatusPublic = MainActivity.infoBundle.getString("temp_event_statuspublic");
            if (tempStatusPublic.equals("Public")) {
                statusPublicSpinner.setSelection(0);
            }
            else {
                statusPublicSpinner.setSelection(1);
            }
        }
        if (MainActivity.infoBundle.containsKey("temp_event_otherinfo")) {
            String tempOtherInfo = MainActivity.infoBundle.getString("temp_event_otherinfo");
            descriptionCreate.setText(tempOtherInfo);
        }
        if (MainActivity.infoBundle.containsKey("duetime")) {
            duetime = (TimeSlot)MainActivity.infoBundle.getSerializable("duetime");
            dueTimeTextView.setText(duetime.toStringDateTime());
        }
        if (MainActivity.infoBundle.containsKey("temp_invited_users")) {
            invitedUsersTemp = MainActivity.infoBundle.getIntegerArrayList("temp_invited_users");
            if (MainActivity.infoBundle.containsKey("delete_clicked")) {
                Boolean delete = MainActivity.infoBundle.getBoolean("delete_clicked");
                if (delete) {
                    User clicked = (User)MainActivity.infoBundle.getSerializable("clicked_user");
                    int deleteIndex = invitedUsersTemp.indexOf(clicked.getUserID());
                    invitedUsersTemp.remove(deleteIndex);
                }
            }
        }
        MainActivity.infoBundle.remove("delete_clicked");
        MainActivity.infoBundle.remove("clicked_user");
        if (MainActivity.infoBundle.containsKey("temp_event_location")) {
            location = (Location)MainActivity.infoBundle.getSerializable("temp_event_location");
            locationCreate.setQuery(location.getName(), false);
        }
        if (MainActivity.infoBundle.containsKey("temp_time_options")) {
            timeOptions = (ArrayList<TimeSlot>)MainActivity.infoBundle.getSerializable("temp_time_options");
            if (MainActivity.infoBundle.containsKey("timeslot")) {
                TimeSlot ts = (TimeSlot) MainActivity.infoBundle.getSerializable("timeslot");
                timeOptions.add(ts);
                MainActivity.infoBundle.remove("timeslot");
            }
            if (MainActivity.infoBundle.containsKey("delete_slot")) {
                TimeSlot delete = (TimeSlot)MainActivity.infoBundle.getSerializable("delete_slot");
                timeOptions.remove(delete);
                MainActivity.infoBundle.remove("delete_slot");
            }
        }
    }
}