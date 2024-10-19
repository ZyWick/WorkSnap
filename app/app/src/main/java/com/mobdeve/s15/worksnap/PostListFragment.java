package com.mobdeve.s15.worksnap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<PostData> postDataList;
    private PostAdapter postAdapter;

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

        postDataList = new ArrayList<>();
        for (int i = 0; i < 46; i++) {
            postDataList.add(new PostData("Post " + i, "Location " + i, "Date " + i, R.drawable.danda));
        }

        postAdapter = new PostAdapter(postDataList, getActivity());
        recyclerView.setAdapter(postAdapter);

        ImageButton backButton = view.findViewById(R.id.post_back_button);
        // Functionality for backButton not yet implemented

        return view;
    }
}