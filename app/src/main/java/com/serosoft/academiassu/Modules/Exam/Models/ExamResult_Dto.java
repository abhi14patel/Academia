package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class ExamResult_Dto implements Serializable {

    private String programName,batch,period;
    private String batchPrintName,periodPrintName;
    private String programId,batchId,periodId;

    public ExamResult_Dto() {}

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

    public String getBatchPrintName() {
        return batchPrintName;
    }

    public void setBatchPrintName(String batchPrintName) {
        this.batchPrintName = batchPrintName;
    }

    public String getPeriodPrintName() {
        return periodPrintName;
    }

    public void setPeriodPrintName(String periodPrintName) {
        this.periodPrintName = periodPrintName;
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
}
