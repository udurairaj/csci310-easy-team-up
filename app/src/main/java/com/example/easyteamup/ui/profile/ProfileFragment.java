package com.example.easyteamup.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private StorageReference storageRef = null;
    private FragmentProfileBinding binding;
    private User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) MainActivity.infoBundle.getSerializable("user");
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button editButton = (Button)root.findViewById(R.id.editProfileButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEditProfile(view);
            }
        });

        displayProfile(root);

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
            storageRef = FirebaseStorage.getInstance().getReference().child("images/" + user.getProfilePic() + ".jpeg");
            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error loading image.", Toast.LENGTH_SHORT);
            }
            if (localFile != null) {
                File finalLocalFile = localFile;
                storageRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                profilePicView.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath()));
                                Toast.makeText(getContext(), "Profile loaded.", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        profilePicView.setImageResource(R.drawable.no_prof_pic);
                        Toast.makeText(getContext(), "Error loading profile. Try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
//            storageRef.getDownloadUrl()
//                    .addOnSuccessListener(uri -> {
//                        Glide.with(this)
//                                .load(uri)
//                                .into(profilePicView);
//                    });
//            Glide.with(getContext())
//                    //.using(new FirebaseImageLoader())
//                    .load(storageRef)
//                    //.placeholder(R.drawable.no_prof_pic)
//                    .into(profilePicView);
//            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    profilePicView.setImageURI(uri);
//                    Toast.makeText(getContext(), "Profile loaded.", Toast.LENGTH_LONG).show();
//                }
//            final long ONE_MEGABYTE = 1024 * 1024;
//            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    profilePicView.setImageBitmap(bmp);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    profilePicView.setImageResource(R.drawable.no_prof_pic);
//                    Toast.makeText(getContext(), "Error loading profile. Try again.", Toast.LENGTH_LONG).show();
//                }
//            });
        }
        else {
            profilePicView.setImageResource(R.drawable.no_prof_pic);
        }
//        File image = new File(user.getProfilePic() + ".jpeg");
//        storageRef.getFile(image)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getContext(), "Profile loaded.", Toast.LENGTH_LONG).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "Error loading profile. Try again.", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        profilePicView.setImageURI(Uri.parse(user.getProfilePic()));
//    }
//        else {
//        profilePicView.setImageResource(R.drawable.no_prof_pic);
//    }
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