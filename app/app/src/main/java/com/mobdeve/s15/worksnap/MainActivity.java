package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<EmployeeData> EmployeeDataList = new ArrayList<EmployeeData>();
    private EmployeeAdapter EmployeeAdapter;

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

        RecyclerView recyclerView = findViewById(R.id.allEmployeeRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        EmployeeDataList = new ArrayList<EmployeeData>();
        for(int i = 0; i < 46; i++) {
            EmployeeDataList.add(new EmployeeData("employee " + i));
        }

        EmployeeAdapter = new EmployeeAdapter(EmployeeDataList,MainActivity.this);
        recyclerView.setAdapter(EmployeeAdapter);
    }
}