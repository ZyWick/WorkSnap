package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<PostData> postDataList;
    private PostAdapter postAdapter;
    private ArrayList<DayAttendanceData> dayAttendanceDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        recyclerView = view.findViewById(R.id.allPostRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Add space between items
        int space = getResources().getDimensionPixelSize(R.dimen.recycler_item_space);
        recyclerView.addItemDecoration(new PostRecyclerSpaceItemDecoration(space));

        Log.e("taggg", "" + dayAttendanceDataList);
        postDataList = new ArrayList<>();
        SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (DayAttendanceData ey : dayAttendanceDataList) {
            for (AttendancePhotoData yo : ey.getAttendancePhotoDataList()) {
                postDataList.add(new PostData(yo.getUsername(), yo.getLocation(),
                        dayFormatter.format(yo.getCreated_at()), yo.getImageLink()));
            }
        }

        postAdapter = new PostAdapter(postDataList, getActivity());
        recyclerView.setAdapter(postAdapter);

        ImageButton backButton = view.findViewById(R.id.post_back_button);
        // Functionality for backButton not yet implemented
        backButton.setOnClickListener(new View.OnClickListener() {
            /* TODO Call an intent for OrderActivity allowing you to order food */
            @Override
            public void onClick(View v) {
                /* Remove this and replace it with an intent call*/
                // Create the fragment
                checkEmployees newFragment = new checkEmployees();
                newFragment.setDayAttendanceList(dayAttendanceDataList);

                // Begin the fragment transaction
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutt, newFragment); // R.id.frameLayoutt is your container
                fragmentTransaction.addToBackStack(null); // Optionally add to back stack
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    public void setDayAttendanceList(ArrayList<DayAttendanceData> dayAttendanceList) {
        this.dayAttendanceDataList = dayAttendanceList;
    }
}