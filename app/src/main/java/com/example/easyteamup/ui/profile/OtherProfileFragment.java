package com.example.easyteamup.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easyteamup.ui.EditEvent;
import com.example.easyteamup.ui.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.ui.create.CreateFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class OtherProfileFragment extends Fragment {

    private StorageReference storageRef = null;
    private User clicked_user = null;
    Boolean viewOtherUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.infoBundle.containsKey("clicked_user")) {
            clicked_user = (User)MainActivity.infoBundle.getSerializable("clicked_user");
            viewOtherUser = true;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_other_profile, container, false);

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
        TextView nameView = (TextView)view.findViewById(R.id.nameOtherProfileView);
        nameView.setText(clicked_user.getName());
        TextView userIDView = (TextView)view.findViewById(R.id.userIDOtherProfileView);
        userIDView.setText(String.valueOf(clicked_user.getUserID()));
        TextView usernameView = (TextView)view.findViewById(R.id.usernameOtherProfileView);
        usernameView.setText(clicked_user.getUsername());
        TextView emailView = (TextView)view.findViewById(R.id.emailOtherProfileView);
        emailView.setText(clicked_user.getEmail());
        TextView phoneView = (TextView)view.findViewById(R.id.phoneOtherProfileView);
        if (clicked_user.getPhone() != null) {
            phoneView.setText(clicked_user.getPhone());
        }
        else {
            phoneView.setText("");
        }
        ImageView profilePicView = (ImageView)view.findViewById(R.id.imageOtherProfileButton);
        if (clicked_user.getProfilePic() != null) {
            storageRef = FirebaseStorage.getInstance().getReference().child("images").child(clicked_user.getProfilePic());
            String filePath = getContext().getFilesDir().getPath().toString() + "/" + clicked_user.getProfilePic() + ".jpeg";
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
                                    profilePicView.setImageBitmap(bmp);
                                    profilePicView.setContentDescription("SUCCESS");
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error loading profile. Try again.", Toast.LENGTH_LONG).show();
                    profilePicView.setContentDescription("ERROR");
                }
            });
        }
        else {
            profilePicView.setImageResource(R.drawable.no_prof_pic);
        }



        TextView otherInfoView = (TextView)view.findViewById(R.id.otherInfoOtherProfileView);
        if (clicked_user.getOtherInfo() != null) {
            otherInfoView.setText(clicked_user.getOtherInfo());
        }
        else {
            otherInfoView.setText("");
        }
        Button backButton = (Button)view.findViewById(R.id.backToCreateButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoBack(view);
            }
        });
        Button uninviteButton = (Button)view.findViewById(R.id.uninviteUserButton);
        uninviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUninvite(view);
            }
        });
    }

    public void onClickGoBack(View view) {
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

    public void onClickUninvite(View view) {
        MainActivity.infoBundle.putBoolean("delete_clicked", true);
        onClickGoBack(view);
    }

    public OtherProfileFragment() {}
}