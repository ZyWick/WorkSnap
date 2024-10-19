package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

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
        replaceFragment(new leaderboard());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.LeaderboardMenu) {
                replaceFragment(new leaderboard());
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

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutt, fragment);
        fragmentTransaction.commit();
    }

}