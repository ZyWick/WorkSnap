package com.mobdeve.s15.worksnap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostData> postList;
    private Context context;

    public PostAdapter(List<PostData> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostData post = postList.get(position);
        holder.fullName.setText(post.getFullName());
        holder.location.setText(post.getLocation());
        holder.dateTime.setText(post.getDateTime());
        holder.picture.setImageResource(post.getPictureResId());

        // Set up click listener for the picture
        holder.picture.setOnClickListener(v -> {
            PostImageDialogFragment dialogFragment = PostImageDialogFragment.newInstance(post.getPictureResId());
            dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "post_image_dialog");
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, location, dateTime;
        ImageView picture;
        Button btnWrong, btnVerify;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            location = itemView.findViewById(R.id.location);
            dateTime = itemView.findViewById(R.id.dateTime);
            picture = itemView.findViewById(R.id.picture);
            btnWrong = itemView.findViewById(R.id.btnWrong);
            btnVerify = itemView.findViewById(R.id.btnVerify);
        }
    }
}