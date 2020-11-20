package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class Period_Dto implements Serializable {

    private String periodName;
    private int periodId;

    public Period_Dto(String periodName, int periodId) {
               this.periodName = periodName;
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public int getPeriodId() {
        return periodId;
    }

    @Override
    public String toString() {
        return  periodName;
    }
}
