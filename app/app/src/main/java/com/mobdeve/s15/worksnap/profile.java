package com.mobdeve.s15.worksnap;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<MyPhotoData> PhotoDataList;
    private MyPhotoAdapter MyPhotoAdapter;
    TextView allphotosText;
    TextView badgesText;


    LinearLayout btnGoToCamera;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inflate the layout for this fragment
        allphotosText = view.findViewById(R.id.allphotosText);
        badgesText = view.findViewById(R.id.badgesText);
        allphotosText.setOnClickListener(this::chooseAllPhotos);
        badgesText.setOnClickListener(this::chooseBadges);

        RecyclerView recyclerView = view.findViewById(R.id.allPhotosRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        PhotoDataList = new ArrayList<MyPhotoData>();
        for(int i = 0; i < 46; i++) {
            PhotoDataList.add(new MyPhotoData(R.drawable.danda));
        }

        MyPhotoAdapter = new MyPhotoAdapter(PhotoDataList,(MainActivity) getActivity());
        recyclerView.setAdapter(MyPhotoAdapter);

        btnGoToCamera = view.findViewById(R.id.activity_camera_view_btn_gotocamera);

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
    }
    public void chooseBadges(View v) {
        allphotosText.setTypeface(null, Typeface.NORMAL);
        badgesText.setTypeface(null, Typeface.BOLD);
    }
}