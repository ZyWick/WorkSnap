package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import android.os.Parcel;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.mobdeve.s15.worksnap.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DayAttendanceData> DayAttendanceList = new ArrayList<DayAttendanceData>();
    private DayAttendanceAdapter DayAttendanceAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.dayAttendanceRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DayAttendanceList = new ArrayList<DayAttendanceData>();
        for(int i = 0; i < 20; i++) {
            ArrayList<AttendancePhotoData> AttendancePhotoDataList = new ArrayList<AttendancePhotoData>();
            int randomNumPhotos = (int) (Math.random() * 12 + 1);
            for(int j = 0; j < randomNumPhotos; j++) {
                AttendancePhotoDataList.add(new AttendancePhotoData());
            }
            DayAttendanceList.add(new DayAttendanceData(Timestamp.now(), AttendancePhotoDataList));
        }

        DayAttendanceAdapter = new DayAttendanceAdapter(DayAttendanceList,MainActivity.this);
        recyclerView.setAdapter(DayAttendanceAdapter);
//        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
//            return insets;
//        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new settings());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.LeaderboardMenu) {
            } else if (itemId == R.id.ProfileMenu) {
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutt, fragment);
        fragmentTransaction.commit();
    }

}