package com.example.easyteamup.ui.shared;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyteamup.Event;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {

    User user = null;
    Event event = null;

    TextView invitedUsersText = null;
    TextView invitedUsersView = null;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = MainActivity.userTable.getUser(MainActivity.userID);
        event = (Event)MainActivity.infoBundle.getSerializable("event");
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
        Log.i("event", event.getEventName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_details, container, false);

        invitedUsersText = root.findViewById(R.id.invitedUsersDetailsText);
        invitedUsersView = root.findViewById(R.id.invitedUsersDetailsView);

        if (MainActivity.userID == event.getOwner()) {
            invitedUsersText.setVisibility(View.VISIBLE);
            invitedUsersView.setVisibility(View.VISIBLE);
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

        return root;
    }

    protected void displayEvent(View view, Event event) {
        TextView nameView = (TextView) view.findViewById(R.id.nameDetailsView);
        setTextCheck(nameView, event.getEventName());
        Log.i("eventPublic", event.getEventName());
        TextView publicView = (TextView) view.findViewById(R.id.statusPublicDetailsView);
        if (event.getStatusPublic() == null || event.getStatusPublic()) {
            publicView.setText("Public");
        }
        else {
            publicView.setText("Private");
        }
        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionDetailsView);
        setTextCheck(descriptionView, event.getDescription());
        TextView ownerView = (TextView) view.findViewById(R.id.ownerDetailsView);
        User owner = MainActivity.userTable.getUser(event.getOwner());
        setTextCheck(ownerView, owner.getName());
        Log.i("invitees", Integer.toString(event.getInvitees().size()));
        TextView invitees = (TextView) view.findViewById(R.id.invitedUsersDetailsView);
        invitees.setText(makeCommaString(event.getInvitees()));
        TextView participants = (TextView) view.findViewById(R.id.participantsDetailsView);
        participants.setText(makeCommaString(event.getParticipants()));
    }

    public String makeCommaString(ArrayList<Integer> list) {
        if (list == null) {
            return "none";
        }
        if (list.size() == 0) {
            return "none";
        }
        String names= "";
        String nextName;
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

    public void onClickEditEvent(View view) {
        MainActivity.infoBundle.putInt("eventID", event.getEventID());
//        Fragment editEventFrag = new EditEventFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editEventFrag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}