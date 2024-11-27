package com.mobdeve.s15.worksnap;

import static android.text.format.DateUtils.isToday;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    Fragment parentFragment;

    private FragmentManager fragmentManager;
    public DayAttendanceAdapter(ArrayList<DayAttendanceData> DayAttendanceData, MainActivity activity, FragmentManager fragmentManager, Fragment parentFragment) {
        this.DayAttendanceData = DayAttendanceData;
        this.context = activity;
        this.fragmentManager = fragmentManager;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dayattendancephotos,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public String getDayOfWeek (String groupDate) {
        String[] parts = groupDate.split("-");
        int year = Integer.parseInt(parts[0]);  // Year
        int month = Integer.parseInt(parts[1]); // Month
        int day = Integer.parseInt(parts[2]);   // Day

        // Adjust for January and February
        if (month < 3) {
            month += 12;
            year--;
        }

        // Apply Zeller's Congruence formula
        int h = (day + (13 * (month + 1)) / 5 + year + year / 4 + 6 * (year / 100) + year / 400) % 7;

        // Map the result to the day of the week (0 = Saturday, 1 = Sunday, ..., 6 = Friday)
        String[] daysOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        // Print the corresponding day of the week
        return daysOfWeek[h];
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DayAttendanceData theDayAttendanceData = DayAttendanceData.get(position);

        String thedate = theDayAttendanceData.getDate();
        holder.dayDate.setText(thedate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (thedate.equals(sdf.format(new Date()))) holder.dayText.setText("Today");
        else holder.dayText.setText(getDayOfWeek(thedate));

        holder.photosRecycler.setHasFixedSize(true);
        holder.photosRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        holder.photosRecycler.setAdapter(
                new DayAttendancePhotosAdapter(theDayAttendanceData.getAttendancePhotoDataList(),
                        (MainActivity) context, fragmentManager, parentFragment)
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
