package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MyPhotoData> PhotoDataList;
    private MyPhotoAdapter MyPhotoAdapter;
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
        RecyclerView recyclerView = findViewById(R.id.allPhotosRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        PhotoDataList = new ArrayList<MyPhotoData>();
        for(int i = 0; i < 46; i++) {
            PhotoDataList.add(new MyPhotoData(R.drawable.nav_profile));
        }

        MyPhotoAdapter = new MyPhotoAdapter(PhotoDataList,MainActivity.this);
        recyclerView.setAdapter(MyPhotoAdapter);
    }
}