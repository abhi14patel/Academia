package com.serosoft.academiassu.Modules.Attendance.Models;

import java.io.Serializable;

public class Attendance_Dto implements Serializable {

    private String programName,batch,period,section,courseCode,courseVariant;
    private String programId,batchId,periodId,sectionId,courseCodeId,courseVariantId;
    private String startDate, endDate;
    private String percentageTo,percentageFrom;

    public Attendance_Dto() {}

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseVariant() {
        return courseVariant;
    }

    public void setCourseVariant(String courseVariant) {
        this.courseVariant = courseVariant;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCourseCodeId() {
        return courseCodeId;
    }

    public void setCourseCodeId(String courseCodeId) {
        this.courseCodeId = courseCodeId;
    }

    public String getCourseVariantId() {
        return courseVariantId;
    }

    public void setCourseVariantId(String courseVariantId) {
        this.courseVariantId = courseVariantId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPercentageTo() {
        return percentageTo;
    }

    public void setPercentageTo(String percentageTo) {
        this.percentageTo = percentageTo;
    }

    public String getPercentageFrom() {
        return percentageFrom;
    }

    public void setPercentageFrom(String percentageFrom) {
        this.percentageFrom = percentageFrom;
    }
}
