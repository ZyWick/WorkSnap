package com.mobdeve.s15.worksnap;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class DayAttendanceData {

    private String date;
    private ArrayList<AttendancePhotoData> attendancePhotoDataList;

    public DayAttendanceData(String date) {
        this.date = date;
        this.attendancePhotoDataList = new ArrayList<>();
    }

    public DayAttendanceData() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AttendancePhotoData> getAttendancePhotoDataList() {
        return attendancePhotoDataList;
    }

    public void addAttendancePhotoData(AttendancePhotoData data) {
        this.attendancePhotoDataList.add(data);
    }

    public void setAttendancePhotoDataList(ArrayList<AttendancePhotoData> AttendancePhotoDataList) {
        this.attendancePhotoDataList = AttendancePhotoDataList;
    }
}
