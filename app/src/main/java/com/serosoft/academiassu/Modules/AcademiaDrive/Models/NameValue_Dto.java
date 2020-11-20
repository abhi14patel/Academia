package com.serosoft.academiassu.Modules.AcademiaDrive.Models;

import java.io.Serializable;

public class NameValue_Dto implements Serializable {

    private String value;
    private String code;

    public NameValue_Dto(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
