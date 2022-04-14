package com.example.easyteamup.ui.shared;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.easyteamup.EventTable;
import com.example.easyteamup.Event;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.OnIntegerChangeListener;
import com.example.easyteamup.R;
import com.example.easyteamup.databinding.FragmentUserEventDisplayBinding;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private FragmentUserEventDisplayBinding binding;
    ListView l;
    EventTable table = MainActivity.eventTable;
    String[] eventNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserEventDisplayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        l = root.findViewById(R.id.list);
        EventTable et = new EventTable();
        ArrayAdapter<String> arr;
        eventNames = table.getAllEventNames();
        arr
                = new ArrayAdapter<String>(
                getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                eventNames);
        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
            {
                ArrayList<Event> allEvents;
                allEvents = et.getAllEvents();
                Log.i("ID", eventNames[position]);
                MainActivity.infoBundle.putInt("eventID", allEvents.get(position).getEventID());
                Fragment editFrag = new DetailsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        table.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                ArrayAdapter<String> arr;
                eventNames = table.getAllEventNames();
                arr
                        = new ArrayAdapter<String>(
                        getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        eventNames);
                l.setAdapter(arr);
                l.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
                    {
                        Log.i("ID", eventNames[position]);
                        ArrayList<Event> allEvents;
                        allEvents = et.getAllEvents();
                        MainActivity.infoBundle.putInt("eventID", allEvents.get(position).getEventID());
                        Fragment editFrag = new DetailsFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
