package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class Approval_Dto implements Serializable {

    private long actionDate;
    private String actionTakenBy;
    private String remarks;
    private String actionType;
    private String assignedUser;

    public Approval_Dto(long actionDate, String actionTakenBy, String remarks, String actionType, String assignedUser) {
        this.actionDate = actionDate;
        this.actionTakenBy = actionTakenBy;
        this.remarks = remarks;
        this.actionType = actionType;
        this.assignedUser = assignedUser;
    }

    public long getActionDate() {
        return actionDate;
    }

    public String getActionTakenBy() {
        return actionTakenBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getActionType() {
        return actionType;
    }

    public String getAssignedUser() {
        return assignedUser;
    }
}
