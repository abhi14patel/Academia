package com.serosoft.academiassu.Modules.Assignments.Assignment.Models;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Abhishek on October 31 2019.
 */

public class Assignment_Dto implements Serializable
{
    int id,docId,serial_no;
    long submission_last_date_long,publish_date_long;
    String courseName,courseCode,assignmentName,assignment_type,assignSectionType,hwOnlineSubmissions,courseVariant,submissionLastDate,publishDate,hwExtendedDate,facultyName;

    public Assignment_Dto() {
    }

    public Assignment_Dto(int id, int docId, int serial_no, long submission_last_date_long, long publish_date_long, String courseName, String courseCode, String assignmentName, String assignment_type, String assignSectionType, String hwOnlineSubmissions, String courseVariant, String submissionLastDate, String publishDate, String hwExtendedDate, String facultyName) {
        this.id = id;
        this.docId = docId;
        this.serial_no = serial_no;
        this.submission_last_date_long = submission_last_date_long;
        this.publish_date_long = publish_date_long;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.assignmentName = assignmentName;
        this.assignment_type = assignment_type;
        this.assignSectionType = assignSectionType;
        this.hwOnlineSubmissions = hwOnlineSubmissions;
        this.courseVariant = courseVariant;
        this.submissionLastDate = submissionLastDate;
        this.publishDate = publishDate;
        this.hwExtendedDate = hwExtendedDate;
        this.facultyName = facultyName;
    }

    public int getId() {
        return id;
    }

    public int getDocId() {
        return docId;
    }

    public int getSerial_no() {
        return serial_no;
    }

    public long getSubmission_last_date_long() {
        return submission_last_date_long;
    }

    public long getPublish_date_long() {
        return publish_date_long;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getAssignment_type() {
        return assignment_type;
    }

    public String getAssignSectionType() {
        return assignSectionType;
    }

    public String getHwOnlineSubmissions() {
        return hwOnlineSubmissions;
    }

    public String getCourseVariant() {
        return courseVariant;
    }

    public String getSubmissionLastDate() {
        return submissionLastDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getHwExtendedDate() {
        return hwExtendedDate;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public static Comparator<Assignment_Dto> sortByName = new Comparator<Assignment_Dto>()
    {
        @Override
        public int compare(Assignment_Dto m1, Assignment_Dto m2)
        {
            String name1 = m1.getCourseName();
            String name2 = m2.getCourseName();

            return name1.compareToIgnoreCase(name2);
        }
    };

    @Override
    public String toString() {
        return "assignment_type=" + assignment_type;
    }
}
