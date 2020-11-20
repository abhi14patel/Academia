package com.serosoft.academiassu.Modules.Assignments.Assignment.Models;

public class Student_Dto {

    int studentId;
    int groupAssignmentId;
    String status;

    public Student_Dto(int studentId, int groupAssignmentId, String status) {
        this.studentId = studentId;
        this.groupAssignmentId = groupAssignmentId;
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGroupAssignmentId() {
        return groupAssignmentId;
    }

    public void setGroupAssignmentId(int groupAssignmentId) {
        this.groupAssignmentId = groupAssignmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
