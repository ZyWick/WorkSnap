package com.mobdeve.s15.worksnap;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    TextView allphotosText;
    TextView badgesText;
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
        allphotosText = findViewById(R.id.allphotosText);
        badgesText = findViewById(R.id.badgesText);
        RecyclerView recyclerView = findViewById(R.id.allPhotosRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        PhotoDataList = new ArrayList<MyPhotoData>();
        for(int i = 0; i < 46; i++) {
            PhotoDataList.add(new MyPhotoData(R.drawable.user_filled));
        }

        MyPhotoAdapter = new MyPhotoAdapter(PhotoDataList,MainActivity.this);
        recyclerView.setAdapter(MyPhotoAdapter);
    }
    public void chooseAllPhotos(View v){
        allphotosText.setTypeface(null, Typeface.BOLD);
        badgesText.setTypeface(null, Typeface.NORMAL);
    }
    public void chooseBadges(View v){
        allphotosText.setTypeface(null, Typeface.NORMAL);
        badgesText.setTypeface(null, Typeface.BOLD);
    }
}