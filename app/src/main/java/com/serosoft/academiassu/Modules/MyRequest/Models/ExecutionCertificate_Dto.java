package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class ExecutionCertificate_Dto implements Serializable {

    private String remark;
    private int modeId;
    private int closureReasonId;
    private long handoverDate;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public int getClosureReasonId() {
        return closureReasonId;
    }

    public void setClosureReasonId(int closureReasonId) {
        this.closureReasonId = closureReasonId;
    }

    public long getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(long handoverDate) {
        this.handoverDate = handoverDate;
    }
}
