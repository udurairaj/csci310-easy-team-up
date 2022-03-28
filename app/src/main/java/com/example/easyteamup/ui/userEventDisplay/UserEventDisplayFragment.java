package com.example.easyteamup.ui.userEventDisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.easyteamup.EventTable;
import com.example.easyteamup.Event;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.OnIntegerChangeListener;
import com.example.easyteamup.R;
import com.example.easyteamup.UserTable;
import com.example.easyteamup.databinding.FragmentUserEventDisplayBinding;


public class UserEventDisplayFragment extends Fragment {

    private FragmentUserEventDisplayBinding binding;
    ListView l;
    EventTable table = MainActivity.eventTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserEventDisplayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        l = root.findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                table.getAllEventNames());
        l.setAdapter(arr);

        table.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                ArrayAdapter<String> arr;
                arr
                        = new ArrayAdapter<String>(
                        getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        table.getAllEventNames());
                l.setAdapter(arr);
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