package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models;

import java.io.Serializable;

public class Document_Dto implements Serializable {

    int documentId;
    long submissionDate,inspectionDate;
    boolean isPreDefined;
    String documentName,docPath,status,remark,programName,seatType,value;

    public Document_Dto(int documentId, long submissionDate, long inspectionDate, boolean isPreDefined, String documentName, String docPath, String status, String remark, String programName, String seatType, String value) {
        this.documentId = documentId;
        this.submissionDate = submissionDate;
        this.inspectionDate = inspectionDate;
        this.isPreDefined = isPreDefined;
        this.documentName = documentName;
        this.docPath = docPath;
        this.status = status;
        this.remark = remark;
        this.programName = programName;
        this.seatType = seatType;
        this.value = value;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public long getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(long submissionDate) {
        this.submissionDate = submissionDate;
    }

    public long getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(long inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public boolean isPreDefined() {
        return isPreDefined;
    }

    public void setPreDefined(boolean preDefined) {
        isPreDefined = preDefined;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
