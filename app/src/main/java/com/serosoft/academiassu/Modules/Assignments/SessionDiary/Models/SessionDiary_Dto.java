package com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Abhishek on October 24 2019.
 */

public class SessionDiary_Dto implements Serializable {

    int id;
    long date_long,fromSlot_long,toSlot_long;
    String date,sessionNo,topic,course,facultyName,courseVariant;
    ArrayList<SessionDocuemnt_Dto> docuemnt_dtos;

    public SessionDiary_Dto(int id, long date_long, long fromSlot_long, long toSlot_long, String date, String sessionNo, String topic, String course, String facultyName, String courseVariant, ArrayList<SessionDocuemnt_Dto> docuemnt_dtos) {
        this.id = id;
        this.date_long = date_long;
        this.fromSlot_long = fromSlot_long;
        this.toSlot_long = toSlot_long;
        this.date = date;
        this.sessionNo = sessionNo;
        this.topic = topic;
        this.course = course;
        this.facultyName = facultyName;
        this.courseVariant = courseVariant;
        this.docuemnt_dtos = docuemnt_dtos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate_long() {
        return date_long;
    }

    public long getFromSlot_long() {
        return fromSlot_long;
    }

    public long getToSlot_long() {
        return toSlot_long;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSessionNo() {
        return sessionNo;
    }

    public String getTopic() {
        return topic;
    }

    public String getCourse() {
        return course;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getCourseVariant() {
        return courseVariant;
    }

    public ArrayList<SessionDocuemnt_Dto> getDocuemnt_dtos() {
        return docuemnt_dtos;
    }

    public static Comparator<SessionDiary_Dto> sortByName = new Comparator<SessionDiary_Dto>()
    {
        @Override
        public int compare(SessionDiary_Dto m1, SessionDiary_Dto m2)
        {
            String name1 = m1.getCourse();
            String name2 = m2.getCourse();

            return name1.compareToIgnoreCase(name2);
        }
    };
}
