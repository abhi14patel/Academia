package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class RequestType_Dto implements Serializable {

    private int id;
    private String value;

    public RequestType_Dto(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
