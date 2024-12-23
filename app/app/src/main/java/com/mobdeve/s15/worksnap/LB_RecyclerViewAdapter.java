package com.mobdeve.s15.worksnap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Import Glide
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LB_RecyclerViewAdapter extends RecyclerView.Adapter<LB_RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<leaderboardModel> leaderboardModels;

    public LB_RecyclerViewAdapter(ArrayList<leaderboardModel> leaderboardModels) {
        this.leaderboardModels = leaderboardModels;
    }

    @NonNull
    @Override
    public LB_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LB_RecyclerViewAdapter.MyViewHolder holder, int position) {
        leaderboardModel currentItem = leaderboardModels.get(position);

        holder.employeeNameTextView.setText(currentItem.getEmployeeName());

        String imageUrl = currentItem.getProfilePhotoPath();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image into ImageView using Glide
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.danda1) // Optional: Add a placeholder image
                    .error(R.drawable.danda1)       // Optional: Add an error image// Optional: Add an error image
                    .into(holder.employeeImageView);
        } else {
            // Handle missing URL
            System.out.println("Image URL is empty!");
        }

        holder.progressBar.setProgress(currentItem.getLBprogress(), true);



        // Load badges
        for (int i = 0; i < 3; i++) {
            holder.badgeImageViews.get(i).setImageResource(currentItem.getBadges()[i]);
        }

    }

    @Override
    public int getItemCount() {
        return leaderboardModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView employeeNameTextView;
        ImageView employeeImageView;
        ArrayList<ImageView> badgeImageViews;
        LinearProgressIndicator progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeNameTextView = itemView.findViewById(R.id.textView);
            employeeImageView = itemView.findViewById(R.id.imageView);
            progressBar = itemView.findViewById(R.id.progressBar);

            badgeImageViews = new ArrayList<>(3);
            badgeImageViews.add(itemView.findViewById(R.id.imageView4));
            badgeImageViews.add(itemView.findViewById(R.id.imageView5));
            badgeImageViews.add(itemView.findViewById(R.id.imageView6));
        }
    }
}