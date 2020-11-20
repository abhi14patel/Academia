package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DisciplinaryAction_Dto implements Serializable {

    private String incidentType;
    private String incidentDetails;
    private String reporterName;
    private String actionTaken;
    private String remarks;
    private Long dateOfAction;
    private Long incidentDate;
    private String code;
    private String value;
    private ArrayList<DisciplinaryDocument_Dto> disciplinaryDocument_dtos;

    public DisciplinaryAction_Dto(String incidentType, String incidentDetails, String reporterName, String actionTaken, String remarks, Long dateOfAction, Long incidentDate, String code, String value, ArrayList<DisciplinaryDocument_Dto> disciplinaryDocument_dtos) {
        this.incidentType = incidentType;
        this.incidentDetails = incidentDetails;
        this.reporterName = reporterName;
        this.actionTaken = actionTaken;
        this.remarks = remarks;
        this.dateOfAction = dateOfAction;
        this.incidentDate = incidentDate;
        this.code = code;
        this.value = value;
        this.disciplinaryDocument_dtos = disciplinaryDocument_dtos;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getIncidentDetails() {
        return incidentDetails;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public String getRemarks() {
        return remarks;
    }

    public Long getDateOfAction() {
        return dateOfAction;
    }

    public Long getIncidentDate() {
        return incidentDate;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<DisciplinaryDocument_Dto> getDisciplinaryDocument_dtos() {
        return disciplinaryDocument_dtos;
    }
}
