package com.mobdeve.s15.worksnap;

import static android.text.format.DateUtils.isToday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayAttendanceAdapter extends RecyclerView.Adapter<DayAttendanceAdapter.ViewHolder> {

    ArrayList<DayAttendanceData> DayAttendanceData;
    Context context;

    public DayAttendanceAdapter(ArrayList<DayAttendanceData> DayAttendanceData, MainActivity activity) {
        this.DayAttendanceData = DayAttendanceData;
        this.context = activity;
    }

    public boolean isToday(Date date1, Date date2) {
        // Create Calendar instances for both dates
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        // Get the year, month, and day of each date
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);

        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        // Compare the year, month, and day values
        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dayattendancephotos,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DayAttendanceData theDayAttendanceData = DayAttendanceData.get(position);

        Date date = theDayAttendanceData.getDate().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MM / dd / yy", Locale.getDefault());
        holder.dayDate.setText(sdf.format(date));

        if (isToday(date, Timestamp.now().toDate())) holder.dayText.setText("Today");
        else {
            sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            holder.dayText.setText(sdf.format(date));
        }


        holder.photosRecycler.setHasFixedSize(true);
        holder.photosRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        holder.photosRecycler.setAdapter(
                new DayAttendancePhotosAdapter(theDayAttendanceData.getAttendancePhotoDataList(),
                        (MainActivity) context)
        );

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
        return DayAttendanceData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dayText;
        TextView dayDate;
        RecyclerView photosRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            dayDate = itemView.findViewById(R.id.dayDate);
            photosRecycler = itemView.findViewById(R.id.attendancePhotoRecycler);
        }
    }

}
