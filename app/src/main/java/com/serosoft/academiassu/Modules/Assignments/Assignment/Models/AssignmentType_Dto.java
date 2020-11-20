package com.serosoft.academiassu.Modules.Assignments.Assignment.Models;

import java.io.Serializable;

public class AssignmentType_Dto implements Serializable {

    private int id;
    private String value;

    public AssignmentType_Dto(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
