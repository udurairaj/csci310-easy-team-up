package com.example.easyteamup.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.Event;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentCreateBinding;
import com.example.easyteamup.ui.shared.DetailsFragment;
import com.example.easyteamup.ui.shared.SetDueFragment;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private User user = null;

    EditText nameCreate = null;
    EditText descriptionCreate = null;
    Spinner statusPublicSpinner = null;
    SearchView locationCreate = null;
    TextView dueTimeTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) MainActivity.infoBundle.getSerializable("user");
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
        //locationCreate = (SearchView)root.findViewById(R.id.locationEventSearch);
        dueTimeTextView = (TextView) root.findViewById(R.id.dueTimeEventView);

        // fill in temp info
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
            String datetime = MainActivity.infoBundle.getString("duetime");
            dueTimeTextView.setText(datetime);
        }

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onClickCreateEvent(View view) {
        // delete temps
        MainActivity.infoBundle.remove("temp_event_name");
        MainActivity.infoBundle.remove("temp_event_statuspublic");
        MainActivity.infoBundle.remove("temp_event_otherinfo");
        MainActivity.infoBundle.remove("duetime");

        String createdName = nameCreate.getText().toString();
        String createdStatusPublic = statusPublicSpinner.getSelectedItem().toString();
        Boolean status;
        if (createdStatusPublic.equals("Public")) {
            status = true;
        }
        else {
            status = false;
        }
        // READ LOCATION HERE
        // READ TIME SLOTS HERE
        // READ SET DUE TIME HERE FROM INTENT

        // CREATE EVENT WITH ALL INFO
        Event event = new Event(user, createdName, status);
        // ADD EVENT TO DB

        // SAVE EVENT LOCALLY
        MainActivity.infoBundle.putSerializable("event", event);
        // SWITCH FRAGMENTS
        Fragment detailsFrag = new DetailsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, detailsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onClickSetTime(View view) {
        String createdName = nameCreate.getText().toString();
        String createdStatusPublic = statusPublicSpinner.getSelectedItem().toString();
        String createdDescription = descriptionCreate.getText().toString();

        MainActivity.infoBundle.putString("temp_event_name", createdName);
        MainActivity.infoBundle.putString("temp_event_statuspublic", createdStatusPublic);
        MainActivity.infoBundle.putString("temp_event_otherinfo", createdDescription);
        // SAVE LOCATION, TIME SLOTS, AND INVITED USERS

        Fragment setDueFrag = new SetDueFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, setDueFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}