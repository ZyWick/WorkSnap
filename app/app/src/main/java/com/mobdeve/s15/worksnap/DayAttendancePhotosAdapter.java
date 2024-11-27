package com.mobdeve.s15.worksnap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DayAttendancePhotosAdapter extends RecyclerView.Adapter<DayAttendancePhotosAdapter.ViewHolder> {

    ArrayList<AttendancePhotoData> AttendancePhotoData;
    Context context;

    private FragmentManager fragmentManager;

    public DayAttendancePhotosAdapter(ArrayList<AttendancePhotoData> AttendancePhotoData, MainActivity activity, FragmentManager fragmentManager) {
        this.AttendancePhotoData = AttendancePhotoData;
        this.context = activity;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.attendancephotoholder,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AttendancePhotoData theAttendancePhotoData = AttendancePhotoData.get(position);

        String imageUrl = theAttendancePhotoData.getImageLink();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image into ImageView using Glide
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.danda) // Optional: Add a placeholder image
                    .error(R.drawable.momo)       // Optional: Add an error image
                    .into(holder.attendancePhoto);
        } else {
            // Handle missing URL
            System.out.println("Image URL is empty!");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            /* TODO Call an intent for OrderActivity allowing you to order food */
            @Override
            public void onClick(View v) {
                /* Remove this and replace it with an intent call*/
                Fragment newFragment = new PostListFragment(); // Replace with your fragment class

                // Begin the fragment transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutt, newFragment); // R.id.frameLayoutt is your container
                fragmentTransaction.addToBackStack(null); // Optionally add to back stack
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return AttendancePhotoData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView attendancePhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            attendancePhoto = itemView.findViewById(R.id.attendanceImage);
        }
    }

}
