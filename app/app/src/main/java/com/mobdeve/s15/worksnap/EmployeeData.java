package com.mobdeve.s15.worksnap;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentId;

public class EmployeeData {

    @DocumentId
    private DocumentReference employeeID; // Firestore document reference for the employee
    private String employeeName;
    private Integer employeeImage;
    private String title;
    private String email;
    private String work_start;
    private String work_end;
    private DocumentReference employer; // Firestore reference for the employer
    private DocumentReference[] employees; // Firestore references for subordinate employees
    private int image_count_today;
    private int image_count_week;
    private int image_count_year;
    private int[] badges;

    // Constructor with all fields
    public EmployeeData(DocumentReference employeeID, String employeeName, Integer employeeImage, String title,
                        String email, String work_start, String work_end, DocumentReference employer,
                        DocumentReference[] employees, int image_count_today, int image_count_week,
                        int image_count_year, int[] badges) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeImage = employeeImage;
        this.title = title;
        this.email = email;
        this.work_start = work_start;
        this.work_end = work_end;
        this.employer = employer;
        this.employees = employees;
        this.image_count_today = image_count_today;
        this.image_count_week = image_count_week;
        this.image_count_year = image_count_year;
        this.badges = badges;
    }

    // Simplified constructor
    public EmployeeData(String employeeName) {
        this.employeeName = employeeName;
        this.employeeImage = R.drawable.danda; // Placeholder for employee image
        this.badges = new int[]{}; // Default empty badge array
    }

    public EmployeeData() {

    }

    // Getter and Setter for employeeID
    public DocumentReference getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(DocumentReference employeeID) {
        this.employeeID = employeeID;
    }

    // Getter and Setter for employeeName
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    // Getter and Setter for employeeImage
    public Integer getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(Integer employeeImage) {
        this.employeeImage = employeeImage;
    }

    // Getter and Setter for badges
    public int[] getBadges() {
        return badges;
    }

    public void setBadges(int[] badges) {
        this.badges = badges;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for work_start
    public String getWork_start() {
        return work_start;
    }

    public void setWork_start(String work_start) {
        this.work_start = work_start;
    }

    // Getter and Setter for work_end
    public String getWork_end() {
        return work_end;
    }

    public void setWork_end(String work_end) {
        this.work_end = work_end;
    }

    // Getter and Setter for employer
    public DocumentReference getEmployer() {
        return employer;
    }

    public void setEmployer(DocumentReference employer) {
        this.employer = employer;
    }

    // Getter and Setter for employees
    public DocumentReference[] getEmployees() {
        return employees;
    }

    public void setEmployees(DocumentReference[] employees) {
        this.employees = employees;
    }

    // Getter and Setter for image_count_today
    public int getImage_count_today() {
        return image_count_today;
    }

    public void setImage_count_today(int image_count_today) {
        this.image_count_today = image_count_today;
    }

    // Getter and Setter for image_count_week
    public int getImage_count_week() {
        return image_count_week;
    }

    public void setImage_count_week(int image_count_week) {
        this.image_count_week = image_count_week;
    }

    // Getter and Setter for image_count_year
    public int getImage_count_year() {
        return image_count_year;
    }

    public void setImage_count_year(int image_count_year) {
        this.image_count_year = image_count_year;
    }
}
