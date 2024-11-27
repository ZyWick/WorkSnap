package com.mobdeve.s15.worksnap;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
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


    FirebaseFirestore db;
    CollectionReference usersRef, imagesRef ;
    int[] leaderboard_categories;

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostData post = postList.get(position);
        String theDate = post.getDateTime();

        holder.fullName.setText(post.getFullName());
        holder.location.setText(post.getLocation());
        holder.dateTime.setText(theDate);

        String imageUrl = post.getPictureResId();
        String userID = post.getUserID();
        String imageID = post.getImageID();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load image into ImageView using Glide
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.danda1) // Optional: Add a placeholder image
                    .error(R.drawable.danda1)       // Optional: Add an error image
                    .into(holder.picture);
        } else {
            // Handle missing URL
            System.out.println("Image URL is empty!");
        }

        // Set up click listener for the picture
        holder.picture.setOnClickListener(v -> {
            PostImageDialogFragment dialogFragment = PostImageDialogFragment.newInstance(post.getPictureResId());
            dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "post_image_dialog");
        });

        final boolean[] imageUpdateSuccess = {false};
        final boolean[] userUpdateSuccess = {false};

        // Callback to check when both updates are complete

        db = FirebaseFirestore.getInstance();
        imagesRef = db.collection(MyFirestoreReferences.IMAGES_COLLECTION);
        usersRef = db.collection(MyFirestoreReferences.USERS_COLLECTION);
        DocumentReference theImage = imagesRef.document(imageID);
        Log.e("taggg", "" + imageID);
        DocumentReference theUser = usersRef.document(userID);

        holder.btnVerify.setOnClickListener(v->{

            Task<Void> imageUpdateTask = theImage.update("verified", true)
                    .addOnSuccessListener(aVoid -> {imageUpdateSuccess[0] = true; Log.d(TAG, "Image document successfully updated!");})
                    .addOnFailureListener(e -> {imageUpdateSuccess[0] = false; Log.w(TAG, "Error updating image document");});

            int[] leaderboard_categories = checkDateWithin(theDate);
            Task<Void> userUpdateTask = theUser.update("image_count_today", FieldValue.increment(leaderboard_categories[2]),
                            "image_count_week", FieldValue.increment(leaderboard_categories[1]),
                            "image_count_year", FieldValue.increment(leaderboard_categories[0]))
                    .addOnSuccessListener(aVoid -> {userUpdateSuccess[0] = true; Log.d(TAG, "User document successfully updated!");})
                    .addOnFailureListener(e -> {userUpdateSuccess[0] = false; Log.w(TAG, "Error updating user document", e);});

            Tasks.whenAllSuccess(imageUpdateTask, userUpdateTask)
                    .addOnSuccessListener(aVoid -> {
                        // If both tasks were successful
                        Log.d(TAG, "Both documents successfully updated!");
                        // Perform additional actions here (e.g., update UI)
                        removeItem(position);
                    })
                    .addOnFailureListener(e -> {
                        // If either task failed, revert the successful one
                        Log.w(TAG, "One or both tasks failed, reverting updates.");
                        if (imageUpdateSuccess[0]) theImage.update("verified", false)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Reverted image document update"))
                                .addOnFailureListener(lole -> Log.w(TAG, "Error reverting image document", lole));

                        // If the user update succeeded but image update failed, revert the user update.
                        if (userUpdateSuccess[0]) theUser.update("image_count_today", FieldValue.increment(-leaderboard_categories[2]),
                                        "image_count_week", FieldValue.increment(-leaderboard_categories[1]),
                                        "image_count_year", FieldValue.increment(-leaderboard_categories[0]))
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Reverted user document update"))
                                .addOnFailureListener(eeee -> Log.w(TAG, "Error reverting user document", eeee));
                    });

        });

        holder.btnWrong.setOnClickListener(v->{
            theImage
                    .update("rejected", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            removeItem(position);
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
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

    public void removeItem(int position) {
        // Remove the item from the data list
        postList.remove(position);
        // Notify the adapter about the removed item
        notifyItemRemoved(position);
    }

    public static int[] checkDateWithin(String inputDate) {
        // Get today's date components
        Calendar today = Calendar.getInstance();
        int yearToday = today.get(Calendar.YEAR);
        int weekToday = today.get(Calendar.WEEK_OF_YEAR);
        int dayOfYearToday = today.get(Calendar.DAY_OF_YEAR);

        // Extract year, month, and day from the input string
        int inputYear = Integer.parseInt(inputDate.substring(0, 4));
        int inputMonth = Integer.parseInt(inputDate.substring(5, 7));
        int inputDay = Integer.parseInt(inputDate.substring(8, 10));

        // Create a Calendar instance for the input date
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.set(inputYear, inputMonth - 1, inputDay);

        // Calculate the week and day of year for the input date
        int inputWeek = inputCalendar.get(Calendar.WEEK_OF_YEAR);
        int inputDayOfYear = inputCalendar.get(Calendar.DAY_OF_YEAR);

        // Year match
        int sameYear = (inputYear == yearToday) ? 1 : 0;

        // Week match
        int sameWeek = (sameYear == 1 && inputWeek == weekToday) ? 1 : 0;

        // Day match
        int sameDay = (sameYear == 1 && inputDayOfYear == dayOfYearToday) ? 1 : 0;

        return new int[]{sameYear, sameWeek, sameDay};
    }
}