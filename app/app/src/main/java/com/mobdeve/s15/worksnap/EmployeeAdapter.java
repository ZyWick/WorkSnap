package com.mobdeve.s15.worksnap;

import android.app.Activity;
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

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    ArrayList<EmployeeData> EmployeeData;
    Context context;

    public EmployeeAdapter(ArrayList<EmployeeData> EmployeeData, MainActivity activity) {
        this.EmployeeData = EmployeeData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.employee_button,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EmployeeData theEmployeeData = EmployeeData.get(position);
        holder.employeeName.setText(theEmployeeData.getEmployeeName());
        holder.employeeProfile.setImageResource(theEmployeeData.getEmployeeImage());

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
        return EmployeeData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView employeeProfile;
        TextView employeeName;
        TextView textViewDesc;
        TextView textQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeProfile = itemView.findViewById(R.id.employeeProfileHolder);
            employeeName = itemView.findViewById(R.id.profile_name);
        }
    }

}
