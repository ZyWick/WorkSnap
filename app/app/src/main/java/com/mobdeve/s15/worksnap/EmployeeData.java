package com.mobdeve.s15.worksnap;

public class EmployeeData {

    private String employeeName;
    private int employeeID;
    private int[] badges;
    private Integer employeeImage;

    public EmployeeData(String employeeName, int employeeID, Integer foodImage , int[] badges) {
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.employeeImage = foodImage;
        this.badges = badges;
    }

    public EmployeeData(String employeeName) {
        this.employeeName = employeeName;
        this.employeeID = 1;
        this.employeeImage = R.drawable.ic_launcher_foreground;
        this.badges = new int[]{};
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

    public Integer getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(Integer employeeImage) {
        this.employeeImage = employeeImage;
    }

    public int[] getBadges() {return badges;}

    public void setBadges(int[] badges) {this.badges = badges;}

}