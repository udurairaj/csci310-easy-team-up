package com.example.easyteamup.ui.profile;

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
import android.widget.Toast;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentProfileBinding;

public class EditProfileFragment extends Fragment {

    User user = null;
    Boolean success = true;

    EditText nameEdit = null;
    EditText usernameEdit = null;
    EditText emailEdit = null;
    EditText phoneEdit = null;
    ImageView profilePicEdit = null;
    EditText otherInfoEdit = null;
    Button saveButton = null;
    Button profButton = null;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) MainActivity.infoBundle.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        nameEdit = (EditText)root.findViewById(R.id.nameProfileView);
        usernameEdit = (EditText)root.findViewById(R.id.usernameProfileView);
        emailEdit = (EditText)root.findViewById(R.id.emailProfileView);
        phoneEdit = (EditText)root.findViewById(R.id.phoneProfileView);
        profilePicEdit = (ImageView)root.findViewById(R.id.imageButton);
        otherInfoEdit = (EditText)root.findViewById(R.id.otherInfoProfileView);
        saveButton = (Button)root.findViewById(R.id.confirmProfileChangesButton);
        profButton = (Button)root.findViewById(R.id.editProfilePicButton);

        nameEdit.setText(user.getName());
        usernameEdit.setText(user.getUsername());
        emailEdit.setText(user.getEmail());
        if (user.getPhone() != null) {
            phoneEdit.setText(user.getPhone());
        }
        if (user.getProfilePic() != null) {
            profilePicEdit.setImageURI(Uri.parse(user.getProfilePic()));
        }
        else {
            profilePicEdit.setImageResource(R.drawable.no_prof_pic);
        }
        if (user.getOtherInfo() != null) {
            otherInfoEdit.setText(user.getOtherInfo());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveEdits(view);
            }
        });
        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSetProfilePic(view);
            }
         });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClickSaveEdits(View view) {
        String editedName = nameEdit.getText().toString();
        if (editedName.length() == 0) {
            nameEdit.setError("Name is required.");
            success = false;
        }
        else {
            user.setName(editedName);
        }

        String editedUsername = usernameEdit.getText().toString();
        if (editedUsername.length() == 0) {
            usernameEdit.setError("Username is required");
            success = false;
        }
        // ELSE IF USERNAME FOUND IN DB, ERROR
        else {
            user.setUsername(editedUsername);
        }

        String editedEmail = emailEdit.getText().toString();
        if (editedEmail.length() == 0) {
            emailEdit.setError("Email is required");
            success = false;
        }
        else {
            user.setEmail(editedEmail);
        }

        String editedPhone = phoneEdit.getText().toString();
        if (editedPhone.length() == 0) {
            user.setPhone(null);
        }
        else {
            user.setPhone(editedPhone);
        }

        String editedOtherInfo = otherInfoEdit.getText().toString();
        if (editedOtherInfo.length() == 0) {
            user.setOtherInfo(null);
        }
        else if (editedOtherInfo.length() > 120) {
            otherInfoEdit.setError("Max 120 characters.");
            success = false;
        }
        else {
            user.setOtherInfo(editedOtherInfo);
        }

        if (success) {
            MainActivity.infoBundle.putSerializable("user", user);
            Fragment profFrag = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, profFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    ActivityResultLauncher<String> arl = registerForActivityResult
            (new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        profilePicEdit.setImageURI(result);
                        user.setProfilePic(result.toString());
                    }
                    else {
                        Toast tryAgainToast = Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT);
                    }
                }
            });

    public void onClickSetProfilePic(View View) {
        arl.launch("image/*");
    }
}