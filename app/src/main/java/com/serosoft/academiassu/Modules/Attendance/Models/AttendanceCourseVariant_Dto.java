package com.serosoft.academiassu.Modules.Attendance.Models;

import java.io.Serializable;

public class AttendanceCourseVariant_Dto implements Serializable {

    private String courseVariantCode;
    private int id;

    public AttendanceCourseVariant_Dto(String courseVariantCode, int id) {
        this.courseVariantCode = courseVariantCode;
        this.id = id;
    }

    public String getCourseVariantCode() {
        return courseVariantCode;
    }

    public int getId() {
        return id;
    }
}
