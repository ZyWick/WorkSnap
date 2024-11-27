package com.mobdeve.s15.worksnap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
    // Just pass user id to this fragment from wherever you are calling it
    private static final String ARG_USER_ID = "userId";
    private String userId;

    RelativeLayout mainContent;

    private ArrayList<MyPhotoData> PhotoDataList;
    private MyPhotoAdapter MyPhotoAdapter;
    ImageView profileImage;
    TextView allphotosText;
    TextView badgesText;
    TextView employeeTitleText;
    TextView employeeNameText;
    LinearProgressIndicator progressBar;

    RecyclerView recyclerView;
    private boolean isLoading = false;
    private int visibleThreshold = 6;
    private DocumentSnapshot lastVisibleImage;

    CardView bottomCardView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    LinearLayout btnGoToCamera;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    public static profile newInstance(String userId) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mainContent = view.findViewById(R.id.mainContent);

        employeeTitleText = view.findViewById(R.id.employeeTitle);
        employeeNameText = view.findViewById(R.id.employeeName);
        profileImage = view.findViewById(R.id.profileImage);
        progressBar = view.findViewById(R.id.progressBar);

        // Inflate the layout for this fragment
        allphotosText = view.findViewById(R.id.allphotosText);
        badgesText = view.findViewById(R.id.badgesText);
        allphotosText.setOnClickListener(this::chooseAllPhotos);
        badgesText.setOnClickListener(this::chooseBadges);

        recyclerView = view.findViewById(R.id.allPhotosRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        PhotoDataList = new ArrayList<>();
        MyPhotoAdapter = new MyPhotoAdapter((ArrayList<MyPhotoData>) PhotoDataList, getActivity());
        recyclerView.setAdapter(MyPhotoAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loadMoreImages(userId);
                    isLoading = true;
                }
            }
        });

        bottomCardView = view.findViewById(R.id.bottomCardView);
        btnGoToCamera = view.findViewById(R.id.activity_camera_view_btn_gotocamera);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (userId != null) {
            fetchUserData(userId);
            fetchUserImages(userId);
            if (currentUser != null && userId.equals(currentUser.getUid())) {
                bottomCardView.setVisibility(View.VISIBLE);
            } else {
                bottomCardView.setVisibility(View.GONE);
            }
        } else {
            if (currentUser != null) {
                String uid = currentUser.getUid();
                fetchUserData(uid);
                fetchUserImages(uid);
                bottomCardView.setVisibility(View.VISIBLE);
            }
        }

        btnGoToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraView.class);
                startActivity(intent);
            }
        });



        return view;
    }

    public void chooseAllPhotos(View v){
        allphotosText.setTypeface(null, Typeface.BOLD);
        badgesText.setTypeface(null, Typeface.NORMAL);
        recyclerView.setVisibility(View.VISIBLE);
        // Hide images and show badges
    }
    public void chooseBadges(View v) {
        allphotosText.setTypeface(null, Typeface.NORMAL);
        badgesText.setTypeface(null, Typeface.BOLD);
        recyclerView.setVisibility(View.GONE);
    }

    private void fetchUserData(String uid) {
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String title = document.getString("title");
                    String name = document.getString("username");
                    String profilePicture = document.getString("profilePhoto");
                    Long imageCountWeek = document.getLong("image_count_week");

                    employeeTitleText.setText(title);
                    employeeNameText.setText(name);
                    if (profilePicture != null && !profilePicture.isEmpty()) {
                        Glide.with(this)
                                .load(profilePicture)
                                .into(profileImage);
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());

                    // Get the day of the week (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    // This shit is for the progress bar No clue what that total is supposed to be so its week /7 for now
                    if (imageCountWeek != null) {

                        int progress = (int) ((imageCountWeek / ((dayOfWeek-1) * 5)) * 100);
                        if (progressBar != null) {
                            progressBar.setProgress(progress, true);
                        }
                    }

                    mainContent.setVisibility(View.VISIBLE);
                } else {
                    // Handle the case where the document does not exist
                }
            } else {
                // Handle the error
            }
        });
    }

    private void fetchUserBadges(String uid) {
        // i fcuking love badges
    }

    private void fetchUserImages(String uid) {
        Log.d("profile", "Fetching images for user: " + uid);
        db.collection("images")
                .whereEqualTo("user_id", uid)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(12)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("profile", "fetchUserImages: task successful, number of images: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageLink = document.getString("imageLink");
                            if (imageLink != null && !imageLink.isEmpty()) {
                                PhotoDataList.add(new MyPhotoData(imageLink));
                            }
                        }
                        if (!task.getResult().isEmpty()) {
                            lastVisibleImage = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        }
                        MyPhotoAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("profile", "fetchUserImages: task failed", task.getException());
                    }
                });
    }

    private void loadMoreImages(String uid) {
        db.collection("images")
                .whereEqualTo("user_id", uid)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .startAfter(lastVisibleImage) // Start after the last visible image
                .limit(6) // Load 6 more images
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageLink = document.getString("imageLink");
                            if (imageLink != null && !imageLink.isEmpty()) {
                                PhotoDataList.add(new MyPhotoData(imageLink));
                            }
                        }
                        if (!task.getResult().isEmpty()) {
                            lastVisibleImage = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        }
                        MyPhotoAdapter.notifyDataSetChanged();
                        isLoading = false;
                    } else {
                        // Handle the error
                    }
                });
    }


}