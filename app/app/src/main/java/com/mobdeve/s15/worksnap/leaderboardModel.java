package com.mobdeve.s15.worksnap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private int LBprogress;

    public int getLBprogress() {
        return this.LBprogress;
    }

    public  void setLBprogress(int count_lb, int category) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int[] lbcategory_cap = new int[]{5, ((dayOfWeek-1) * 5), 125};
        this.LBprogress = (int) ((count_lb / (double)lbcategory_cap[category]) * 100);
    }


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
