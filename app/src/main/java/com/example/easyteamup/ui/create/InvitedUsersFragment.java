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

import com.example.easyteamup.ui.EditEvent;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.ui.profile.OtherProfileFragment;

import java.util.ArrayList;

public class InvitedUsersFragment extends Fragment {

    ListView invitedUsersList = null;
    ArrayAdapter listAdapter = null;

    public InvitedUsersFragment() {
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
        View root = inflater.inflate(R.layout.fragment_invited_users, container, false);

        String[] invitedUsersStringArray = MainActivity.infoBundle.getStringArray("invitedUsersArray");
        ArrayList<Integer> invitedUserIDs = MainActivity.infoBundle.getIntegerArrayList("invitedUserIDs");

        invitedUsersList = (ListView)root.findViewById(R.id.invitedUsersList);
        listAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, invitedUsersStringArray);
        invitedUsersList.setAdapter(listAdapter);
        invitedUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                int clickedUserID = invitedUserIDs.get(position);
                User clickedUser = MainActivity.userTable.getUser(clickedUserID);
                if (clickedUser != null) {
                    MainActivity.infoBundle.putSerializable("clicked_user", clickedUser);
                    Fragment otherProfFrag = new OtherProfileFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, otherProfFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(getContext(), "An error occurred. Please try again later.", Toast.LENGTH_LONG);
                }
            }
        });

        Button backButton = (Button)root.findViewById(R.id.backInvitedUsersButton);
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