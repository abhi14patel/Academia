package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class RequestStatus_Dto implements Serializable {

    private int id;
    private String value;
    private String code;

    public RequestStatus_Dto(int id, String value, String code) {
        this.id = id;
        this.value = value;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
