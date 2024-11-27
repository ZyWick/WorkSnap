package com.mobdeve.s15.worksnap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.bumptech.glide.Glide;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder> {
    private ArrayList<MyPhotoData> photoDataList;
    private Context context;

    public MyPhotoAdapter(ArrayList<MyPhotoData> photoDataList, Context context) {
        this.photoDataList = photoDataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendancephotoholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyPhotoData photoData = photoDataList.get(position);
        if (context != null && photoData.getImageUrl() != null && holder.attendanceImage != null) {
            Glide.with(context)
                    .load(photoData.getImageUrl())
                    .placeholder(R.drawable.danda1) // Optional: Add a placeholder image
                    .error(R.drawable.danda1)       // Optional: Add an error image
                    .into(holder.attendanceImage);
        } else {
            Log.e("MyPhotoAdapter", "Context, imageUrl, or attendanceImage is null");
        }
    }

    @Override
    public int getItemCount() {
        return photoDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView attendanceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            attendanceImage = itemView.findViewById(R.id.attendanceImage);
        }
    }
}