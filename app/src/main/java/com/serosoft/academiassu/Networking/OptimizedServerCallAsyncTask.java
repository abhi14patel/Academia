package com.serosoft.academiassu.Networking;

import android.content.Context;
import android.os.AsyncTask;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Manager.LoginManager;
import com.serosoft.academiassu.Manager.SwitchStudentManager;

import java.util.HashMap;

public class OptimizedServerCallAsyncTask extends AsyncTask<String, Void, String> {

    private String returnResponse = "";
    private String returnResult;
    Context context;
    private AsyncTaskCompleteListener callback;
    private String callFor;
    private APIManager apiManager;
    private LoginManager loginManager;
    private SwitchStudentManager switchStudentManager;

    public OptimizedServerCallAsyncTask(Context context, AsyncTaskCompleteListener callback, String callFor) {

        this.context = context;
        this.callback = callback;
        this.callFor = callFor;
        apiManager = new APIManager(context);
        loginManager = new LoginManager(context);
        switchStudentManager = new SwitchStudentManager(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        switch (callFor) {
            /*Login*/
            case KEYS.SWITCH_USER_LOGIN:
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(KEYS.USERNAME, params[0]);
                hashMap.put(KEYS.PASSWORD, params[1]);
                hashMap.put(KEYS.GRANT_TYPE, params[2]);
                hashMap.put(KEYS.CLIENT_ID, params[3]);
                hashMap.put(KEYS.CLIENT_SECRET, params[4]);
                if (loginManager.validateUserNameandPassword(hashMap)) {
                    returnResult = KEYS.TRUE;
                } else {
                    returnResult = KEYS.FALSE;
                }
                break;
            case KEYS.SWITCH_USER_LOGIN_INCREPTION:
                HashMap<String, String> hashMap1 = new HashMap<String, String>();
                hashMap1.put(KEYS.USERNAME, params[0]);
                hashMap1.put(KEYS.PASSWORD, params[1]);
                hashMap1.put(KEYS.GRANT_TYPE, params[2]);
                hashMap1.put(KEYS.CLIENT_ID, params[3]);
                hashMap1.put(KEYS.CLIENT_SECRET, params[4]);
                if (loginManager.validateUserNameandPassword(hashMap1)) {
                    returnResult = KEYS.TRUE;
                } else {
                    returnResult = KEYS.FALSE;
                }
                break;
            case KEYS.SWITCH_GOOGLE_SIGNIN:
                HashMap<String, String> hashMap2 = new HashMap<String, String>();
                hashMap2.put(KEYS.USERNAME, params[0]);
                hashMap2.put(KEYS.PASSWORD, params[1]);
                hashMap2.put(KEYS.GRANT_TYPE, params[2]);
                hashMap2.put(KEYS.CLIENT_ID, params[3]);
                hashMap2.put(KEYS.CLIENT_SECRET, params[4]);
                hashMap2.put(KEYS.PORTAL_CODE, params[5]);
                hashMap2.put(KEYS.GOOGLE_ACCESS_TOKEN, params[6]);
                if (loginManager.googleSignin(hashMap2)) {
                    returnResult = KEYS.TRUE;
                } else {
                    returnResult = KEYS.FALSE;
                }
                break;
            case KEYS.SWITCH_SWITCH_STUDENT:
                if (switchStudentManager.getAssociatedStudentsFromServer()) {
                    returnResult = KEYS.TRUE;
                } else {
                    returnResult = KEYS.FALSE;
                }
                break;
            case KEYS.SWITCH_GOOGLE_SIGNIN_SETUP:
                returnResponse = apiManager.getGoogleSigninFromServer();
                break;
            case KEYS.SWITCH_LATEST_VERSION_NEW:
                returnResponse = apiManager.getLatestVersionNewFromServer();
                break;
            case KEYS.SWITCH_LATEST_VERSION_NEW2:
                returnResponse = apiManager.getLatestVersionNew2FromServer();
                break;
            case KEYS.SWITCH_FORGOT_PASSWORD:
                returnResponse = apiManager.getForgotPasswordStatusFromServer(params[0]);
                break;

            /*Dashboard*/
            case KEYS.SWITCH_FEATURE_PRIVILEGES:
                returnResponse = apiManager.getFeaturePrivilegesFromServer(params[0],params[1]);
                break;
            case KEYS.SWITCH_ACADEMY_IMAGE:
                returnResponse = apiManager.getAcademyImageFromServer();
                break;
            case KEYS.SWITCH_LATEST_VERSION:
                returnResponse = apiManager.getLatestVersionFromServer();
                break;
            case KEYS.SWITCH_ALL_CURRENCY:
                returnResponse = apiManager.getAllCurrencyDetailsFromServer();
                break;
            case KEYS.SWITCH_LOGOUT:
                returnResponse = apiManager.userLogout();
                break;
            case KEYS.SWITCH_CIRCULARS:
                returnResponse = apiManager.getNotificationsFromServer();
                break;
            case KEYS.SWITCH_REFRESH_LOGIN:
                hashMap = new HashMap<String, String>();
                hashMap.put(KEYS.USERNAME, params[0]);
                hashMap.put(KEYS.PASSWORD, params[1]);
                hashMap.put(KEYS.GRANT_TYPE, params[2]);
                hashMap.put(KEYS.CLIENT_ID, params[3]);
                hashMap.put(KEYS.CLIENT_SECRET, params[4]);
                if (apiManager.refreshLogin(hashMap)) {
                    returnResult = KEYS.TRUE;
                } else {
                    returnResult = KEYS.FALSE;
                }
                break;
            case KEYS.SWITCH_ASSOCIATED_SIBLINGS:
                returnResponse = apiManager.getAssociatedSiblingsFromServer();
                break;
            case KEYS.SWITCH_NOTIFICATIONS:
                returnResponse = apiManager.getNotifications2FromServer();
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                returnResponse = apiManager.getNotificationsCountFromServer();
                break;
            case KEYS.SWITCH_FCM_LOGOUT:
                returnResponse = apiManager.deleteFCMTokenFromServer();
                break;
            case KEYS.SWITCH_FEEDBACK_EMAILS:
                returnResponse = apiManager.getFeedbackEMailsFromServer();
                break;

            /*UserProfile*/
            case KEYS.SWITCH_PARENT_DETAILS:
                returnResponse = apiManager.getParentDetailsFromServer();
                break;
            case KEYS.SWITCH_DOCUMENT_LIST:
                returnResponse = apiManager.getDocumentListFromServer();
                break;
            case KEYS.SWITCH_DOCUMENT_TYPE:
                returnResponse = apiManager.getDocumentTypeFromServer();
                break;
            case KEYS.SWITCH_MEDICAL_CONDITION_LIST:
                returnResponse = apiManager.getMedicalConditionListFromServer();
                break;
            case KEYS.SWITCH_MEDICAL_CONDITION_TYPES:
                returnResponse = apiManager.getMedicalConditionTypesFromServer();
                break;
            case KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE:
                returnResponse = apiManager.getMedicalConditionCountryCodeFromServer();
                break;
            case KEYS.SWITCH_MEDICAL_CONDITION_CREATE:
                returnResponse = apiManager.createMedicalCondition(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
                break;
            case KEYS.SWITCH_DISCIPLINARY_ACTION_LIST:
                returnResponse = apiManager.getDisciplinaryActionListFromServer();
                break;
            case KEYS.SWITCH_FEE_PAYER_LIST:
                returnResponse = apiManager.getFeePayerListFromServer();
                break;
            case KEYS.SWITCH_FEE_PAYER_CURRENT_ADDRESS:
                returnResponse = apiManager.getCurrentAddressFromServer();
                break;
            case KEYS.SWITCH_COUNTRY_REGION_CODE:
                returnResponse = apiManager.getCountryRegionListFromServer(params[0]);
                break;
            case KEYS.SWITCH_FEE_PAYER_CITY:
                returnResponse = apiManager.getCityListFromServer(params[0],params[1]);
                break;
            case KEYS.SWITCH_DEBIT_ORDER_DATE:
                returnResponse = apiManager.getDebitOrderDateFromServer();
                break;
            case KEYS.SWITCH_FEE_BANK_LIST:
                returnResponse = apiManager.getBankListDateFromServer();
                break;
            case KEYS.SWITCH_FEE_BANK_BRANCH_CODE:
                returnResponse = apiManager.getBranchCodeFromServer(params[0]);
                break;
            case KEYS.SWITCH_REMOVE_PROFILE:
                returnResponse = apiManager.getRemoveProfileFromServer();
                break;
            case KEYS.SWITCH_STUDENT_PROFILE_PICTURE:
                returnResponse = apiManager.getUpdateProfilePictureFromServer(params[0]);
                break;
            case KEYS.SWITCH_PROFILE_DETAILS:
                returnResponse = apiManager.getProfileDetailsFromServer();
                break;
            case KEYS.SWITCH_PROFILE_SALUTATIONS:
                returnResponse = apiManager.getProfileSalutationFromServer();
                break;
            case KEYS.SWITCH_PROFILE_MULTILANGUAGE:
                returnResponse = apiManager.getProfileMultiLanguageFromServer();
                break;
            case KEYS.SWITCH_PROFILE_CONTACTLANGUAGE:
                returnResponse = apiManager.getProfileContactLanguageFromServer();
                break;
            case KEYS.SWITCH_PROFILE_DISABILITIES:
                returnResponse = apiManager.getProfileDisabilitiesFromServer();
                break;
            case KEYS.SWITCH_PROFILE_CURRENT_EDUCATIONAL:
                returnResponse = apiManager.getProfileCurrentEducationFromServer();
                break;
            case KEYS.SWITCH_PROFILE_GENDER:
                returnResponse = apiManager.getProfileGenderFromServer();
                break;
            case KEYS.SWITCH_PROFILE_NATIONALITY:
                returnResponse = apiManager.getProfileNationalityFromServer();
                break;
            case KEYS.SWITCH_PROFILE_DOMICILE:
                returnResponse = apiManager.getProfileDomicileFromServer();
                break;

            /*Attendance*/
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY:
                returnResponse = apiManager.getAttendanceSummaryCompleteDayFromServer(params[0],params[1],params[2],params[3]);
                break;
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL:
                returnResponse = apiManager.getAttendanceSummaryCourseLevelFromServer(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8]);
                break;
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION:
                returnResponse = apiManager.getAttendanceSummaryMultipleSessionFromServer(params[0],params[1],params[2],params[3]);
                break;
            case KEYS.SWITCH_ATTENDANCE_PROGRAMS:
                returnResponse = apiManager.getAttendanceProgramsFromServer();
                break;
            case KEYS.SWITCH_ATTENDANCE_BATCH:
                returnResponse = apiManager.getAttendanceBatchFromServer(params[0]);
                break;
            case KEYS.SWITCH_ATTENDANCE_PERIOD:
                returnResponse = apiManager.getAttendancePeriodFromServer(params[0]);
                break;
            case KEYS.SWITCH_ATTENDANCE_SECTION:
                returnResponse = apiManager.getAttendanceSectionFromServer(params[0]);
                break;
            case KEYS.SWITCH_COURSE_LIST:
                returnResponse = apiManager.getCourseListFromServer(params[0],params[1],params[2]);
                break;
            case KEYS.SWITCH_COURSE_VARIANT:
                returnResponse = apiManager.getCourseVariantFromServer(params[0],params[1],params[2],params[3]);
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY:
                returnResponse = apiManager.getAttendanceDetailsCompleteDayFromServer();
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL:
                returnResponse = apiManager.getAttendanceDetailsCourseLevelFromServer(params[0]);
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION:
                returnResponse = apiManager.getAttendanceDetailsMultipleSessionFromServer();
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE:
                returnResponse = apiManager.getAttendanceDetailsCompleteDayMonthwiseFromServer(params[0], params[1], params[2]);
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE:
                returnResponse = apiManager.getAttendanceDetailsCourseLevelMonthwiseFromServer(params[0], params[1], params[2]);
                break;
            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE:
                returnResponse = apiManager.getAttendanceDetailsMultipleSessionMonthwiseFromServer(params[0], params[1],params[2]);
                break;

            /*Fees*/
            case KEYS.SWITCH_STUDENT_FEES:
                returnResponse = apiManager.getFeesFromServer();
                break;
            case KEYS.SWITCH_STUDENT_RECEIPTS:
                returnResponse = apiManager.getReceiptsFromServer();
                break;
            case KEYS.SWITCH_RECEIPT_DOWNLOAD:
                returnResponse = apiManager.downloadReceiptFromServer(params[0], params[1], params[2]);
                break;
            case KEYS.SWITCH_STUDENT_INVOICE:
                returnResponse = apiManager.getStudentInvoicesFromServer();
                break;
            case KEYS.SWITCH_PAYMENT_GATEWAY:
                returnResponse = apiManager.getPaymentGatewayKeysFromServer(params[0]);
                break;
            case KEYS.SWITCH_PAYMENT_ID:
                returnResponse = apiManager.getPaymentIDFromServer(params[0], params[1]);
                break;
            case KEYS.SWITCH_PAYTM_GET_CHECKSUMHASH:
                returnResponse = apiManager.getPayTMChecksumHashFromServer(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7],params[8]);
                break;
            case KEYS.SWITCH_TRANSACTION_ID_UPDATE:
                returnResponse = apiManager.postTransactionIDUpdateToServer(params[0], params[1]);
                break;
            case KEYS.SWITCH_TRANSACTION_UPDATE:
                returnResponse = apiManager.postTransactionUpdateToServer(params[0]);
                break;
            case KEYS.SWITCH_BILL_HEADER:
                returnResponse = apiManager.getBillHeaderDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_INVOICE_DETAILS:
                returnResponse = apiManager.getInvoiceDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_INVOICE_DOWNLOAD:
                returnResponse = apiManager.downloadInvoiceFromServer(params[0], params[1]);
                break;
            case KEYS.SWITCH_PENDING_BILLS:
                returnResponse = apiManager.getPendingBillsFromServer();
                break;
            case KEYS.SWITCH_PENDING_FEE_HEADS:
                returnResponse = apiManager.getPendingFeeHeadsFromServer();
                break;
            case KEYS.SWITCH_PAYMENT_ID_FEE_HEAD:
                returnResponse = apiManager.getPaymentIDFeeHeadFromServer(params[0], params[1]);
                break;

            /*Assignment*/
            case KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS:
                returnResponse = apiManager.getCourseCoveragePlan3FromServer();
                break;
            case KEYS.SWITCH_COURSE_DIARY_DESCRIPTION:
                returnResponse = apiManager.getCourseSessionDiaryDescriptionFromServer(params[0]);
                break;
            case KEYS.SWITCH_COURSE_COVERAGE_PLAN_1_DETAILS:
                returnResponse = apiManager.getCourseCoveragePlan1FromServer();
                break;
            case KEYS.SWITCH_COURSE_COVERAGE_PLAN_2_DETAILS:
                returnResponse = apiManager.getCourseCoveragePlan2FromServer();
                break;
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS:
                returnResponse = apiManager.getHomeworkAssignmentDetailsFromServer();
                break;
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2:
                returnResponse = apiManager.getHomeworkAssignmentDetailsFromServer(params[0],params[1]);
                break;
            case KEYS.SWITCH_HOMEWORK_DOCUMENTS:
                returnResponse = apiManager.getHomeworkDocumentsFromServer(params[0]);
                break;
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION:
                returnResponse = apiManager.getHomeworkAssignmentDescriptionFromServer(params[0]);
                break;
            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                returnResponse = apiManager.downloadHomeworkAssignmentFromServer(params[0], params[1],params[2]);
                break;
            case KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT:
                returnResponse = apiManager.getSavedDocuments(params[0],params[1]);
                break;
            case KEYS.SWITCH_ASSIGNMENT_TYPE:
                returnResponse = apiManager.getAssignmentTypeFromServer();
                break;

            /*Timetable*/
            case KEYS.SWITCH_STUDENT_CALENDAR_COURSEWISE:
                returnResponse = apiManager.getCalendarFromServerCourseWise(params[0]);
                break;
            case KEYS.SWITCH_TIMETABLE_COURSES:
                returnResponse = apiManager.getTimetableCoursesFromServer();
                break;
            case KEYS.SWITCH_STUDENT_HOLIDAYS_MONTHWISE:
                returnResponse = apiManager.getHolidaysFromServerMonthWise(params[0],params[1]);
                break;
            case KEYS.SWITCH_STUDENT_CALENDAR_MONTHWISE:
                returnResponse = apiManager.getCalendarFromServerMonthWise(params[0],params[1]);
                break;
            case KEYS.SWITCH_STUDENT_CALENDAR_DURATION:
                returnResponse = apiManager.getCalendarDurationFromServer(params[0]);
                break;
            case KEYS.SWITCH_STUDENT_CALENDAR:
                returnResponse = apiManager.getCalendarFromServer();
                break;

            /*Event*/
            case KEYS.SWITCH_STUDENT_EVENTS:
                returnResponse = apiManager.getStudentEventsFromServer();
                break;
            case KEYS.SWITCH_EVENT_ATTACHMENTS:
                returnResponse = apiManager.getEventAttachmentsFromServer(params[0]);
                break;
            case KEYS.SWITCH_EVENT_PICTURE:
                returnResponse = apiManager.getEventImageFromServer(params[0], params[1]);
                break;

            /*Circular*/
            case KEYS.SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD:
                returnResponse = apiManager.downloadNotificationDocumentFromServer(params[0], params[1],params[2]);
                break;

            /*Reset Password*/
            case KEYS.SWITCH_RESET_PASSWORD:
                returnResponse = apiManager.resetPassword(params[0], params[1]);
                break;
            case KEYS.SWITCH_RESET_PASSWORD_ENCRYPT:
                returnResponse = apiManager.resetPassword(params[0], params[1]);
                break;
            case KEYS.SWITCH_PASSWORD_POLICY:
                returnResponse = apiManager.getPasswordPolicyFromServer();
                break;

            case KEYS.SWITCH_MARK_NOTIFICATION_AS_READ:
                returnResponse = apiManager.markNotificationAsRead(params[0]);
                break;

            /*Exam*/
            case KEYS.SWITCH_RESULT_REPORT:
                returnResponse = apiManager.getResultReportFromServer(params[0],params[1],params[2]);
                break;
            case KEYS.SWITCH_STUDENT_MARKSHEETS:
                returnResponse = apiManager.getMarksheetsFromServer();
                break;
            case KEYS.SWITCH_STUDENT_HALLTICKET:
                returnResponse = apiManager.getHallTicketFromServer();
                break;
            case KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD:
                returnResponse = apiManager.downloadMarksheetFromServer(params[0],params[1]);
                break;
            case KEYS.SWITCH_PROGRAMS:
                returnResponse = apiManager.getResultProgramsFromServer(params[0]);
                break;
            case KEYS.SWITCH_BATCH:
                returnResponse = apiManager.getResultBatchFromServer(params[0],params[1]);
                break;
            case KEYS.SWITCH_PERIOD:
                returnResponse = apiManager.getResultPeriodFromServer(params[0],params[1]);
                break;

            /*My Courses*/
            case KEYS.SWITCH_MYCOURSE_PROGRAM:
                returnResponse = apiManager.getMyCourseProgramsFromServer();
                break;
            case KEYS.SWITCH_MYCOURSE_BATCH:
                returnResponse = apiManager.getMyCourseBatchFromServer(params[0]);
                break;
            case KEYS.SWITCH_MYCOURSE_PERIOD:
                returnResponse = apiManager.getMyCoursePeriodFromServer(params[0]);
                break;
            case KEYS.SWITCH_MY_COURSES_LIST:
                returnResponse = apiManager.getMyCoursesFromServer(params[0],params[1],params[2],params[3]);
                break;

            /*Academia Drive*/
            case KEYS.SWITCH_ACADEMIA_DRIVE_FOLDER:
                returnResponse = apiManager.getAcademiaDriveFolderFromServer();
                break;
            case KEYS.SWITCH_ALBUM_DETAILS:
                returnResponse = apiManager.getDriveDetailFromServer(params[0]);
                break;
            case KEYS.SWITCH_ULTIMATE_GALLERY:
                returnResponse = apiManager.getUltimateGalleryFromServer();
                break;

                /*My Request*/
            case KEYS.SWITCH_RAISE_REQUEST_TYPE:
                returnResponse = apiManager.getMyRequestRaiseRequestTypeFromServer(params[0]);
                break;
            case KEYS.SWITCH_REQUEST_STATUS:
                returnResponse = apiManager.getMyRequestStatusFromServer();
                break;
            case KEYS.SWITCH_REQUEST_TYPE:
                returnResponse = apiManager.getMyRequestTypeFromServer(params[0]);
                break;
            case KEYS.SWITCH_REQUESTER_DETAILS:
                returnResponse = apiManager.getMyRequestRequesterDetailsFromServer();
                break;
            case KEYS.SWITCH_REQUESTER_HOSTEL_BASIC_DETAILS:
                returnResponse = apiManager.getRequesterHostelBasicDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_REQUESTER_BASIC_DETAILS:
                returnResponse = apiManager.getRequesterBasicDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_MANDETORY_DOCUMENTS:
                returnResponse = apiManager.getMandatoryDocumentFromServer(params[0]);
                break;
            case KEYS.SWITCH_FOLLOWUP_DETAILS:
                returnResponse = apiManager.getFollowupDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_APPROVAL_DETAILS:
                returnResponse = apiManager.getApprovalDetailsFromServer(params[0]);
                break;
            case KEYS.SWITCH_WITHDRAW_REQUEST:
                returnResponse = apiManager.getWithdrawnRequestFromServer(params[0]);
                break;
            case KEYS.SWITCH_EXECUTION_HANDOVER_MODE:
                returnResponse = apiManager.getExecutionHandoverModeFromServer();
                break;
            case KEYS.SWITCH_EXECUTION_CLOSURE_REASON:
                returnResponse = apiManager.getExecutionClosureModeFromServer();
                break;
            case KEYS.SWITCH_EXECUTION_PRINT_CERTIFICATE:
                returnResponse = apiManager.getPrintCertificateFromServer(params[0]);
                break;
            case KEYS.SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE:
                returnResponse = apiManager.getDownloadCertificateFromServer(params[0],params[1]);
                break;
        }
        return returnResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put(KEYS.RETURN_RESPONSE, returnResponse);
        responseMap.put(KEYS.RETURN_RESULT, returnResult);
        responseMap.put(KEYS.CALL_FOR, callFor);
        callback.onTaskComplete(responseMap);
    }
}
