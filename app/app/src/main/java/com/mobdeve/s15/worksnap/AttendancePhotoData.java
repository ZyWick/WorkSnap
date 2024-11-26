package com.mobdeve.s15.worksnap;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class AttendancePhotoData {

    @DocumentId
    private DocumentReference imageID;
    private String employeeName;
    private DocumentReference employeeID;
    private Integer attendanceImage;
    private @ServerTimestamp Date takenAt;
//    private Timestamp takenAt;
    private double lon;
    private double lat;
    private boolean verified;
    private boolean rejected;

    public AttendancePhotoData(DocumentReference imageID, String employeeName, DocumentReference employeeID, Integer attendanceImage,
                               Date takenAt, double lon, double lat, boolean verified, boolean rejected) {
        this.imageID = imageID;
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.attendanceImage = attendanceImage;
        this.takenAt = takenAt;
        this.lon = lon;
        this.lat = lat;
        this.verified = verified;
        this.rejected = rejected;
    }

    public AttendancePhotoData(int a) {
        this.employeeName = "Charles";
        this.attendanceImage = R.drawable.danda;
        this.lon = 0.0040124;
        this.lat = 0.2313044;
    }

    public AttendancePhotoData() {

    }

    public DocumentReference getImageID() {
        return imageID;
    }

    public void setImageID(DocumentReference imageID) {
        this.imageID = imageID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public DocumentReference getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(DocumentReference employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAttendanceImage() {
        return attendanceImage;
    }

    public void setAttendanceImage(Integer attendanceImage) {
        this.attendanceImage = attendanceImage;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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