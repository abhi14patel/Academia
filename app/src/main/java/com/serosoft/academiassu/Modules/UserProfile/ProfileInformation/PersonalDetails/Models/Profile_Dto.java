package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models;

import java.io.Serializable;

public class Profile_Dto implements Serializable {

    private int id;
    private String value;

    public Profile_Dto(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
