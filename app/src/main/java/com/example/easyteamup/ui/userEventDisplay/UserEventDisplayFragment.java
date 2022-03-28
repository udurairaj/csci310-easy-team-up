package com.example.easyteamup.ui.userEventDisplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.EventTable;
import com.example.easyteamup.Event;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.OnIntegerChangeListener;
import com.example.easyteamup.R;
import com.example.easyteamup.UserTable;
import com.example.easyteamup.databinding.FragmentUserEventDisplayBinding;
import com.example.easyteamup.ui.profile.EditProfileFragment;
import com.example.easyteamup.ui.shared.DetailsFragment;


public class UserEventDisplayFragment extends Fragment {

    private FragmentUserEventDisplayBinding binding;
    private ListView l;
    private EventTable table = MainActivity.eventTable;
    private String[] eventNames;
    private RadioGroup radioGroup;
    private TextView titleText;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserEventDisplayBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        titleText = (TextView) root.findViewById(R.id.textView);
        button1 = (RadioButton) root.findViewById(R.id.radioButton);
        button2 = (RadioButton) root.findViewById(R.id.radioButton2);
        button3 = (RadioButton) root.findViewById(R.id.radioButton3);
        button4 = (RadioButton) root.findViewById(R.id.radioButton4);

        l = root.findViewById(R.id.list);
        makeDisplay(filterEvents());

        radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
               makeDisplay(filterEvents());
            }
        });

        table.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                makeDisplay(filterEvents());
            }
        });

        return root;
    }

    public String[] filterEvents() {
        if (button1.isChecked()) {
            titleText.setText("Your Created Events");
            return table.getOwnEvents();
        }
        else if (button2.isChecked()) {
            titleText.setText("Your Invites");
            return table.getInvitedEvents();
        }
        else if (button3.isChecked()) {
            titleText.setText("Events You've Joined");
            return table.getJoinedEvents();
        }
        else {
            titleText.setText("Your Past Events");
            return table.getPastEvents();
        }
    }

    public void makeDisplay(String[] events) {
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                events);
        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
            {
                MainActivity.infoBundle.putString("event", eventNames[position]);
                Fragment editFrag = new DetailsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}