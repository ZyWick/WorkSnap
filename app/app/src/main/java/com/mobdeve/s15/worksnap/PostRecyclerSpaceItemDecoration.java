package com.mobdeve.s15.worksnap;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostRecyclerSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public PostRecyclerSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = space;

//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = space;
//        }
    }
}