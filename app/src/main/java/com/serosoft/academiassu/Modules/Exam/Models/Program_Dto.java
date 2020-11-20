package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class Program_Dto implements Serializable {

    private String programName,programCode;
    private int programId;

    public Program_Dto(String programName, String programCode, int programId) {
        this.programName = programName;
        this.programCode = programCode;
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public int getProgramId() {
        return programId;
    }
}
