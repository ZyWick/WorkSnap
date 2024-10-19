package com.mobdeve.s15.worksnap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DayAttendancePhotosAdapter extends RecyclerView.Adapter<DayAttendancePhotosAdapter.ViewHolder> {

    ArrayList<AttendancePhotoData> AttendancePhotoData;
    Context context;

    public DayAttendancePhotosAdapter(ArrayList<AttendancePhotoData> AttendancePhotoData, MainActivity activity) {
        this.AttendancePhotoData = AttendancePhotoData;
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
        final AttendancePhotoData theAttendancePhotoData = AttendancePhotoData.get(position);
        holder.attendancePhoto.setImageResource(theAttendancePhotoData.getAttendanceImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            /* TODO Call an intent for OrderActivity allowing you to order food */
            @Override
            public void onClick(View v) {
                /* Remove this and replace it with an intent call*/
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
