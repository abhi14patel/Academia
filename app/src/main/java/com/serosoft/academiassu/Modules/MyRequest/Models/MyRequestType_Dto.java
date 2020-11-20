package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class MyRequestType_Dto implements Serializable {

    private String value;
    private int id;

    public MyRequestType_Dto(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}
