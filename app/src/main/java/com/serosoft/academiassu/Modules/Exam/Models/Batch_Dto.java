package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class Batch_Dto implements Serializable {

    private String batchName;
    private int batchId;

    public Batch_Dto(String batchName, int batchId) {
        this.batchName = batchName;
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public int getBatchId() {
        return batchId;
    }

    @Override
    public String toString() {
        return batchName;
    }
}
