package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leaderboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leaderboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String uid = user.getUid();

    private LB_RecyclerViewAdapter adapter;
    ArrayList<leaderboardModel> leaderboardModels = new ArrayList<>();
    int[] employeeImages = {R.drawable.danda};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public leaderboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leaderboard.
     */
    // TODO: Rename and change types and number of parameters
    public static leaderboard newInstance(String param1, String param2) {
        leaderboard fragment = new leaderboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.leaderboardRecyclerView);


        adapter = new LB_RecyclerViewAdapter(leaderboardModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setLeaderboardModels();
        return view;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void setLeaderboardModels() {
        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Step 2: Check if the user has an employer
                        String employerUid = task.getResult().getString("employer");
                        if (employerUid != null && !employerUid.isEmpty()) {
                            // User is an employee, fetch the employer's document
                            fetchEmployerEmployees(employerUid);
                        } else {
                            Log.d("FirestoreInfo", "User does not have an employer assigned.");
                        }
                    } else {
                        Log.e("FirestoreError", "Failed to fetch user data", task.getException());
                    }
                });
    }

    private void fetchEmployerEmployees(String employerUid) {
        // Step 3: Fetch the employer's document to get the list of employees
        db.collection("users").document(employerUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> employeeUids = (ArrayList<String>) task.getResult().get("employees");
                        if (employeeUids != null && !employeeUids.isEmpty()) {
                            fetchEmployees(employeeUids); // Fetch details for each employee
                        } else {
                            Log.d("FirestoreInfo", "Employer does not have any employees.");
                        }
                    } else {
                        Log.e("FirestoreError", "Failed to fetch employer data", task.getException());
                    }
                });
    }

    private void fetchEmployees(ArrayList<String> employeeUids) {
        leaderboardModels.clear(); // Clear any existing leaderboard data
        for (String employeeUid : employeeUids) {
            db.collection("users").document(employeeUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            // Extract employee details
                            String employeeName = task.getResult().getString("username");
                            int imageCount = task.getResult().getLong("image_count_today").intValue();
                            int[] badges = {
                                    R.drawable.crown, // Example badge
                                    R.drawable.crown,
                                    R.drawable.crown
                            };
                            String profilePhotoPath = task.getResult().getString("profilePhoto");

                            // Add employee data to the leaderboard list
                            leaderboardModels.add(new leaderboardModel(
                                    badges,
                                    R.drawable.danda, // Replace with dynamic logic if necessary
                                    imageCount,
                                    employeeName
                            ));
                            adapter.notifyDataSetChanged(); // Notify adapter of changes
                        } else {
                            Log.e("FirestoreError", "Failed to fetch employee data", task.getException());
                        }
                    });
        }
    }
}
