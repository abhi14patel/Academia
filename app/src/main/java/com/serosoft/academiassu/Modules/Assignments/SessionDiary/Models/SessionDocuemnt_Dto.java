package com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models;

import java.io.Serializable;

/**
 * Created by Abhishek on October 24 2019.
 */

public class SessionDocuemnt_Dto implements Serializable {

    int id;
    String value,secondValue;

    public SessionDocuemnt_Dto(int id, String value, String secondValue) {
        this.id = id;
        this.value = value;
        this.secondValue = secondValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }
}
