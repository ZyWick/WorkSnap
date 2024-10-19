package com.mobdeve.s15.worksnap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder> {

    ArrayList<MyPhotoData> myPhotoData;
    Context context;

    public MyPhotoAdapter(ArrayList<MyPhotoData> myPhotoData, MainActivity activity) {
        this.myPhotoData = myPhotoData;
        this.context = activity;
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
        final MyPhotoData myPhotoDataList = myPhotoData.get(position);
        holder.photoImage.setImageResource(myPhotoDataList.getPhotoImage());
    }

    @Override
    public int getItemCount() {
        return myPhotoData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.attendanceImage);

        }
    }

}
