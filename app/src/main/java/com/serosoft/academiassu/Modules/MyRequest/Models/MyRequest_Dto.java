package com.serosoft.academiassu.Modules.MyRequest.Models;

import java.io.Serializable;

public class MyRequest_Dto implements Serializable
{
    private int id;
    private String requestBy;
    private String requestId;
    private String requestAssignedTo;
    private String serviceRequestStatus;
    private String requestType;
    private String requestDate;

    public MyRequest_Dto(int id, String requestBy, String requestId, String requestAssignedTo, String serviceRequestStatus, String requestType, String requestDate) {
        this.id = id;
        this.requestBy = requestBy;
        this.requestId = requestId;
        this.requestAssignedTo = requestAssignedTo;
        this.serviceRequestStatus = serviceRequestStatus;
        this.requestType = requestType;
        this.requestDate = requestDate;
    }

    public int getId() {
        return id;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestAssignedTo() {
        return requestAssignedTo;
    }

    public String getServiceRequestStatus() {
        return serviceRequestStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestDate() {
        return requestDate;
    }
}
