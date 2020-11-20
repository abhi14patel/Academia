package com.serosoft.academiassu.Modules.MyCourses.Models;

import java.io.Serializable;

public class MyCourse_Dto implements Serializable {

    private String courseName;
    private String courseCode;
    private String faculty;
    private String courseVarient;
    private int courseId;

    public MyCourse_Dto(String courseName, String courseCode, String faculty, String courseVarient, int courseId) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.faculty = faculty;
        this.courseVarient = courseVarient;
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getCourseVarient() {
        return courseVarient;
    }

    public int getCourseId() {
        return courseId;
    }
}
