package com.example.easyteamup.ui.create;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.Event;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentCreateBinding;
import com.example.easyteamup.ui.profile.ProfileFragment;
import com.example.easyteamup.ui.shared.DetailsFragment;
import com.example.easyteamup.ui.shared.SetDueFragment;

import java.sql.Array;
import java.util.ArrayList;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private User user = null;
    private User searchedInviteUser = null;
    private Event eventInProgress = null;

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
//        invitedUsersCreate = (TextView)root.findViewById(R.id.invitedUsersEventView);
//        invitedUsersCreate.setVisibility(View.VISIBLE);
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

//        locationCreate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                // SEARCH MAPS API FOR LOCATION
//                // SAVE LOCATION INFO THAT JOE NEEDS FOR GOOGLE MAPS MARKER
//                // RETURN TRUE
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });

        inviteCreate.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchedInviteUser = MainActivity.userTable.getUser(Integer.parseInt(s));

                displayInvitedUserButton.setVisibility(View.VISIBLE);
                viewInvitedUsersButton.setVisibility(View.INVISIBLE);
                if (searchedInviteUser != null) {
                    displayInvitedUserButton.setText(searchedInviteUser.getName());
                }
                else {
                    displayInvitedUserButton.setText("User not found. Click to try again.");
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
        String createdStatusPublic = statusPublicSpinner.getSelectedItem().toString();
        Boolean status;
        if (createdStatusPublic.equals("Public")) {
            status = true;
        }
        else {
            status = false;
        }
        String description = descriptionCreate.getText().toString();
        // READ LOCATION HERE
        // READ TIME SLOTS HERE
        duetime = (TimeSlot)MainActivity.infoBundle.getSerializable("duetime");

        // CREATE EVENT WITH ALL INFO
        eventInProgress = new Event(user.getUserID(), createdName, status);
        MainActivity.eventTable.addEvent(eventInProgress);
        eventInProgress.setDescription(description);
        eventInProgress.setDueTime(duetime);
        MainActivity.eventTable.editEvent(eventInProgress);
        // ADD EVENT TO DB (int eventID = addToDB)
        // GET EVENT FROM DB BY ID
        // EDIT EVENT TO ADD LOCATION, TIME SLOTS, DUE TIME, INVITED USERS (PARTICIPANTS), etc.

        // delete temps
        MainActivity.infoBundle.remove("temp_event_name");
        MainActivity.infoBundle.remove("temp_event_statuspublic");
        MainActivity.infoBundle.remove("temp_event_otherinfo");
        MainActivity.infoBundle.remove("duetime");
        MainActivity.infoBundle.remove("temp_invited_users");

        // SAVE EVENT LOCALLY
        MainActivity.infoBundle.putSerializable("event", eventInProgress);
        // SWITCH FRAGMENTS
        Fragment detailsFrag = new DetailsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, detailsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickSetTime(View view) {
        savePageEntries();

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

            Fragment invitedFrag = new InvitedUsersFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, invitedFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
        // SAVE LOCATION, TIME SLOTS
    }

    public void restorePageEntries() {

        if (MainActivity.infoBundle.containsKey("temp_event_name")) {
            String tempName = MainActivity.infoBundle.getString("temp_event_name");
            nameCreate.setText(tempName);
        }
        if (MainActivity.infoBundle.containsKey("temp_event_statuspublic")) {
            String tempStatusPublic = MainActivity.infoBundle.getString("temp_event_statuspublic");
            if (tempStatusPublic == "Public") {
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
            dueTimeTextView.setText(duetime.getDateTimeToString());
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
    }
}