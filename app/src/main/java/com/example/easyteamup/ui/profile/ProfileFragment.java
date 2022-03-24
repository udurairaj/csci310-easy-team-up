package com.example.easyteamup.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) MainActivity.infoBundle.getSerializable("user");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        displayProfile(root);

        Button editButton = (Button)root.findViewById(R.id.editProfileButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditProfile(view);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void displayProfile(View view) {
        TextView nameView = (TextView)view.findViewById(R.id.nameProfileView);
        nameView.setText(user.getName());
        TextView userIDView = (TextView)view.findViewById(R.id.userIDProfileView);
        userIDView.setText(String.valueOf(user.getUserID()));
        TextView usernameView = (TextView)view.findViewById(R.id.usernameProfileView);
        usernameView.setText(user.getUsername());
        TextView emailView = (TextView)view.findViewById(R.id.emailProfileView);
        emailView.setText(user.getEmail());
        TextView phoneView = (TextView)view.findViewById(R.id.phoneProfileView);
        if (user.getPhone() != null) {
            phoneView.setText(user.getPhone());
        }
        else {
            phoneView.setText("");
        }
        ImageView profilePicView = (ImageView)view.findViewById(R.id.imageButton);
        if (user.getProfilePic() != null) {
            profilePicView.setImageURI(Uri.parse(user.getProfilePic()));
        }
        else {
            profilePicView.setImageResource(R.drawable.no_prof_pic);
        }
        TextView otherInfoView = (TextView)view.findViewById(R.id.otherInfoProfileView);
        if (user.getOtherInfo() != null) {
            otherInfoView.setText(user.getOtherInfo());
        }
        else {
            otherInfoView.setText("");
        }
    }

    public void onClickEditProfile(View view) {
        MainActivity.infoBundle.putSerializable("user", user);
        Fragment editFrag = new EditProfileFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, editFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public ProfileFragment() {}
}