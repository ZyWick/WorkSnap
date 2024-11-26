package com.mobdeve.s15.worksnap;

public class MyFirestoreReferences {

    // Collections
    public static final String
            BADGES_COLLECTION = "Badges",
            ATTENDANCE_PHOTOS_COLLECTION = "AttendancePhotos",
            USERS_COLLECTION = "users";

    // BadgesData fields
    public static final String
            BADGE_ID_FIELD = "badgeID",
            BADGE_NAME_FIELD = "badgeName",
            BADGE_IMAGE_FIELD = "badgeImage";

    // AttendancePhotoData fields
    public static final String
            IMAGE_ID_FIELD = "imageID",
            EMPLOYEE_NAME_FIELD = "employeeName",
            EMPLOYEE_ID_FIELD = "employeeID",
            ATTENDANCE_IMAGE_FIELD = "attendanceImage",
            TAKEN_AT_FIELD = "takenAt",
            LONGITUDE_FIELD = "lon",
            LATITUDE_FIELD = "lat",
            VERIFIED_FIELD = "verified",
            REJECTED_FIELD = "rejected";

    // EmployeeData fields
    public static final String
//            Already defined above
//            EMPLOYEE_ID_FIELD = "employeeID",
//            EMPLOYEE_NAME_FIELD = "employeeName",
            EMPLOYEE_IMAGE_FIELD = "employeeImage",
            TITLE_FIELD = "title",
            EMAIL_FIELD = "email",
            WORK_START_FIELD = "work_start",
            WORK_END_FIELD = "work_end",
            EMPLOYER_FIELD = "employer",
            EMPLOYEES_FIELD = "employees",
            IMAGE_COUNT_TODAY_FIELD = "image_count_today",
            IMAGE_COUNT_WEEK_FIELD = "image_count_week",
            IMAGE_COUNT_YEAR_FIELD = "image_count_year",
            BADGES_FIELD = "badges";

    private MyFirestoreReferences() {
        // Prevent instantiation
    }
}
