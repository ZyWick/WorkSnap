package com.mobdeve.s15.worksnap;


public class PostData {
    private String fullName;
    private String location;
    private String dateTime;
    private String pictureResId;
    private String userID;
    private String ImageID;

    // Constructor to initialize all fields
    public PostData(String fullName, String location, String dateTime,
                    String pictureResId, String userID, String ImageID) {
        this.fullName = fullName;
        this.location = location;
        this.dateTime = dateTime;
        this.pictureResId = pictureResId;
        this.userID = userID;
        this.ImageID = ImageID;
    }

    // Getters
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

    public String getUserID() {
        return userID;
    }

    public String getImageID() {
        return ImageID;
    }

    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPictureResId(String pictureResId) {
        this.pictureResId = pictureResId;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setImageID(String ImageID) {
        this.ImageID = ImageID;
    }
}