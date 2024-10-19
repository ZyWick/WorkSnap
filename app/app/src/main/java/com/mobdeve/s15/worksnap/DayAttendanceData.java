package com.mobdeve.s15.worksnap;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class DayAttendanceData {
    private Timestamp Date;
    private ArrayList<AttendancePhotoData> AttendancePhotoDataList;

    public DayAttendanceData(Timestamp Date, ArrayList<AttendancePhotoData> AttendancePhotoDataList) {
        this.Date = Date;
        this.AttendancePhotoDataList = AttendancePhotoDataList;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp Date) {
        this.Date = Date;
    }

    public ArrayList<AttendancePhotoData> getAttendancePhotoDataList() {
        return AttendancePhotoDataList;
    }

    public void setAttendancePhotoDataList(ArrayList<AttendancePhotoData> AttendancePhotoDataList) {
        this.AttendancePhotoDataList = AttendancePhotoDataList;
    }
}
