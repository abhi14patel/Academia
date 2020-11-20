package com.serosoft.academiassu.Helpers;

public interface StatusClass {

    String ASSIGNED = "Assigned";
    String APPROVED = "Approved";
    String WITHDRAWN = "Withdrawn";
    String CLOSED = "Closed";
    String CANCELLED = "Cancelled";
    String COMPLETED = "Completed";
    String ESCALATED = "Escalated";
    String GIVEN = "Given";
    String REJECTED = "Rejected";
    String SUBMITTED = "Submitted";

    int REQUEST_STATUS_SUBMITTED = 1;
    int REQUEST_STATUS_ASSIGNED = 2;
    int REQUEST_STATUS_ESCLATED = 3;
    int REQUEST_STATUS_APPROVED = 4;
    int REQUEST_STATUS_REJECTED = 5;
    int REQUEST_STATUS_CLOSED = 6;
    int REQUEST_STATUS_WITHDRAWN = 7;
}
