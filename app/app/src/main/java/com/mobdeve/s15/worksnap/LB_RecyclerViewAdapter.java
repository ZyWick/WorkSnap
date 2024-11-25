package com.mobdeve.s15.worksnap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LB_RecyclerViewAdapter extends RecyclerView.Adapter<LB_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<leaderboardModel> leaderboardModels;


    public LB_RecyclerViewAdapter(ArrayList<leaderboardModel> leaderboardModels) {
        this.leaderboardModels = leaderboardModels;
    }

    @NonNull
    @Override
    public LB_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new LB_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LB_RecyclerViewAdapter.MyViewHolder holder, int position) {
        leaderboardModel currentItem = leaderboardModels.get(position);

        holder.employeeNameTextView.setText(currentItem.getEmployeeName());
        holder.employeeImageView.setImageResource(currentItem.getEmployeeImage());

        for (int i = 0; i < 3; i++) {
            holder.badgeImageViews.get(i).setImageResource(currentItem.getBadges()[i]);
        }
    }

    @Override
    public int getItemCount() {
        return 45;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView employeeNameTextView;
        ImageView employeeImageView;
        ArrayList<ImageView> badgeImageViews;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeNameTextView = itemView.findViewById(R.id.textView);
            employeeImageView = itemView.findViewById(R.id.textureView);

            badgeImageViews = new ArrayList<>(3);
            badgeImageViews.add(itemView.findViewById(R.id.imageView4));
            badgeImageViews.add(itemView.findViewById(R.id.imageView5));
            badgeImageViews.add(itemView.findViewById(R.id.imageView6));
        }
    }
}
