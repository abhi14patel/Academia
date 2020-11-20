package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

import java.io.Serializable;

public class DisciplinaryDocument_Dto implements Serializable {

    private int id;
    private String name;
    private String path;

    public DisciplinaryDocument_Dto(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
