package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models;

import java.io.Serializable;
import java.util.Comparator;

public class Disabilities_Dto implements Serializable {

    private int id;
    private String value;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Comparator<Disabilities_Dto> sortById = new Comparator<Disabilities_Dto>()
    {
        @Override
        public int compare(Disabilities_Dto m1, Disabilities_Dto m2)
        {
            int id1 = m1.getId();
            int id2 = m2.getId();

            return id1-id2;
        }
    };
}
