package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers.AddFeePayer;

import java.io.Serializable;

public class Country_Dto implements Serializable {

    private int id;
    private String isdCode;
    private String maximumDigit;
    private String minimumDigit;
    private String countryName;
    private boolean whetherDefault;

    public Country_Dto(int id, String isdCode, String maximumDigit, String minimumDigit, String countryName, boolean whetherDefault) {
        this.id = id;
        this.isdCode = isdCode;
        this.maximumDigit = maximumDigit;
        this.minimumDigit = minimumDigit;
        this.countryName = countryName;
        this.whetherDefault = whetherDefault;
    }

    public int getId() {
        return id;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public String getMaximumDigit() {
        return maximumDigit;
    }

    public String getMinimumDigit() {
        return minimumDigit;
    }

    public String getCountryName() {
        return countryName;
    }

    public boolean isWhetherDefault() {
        return whetherDefault;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
