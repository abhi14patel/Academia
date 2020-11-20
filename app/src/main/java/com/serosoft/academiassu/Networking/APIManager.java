package com.serosoft.academiassu.Networking;

import android.content.Context;

import com.serosoft.academiassu.Manager.BaseClass;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;

import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by ammarali on 24/06/16.
 */
public class APIManager extends BaseClass {

    Context context;
    private SharedPrefrenceManager sharedPrefrenceManager;
    private ServiceCalls serverCalls;
    private HashMap<String, String> hashMap;
    private String responseString;
    private JSONObject responseObject;
    private Integer admissionId;
    private Integer periodId;
    private Integer sectionId;
    private Integer academyLocationId;
    private Integer studentId;
    private Integer portalId;
    private Integer programId;
    private Integer batchId;
    private Integer personId;
    private Long firebaseID;

    public APIManager(Context context) {
        super(context);
        this.context = context;

        responseString = KEYS.SERVER_ISSUE;
        sharedPrefrenceManager = new SharedPrefrenceManager(context);
        serverCalls = new ServiceCalls();
        academyLocationId = sharedPrefrenceManager.getAcademyLocationIDFromKey();
        studentId = sharedPrefrenceManager.getUserIDFromKey();
        portalId = sharedPrefrenceManager.getPortalIDFromKey();
        programId = sharedPrefrenceManager.getProgramIDFromKey();
        batchId = sharedPrefrenceManager.getBatchIDFromKey();
        admissionId = sharedPrefrenceManager.getAdmissionIDFromKey();
        periodId = sharedPrefrenceManager.getPeriodIDFromKey();
        sectionId = sharedPrefrenceManager.getSectionIDFromKey();
        firebaseID = sharedPrefrenceManager.getFirebaseIDFromKey();
        personId = sharedPrefrenceManager.getPersonIDFromKey();
    }

    /*Attendance*/
    public String getAttendanceSummaryCompleteDayFromServer(String periodId, String sectionId, String startDate, String endDate) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("periodId", periodId);
        hashMap.put("sectionId", sectionId);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("viewAttendanceType", "CONSOLIDATE");
        hashMap.put("onlyCurrentRecords", "false");
        hashMap.put("includeAttendanceOfOnlyPlannedSession", "false");
        hashMap.put("attendanceType", "COMPLETE_DAY");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceSummaryCourseLevelFromServer(String programId, String batchId, String periodId, String courseCodeId, String courseVariantId, String percentageFrom, String percentageTo, String startDate, String endDate ) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("programId", programId);
        hashMap.put("batchId", batchId);
        hashMap.put("periodId", periodId);
        hashMap.put("courseId", courseCodeId);
        hashMap.put("courseVariantId", courseVariantId);
        hashMap.put("attendancePercentageFrom", percentageFrom);
        hashMap.put("attendancePercentageTo", percentageTo);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("attendanceType", "COURSE_LEVEL");
        hashMap.put("isCurrentPeriod", "false");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceSummaryMultipleSessionFromServer(String periodId, String sectionId, String startDate, String endDate) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("periodId", periodId);
        hashMap.put("sectionId", sectionId);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("viewAttendanceType", "CONSOLIDATE");
        hashMap.put("onlyCurrentRecords", "false");
        hashMap.put("includeAttendanceOfOnlyPlannedSession", "false");
        hashMap.put("attendanceType", "MULTIPLE_SESSION");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceProgramsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("academyLocationIds", academyLocationId.toString());
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_PROGRAMS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceBatchFromServer(String programId) {

        hashMap = new HashMap<>();
        hashMap.put("programId", programId);
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_BATCH);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendancePeriodFromServer(String batchId) {

        hashMap = new HashMap<>();
        hashMap.put("batchId", batchId);
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_PERIOD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceSectionFromServer(String periodId) {

        hashMap = new HashMap<>();
        hashMap.put("periodId", periodId);
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_SECTION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCourseListFromServer(String programId, String batchId, String periodId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("programId", programId);
        hashMap.put("batchId", batchId);
        hashMap.put("periodId", periodId);
        hashMap.put("onlyCurrentRecords", "false");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCourseVariantFromServer(String programId, String batchId, String periodId,String courseId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("programId", programId);
        hashMap.put("batchId", batchId);
        hashMap.put("periodId", periodId);
        hashMap.put("courseId", courseId);
        hashMap.put("onlyCurrentRecords", "false");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_VARIANT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsCompleteDayFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("sectionId", sectionId.toString());
        hashMap.put("attendanceType", KEYS.COMPLETE_DAY);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsCourseLevelFromServer(String courseVariantId) {

        hashMap = new HashMap<>();
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("courseVariantId", courseVariantId);
        hashMap.put("attendanceType", KEYS.COURSE_LEVEL);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsMultipleSessionFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("sectionId", sectionId.toString());
        hashMap.put("attendanceType", KEYS.MULTIPLE_SESSION);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsCompleteDayMonthwiseFromServer(String startDate, String endDate, String sectionId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("sectionId", sectionId);
        hashMap.put("attendanceType", KEYS.COMPLETE_DAY);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsCourseLevelMonthwiseFromServer(String courseId, String startDate, String endDate) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("courseId", courseId);
        hashMap.put("attendanceType", KEYS.COURSE_LEVEL);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAttendanceDetailsMultipleSessionMonthwiseFromServer(String startDate, String endDate, String sectionId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("sectionId", sectionId);
        hashMap.put("attendanceType", KEYS.MULTIPLE_SESSION);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Fees*/
    public String getFeesFromServer() {

        Integer studentId = sharedPrefrenceManager.getUserIDFromKey();
        hashMap = new HashMap<>();
        hashMap.put("id", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("billedUserType", "STUDENT");
        hashMap.put("raiseBillCategory", "BILL_RECEIVABLE");
        hashMap.put("whetherDeleted", "false");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_FEES);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getReceiptsFromServer() {

        studentId = sharedPrefrenceManager.getUserIDFromKey();
        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_RECEIPTS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String downloadReceiptFromServer(String receiptId, String viewOrDownload, String receiptStatus) {

        hashMap = new HashMap<>();
        hashMap.put("receiptId", receiptId);
        hashMap.put("viewOrDownload", viewOrDownload);
        hashMap.put("receiptStatus", receiptStatus);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_RECEIPT_DOWNLOAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getStudentInvoicesFromServer(){

        hashMap = new HashMap<>();
        hashMap.put("id", studentId.toString());
        hashMap.put("billedUserType", "STUDENT");
        hashMap.put("raiseBillCategory", "BILL_RECEIVABLE");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_INVOICE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPaymentGatewayKeysFromServer(String environment) {

        studentId = sharedPrefrenceManager.getUserIDFromKey();
        hashMap = new HashMap<>();
        hashMap.put("type", environment);
        hashMap.put("academyLocationIds", academyLocationId.toString());
        hashMap.put("isActive", "true");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PAYMENT_GATEWAY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPaymentIDFromServer(String payableAmount, String payLoadString) {

        hashMap = new HashMap<>();
        hashMap.put("payableAmount", payableAmount);
        hashMap.put("payLoadString", payLoadString);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PAYMENT_ID);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPayTMChecksumHashFromServer(String MID, String ORDER_ID, String CUST_ID, String INDUSTRY_TYPE_ID, String CHANNEL_ID, String TXN_AMOUNT, String WEBSITE, String CALLBACK_URL,String env) {

        hashMap = new HashMap<>();
        hashMap.put("MID", MID);
        hashMap.put("ORDER_ID", ORDER_ID);
        hashMap.put("CUST_ID", CUST_ID);
        hashMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        hashMap.put("CHANNEL_ID", CHANNEL_ID);
        hashMap.put("TXN_AMOUNT", TXN_AMOUNT);
        hashMap.put("WEBSITE", WEBSITE);
        hashMap.put("CALLBACK_URL", CALLBACK_URL);
        hashMap.put("academyLocationIds", academyLocationId.toString());
        hashMap.put("env", env);
        hashMap.put("isActive", "true");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PAYTM_GET_CHECKSUMHASH);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String postTransactionIDUpdateToServer(String paymentID, String transactionID) {

        hashMap = new HashMap<>();
        hashMap.put("status", "Paid");
        hashMap.put("id", paymentID);
        hashMap.put("txnid", transactionID);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_TRANSACTION_ID_UPDATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String postTransactionUpdateToServer(String paymentID) {

        hashMap = new HashMap<>();
        hashMap.put("id", paymentID);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_TRANSACTION_UPDATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getBillHeaderDetailsFromServer(String invoiceID) {

        hashMap = new HashMap<>();
        hashMap.put("billId", invoiceID);
        hashMap.put("page", String.valueOf(1) );
        hashMap.put("start", String.valueOf(0) );
        hashMap.put("limit", String.valueOf(-1) );

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_BILL_HEADER);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getInvoiceDetailsFromServer(String invoiceID) {

        hashMap = new HashMap<>();
        hashMap.put("billId", invoiceID);
        hashMap.put("portalId", portalId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_INVOICE_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String downloadInvoiceFromServer(String billId, String viewOrDownload) {

        hashMap = new HashMap<>();
        hashMap.put("billId", billId);
        hashMap.put("viewOrDownload", viewOrDownload);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_INVOICE_DOWNLOAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPendingBillsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("id", studentId.toString());
        hashMap.put("billedUserType", "STUDENT");
        hashMap.put("raiseBillCategory", "BILL_RECEIVABLE");
        hashMap.put("isRequestFromMobile", "true");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PENDING_BILLS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPendingFeeHeadsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("id", studentId.toString());
        hashMap.put("billedUserType", "STUDENT");
        hashMap.put("showAllSettlement", "Pending");
        hashMap.put("adjustmentType", "Bill%20Receivable");
        hashMap.put("isRequestFromMobile", "true");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PENDING_FEE_HEADS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPaymentIDFeeHeadFromServer(String payableAmount, String payLoadString) {

        hashMap = new HashMap<>();
        hashMap.put("payableAmount", payableAmount);
        hashMap.put("payLoadString", payLoadString);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PAYMENT_ID_FEE_HEAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Timetable*/
    public String getCalendarFromServerCourseWise(String courseId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("courseId", courseId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_CALENDAR_COURSEWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getTimetableCoursesFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", String.valueOf(studentId));
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_TIMETABLE_COURSES);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHolidaysFromServerMonthWise(String start, String end) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("startDate", start);
        hashMap.put("endDate", end);
        hashMap.put("isweekDayOff", "false");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_HOLIDAYS_MONTHWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCalendarFromServerMonthWise(String start, String end) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("start", start);
        hashMap.put("end", end);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_CALENDAR_MONTHWISE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCalendarDurationFromServer(String periodId) {

        hashMap = new HashMap<>();
        hashMap.put("periodId", periodId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_CALENDAR_DURATION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCalendarFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_CALENDAR);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Assignment*/
    public String getCourseCoveragePlan3FromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("programId", programId.toString());
        hashMap.put("batchId", batchId.toString());
        hashMap.put("periodId", periodId.toString());
        hashMap.put("portalId", portalId.toString());
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("studentIds", studentId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCourseSessionDiaryDescriptionFromServer(String courseId) {

        hashMap = new HashMap<>();
        hashMap.put("id", courseId );

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_DIARY_DESCRIPTION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCourseCoveragePlan1FromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("portalId", portalId.toString());
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("academyLocationId", academyLocationId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_COVERAGE_PLAN_1_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCourseCoveragePlan2FromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("portalId", portalId.toString());
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("periodId", periodId.toString());
        hashMap.put("programId", programId.toString());
        hashMap.put("sectionId", sectionId.toString());
        hashMap.put("batchId", batchId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COURSE_COVERAGE_PLAN_2_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHomeworkAssignmentDetailsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHomeworkAssignmentDetailsFromServer(String assignmentId,String studentId) {

        hashMap = new HashMap<>();
        hashMap.put("id", assignmentId);
        hashMap.put("studentId", studentId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHomeworkDocumentsFromServer(String courseHomeWorkAssignmentId) {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("courseHomeWorkAssignmentId", String.valueOf(courseHomeWorkAssignmentId));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_DOCUMENTS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHomeworkAssignmentDescriptionFromServer(String assignmentId) {

        hashMap = new HashMap<>();
        hashMap.put("id", assignmentId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String downloadHomeworkAssignmentFromServer(String id, String fileName,String folderName) {

        hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("fileName", fileName);
        hashMap.put("folderName", folderName);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getSavedDocuments(String assignmentId, String studentId) {

        hashMap = new HashMap<>();
        hashMap.put("id", assignmentId);
        hashMap.put("docType", "SUBMITED_DOC");
        hashMap.put("userId", studentId);
        hashMap.put("portalId", "3");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAssignmentTypeFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("type", "AssignmentType");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ASSIGNMENT_TYPE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String downloadNotificationDocumentFromServer(String id, String fileName,String folderName) {

        hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("fileName", fileName);
        hashMap.put("folderName", folderName);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Reset Password*/
    public String resetPassword(String oldPassword, String newPassword) {

        hashMap = new HashMap<>();
        hashMap.put("currentPassword", oldPassword);
        hashMap.put("newPassword", newPassword);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_RESET_PASSWORD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPasswordPolicyFromServer() {

        hashMap = new HashMap<>();
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PASSWORD_POLICY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Event*/
    public String getStudentEventsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_EVENTS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getEventAttachmentsFromServer(String resourceBookingId) {

        hashMap = new HashMap<>();
        hashMap.put("resourceBookingId", resourceBookingId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EVENT_ATTACHMENTS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getEventImageFromServer(String imagePath, String index) {

        hashMap = new HashMap<>();
        hashMap.put("imagePath", imagePath);
        hashMap.put("index", index);
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EVENT_PICTURE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String markNotificationAsRead(String messageId) {

        hashMap = new HashMap<>();
        hashMap.put("messageId", messageId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MARK_NOTIFICATION_AS_READ);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Login*/
    public Boolean refreshLogin(HashMap<String, String> mapValues) {

        responseObject = serverCalls.sendDataToServer(context, mapValues, KEYS.SWITCH_USER_LOGIN);

        if (responseObject != null) {
            return saveAccessToken(responseObject);
        }
        return false;
    }

    private boolean saveAccessToken(JSONObject responseObject)
    {
        String accessToken = responseObject.optString("access_token");
        String tokenType = responseObject.optString("token_type");
        String refreshToken = responseObject.optString("refresh_token");
        String scope = responseObject.optString("scope");

        if (accessToken.length() > 0) {
            sharedPrefrenceManager.setaccessTokenInSP(accessToken);
            sharedPrefrenceManager.setTokenTypeInSP(tokenType);
            sharedPrefrenceManager.setRefreshTokenInSP(refreshToken);
            sharedPrefrenceManager.setTokenScopeInSP(scope);
            return true;
        } else {
            return false;
        }
    }

    public String getGoogleSigninFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("socialId", "2");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_GOOGLE_SIGNIN_SETUP);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getLatestVersionNewFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("platform", "android");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_LATEST_VERSION_NEW);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getLatestVersionNew2FromServer() {

        hashMap = new HashMap<>();
        hashMap.put("platform", "android");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_LATEST_VERSION_NEW2);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getForgotPasswordStatusFromServer(String code) {

        hashMap = new HashMap<>();
        hashMap.put("code", code);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FORGOT_PASSWORD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Dashboard*/
    public String getFeaturePrivilegesFromServer(String portalId,String appPortal) {

        hashMap = new HashMap<>();
        hashMap.put("portalId", String.valueOf(portalId));
        hashMap.put("appPortal", String.valueOf(appPortal));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEATURE_PRIVILEGES);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAcademyImageFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("academyLocationId", academyLocationId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ACADEMY_IMAGE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getLatestVersionFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("platform", "ANDROID");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_LATEST_VERSION);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAllCurrencyDetailsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ALL_CURRENCY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String userLogout() {

        hashMap = new HashMap<>();
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_LOGOUT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getNotificationsFromServer() {

        hashMap = new HashMap<>();
        String messageType = "Notifications";
        hashMap.put("whetherAdhoc","TRUE");
        hashMap.put("messageType",messageType);
        hashMap.put("userId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_CIRCULARS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getAssociatedSiblingsFromServer() {

        hashMap = new HashMap<>();
        Integer parentPersonId = sharedPrefrenceManager.getParentPersonIDFromKey();
        Integer parentUserId = sharedPrefrenceManager.getParentUserIDFromKey();
        Integer studentPersonId = sharedPrefrenceManager.getPersonIDFromKey();
        hashMap.put("parentPersonId", String.valueOf(parentPersonId));
        hashMap.put("parentUserId", String.valueOf(parentUserId));
        hashMap.put("studentPersonId", String.valueOf(studentPersonId));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ASSOCIATED_SIBLINGS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getNotifications2FromServer() {

        hashMap = new HashMap<>();
        String messageType = "Notifications";
        hashMap.put("messageType",messageType);
        hashMap.put("userId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_NOTIFICATIONS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getNotificationsCountFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("userId", studentId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_NOTIFICATIONS_COUNT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String deleteFCMTokenFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("id", firebaseID.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FCM_LOGOUT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getFeedbackEMailsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEEDBACK_EMAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*UserProfile*/
    public String getParentDetailsFromServer() {

        hashMap = new HashMap<>();
        Integer studentPersonId = sharedPrefrenceManager.getPersonIDFromKey();
        hashMap.put("personId", studentPersonId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PARENT_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDocumentListFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_DOCUMENT_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDocumentTypeFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("isApplicableForStudent", "true");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_DOCUMENT_TYPE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMedicalConditionListFromServer() {

        hashMap = new HashMap<>();
        Integer studentPersonId = sharedPrefrenceManager.getPersonIDFromKey();
        hashMap.put("personId", studentPersonId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MEDICAL_CONDITION_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMedicalConditionTypesFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MEDICAL_CONDITION_TYPES);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMedicalConditionCountryCodeFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String createMedicalCondition(String medConditionTypeId, String consultingDoctor, String dateSince,String countryCode,String contact, String medicalCondition, String remark) {

        Integer studentPersonId = sharedPrefrenceManager.getPersonIDFromKey();
        hashMap = new HashMap<>();
        hashMap.put("conditionType", medConditionTypeId);
        hashMap.put("consultingDoctor", consultingDoctor);
        hashMap.put("dateSince", dateSince);
        hashMap.put("doctorTelephoneCountryCode", countryCode);
        hashMap.put("doctorTelephoneNo", contact);
        hashMap.put("medicalCondition", medicalCondition);
        hashMap.put("remarks", remark);
        hashMap.put("personId", studentPersonId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MEDICAL_CONDITION_CREATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDisciplinaryActionListFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("personId", personId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_DISCIPLINARY_ACTION_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getFeePayerListFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("personId", personId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEE_PAYER_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCurrentAddressFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("personId", personId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEE_PAYER_CURRENT_ADDRESS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }


    public String getCountryRegionListFromServer(String id) {

        hashMap = new HashMap<>();
        hashMap.put("cid", id);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_COUNTRY_REGION_CODE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getCityListFromServer(String cid, String cRegionId) {

        hashMap = new HashMap<>();
        hashMap.put("countryId", cid);
        hashMap.put("countryRegionId", cRegionId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEE_PAYER_CITY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDebitOrderDateFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_DEBIT_ORDER_DATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getBankListDateFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEE_BANK_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getBranchCodeFromServer(String bankId) {

        hashMap = new HashMap<>();
        hashMap.put("bankId", bankId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FEE_BANK_BRANCH_CODE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getRemoveProfileFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("personId", personId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REMOVE_PROFILE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getUpdateProfilePictureFromServer(String imagePath) {

        hashMap = new HashMap<>();
        hashMap.put("imagePath", imagePath);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_PROFILE_PICTURE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileDetailsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("id", ""+sharedPrefrenceManager.getUserIDFromKey());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileSalutationFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_SALUTATIONS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileMultiLanguageFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_MULTILANGUAGE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileContactLanguageFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_CONTACTLANGUAGE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileDisabilitiesFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("type", "HaveDisabilities");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_DISABILITIES);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileCurrentEducationFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("type", "CurrentEducationalInterventions");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_CURRENT_EDUCATIONAL);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileGenderFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_GENDER);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileNationalityFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_NATIONALITY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getProfileDomicileFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROFILE_DOMICILE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*Exam*/
    public String getResultReportFromServer(String programId,String batchId,String periodId) {

        hashMap = new HashMap<>();
        hashMap.put("admissionId", admissionId.toString());
        hashMap.put("studentId", studentId.toString());
        hashMap.put("programId", programId);
        hashMap.put("batchId", batchId);
        hashMap.put("periodId", periodId);
        hashMap.put("sort", "%5B%7B%22property%22%3A%22leaf%22%2C%22direction%22%3A%22ASC%22%7D%5D");
        hashMap.put("node", "src");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_RESULT_REPORT);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMarksheetsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_MARKSHEETS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getHallTicketFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("reportEnum", "HALL_TICKET");
        hashMap.put("periodId", periodId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_HALLTICKET);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String downloadMarksheetFromServer(String marksheetPath,String folderName) {

        hashMap = new HashMap<>();
        hashMap.put("marksheetPath", marksheetPath);
        hashMap.put("folderName", folderName);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getResultProgramsFromServer(String programIds) {

        hashMap = new HashMap<>();
        hashMap.put("academyLocationIds", academyLocationId.toString());
        hashMap.put("programIds", programIds);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PROGRAMS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getResultBatchFromServer(String filterType,String programId) {

        hashMap = new HashMap<>();
        hashMap.put("filterType", filterType);
        hashMap.put("programId", programId);
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_BATCH);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getResultPeriodFromServer(String filterType,String batchId) {

        hashMap = new HashMap<>();
        hashMap.put("filterType", filterType);
        hashMap.put("batchId", batchId);
        hashMap.put("studentId", studentId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_PERIOD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    /*My Courses*/
    public String getMyCourseProgramsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("onlyCurrentRecord", "false");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MYCOURSE_PROGRAM);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyCourseBatchFromServer(String programId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("onlyCurrentRecord", "false");
        hashMap.put("programId", programId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MYCOURSE_BATCH);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyCoursePeriodFromServer(String batchId) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("onlyCurrentRecord", "false");
        hashMap.put("batchId", batchId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MYCOURSE_PERIOD);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyCoursesFromServer(String programId,String batchId,String periodId,String isSchool) {

        hashMap = new HashMap<>();
        hashMap.put("studentId", studentId.toString());
        hashMap.put("programId", programId);
        hashMap.put("batchId", batchId);
        hashMap.put("periodId", periodId);
        hashMap.put("wheatherSchool", isSchool);
        hashMap.put("portalId", portalId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MY_COURSES_LIST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }


    /*Academia Drive*/
    public String getAcademiaDriveFolderFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("academyLocationIds", academyLocationId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ACADEMIA_DRIVE_FOLDER);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDriveDetailFromServer(String id) {

        hashMap = new HashMap<>();
        hashMap.put("galleryId", id);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(-1));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ALBUM_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getUltimateGalleryFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("batchId", batchId.toString());
        hashMap.put("isFacultyPortal", "false");
        hashMap.put("isStudentPortal", "true");

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ULTIMATE_GALLERY);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }


    /*My Request*/
    public String getMyRequestRaiseRequestTypeFromServer(String requestGroup) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestGroup", requestGroup);
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("portalId", portalId.toString());
        hashMap.put("serviceRequestCategory", "STUDENT_REQUEST");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_RAISE_REQUEST_TYPE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyRequestStatusFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REQUEST_STATUS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyRequestTypeFromServer(String requestGroup) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestGroup", requestGroup);
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("serviceRequestCategory", "STUDENT_REQUEST");
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REQUEST_TYPE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMyRequestRequesterDetailsFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("id", studentId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REQUESTER_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getRequesterHostelBasicDetailsFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("requestId", requestId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REQUESTER_HOSTEL_BASIC_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getRequesterBasicDetailsFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("requestId", requestId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_REQUESTER_BASIC_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getMandatoryDocumentFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestId", requestId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_MANDETORY_DOCUMENTS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getFollowupDetailsFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestId", requestId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_FOLLOWUP_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getApprovalDetailsFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestId", requestId);
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_APPROVAL_DETAILS);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getWithdrawnRequestFromServer(String requestId) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestId", requestId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_WITHDRAW_REQUEST);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getExecutionHandoverModeFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EXECUTION_HANDOVER_MODE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getExecutionClosureModeFromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EXECUTION_CLOSURE_REASON);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getPrintCertificateFromServer(String leaveType) {

        hashMap = new HashMap<>();
        hashMap.put("certificateCategory", leaveType);
        hashMap.put("academyLocationId", academyLocationId.toString());
        hashMap.put("page", String.valueOf(1));
        hashMap.put("start", String.valueOf(0));
        hashMap.put("limit", String.valueOf(25));

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EXECUTION_PRINT_CERTIFICATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }

    public String getDownloadCertificateFromServer(String serviceRequestId,String certificateId) {

        hashMap = new HashMap<>();
        hashMap.put("serviceRequestId", serviceRequestId);
        hashMap.put("certificateId", certificateId);

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE);

        if (responseObject != null) {
            try {
                responseString = responseObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return KEYS.SERVER_ISSUE;
            }
        }
        return responseString;
    }
}
