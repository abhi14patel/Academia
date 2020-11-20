package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class FollowUp_Dto implements Serializable {

    private long plannedDate;
    private long actualDate;
    private long nextDate;
    private String name;
    private String remarks;
    private String academyLocation;

    public FollowUp_Dto(long plannedDate, long actualDate, long nextDate, String name, String remarks, String academyLocation) {
        this.plannedDate = plannedDate;
        this.actualDate = actualDate;
        this.nextDate = nextDate;
        this.name = name;
        this.remarks = remarks;
        this.academyLocation = academyLocation;
    }

    public long getPlannedDate() {
        return plannedDate;
    }

    public long getActualDate() {
        return actualDate;
    }

    public long getNextDate() {
        return nextDate;
    }

    public String getName() {
        return name;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getAcademyLocation() {
        return academyLocation;
    }
}
