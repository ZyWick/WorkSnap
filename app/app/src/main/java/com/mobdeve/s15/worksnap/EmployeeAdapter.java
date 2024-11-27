package com.mobdeve.s15.worksnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    ArrayList<EmployeeData> EmployeeData;
    Context context;

    private FragmentManager fragmentManager;

    public EmployeeAdapter(ArrayList<EmployeeData> EmployeeData, MainActivity activity, FragmentManager fragmentManager) {
        this.EmployeeData = EmployeeData;
        this.context = activity;
        this.fragmentManager = fragmentManager;
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


        String imageUrl = theEmployeeData.getEmployeeImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image into ImageView using Glide
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.danda) // Optional: Add a placeholder image
                    .error(R.drawable.momo)       // Optional: Add an error image
                    .into(holder.employeeProfile);
        } else {
            // Handle missing URL
            System.out.println("Image URL is empty!");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Remove this and replace it with an intent call*/
                Fragment newFragment = new profile(); // Replace with your fragment class

                // Begin the fragment transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutt, newFragment); // R.id.frameLayoutt is your container
                fragmentTransaction.addToBackStack(null); // Optionally add to back stack
                fragmentTransaction.commit();
            }
        });
    }

    public void setData(ArrayList<EmployeeData> EmployeeData) {
        this.EmployeeData = EmployeeData;
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
