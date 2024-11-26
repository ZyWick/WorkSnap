package com.mobdeve.s15.worksnap;


import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

public class badgesData {

    @DocumentId
    private DocumentReference badgeID;
    private String badgeName;
    private Integer badgeImage;

    // Constructor
    public badgesData(DocumentReference badgeID, String badgeName, Integer badgeImage) {
        this.badgeID = badgeID;
        this.badgeName = badgeName;
        this.badgeImage = badgeImage;
    }

    public badgesData() {

    }

    // Getters and Setters

    public DocumentReference getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(DocumentReference badgeID) {
        this.badgeID = badgeID;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public Integer getBadgeImage() {
        return badgeImage;
    }

    public void setBadgeImage(Integer badgeImage) {
        this.badgeImage = badgeImage;
    }
}
