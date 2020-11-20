package com.serosoft.academiassu.Modules.MyRequest.Models;

public class RequestDocuments_Dto {

    int id;
    String value;

    public RequestDocuments_Dto(int id, String value) {
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
