package com.mobdeve.s15.worksnap;


public class PostData {
    private String fullName;
    private String location;
    private String dateTime;
    private String pictureResId;

    public PostData(String fullName, String location, String dateTime, String pictureResId) {
        this.fullName = fullName;
        this.location = location;
        this.dateTime = dateTime;
        this.pictureResId = pictureResId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPictureResId() {
        return pictureResId;
    }
}