package com.mobdeve.s15.worksnap;

import java.util.ArrayList;

public class leaderboardModel {
    private String employeeName;
    private int employeeID;
    private int[] badges;
    private Integer employeeImage;


    public leaderboardModel(int[] badges, Integer employeeImage, int employeeID, String employeeName) {
        this.badges = badges;
        this.employeeImage = employeeImage;
        this.employeeID = employeeID;
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeImage() {
        return employeeImage;
    }

    public int[] getBadges() {
        return badges;
    }

    public void setEmployeeImage(Integer employeeImage) {
        this.employeeImage = employeeImage;
    }


    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
}
