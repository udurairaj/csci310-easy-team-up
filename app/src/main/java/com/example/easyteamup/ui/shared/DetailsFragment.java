package com.example.easyteamup.ui.shared;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
        //event = MainActivity.eventTable.getEvent(MainActivity.infoBundle.get("event");
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}