package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class Section_Dto implements Serializable {

    private String sectionName;
    private int sectionId;

    public Section_Dto(String sectionName, int sectionId) {
        this.sectionName = sectionName;
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSectionId() {
        return sectionId;
    }
}
