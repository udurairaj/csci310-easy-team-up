package com.example.easyteamup.ui.shared;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easyteamup.ui.EditEvent;
import com.example.easyteamup.Event;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.TimeSlot;
import com.example.easyteamup.User;
import com.example.easyteamup.ui.userEventDisplay.UserEventDisplayFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class DetailsFragment extends Fragment {

    User user = null;
    Event event = null;

    TextView invitedUsersText = null;
    TextView invitedUsersView = null;
    Button editButton = null;
    LinearLayout inviteLayout = null;
    Button button2 = null;
    Button button3 = null;
    Button button4 = null;
    Button timeOption1Button = null;
    Button timeOption2Button = null;
    Button timeOption3Button = null;
    Button withdrawButton = null;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = MainActivity.userTable.getUser(MainActivity.userID);
        event = MainActivity.eventTable.getEvent(MainActivity.infoBundle.getInt("eventID"));
//        if (MainActivity.infoBundle.containsKey("eventID")) {
//            event = MainActivity.eventTable.getEvent(MainActivity.infoBundle.getString("eventID"));
//        }
//        else {
//            AlertDialog.Builder fail = new AlertDialog.Builder(getContext());
//            fail.setMessage("Issue loading event. Please try again.");
//            fail.setTitle("Error");
//            fail.setPositiveButton("Close", null);
//            fail.create().show();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();

        View root = inflater.inflate(R.layout.fragment_details, container, false);

        invitedUsersText = root.findViewById(R.id.invitedUsersDetailsText);
        invitedUsersView = root.findViewById(R.id.invitedUsersDetailsView);
        editButton = root.findViewById(R.id.editEventButton);

        if (MainActivity.userID == event.getOwner()) {
            invitedUsersText.setVisibility(View.VISIBLE);
            invitedUsersView.setVisibility(View.VISIBLE);
            TimeSlot finalTime = event.getFinalTime();
            Date now = new Date();
            if (finalTime == null || finalTime.dateTimeAsDate() == null
                    || finalTime.dateTimeAsDate().after(now)) {
                editButton.setVisibility(View.VISIBLE);
            }
        }

        inviteLayout = root.findViewById(R.id.inviteLayout);

        event = MainActivity.eventTable.getEvent(MainActivity.infoBundle.getInt("eventID"));
        if (event.getInvitees() != null) {
            Log.d("INVITEES", "DOES THIS PRINT");
            if (event.getInvitees().contains(user.getUserID())) {
                if (event.getParticipants() == null ||
                        !event.getParticipants().contains(user.getUserID())) {
                    inviteLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        button2 = root.findViewById(R.id.button2);
        button3 = root.findViewById(R.id.button3);
        button4 = root.findViewById(R.id.button4);
        withdrawButton = root.findViewById(R.id.withdrawButton);
        timeOption1Button = (Button) root.findViewById(R.id.button5);
        timeOption2Button = (Button) root.findViewById(R.id.button6);
        timeOption3Button = (Button) root.findViewById(R.id.button7);


        if (event.getParticipants() != null) {
            if (event.getParticipants().contains(user.getUserID())) {
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                withdrawButton.setVisibility(View.VISIBLE);
            }
        }

        if ((event.getTimeOptions().size() > 0))
        {
            timeOption1Button.setVisibility(View.VISIBLE);
            timeOption1Button.setText(event.getTimeOptions().get(0).toStringDateTime());
        }

        if ((event.getTimeOptions().size() > 1))
        {
            timeOption2Button.setVisibility(View.VISIBLE);
            timeOption2Button.setText(event.getTimeOptions().get(1).toStringDateTime());
        }

        if ((event.getTimeOptions().size() > 2))
        {
            timeOption3Button.setVisibility(View.VISIBLE);
            timeOption3Button.setText(event.getTimeOptions().get(2).toStringDateTime());
        }

        // Inflate the layout for this fragment

        displayEvent(root, event);

        Button editButton = (Button)root.findViewById(R.id.editEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditEvent(view);
            }
        });

        Button acceptButton = (Button)root.findViewById(R.id.acceptInviteButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onClickAccept(view); }
        });

        Button rejectButton = (Button)root.findViewById(R.id.rejectInviteButton);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onClickReject(view); }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onClickWithdraw(view); }
        });

        timeOption1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                event.getTimeOptions().get(0).select();
            }
        });

        timeOption2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                event.getTimeOptions().get(1).select();
            }
        });

        timeOption3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                event.getTimeOptions().get(2).select();
            }
        });

        return root;
    }

    protected void displayEvent(View view, Event event) {
        TextView nameView = (TextView) view.findViewById(R.id.nameDetailsView);
        setTextCheck(nameView, event.getEventName());
        TextView publicView = (TextView) view.findViewById(R.id.statusPublicDetailsView);
        if (event.getStatusPublic() == null || event.getStatusPublic()) {
            publicView.setText("Status: Public");
        }
        else {
            publicView.setText("Status: Private");
        }
        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionDetailsView);
        setTextCheck(descriptionView, event.getDescription());
        TextView ownerView = (TextView) view.findViewById(R.id.ownerDetailsView);
        User owner = MainActivity.userTable.getUser(event.getOwner());
        setTextCheck(ownerView, owner.getName());
        TextView invitees = (TextView) view.findViewById(R.id.invitedUsersDetailsView);
        invitees.setText(makeCommaString(event.getInvitees()));
        TextView participants = (TextView) view.findViewById(R.id.participantsDetailsView);
        participants.setText(makeCommaString(event.getParticipants()));
        TextView timeSlots = (TextView) view.findViewById(R.id.timeOptionsDetailsView);
        timeSlots.setText(makeTimeString(event.getTimeOptions()));
        TextView location = (TextView) view.findViewById(R.id.locationDetailsView);
        if (event.getLocation() != null) {
            location.setText(event.getLocation().getName());
        }
        TextView time = (TextView) view.findViewById(R.id.finalTimeDetailsView);
        if (event.getFinalTime() == null)
        {
            time.setText("TBD");
        }

        else
        {
            time.setText(event.getFinalTime().toStringDateTime());
        }
    }

    public String makeTimeString(ArrayList<TimeSlot> times) {
        if (times == null) {
            return "none";
        }
        if (times.size() == 0) {
            return "none";
        }
        String names= "";
        for (int i = 0; i < times.size() - 1; i++) {
            if (times.get(i) != null) {
                names = names + times.get(i).toStringDateTime();
                names = names + ", ";
            }
        }
        if (times.get(times.size() - 1) != null) {
            names = names + times.get(times.size() - 1).toStringDateTime();
        }
        return names;
    }

    public String makeCommaString(ArrayList<Integer> list) {
        if (list == null) {
            return "none";
        }
        if (list.size() == 0) {
            return "none";
        }
        String names= "";
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) != null) {
                names = names + MainActivity.userTable.getUser(list.get(i)).getName();
                names = names + ", ";
            }
        }
        if (list.get(list.size() - 1) != null) {
            names = names + MainActivity.userTable.getUser(list.get(list.size() - 1)).getName();
        }
        return names;
    }

    protected void setTextCheck(TextView view, String value) {
        if (value != null) {
            view.setText(value);
        }
        else {
            view.setText("none");
        }
    }

    public void onClickEditEvent(View root) {
        MainActivity.infoBundle.putInt("eventID", event.getEventID());
        MainActivity.infoBundle.putBoolean("first", true);

        Fragment editEventFrag = new EditEvent();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editEventFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickAccept(View view) {
        event.addParticipant(user);
        event.removeInvitee(user);
        MainActivity.eventTable.editEvent(event);

        MainActivity.infoBundle.putInt("eventID", event.getEventID());
        Fragment eventsFrag = new UserEventDisplayFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, eventsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickReject(View view) {
        event.removeInvitee(user);
        MainActivity.eventTable.editEvent(event);

        MainActivity.infoBundle.putInt("eventID", event.getEventID());
        Fragment eventsFrag = new UserEventDisplayFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, eventsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickWithdraw(View view) {
        event.removeParticipant(user);
        MainActivity.eventTable.editEvent(event);

        MainActivity.infoBundle.putInt("eventID", event.getEventID());
        Fragment eventsFrag = new UserEventDisplayFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, eventsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}