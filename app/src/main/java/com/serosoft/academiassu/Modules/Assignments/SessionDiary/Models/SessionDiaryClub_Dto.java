package com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhishek on October 24 2019.
 */

public class SessionDiaryClub_Dto implements Serializable {

    String courseName;
    ArrayList<SessionDiary_Dto> sessionDiaryList;

    public SessionDiaryClub_Dto(String courseName, ArrayList<SessionDiary_Dto> sessionDiaryList) {
        this.courseName = courseName;
        this.sessionDiaryList = sessionDiaryList;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<SessionDiary_Dto> getSessionDiaryList() {
        return sessionDiaryList;
    }

    public void setSessionDiaryList(ArrayList<SessionDiary_Dto> sessionDiaryList) {
        this.sessionDiaryList = sessionDiaryList;
    }
}
