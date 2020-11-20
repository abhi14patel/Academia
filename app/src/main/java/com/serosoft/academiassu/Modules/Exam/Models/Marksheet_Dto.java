package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class Marksheet_Dto implements Serializable {

    private int id;
    private String programName,sectionName,batchName,periodName,marksheetPath;

    public Marksheet_Dto(int id, String programName, String sectionName, String batchName, String periodName, String marksheetPath) {
        this.id = id;
        this.programName = programName;
        this.sectionName = sectionName;
        this.batchName = batchName;
        this.periodName = periodName;
        this.marksheetPath = marksheetPath;
    }

    public int getId() {
        return id;
    }

    public String getProgramName() {
        return programName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getBatchName() {
        return batchName;
    }

    public String getPeriodName() {
        return periodName;
    }

    public String getMarksheetPath() {
        return marksheetPath;
    }
}
