package com.serosoft.academiassu.Modules.Attendance.Models;

import java.io.Serializable;

public class AttendanceCourse_Dto implements Serializable {

    private String courseVariantCode;
    private String courseCodeName;
    private int courseId;

    public AttendanceCourse_Dto() {
    }

    public AttendanceCourse_Dto(String courseVariantCode, String courseCodeName, int courseId) {
        this.courseVariantCode = courseVariantCode;
        this.courseCodeName = courseCodeName;
        this.courseId = courseId;
    }

    public String getCourseVariantCode() {
        return courseVariantCode;
    }

    public String getCourseCodeName() {
        return courseCodeName;
    }

    public int getCourseId() {
        return courseId;
    }
}
