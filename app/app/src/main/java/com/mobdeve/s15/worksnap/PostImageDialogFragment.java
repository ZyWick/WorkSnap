package com.mobdeve.s15.worksnap;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

public class PostImageDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_RES_ID = "image_res_id";


    public static PostImageDialogFragment newInstance(String imageResId) {
        PostImageDialogFragment fragment = new PostImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_image_dialog, container, false);
        ImageView imageView = view.findViewById(R.id.dialog_image_view);

        if (getArguments() != null) {
            String imageResId = getArguments().getString(ARG_IMAGE_RES_ID);

            if (imageResId != null && !imageResId.isEmpty()) {
                // Load image into ImageView using Glide
                Glide.with(requireContext())
                        .load(imageResId)
                        .placeholder(R.drawable.danda) // Optional: Add a placeholder image
                        .error(R.drawable.momo)       // Optional: Add an error image
                        .into(imageView);
            } else {
                // Handle missing URL
                System.out.println("Image URL is empty!");
            }
        }

        view.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}