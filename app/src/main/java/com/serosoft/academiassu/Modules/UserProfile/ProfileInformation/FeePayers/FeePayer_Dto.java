package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers;

import java.io.Serializable;
import java.util.ArrayList;

public class FeePayer_Dto implements Serializable {

    private String payerType;
    private String payerName;
    private String workTelephone;
    private String mobileNumber;
    private String emailWork;
    private String emailHome;
    private Boolean isFeePayerConsentToCreditCheck;
    private String address;
    private int id;
    private String path;
    private String docName;

    public FeePayer_Dto(String payerType, String payerName, String workTelephone, String mobileNumber, String emailWork, String emailHome, Boolean isFeePayerConsentToCreditCheck, String address, int id, String path, String docName) {
        this.payerType = payerType;
        this.payerName = payerName;
        this.workTelephone = workTelephone;
        this.mobileNumber = mobileNumber;
        this.emailWork = emailWork;
        this.emailHome = emailHome;
        this.isFeePayerConsentToCreditCheck = isFeePayerConsentToCreditCheck;
        this.address = address;
        this.id = id;
        this.path = path;
        this.docName = docName;
    }

    public String getPayerType() {
        return payerType;
    }

    public String getPayerName() {
        return payerName;
    }

    public String getWorkTelephone() {
        return workTelephone;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailWork() {
        return emailWork;
    }

    public String getEmailHome() {
        return emailHome;
    }

    public Boolean getFeePayerConsentToCreditCheck() {
        return isFeePayerConsentToCreditCheck;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getDocName() {
        return docName;
    }
}
