package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class HallTicket_Dto implements Serializable {

    private int totalOutstanding;
    boolean showAmount;
    private String programName,batchName,periodName,evaluationGroupCode,hallticket,hallTicketPath,currencyCode;

    public HallTicket_Dto(int totalOutstanding, boolean showAmount, String programName, String batchName, String periodName, String evaluationGroupCode, String hallticket, String hallTicketPath, String currencyCode) {
        this.totalOutstanding = totalOutstanding;
        this.showAmount = showAmount;
        this.programName = programName;
        this.batchName = batchName;
        this.periodName = periodName;
        this.evaluationGroupCode = evaluationGroupCode;
        this.hallticket = hallticket;
        this.hallTicketPath = hallTicketPath;
        this.currencyCode = currencyCode;
    }

    public int getTotalOutstanding() {
        return totalOutstanding;
    }

    public boolean isShowAmount() {
        return showAmount;
    }

    public String getProgramName() {
        return programName;
    }

    public String getBatchName() {
        return batchName;
    }

    public String getPeriodName() {
        return periodName;
    }

    public String getEvaluationGroupCode() {
        return evaluationGroupCode;
    }

    public String getHallticket() {
        return hallticket;
    }

    public String getHallTicketPath() {
        return hallTicketPath;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
