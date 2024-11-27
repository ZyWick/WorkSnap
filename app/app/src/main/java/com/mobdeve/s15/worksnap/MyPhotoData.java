package com.mobdeve.s15.worksnap;

import android.graphics.Bitmap;

public class MyPhotoData {
    private String imageUrl;

    public MyPhotoData(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}