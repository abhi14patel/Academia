package com.serosoft.academiassu.Modules.Dashboard.Models;

public class CurrencyDto {

    private String id;
    private String name;
    private String currencyCode;

    public CurrencyDto(String id, String name, String currencyCode) {
        this.id = id;
        this.name = name;
        this.currencyCode = currencyCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
