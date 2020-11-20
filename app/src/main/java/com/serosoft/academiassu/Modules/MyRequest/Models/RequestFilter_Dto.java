package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class RequestFilter_Dto implements Serializable
{
    String name;
    Object values;
    int type;

    public RequestFilter_Dto(String name, Object values,int type) {
        this.name = name;
        this.values = values;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getValues() {
        return values;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
