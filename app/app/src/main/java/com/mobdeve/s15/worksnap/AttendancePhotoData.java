package com.mobdeve.s15.worksnap;

import com.google.firebase.Timestamp;

public class AttendancePhotoData {

    private String employeeName;
    private int employeeID;
    private Integer attendanceImage;
    private Timestamp takenAt;
    private double lon;
    private double lat;


    public AttendancePhotoData(String employeeName, int employeeID, Integer foodImage, Timestamp takenAt, double lon, double lat) {
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.attendanceImage = foodImage;
        this.takenAt = takenAt;
        this.lon = lon;
        this.lat = lat;
    }

    public AttendancePhotoData() {
        this.employeeName = "Charles";
        this.employeeID = 1;
        this.attendanceImage = R.drawable.danda;
        this.takenAt = Timestamp.now();
        this.lon = 0.0040124;
        this.lat = 0.2313044;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAttendanceImage() {
        return attendanceImage;
    }

    public void setAttendanceImage(Integer attendanceImage) {
        this.attendanceImage = attendanceImage;
    }
    public Timestamp getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Timestamp takenAt) {
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



}