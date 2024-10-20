package com.mobdeve.s15.worksnap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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


    ArrayList<leaderboardModel> leaderboardModels = new ArrayList<>();
    int [] employeeImages = {R.drawable.danda};

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
        setLeaderboardModels();

        LB_RecyclerViewAdapter adapter = new LB_RecyclerViewAdapter(leaderboardModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    private void setLeaderboardModels(){
        int[] defaultBadges = {R.drawable.crown, R.drawable.crown, R.drawable.crown};

        for (int i = 0; i < 45; i++){
            String employeeName = "Full Name" + (i+1);
            leaderboardModels.add(new leaderboardModel(defaultBadges, R.drawable.danda, i, employeeName));
        }
    }
}