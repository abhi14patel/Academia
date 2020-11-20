package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class RequesterDetails_Dto implements Serializable {

    private String requeserName;
    private String batch;
    private String program;
    private String emailId;
    private String mobileNumber;

    public String getRequeserName() {
        return requeserName;
    }

    public void setRequeserName(String requeserName) {
        this.requeserName = requeserName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
