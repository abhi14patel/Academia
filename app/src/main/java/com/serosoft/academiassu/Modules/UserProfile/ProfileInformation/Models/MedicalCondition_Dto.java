package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models;

import java.io.Serializable;

public class MedicalCondition_Dto implements Serializable {

    private String medicalCondition;
    private String conditionType;
    private long date;
    private String consultingDoctor;
    private String doctorTelephoneCountryCode;
    private String doctorTelephoneNo;
    private String precaution;

    public MedicalCondition_Dto(String medicalCondition, String conditionType, long date, String consultingDoctor, String doctorTelephoneCountryCode, String doctorTelephoneNo, String precaution) {
        this.medicalCondition = medicalCondition;
        this.conditionType = conditionType;
        this.date = date;
        this.consultingDoctor = consultingDoctor;
        this.doctorTelephoneCountryCode = doctorTelephoneCountryCode;
        this.doctorTelephoneNo = doctorTelephoneNo;
        this.precaution = precaution;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public String getConditionType() {
        return conditionType;
    }

    public long getDate() {
        return date;
    }

    public String getConsultingDoctor() {
        return consultingDoctor;
    }

    public String getDoctorTelephoneCountryCode() {
        return doctorTelephoneCountryCode;
    }

    public String getDoctorTelephoneNo() {
        return doctorTelephoneNo;
    }

    public String getPrecaution() {
        return precaution;
    }
}
