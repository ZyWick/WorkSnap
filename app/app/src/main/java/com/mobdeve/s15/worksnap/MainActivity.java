package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<leaderboardModel> leaderboardModels = new ArrayList<>();
    int [] employeeImages = {R.drawable.user};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.leaderboardRecyclerView);
        setLeaderboardModels();

        LB_RecyclerViewAdapter adapter = new LB_RecyclerViewAdapter(leaderboardModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mobdeve.s15.worksnap.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new settings());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.LeaderboardMenu) {
            } else if (itemId == R.id.ProfileMenu) {
                replaceFragment(new profile());
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

    private void setLeaderboardModels(){
        int[] defaultBadges = {R.drawable.user, R.drawable.user, R.drawable.user};

        for (int i = 0; i < 45; i++){
            String employeeName = "Full Name" + (i+1);
            leaderboardModels.add(new leaderboardModel(defaultBadges, R.drawable.user, i, employeeName));
        }
    }
    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutt, fragment);
        fragmentTransaction.commit();
    }

}