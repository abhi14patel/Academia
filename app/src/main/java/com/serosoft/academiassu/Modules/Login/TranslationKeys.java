package com.serosoft.academiassu.Modules.Login;

public class TranslationKeys {

    private int translationId;
    private String keyName,defaultValue,customerDefineValue;

    public TranslationKeys(int translationId, String keyName, String defaultValue, String customerDefineValue) {
        this.translationId = translationId;
        this.keyName = keyName;
        this.defaultValue = defaultValue;
        this.customerDefineValue = customerDefineValue;
    }

    public int getTranslationId() {
        return translationId;
    }

    public void setTranslationId(int translationId) {
        this.translationId = translationId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCustomerDefineValue() {
        return customerDefineValue;
    }

    public void setCustomerDefineValue(String customerDefineValue) {
        this.customerDefineValue = customerDefineValue;
    }
}
