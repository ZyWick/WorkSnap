package com.mobdeve.s15.worksnap;

import java.util.ArrayList;

public class leaderboardModel {
    private String employeeName;
    private int[] badges;
    private String profilePhotoPath;

    public int getImageCountDaily() {
        return imageCountDaily;
    }

    public void setImageCountDaily(int imageCountDaily) {
        this.imageCountDaily = imageCountDaily;
    }

    public int getImageCountWeekly() {
        return imageCountWeekly;
    }

    public void setImageCountWeekly(int imageCountWeekly) {
        this.imageCountWeekly = imageCountWeekly;
    }

    public int getImageCountYearly() {
        return imageCountYearly;
    }

    public void setImageCountYearly(int imageCountYearly) {
        this.imageCountYearly = imageCountYearly;
    }

    private int imageCountDaily;
    private int imageCountWeekly;
    private int imageCountYearly;


    public leaderboardModel(int[] badges, String profilePhotoPath, int imageCountDaily, int imageCountWeekly, int imageCountYearly,String employeeName) {
        this.badges = badges;
        this.profilePhotoPath = profilePhotoPath;
        this.employeeName = employeeName;
        this.imageCountDaily = imageCountDaily;
        this.imageCountWeekly = imageCountWeekly;
        this.imageCountYearly = imageCountYearly;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public int[] getBadges() {
        return badges;
    }
    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }
}
