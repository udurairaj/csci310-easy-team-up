package com.example.easyteamup.ui.profile;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditProfileFragment extends Fragment {

    private StorageReference storageRef = null;
    private ProgressDialog progress = null;
    String filename = "";

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
        user = MainActivity.userTable.getUser(MainActivity.userID);
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        nameEdit = (EditText)root.findViewById(R.id.nameEditProfileView);
        usernameEdit = (EditText)root.findViewById(R.id.usernameEditProfileView);
        emailEdit = (EditText)root.findViewById(R.id.emailEditProfileView);
        phoneEdit = (EditText)root.findViewById(R.id.phoneEditProfileView);
        profilePicEdit = (ImageView)root.findViewById(R.id.imageEditButton);
        otherInfoEdit = (EditText)root.findViewById(R.id.otherInfoEditProfileView);
        saveButton = (Button)root.findViewById(R.id.confirmProfileChangesButton);
        profButton = (Button)root.findViewById(R.id.editProfilePicButton);

        nameEdit.setText(user.getName());
        usernameEdit.setText(user.getUsername());
        emailEdit.setText(user.getEmail());
        if (user.getPhone() != null) {
            phoneEdit.setText(user.getPhone());
        }
        if (user.getProfilePic() != null) {
            storageRef = FirebaseStorage.getInstance().getReference().child("images").child(user.getProfilePic());
            String filePath = getContext().getFilesDir().getPath().toString() + "/" + user.getProfilePic() + ".jpeg";
            Log.d("path", filePath);
            File image = new File(filePath);
            storageRef.getFile(image)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Profile loaded.", Toast.LENGTH_LONG).show();
                            final long ONE_MEGABYTE = 1024 * 1024;
                            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profilePicEdit.setImageBitmap(bmp);
                                    profilePicEdit.setContentDescription("SUCCESS");
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error loading profile. Try again.", Toast.LENGTH_LONG).show();
                    profilePicEdit.setContentDescription("ERROR");
                }
            });
        }
        else {
            profilePicEdit.setImageResource(R.drawable.no_prof_pic);
            profilePicEdit.setContentDescription("SUCCESS");
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
        else if (!editedUsername.equals(user.getUsername()) && MainActivity.userTable.contains(editedUsername) != null) {
            usernameEdit.setError("Username is taken. Try again.");
            success = false;
        }
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
            MainActivity.userTable.editUser(user);
            Fragment profFrag = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, profFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        success = true;
    }

    ActivityResultLauncher<String> arl = registerForActivityResult
            (new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        profilePicEdit.setImageURI(result);
                        progress = new ProgressDialog(getContext());
                        progress.setTitle("Uploading image...");
                        progress.show();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
                        Date now = new Date();
                        filename = format.format(now);

                        storageRef = FirebaseStorage.getInstance().getReference("images/" + filename);
                        storageRef.putFile(result)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (progress.isShowing()) { progress.dismiss(); }
                                Toast.makeText(getContext(), "Image saved successfully.", Toast.LENGTH_LONG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (progress.isShowing()) { progress.dismiss(); }
                                Toast.makeText(getContext(), "Issue saving image. Try again.", Toast.LENGTH_LONG);
                            }
                        });
                        if (filename.equals("")) {
                            user.setProfilePic(null);
                        }
                        else {
                            user.setProfilePic(filename);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public void onClickSetProfilePic(View View) {
        arl.launch("image/*");
    }
}