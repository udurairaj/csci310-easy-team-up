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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentProfileBinding;

public class OtherProfileFragment extends Fragment {

    private User user = null;
    Boolean viewOtherUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) MainActivity.infoBundle.getSerializable("user");
        if (MainActivity.infoBundle.containsKey("clicked_user")) {
            user = (User)MainActivity.infoBundle.getSerializable("clicked_user");
            viewOtherUser = true;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Button editButton = (Button)root.findViewById(R.id.editProfileButton);
        if (viewOtherUser) {
            displayProfile(root);

        }
        else {
            AlertDialog.Builder viewProfFail = new AlertDialog.Builder(getContext());
            viewProfFail.setMessage("Something went wrong. Please try again.");
            viewProfFail.setTitle("Error");
            viewProfFail.setPositiveButton("Close", null);
            viewProfFail.create().show();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    public OtherProfileFragment() {}
}