package com.mobdeve.s15.worksnap;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class AttendancePhotoData {

    @DocumentId
    private DocumentReference imageID;
    private String username;
    private String user_id;
    private String imageLink;
    private @ServerTimestamp Date created_at;
//    private Timestamp takenAt;
    private String location;
    private boolean verified;
    private boolean rejected;

    public AttendancePhotoData(DocumentReference imageID, String username, String user_id,
                               String imageLink, Date created_at, String location,
                               boolean verified, boolean rejected) {
        this.imageID = imageID;
        this.username = username;
        this.user_id = user_id;
        this.imageLink = imageLink;
        this.created_at = created_at;
        this.location = location;
        this.verified = verified;
        this.rejected = rejected;
    }

    public AttendancePhotoData(int a) {
//        this.employeeName = "Charles";
        this.imageLink = "https://firebasestorage.googleapis.com/v0/b/worksnap-9bdb3.firebasestorage.app/o/images%2F20b6e58c-40bb-40b7-9530-84ac1540efde.jpg?alt=media&token=1db735be-2832-4f71-aa8c-42a30c877f46";

    }

    public AttendancePhotoData() {

    }

    public DocumentReference getImageID() {
        return imageID;
    }

    public void setImageID(DocumentReference imageID) {
        this.imageID = imageID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }
}