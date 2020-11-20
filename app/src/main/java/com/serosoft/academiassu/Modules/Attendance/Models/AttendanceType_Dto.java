package com.serosoft.academiassu.Modules.Attendance.Models;

import java.io.Serializable;

public class AttendanceType_Dto implements Serializable {

    private String programName;
    private String sectionCode;
    private String facultyName;
    private String courseName;
    private int sectionId;
    private int totalRecords;
    private int presentRecords;
    private int absentRecords;
    private int courseVariantId;
    private int courseId;

    public AttendanceType_Dto(String programName, String sectionCode, String facultyName, String courseName, int sectionId, int totalRecords, int presentRecords, int absentRecords, int courseVariantId, int courseId) {
        this.programName = programName;
        this.sectionCode = sectionCode;
        this.facultyName = facultyName;
        this.courseName = courseName;
        this.sectionId = sectionId;
        this.totalRecords = totalRecords;
        this.presentRecords = presentRecords;
        this.absentRecords = absentRecords;
        this.courseVariantId = courseVariantId;
        this.courseId = courseId;
    }

    public String getProgramName() {
        return programName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getPresentRecords() {
        return presentRecords;
    }

    public int getAbsentRecords() {
        return absentRecords;
    }

    public int getCourseVariantId() {
        return courseVariantId;
    }

    public int getCourseId() {
        return courseId;
    }
}
