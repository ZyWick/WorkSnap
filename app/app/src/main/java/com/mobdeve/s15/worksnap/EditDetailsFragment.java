package com.mobdeve.s15.worksnap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditDetailsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePhoto;
    private EditText name, title, email;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri imageUri;
    private String oldProfilePhotoUrl;

    public EditDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_details, container, false);

        profilePhoto = view.findViewById(R.id.profile_photo);
        name = view.findViewById(R.id.name);
        title = view.findViewById(R.id.title);
        email = view.findViewById(R.id.email);
        btnSave = view.findViewById(R.id.save_details);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loadUserProfile(currentUser.getUid());
        } else {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        btnSave.setOnClickListener(v -> saveUserProfile());
        profilePhoto.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    oldProfilePhotoUrl = document.getString("profilePhoto");
                    String userName = document.getString("username");
                    String userTitle = document.getString("title");
                    String userEmail = document.getString("email");

                    if (oldProfilePhotoUrl != null && !oldProfilePhotoUrl.isEmpty()) {
                        Glide.with(getActivity()).load(oldProfilePhotoUrl).into(profilePhoto);
                    }
                    name.setText(userName);
                    title.setText(userTitle);
                    email.setText(userEmail);
                } else {
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePhoto.setImageURI(imageUri);
        }
    }

    private void saveUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String newName = name.getText().toString();
            String newTitle = title.getText().toString();
            String newEmail = email.getText().toString();

            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("username", newName);
            userUpdates.put("title", newTitle);
            userUpdates.put("email", newEmail);

            if (imageUri != null) {
                StorageReference fileReference = storage.getReference("images").child(userId + ".jpg");
                fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newProfilePhotoUrl = uri.toString();
                        userUpdates.put("profilePhoto", newProfilePhotoUrl);
                        updateUserDocument(userId, userUpdates, oldProfilePhotoUrl);
                    });
                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to upload new profile photo", Toast.LENGTH_SHORT).show());
            } else {
                updateUserDocument(userId, userUpdates, null);
            }
        }
    }

    private void updateUserDocument(String userId, Map<String, Object> userUpdates, String oldProfilePhotoUrl) {
        db.collection("users").document(userId).update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                    if (oldProfilePhotoUrl != null && !oldProfilePhotoUrl.isEmpty()) {
                        StorageReference oldPhotoRef = storage.getReferenceFromUrl(oldProfilePhotoUrl);
                        oldPhotoRef.delete().addOnSuccessListener(aVoid1 -> {
                            // Old profile photo deleted successfully
                        }).addOnFailureListener(e -> {
                            // Failed to delete old profile photo
                        });
                    }
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frameLayoutt, new settings())
                            .addToBackStack(null)
                            .commit();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show());
    }
}