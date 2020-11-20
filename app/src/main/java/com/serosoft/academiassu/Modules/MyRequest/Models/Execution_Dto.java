package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class Execution_Dto implements Serializable {

    private int id;
    private String name;

    public Execution_Dto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
