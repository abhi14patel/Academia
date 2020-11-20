package com.serosoft.academiassu.Networking;

import com.serosoft.academiassu.Utils.BaseURL;

public class KEYS {

    /*Application Level Info*/
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String PORTAL_CODE = "portal_code";
    public static final String GOOGLE_ACCESS_TOKEN = "googleAccessToken";

    public static final String CLIENT_ID_VALUE = "erpclient";
    public static final String CLIENT_SECRET_VALUE = "#edu$erp#2016$";
    public static final String GRANT_TYPE_VALUE = "password";
    public static final String PORTAL_CODE_VALUE = "SSO_GOOGLE";
    public static final String GOOGLE_AUTH_TOKEN = "459161975858-5ehdutkp4073onb2j29r2htnfpifht4c.apps.googleusercontent.com";
    public static final String ENCRYPTION_KEY = "C1Rms8o7CKBtH0LG";

    /*User Related Info*/
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    //Server callingprogramBatchPeriodConfiguration/findByPeriodId
    public static final String SERVER_ISSUE = "Unexpected server error!";
    public static final String RESPONSE_SUCCESSFULLY = "Success";
    public static final String RESPONSE_FAILURE = "Response failure";
    public static final int TIME_OUT = 10000;

    //Service Method Name

    /*Login releted*/
    static final String LOGIN_SERVICE_METHOD = "oauth/token";
    static final String LATEST_VERSION_METHOD_NEW = "rest/userAccountResource/findByPlatform";
    static final String GOOGLE_SIGNIN_METHOD = "rest/userAccountResource/getSocialClientInfo";
    static final String FORGOT_PASSWORD_METHOD = "rest/userAccountResource/forgotPassword";
    static final String FEATURES_SERVICE_METHOD = "rest/authentication/features";
    static final String FIND_USERID_SOCIAL_LOGIN_METHOD = "/rest/users/findUserDetailsBySocialLoginId";
    static final String STUDENT_ACADEMIC_DETAILS_SERVICE_METHOD = "rest/student/getStudemtAllDetailsById";
    static final String STUDENT_PERSONAL_DETAILS_SERVICE_METHOD = "rest/student";
    static final String STUDENT_PERSONAL_DETAILS2_SERVICE_METHOD = "rest/cb/executeBasicSearch";
    static final String STUDENT_PROFILE_PICTURE_METHOD = "rest/users/downloadBase64Image";
    static final String STUDENT_ATTENDANCE_TYPE_METHOD = "rest/attendanceALSetting/findByAcademyLocationId";
    static final String MOBILE_TRANSLATION_METHOD = "rest/uITranslation/getMobileTranslations";
    static final String DATE_TIME_FORMAT_METHOD = "rest/organization/findAll";
    static final String FCM_REGISTERATION_METHOD = "rest/fireBasePersonInfo/saveFireBasePersonInfo";
    public static final String BUILD_INFO_METHOD = "buildInfo.xml";

    /*Dashboard releted*/
    static final String FEATURE_PRIVILEGES_METHOD = "rest/authentication/featurePrivilegesForMobileApp";
    static final String ACADEMY_IMAGE_METHOD = "rest/academyLocation/downloadLogo";
    static final String LATEST_VERSION_METHOD = "rest/mobilePlatformDetail/findByPlatform";
    static final String ALL_CURRENY_METHOD = "rest/feePlan/findAllActiveCurrency";
    static final String LOGOUT_METHOD = "logout";
    static final String CIRCULARS_METHOD = "rest/systemInternalMessagesResource/findAdhocNotificationByUserId";
    static final String NOTIFICATIONS_METHOD = "rest/systemInternalMessagesResource/findAllByUserIdAndType";
    static final String NOTIFICATIONS_COUNT_METHOD = "rest/systemInternalMessagesResource/findUnreadMessageCount";
    static final String FCM_LOGOUT_METHOD = "rest/fireBasePersonInfo/logOutFireBase";
    static final String FEEDBACK_EMAILS_METHOD = "rest/MobileApplicationFeedBack/getMobileFeedbackEmails";

    /*User Profile releted*/
    static final String PARENT_DETAILS_METHOD = "rest/personRelationship/findByPersonId";
    public static final String DOCUMENT_UPLOAD1_METHOD = "rest/documents/create";
    public static final String DOCUMENT_UPLOAD2_METHOD = "rest/studentDocument/create";
    static final String DOCUMENT_TYPE_METHOD = "rest/documentType/findAll";
    static final String DOCUMENT_LIST_METHOD = "rest/studentDocument/findByCriteria";
    static final String MEDICAL_CONDITION_LIST_METHOD = "rest/medicalHistory/gridData";
    static final String MEDICAL_CONDITION_TYPES_METHOD = "rest/medicalHistory/findAllConditionTypes";
    static final String MEDICAL_CONDITION_COUNTRY_CODE_METHOD = "rest/countryResource/findAll";
    static final String STUDENT_CURRENT_ADDRESS_METHOD = "rest/personAddress/getPersonCurrentAddress";
    static final String MEDICAL_CONDITION_CREATE_METHOD = "rest/medicalHistory/create";
    static final String DISCIPLINARY_ACTION_LIST_METHOD = "rest/disciplinaryAction/gridData";
    static final String FEE_PAYER_LIST_METHOD = "rest/feePayerDetail/person/gridData";
    static final String COUNTRY_REGION_METHOD = "rest/countryRegionResource/findAllParentRegions";
    static final String FEE_PAYER_CITY_METHOD = "rest/cityResource/findCitiesByCountryAndCountryRegion";
    static final String FEE_PAYER_DEBIT_ORDER_DATE_METHOD = "rest/debitOrderDate/findAll";
    static final String FEE_PAYER_BANKDRAW_ONMASTER_METHOD = "rest/bankDrawOnMaster/findAll";
    static final String FEE_PAYER_BANK_BRANCH_CODE_METHOD = "rest/bankDrawnChildResource/findAllBranchCode";
    public static final String FEE_PAYER_DOCUMENT_UPLOAD_METHOD = "rest/file/uploadFeePayerDocumentsTemp";
    public static final String FEE_PAYER_CREATE_METHOD = "rest/feePayerDetail/person/create";
    static final String REMOVE_PROFILE_METHOD = "rest/person/removeProfilePhoto";
    public static final String UPLOAD_PROFILE_METHOD = "rest/personImage/uploadeB64Image";
    public static final String PROFILE_DOCUMENT_UPLOAD_METHOD = "rest/personImage/uploadDocument";
    public static final String PROFILE_SUBMIT_METHOD = "rest/student";
    static final String PROFILE_DETAILS_METHOD = "rest/student/";
    static final String PROFILE_SALUTATIONS_METHOD = "rest/salutationResource/findAll";
    static final String PROFILE_MULTILANGUAGE_METHOD = "rest/multiLanguageResource/findAll";
    static final String PROFILE_FINDALL_METHOD = "rest/dropDown/findAll";
    static final String PROFILE_GENDER_METHOD = "rest/gender/findAll";
    static final String PROFILE_NATIONALITY_METHOD = "rest/nationality";
    static final String PROFILE_DOMICILE_METHOD = "rest/domicile/findAll";

    /*Reset Password releted*/
    static final String RESET_PASSWORD_METHOD = "rest/users/resetPassword";
    static final String PASSWORD_POLICY_METHOD = "rest/userPasswordPolicy";

    /*MyCourses releted*/
    static final String PROGRAM_BATCH_PERIOD_METHOD = "rest/programBatchStudent/getStudentProgramBatchPeriodSection";
    static final String MY_COURSES_METHOD = "rest/studentCourseEnrollment/findStudentCourseReport";

    /*Attendance releted*/
    static final String STUDENT_ATTENDANCE_SUMMARY_COMPLETE_DAY_METHOD = "rest/studentAttendanceDashboardStudentWise/getAttendanceSummary";
    static final String STUDENT_ATTENDANCE_DETAILS_COMPLETE_DAY_METHOD = "rest/studentAttendance/findStudentCompleteDayAttendanceReport";
    static final String STUDENT_ATTENDANCE_SUMMARY_COURSE_LEVEL_METHOD = "rest/studentAttendanceDashboardStudentWise/findStudentAttendanceSummaryForCoreErp";
    static final String STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD = "rest/studentAttendanceDashboardStudentWise/getStudentAttendanceStatusRecords";
    static final String ATTENDANCE_PROGRAM_BATCH_PERIOD_METHOD = "rest/section/findAttendanceProgramSection";
    static final String ATTENDANCE_COURSE_LIST_METHOD = "rest/courseVariant/getAttendanceCourses";
    static final String ATTENDANCE_COURSE_VARIANT_METHOD = "rest/courseVariant/getAttendanceCourseVariant";

    /*Fees releted*/
    static final String STUDENT_FEES_METHOD = "rest/billFeePlanRuleStage/findAllStudentPendingBillDetails";
    static final String STUDENT_RECEIPTS_METHOD = "rest/feeAdjustmentMaster/findAllPaymentReceivedFromStudent";
    static final String RECEIPT_DOWNLOAD_METHOD = "rest/ReportDocumentResource/exportReceiptInZip";
    static final String STUDENT_INVOICE_METHOD = "rest/billHeader/pendingBillForMobileOnlinePayment";
    static final String PAYMENT_GATEWAY_METHOD = "rest/paymentGatewayMobileApplication/findByEnvironmentAndAcedemyLocationId";
    static final String PAYMENT_ID_METHOD = "rest/onlinePaymentMaster/createAtBillLevel";
    static final String PAYTM_GET_CHECKSUMHASH_METHOD = "paytm_app/generateChecksum.php";
    static final String TRANSACTION_ID_UPDATE_METHOD = "rest/onlinePaymentMaster/updateOnlinePaymentMasterTransactionStatus";
    static final String TRANSACTION_UPDATE_METHOD = "rest/onlinePaymentMaster/createReceiptAndSettlementForOnlinePaymentByOnlinePaymentMasterId";
    static final String STUDENT_PENDING_BILLS_METHOD = "rest/billFeePlanRuleStage/findPendindBillDetailsForOnlinePayment";
    static final String STUDENT_PENDING_FEE_HEADS_METHOD = "rest/billHeader/gridDataBillAdjustmentForOnlinePayment";
    static final String INVOICE_DOWNLOAD_METHOD = "rest/ReportDocumentResource/manageInvoicePdf";
    static final String INVOICE_DETAILS_METHOD = "rest/billFeePlanRuleStage/findAllStudentPendingBillFeePlanDetails";


    /*Assignment releted*/
    static final String STUDENT_HOMEWORK_ASSIGNMENT_DESCRIPTION_METHOD = "rest/courseHomeWorkAssignment/findById";
    static final String STUDENT_COURSE_SESSION_DIARY_DESCRIPTION_METHOD = "rest/courseSessionDiary/findById";
    static final String STUDENT_COURSE_COVERAGE_PLAN_METHOD = "rest/courseCoveragePlan/getDailyLogOrSessionReport";
    static final String STUDENT_HOMEWORK_DOCUMENTS_METHOD = "/rest/groupHomeworkAssignmentDocument/findByCriteria";
    static final String STUDENT_HOMEWORK_DOCUMENT_DOWNLOAD_METHOD = "rest/documents/downloadFile/";
    static final String STUDENT_HOMEWORK_ASSIGNMENT_METHOD = "rest/cb/executeBasicSearch";
    static final String STUDENT_HW_ASSIGNMENT_DETAILS_BY_ASSIGNMENTID = "rest/courseHomeWorkAssignment/homeworkAssignmentDetails";
    public static final String STUDENT_HW_ASSIGNMENT_UPLOAD_TEMP = "rest/file/uploadTemp";
    public static final String STUDENT_HW_ASSIGNMENT_SAVE_MULTIPLE_DOCUMENT = "rest/groupHomeworkAssignmentDocument/saveMultipleDocument";
    public static final String STUDENT_HW_ASSIGNMENT_SUBMIT_DOCUMENT = "rest/groupHomeworkAssignmentDocument/submitForm";
    public static final String STUDENT_HW_SAVED_ASSIGNMENT_DOCUMENT = "rest/groupHomeWorkAssignment/getGroupHomeWorkAssignment";
    static final String ASSIGNMENT_TYPE_METHOD = "rest/dropDown/findAll";

    /*Exam releted*/
    static final String RESULT_REPORT_METHOD = "rest/examStudentAverage/findGridDataStudentResultReport";
    static final String STUDENT_MARKSHEETS_METHOD = "rest/marksheetJobDetailsResource/findAllMarksheetJobDetailsByAdmissionId";
    static final String STUDENT_HALLTICKET_METHOD = "rest/evaluationGroupHallTicket/getAllPublishedHallTicketByAdmissionId";
    static final String STUDENT_MARKSHEETS_DOWNLOAD_METHOD = "rest/markSheet/download";
    static final String PROGRAMS_METHOD = "rest/program/findByProgramAndAcademyLocation";
    static final String BATCH_PERIOD_METHOD = "rest/programBatchStudent/findAllCurrentBatchPeriodByStudentId";

    /*Event releted*/
    static final String STUDENT_EVENTS_METHOD = "rest/eventCalendar/getStudentEventsForApp";
    static final String STUDENT_EVENT_PICTURE_METHOD = "rest/users/downloadBase64Image";
    static final String EVENT_ATTACHMENTS_METHOD = "rest/resourceBooking/findAllResourceBookingDocs";

    /*TimeTable releted*/
    static final String STUDENT_CALENDAR_METHOD = "rest/courseCoveragePlan/getStudentCalendar";
    static final String STUDENT_CALENDAR_DURATION_METHOD = "rest/programBatchPeriodConfiguration/findByPeriodId";
    static final String STUDENT_HOLIDAYS_METHOD = "rest/calendar/findCalendarsHolidays";
    static final String TIMETABLE_COURSES_METHOD = "rest/studentCourseEnrollment/findStudentOptedCourses";

    /*Circular releted*/
    static final String STUDENT_NOTIFICATION_DOCUMENT_DOWNLOAD_METHOD = "rest/systemInternalMessagesResource/downloadAttachment/";

    /*Academia Drive related*/
    static final String ACADEMIA_DRIVE_FOLDER_METHOD = "rest/galleryResource/findsharedGalleryByUserId";
    static final String ALBUM_DETAILS_METHOD = "rest/galleryImagesResource/findAllImagesByGalleryId";
    static final String ULTIMATE_GALLERY_METHOD = "rest/galleryResource/gridDataForMobile";
    public static final String ULTIMATE_GALLERY_IMAGE_METHOD = "resources/images/uploads";

    /*My Request*/
    public static final String MY_REQUEST_LIST_METHOD = "rest/cb/executeBasicSearch";
    public static final String MY_REQUEST_REQUEST_STATUS_METHOD = "rest/requestStatusResource/findAll";
    public static final String MY_REQUEST_REQUEST_TYPE_METHOD = "rest/requestTypeResource/requestTypeByGroup";
    public static final String MY_REQUEST_RAISE_REQUEST_TYPE_METHOD = "rest/serviceRequestSettingResource/findRequestTypesByGroup";
    public static final String MY_REQUEST_REQUESTER_DETAILS_METHOD = "rest/serviceRequest/findStudentRequesterDetails";
    public static final String MY_REQUEST_REQUESTER_HOSTEL_BASIC_DETAILS_METHOD = "rest/hostelLeaveRequest/findByRequestId";
    public static final String MY_REQUEST_REQUESTER_BASIC_DETAILS_METHOD = "rest/leaveServiceRequest/findByRequestId";
    public static final String MY_REQUEST_FOLLOWUP_DETAILS_METHOD = "rest/certificateServiceRequest/gridDataFollowup";
    public static final String MY_REQUEST_APPROVAL_DETAILS_METHOD = "rest/certificateServiceRequest/gridDataApproval";
    public static final String MY_REQUEST_MANDATORY_DOCUMENT_METHOD = "rest/serviceRequest/findMandatoryDocuments";
    public static final String MY_REQUEST_WITHDRAWN_REQUEST_METHOD = "rest/certificateServiceRequest/withdrawRequest";
    public static final String MY_REQUEST_EXECUTION_HANDOVER_MODE_METHOD = "rest/requestHandoverMode/findAll";
    public static final String MY_REQUEST_EXECUTION_CLOSURE_REASON_METHOD = "rest/closureReason/findAll";
    public static final String MY_REQUEST_EXECUTION_PRINT_CERTIFICATE_METHOD = "rest/certificate/findAlMappedDocs";
    public static final String MY_REQUEST_EXECUTION_DOWNLOAD_CERTIFICATE_METHOD = "rest/dynamicDocument/printHostelLeaveCertificate";

    /*Others*/
    static final String MARK_NOTIFICATION_AS_READ_METHOD = "rest/systemInternalMessagesResource/changeMessageStatus";

    /*Parents Login related*/
    static final String ASSOCIATED_STUDENTS_METHOD = "rest/parent/findAssociatedStudents";
    static final String ASSOCIATED_SIBLINGS_METHOD = "rest/parentStudentMapping/getAssociatedSibling";


    //SWITCH CASES FOR SERVICE CALL
    /*Login Screen*/
    public static final String SWITCH_USER_LOGIN = "userLogin";
    public static final String SWITCH_USER_LOGIN_INCREPTION = "userLoginIncreption";
    public static final String SWITCH_SWITCH_STUDENT = "switchStudent";
    public static final String SWITCH_GOOGLE_SIGNIN = "googleSignin";
    public static final String SWITCH_GOOGLE_SIGNIN_SETUP = "googleSigninSetup";
    public static final String SWITCH_LATEST_VERSION_NEW = "latestVersionNew";
    public static final String SWITCH_LATEST_VERSION_NEW2 = "latestVersionNew2";
    public static final String SWITCH_FORGOT_PASSWORD = "forgotPassword";
    public static final String SWITCH_FEATURES = "features";
    public static final String SWITCH_FIND_USERID_SOCIAL_LOGIN = "findUserIdSocialLogin";
    public static final String SWITCH_STUDENT_ACADEMIC_DETAILS = "academicDetails";
    public static final String SWITCH_ASSOCIATED_STUDENTS = "associatedStudents";
    public static final String SWITCH_STUDENT_PERSONAL_DETAILS = "personalDetails";
    public static final String SWITCH_STUDENT_PERSONAL_DETAILS2 = "personalDetails2";
    public static final String SWITCH_STUDENT_PROFILE_PICTURE = "studentProfilePicture";
    public static final String SWITCH_ATTENDANCE_TYPE = "attendanceType";
    public static final String SWITCH_MOBILE_TRANSLATION = "mobileTranslation";
    public static final String SWITCH_DATE_TIME_FORMAT = "dateTimeFormat";
    public static final String SWITCH_FCM_REGISTRATION = "fcmRegistration";

    /*Dashboard Screen*/
    public static final String SWITCH_FEATURE_PRIVILEGES = "featurePrivileges";
    public static final String SWITCH_ACADEMY_IMAGE = "academyImage";
    public static final String SWITCH_LATEST_VERSION = "latestVersion";
    public static final String SWITCH_ALL_CURRENCY = "allActiveCurrency";
    public static final String SWITCH_LOGOUT = "logout";
    public static final String SWITCH_CIRCULARS = "notifications";
    public static final String SWITCH_REFRESH_LOGIN = "refreshLogin";
    public static final String SWITCH_ASSOCIATED_SIBLINGS = "associatedSiblings";
    public static final String SWITCH_NOTIFICATIONS = "notifications2";
    public static final String SWITCH_NOTIFICATIONS_COUNT = "notificationsCount";
    public static final String SWITCH_FCM_LOGOUT = "fcmLogout";
    public static final String SWITCH_FEEDBACK_EMAILS = "feedbackEmail";

    /*User Profile Screen*/
    public static final String SWITCH_PARENT_DETAILS = "parentDetails";
    public static final String SWITCH_DOCUMENT_LIST = "documentList";
    public static final String SWITCH_DOCUMENT_TYPE = "documentType";
    public static final String SWITCH_MEDICAL_CONDITION_LIST = "medicalConditionList";
    public static final String SWITCH_MEDICAL_CONDITION_TYPES = "medicalConditionType";
    public static final String SWITCH_MEDICAL_CONDITION_COUNTRY_CODE = "medicalConditionCountryCode";
    public static final String SWITCH_MEDICAL_CONDITION_CREATE = "medicalConditionCreate";
    public static final String SWITCH_DISCIPLINARY_ACTION_LIST = "desciplinaryActionList";
    public static final String SWITCH_FEE_PAYER_LIST = "feePayerList";
    public static final String SWITCH_FEE_PAYER_CURRENT_ADDRESS = "studentCurrentAddress";
    public static final String SWITCH_COUNTRY_REGION_CODE = "countryRegion";
    public static final String SWITCH_FEE_PAYER_CITY = "feePayerCity";
    public static final String SWITCH_DEBIT_ORDER_DATE = "debitOrderDate";
    public static final String SWITCH_FEE_BANK_LIST = "bankList";
    public static final String SWITCH_FEE_BANK_BRANCH_CODE = "bankBranchCode";
    public static final String SWITCH_REMOVE_PROFILE = "removeProfile";
    public static final String SWITCH_PROFILE_DETAILS = "profileDetails";
    public static final String SWITCH_PROFILE_SALUTATIONS = "profileSalutations";
    public static final String SWITCH_PROFILE_MULTILANGUAGE = "profileMultiLanguage";
    public static final String SWITCH_PROFILE_CONTACTLANGUAGE = "profileContactLanguage";
    public static final String SWITCH_PROFILE_DISABILITIES = "profileDisabilities";
    public static final String SWITCH_PROFILE_CURRENT_EDUCATIONAL = "profileCurrentEducational";
    public static final String SWITCH_PROFILE_GENDER = "profileGender";
    public static final String SWITCH_PROFILE_NATIONALITY = "profileNationality";
    public static final String SWITCH_PROFILE_DOMICILE = "profileDomicile";

    /*Reset Password Screen*/
    public static final String SWITCH_RESET_PASSWORD = "resetPassword";
    public static final String SWITCH_RESET_PASSWORD_ENCRYPT = "resetPasswordEncrypt";
    public static final String SWITCH_PASSWORD_POLICY = "userPasswordPolicy";

    /*MyCourses Screen*/
    public static final String SWITCH_MYCOURSE_PROGRAM = "myCourseProgram";
    public static final String SWITCH_MYCOURSE_BATCH = "myCourseBatch";
    public static final String SWITCH_MYCOURSE_PERIOD = "myCoursePeriod";
    public static final String SWITCH_MY_COURSES_LIST = "myCoursesList";

    /*Attendance Screen*/
    public static final String SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY = "attendanceSummaryCompleteDay";
    public static final String SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL = "attendanceSummaryCourseLevel";
    public static final String SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION = "attendanceSummaryMultipleSession";
    public static final String SWITCH_ATTENDANCE_PROGRAMS = "attendancePrograms";
    public static final String SWITCH_ATTENDANCE_BATCH = "attendanceBatch";
    public static final String SWITCH_ATTENDANCE_PERIOD = "attendancePeriod";
    public static final String SWITCH_ATTENDANCE_SECTION = "attendanceSection";
    public static final String SWITCH_COURSE_LIST = "courseList";
    public static final String SWITCH_COURSE_VARIANT = "courseVariant";
    public static final String SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY = "attendanceDetaisCompleteDay";
    public static final String SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL = "attendanceDetaisCourseLevel";
    public static final String SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION = "attendanceDetaisMultipleSession";
    public static final String SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE = "attendanceDetaisCompleteDayMonthwise";
    public static final String SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE = "attendanceDetaisCourseLevelMonthwise";
    public static final String SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE = "attendanceDetaisMultipleSessionMonthwise";

    /*Fees Screen*/
    public static final String SWITCH_STUDENT_FEES = "studentFees";
    public static final String SWITCH_STUDENT_RECEIPTS = "studentReceipts";
    public static final String SWITCH_RECEIPT_DOWNLOAD = "downloadReceipt";
    public static final String SWITCH_STUDENT_INVOICE = "studnetInvoice";
    public static final String SWITCH_PAYMENT_GATEWAY = "paymentGateway";
    public static final String SWITCH_PAYMENT_ID = "paymentID";
    public static final String SWITCH_PAYTM_GET_CHECKSUMHASH = "generateChecksum";
    public static final String SWITCH_TRANSACTION_ID_UPDATE = "transactionIDUpdate";
    public static final String SWITCH_TRANSACTION_UPDATE = "transactionUpdate";
    public static final String SWITCH_BILL_HEADER = "billHeader";
    public static final String SWITCH_INVOICE_DOWNLOAD = "downloadInvoice";
    public static final String SWITCH_PENDING_BILLS = "pendingBills";
    public static final String SWITCH_PENDING_FEE_HEADS = "pendingFeeHeads";
    public static final String SWITCH_PAYMENT_ID_FEE_HEAD = "paymentID_FeeHead";
    public static final String SWITCH_INVOICE_DETAILS = "invoiceDetails";

    /*Assignment Screen*/
    public static final String SWITCH_COURSE_COVERAGE_LIST_DETAILS = "courseCoveragePlan3";
    public static final String SWITCH_COURSE_DIARY_DESCRIPTION = "courseDiaryDescription";
    public static final String SWITCH_COURSE_COVERAGE_PLAN_1_DETAILS = "courseCoveragePlan1";
    public static final String SWITCH_COURSE_COVERAGE_PLAN_2_DETAILS = "courseCoveragePlan2";
    public static final String SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS = "homeworkAssignments";
    public static final String SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2 = "homeworkAssignments2";
    public static final String SWITCH_HOMEWORK_DOCUMENTS = "assignmentDocuments";
    public static final String SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION = "homeworkAssignmentDescription";
    public static final String SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD = "assignmentDocumentDownload";
    public static final String SWITCH_HOMEWORK_SAVED_DOCUMENT = "savedDocument";
    public static final String SWITCH_ASSIGNMENT_TYPE = "assignmentType";

    /*Exam Screen*/
    public static final String SWITCH_RESULT_REPORT = "resultReport";
    public static final String SWITCH_STUDENT_MARKSHEETS = "studentMarksheets";
    public static final String SWITCH_STUDENT_HALLTICKET = "studentHallTicket";
    public static final String SWITCH_STUDENT_MARKSHEETS_DOWNLOAD = "studentMarksheetsDownload";
    public static final String SWITCH_PROGRAMS = "resultPrograms";
    public static final String SWITCH_BATCH = "resultBatch";
    public static final String SWITCH_PERIOD = "resultPeriod";

    /*Event Screen*/
    public static final String SWITCH_STUDENT_EVENTS = "studentEvents";
    public static final String SWITCH_EVENT_ATTACHMENTS = "eventAttachments";
    public static final String SWITCH_EVENT_PICTURE = "eventPicture";

    /*TimeTable Screen*/
    public static final String SWITCH_STUDENT_CALENDAR_COURSEWISE = "studentCalendarCoursewise";
    public static final String SWITCH_TIMETABLE_COURSES = "timetableCourses";
    public static final String SWITCH_STUDENT_HOLIDAYS_MONTHWISE = "sstudentHolidaysMonthwise";
    public static final String SWITCH_STUDENT_CALENDAR_MONTHWISE = "studentCalendarMonthwise";
    public static final String SWITCH_STUDENT_CALENDAR_DURATION = "studentCalendarDuration";
    public static final String SWITCH_STUDENT_CALENDAR = "studentCalendar";

    /*Circular Screen*/
    public static final String SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD = "notificationDocumentDownload";

    /*Academia Drive*/
    public static final String SWITCH_ACADEMIA_DRIVE_FOLDER = "academiaDriveFolder";
    public static final String SWITCH_ALBUM_DETAILS = "albumDetails";
    public static final String SWITCH_ULTIMATE_GALLERY = "ultimateGallery";

    /*My Request*/
    public static final String SWITCH_REQUEST_STATUS = "myRequestStatus";
    public static final String SWITCH_REQUEST_TYPE = "myRequestType";
    public static final String SWITCH_RAISE_REQUEST_TYPE = "raiseRequestType";
    public static final String SWITCH_REQUESTER_DETAILS = "requesterDetails";
    public static final String SWITCH_REQUESTER_HOSTEL_BASIC_DETAILS = "requesterHostelBasicDetails";
    public static final String SWITCH_REQUESTER_BASIC_DETAILS = "requesterBasicDetails";
    public static final String SWITCH_MANDETORY_DOCUMENTS = "mandatoryDocuments";
    public static final String SWITCH_FOLLOWUP_DETAILS = "followupDetails";
    public static final String SWITCH_APPROVAL_DETAILS = "approvalDetails";
    public static final String SWITCH_WITHDRAW_REQUEST = "withdrawRequest";
    public static final String SWITCH_EXECUTION_HANDOVER_MODE = "executionHandoverMode";
    public static final String SWITCH_EXECUTION_CLOSURE_REASON = "executionClosureReason";
    public static final String SWITCH_EXECUTION_PRINT_CERTIFICATE = "executionPrintCertificate";
    public static final String SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE = "executionDownloadCertificate";

    /*Others*/
    public static final String SWITCH_MARK_NOTIFICATION_AS_READ = "markNotificationAsRead";

    public static final String NET_ERROR_HEAD = "Network Error";
    public static final String NET_ERROR_MESSAGE = "Please Check Your Internet Connection!";

    public static final String CALL_FOR = "callFor";
    public static final String RETURN_RESPONSE = "returnResponse";
    public static final String RETURN_RESULT = "returnResult";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String COURSE_LEVEL = "COURSE_LEVEL";
    public static final String MULTIPLE_SESSION = "MULTIPLE_SESSION";
    public static final String COMPLETE_DAY = "COMPLETE_DAY";

    public static final String FEES_STRING = "FEES_STRING";
    public static final String RECEIPTS_STRING = "RECEIPTS_STRING";

    /**
     * Password encryption key
     */
    public static final String ENCRYPT_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1Nrr7gsMxSv+WN/eaJJE0JcOce8McNfRMK0pYKFrwuyvEj6W0sul+npdIlz6eT69SZA5uh+kmjVa4jHj2CPdEXrtuECl761acyay+nTDpxBNAu5lNZmN3roD+sBciGmqX0A2Awlb78mq/cjf+Jm2TRf2o6Pkv5FLocDAo7FgPIwIDAQAB";

    /**
     * Faculty Dashboard URL
     */
    //public static final String FACULTY_WEBSITE_URL = BaseURL.BASE_URL+"dist/#/auth/login";
    public static final String FACULTY_WEBSITE_URL = BaseURL.BASE_URL+"facultyportal/#/auth/login";
    //public static final String FACULTY_WEBSITE_URL = BaseURL.BASE_URL+"faculty/#/auth/login";
}
