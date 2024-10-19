package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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
        });
    }

    private void setLeaderboardModels(){
        int[] defaultBadges = {R.drawable.user, R.drawable.user, R.drawable.user};

        for (int i = 0; i < 45; i++){
            String employeeName = "Full Name" + (i+1);
            leaderboardModels.add(new leaderboardModel(defaultBadges, R.drawable.user, i, employeeName));
        }
    }
}