package com.mobdeve.s15.worksnap;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s15.worksnap.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(MyFirestoreReferences.USERS_COLLECTION).document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        List<String> arrayField = (List<String>) document.get("employees"); // Replace with your array field name
                        String employer = document.getString("employer");
                        Boolean isEmployer = !(arrayField == null || arrayField.isEmpty());
                        Boolean isEmployee = !(employer == null || employer.isEmpty());

                        if (isEmployer && isEmployee) {
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.employer_bottom_nav_menu);
                            replaceFragment(new leaderboard());
                        } else if (isEmployer) {
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.menu_b);
                            replaceFragment(new AllEmployee());
                        } else if (isEmployee) {
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.menu_c);
                            replaceFragment(new leaderboard());
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.LeaderboardMenu) {
                replaceFragment(new leaderboard());
            } else if (itemId == R.id.ProfileMenu) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();
                    replaceFragment(profile.newInstance(uid));
                }
            } else if (itemId == R.id.allEmployeesMenu) {
                replaceFragment(new AllEmployee());
            } else if (itemId == R.id.checkEmployeeMenu) {
                replaceFragment(new checkEmployees());
            } else if (itemId == R.id.SettingsMenu) {
                replaceFragment(new settings());
            }
            return true;
        });

    }

    private void replaceFragment (Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid); // Pass the uid
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutt, fragment);
        fragmentTransaction.commit();
    }

}