package com.serosoft.academiassu.Modules.Assignments.Assignment.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhishek on October 31 2019.
 */

public class AssignmentClub_Dto implements Serializable {

    String courseName;
    ArrayList<Assignment_Dto> assignmentList;

    public AssignmentClub_Dto(String courseName, ArrayList<Assignment_Dto> assignmentList) {
        this.courseName = courseName;
        this.assignmentList = assignmentList;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<Assignment_Dto> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(ArrayList<Assignment_Dto> assignmentList) {
        this.assignmentList = assignmentList;
    }
}
