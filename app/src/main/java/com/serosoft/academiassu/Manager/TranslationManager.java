package com.serosoft.academiassu.Manager;

import android.content.Context;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Login.TranslationKeys;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class TranslationManager extends BaseClass {

    Context context;
    private SharedPrefrenceManager sharedPrefrenceManager;

    public String NOTIFICATION_KEY = "",NOTIFICATION_DETAILS_KEY = "",DASHBOARD_KEY = "",REFREASH_KEY = "",OK_KEY = "",SHARE_APP_CONTENT_KEY = "";
    public String DASHBOARD_TITLE_KEY = "",ASSIGNMENT_KEY = "",MYCOURSE_KEY="",ATTENDANCE_KEY = "",TIMETABLE_KEY = "",FEES_KEY = "",EXAMRESULT_KEY = "",CIRCULAR_KEY = "",EVENT_KEY = "", ACADEMIA_DRIVE_KEY = "";
    public String HOME_KEY = "",LOGOUT_KEY = "",MYPROFILE_KEY = "",RESET_PASSWORD_KEY  ="",SWITCH_STUDENT_KEY = "",RATE_REVIEW_KEY = "",FEEDBACK_KEY = "";
    public String COURSE_WISE_KEY = "",PROGRAM_WISE_KEY = "",SESSION_WISE_KEY = "",COURSE_WISE_KEY1 = "",DAY_WISE_KEY = "", COURSES_KEY = "";
    public String PRESENT_KEY = "",ABSENT_KEY = "",OTHER_KEY = "",TOTAL_CLASSES_KEY = "",OVERALL_KEY = "",THISMONTH_KEY = "",FACULTY_KEY = "";
    public String HOMEWORKASSIGNMENT_KEY = "",COURSE_SESSIONDIARY_KEY = "",SESSION_NUMBER_KEY = "";
    public String COURSE_VARIANT_KEY = "",SESSION_DATE_KEY = "",TOPIC_KEY = "",ATTACHMENTS_KEY = "",DUE_ON_KEY = "";
    public String ATTACHMENT_KEY = "",COURSE_STATUS_KEY = "",CIRCULAR_DETAILS_KEY = "";
    public String CONFIRM_PASSWORD_KEY = "",NEW_PASSWORD_KEY = "",OLD_PASSWORD_KEY = "";
    public String EVENT_DESCRIPTION_KEY = "",EVENT_VENUE_KEY = "",EVENT_ADMIN_KEY = "",EVENT_START_DATE_KEY = "",EVENT_END_DATE_KEY = "";
    public String PROFILE_INFORMATION_KEY = "",PAST_HISTORY_KEY = "",PERSONAL_DETAILS_KEY = "",PARENTS_DETAILS_KEY = "",DOCUMENTS_KEY = "",STUDENT_NAME_KEY = "",STUDENTID_KEY = "",ADMISSIONID_KEY = "",EMAILID_KEY = "";
    public String PHONE_NUMBER_KEY = "",MOBILE_NUMBER_KEY = "",ADDRESS_KEY = "",DATE_OF_ADMISSION_KEY = "",DATE_OF_BIRTH_KEY = "",CATEGORY_KEY = "",GENDER_KEY = "",FATHER_NAME_KEY = "",MOTHER_NAME_KEY = "",RELIGION_KEY = "",CASTE_KEY = "",BLOOD_GROUP_KEY = "";
    public String DOCUMENT_NAME_KEY = "", SUBMISSION_DATE_KEY = "",INSPECTION_DATE_KEY = "",STATUS_KEY = "", PROGRAM_KEY = "",SEAT_TYPE_KEY = "",REMARK_KEY = "",PREDEFINED_DOC_KEY = "",OTHERS_KEY = "";
    public String RESULT_REPORT_KEY = "",EXAM_DOC_KEY = "",MARKSHEET_KEY = "",HALLTICKET_KEY = "",BATCH_KEY = "",PERIOD_KEY = "",SECTION_KEY = "";
    public String OBTAINEDMARKS_KEY = "",MAXMARKS_KEY = "",WEIGHTAGE_KEY = "",EFFECTIVEMARKS_KEY = "", MODERATIONMARKS_KEY = "";
    public String HALL_TICKETNO_KEY = "",ASSESSEMENT_GROUP_KEY = "",TOTAL_OUTSTANDING_KEY = "";
    public String PUBLISH_DATE_KEY = "";
    public String BILLED_KEY = "",RECEIPT_KEY = "",RECEIPT_NUMBER_KEY = "",DISCOUNT_AMOUNT_KEY = "",AMOUNT_SETTLED_KEY = "",RECEIPT_DATE_KEY = "";
    public String TOTAL_AMOUNT_KEY = "",BALANCE_AMOUNT_KEY = "",FULLY_PENDING_KEY = "",PARTIALLY_PAID_KEY = "";
    public String PAY_KEY = "",PAYNOW_KEY = "",ALL_KEY="",DUE_KEY = "",PAID_KEY = "",CANCEL_KEY = "",AMOUNTTOPAY_KEY = "";
    public String BILL_DATE_KEY = "",DUE_DATE_KEY = "",BILL_NUMBER_KEY = "",PARTIALLY_SETTLED_KEY = "",BILLNO_KEY = "",INVOICE_KEY = "";
    public String COURSE_CODE_KEY = "",USER_CODE_KEY = "",FATHER_DETAILS_KEY = "",MOTHER_DETAILS_KEY = "",LOCAL_GUARDIAN_DETAILS_KEY = "",GUARDIAN_DETAILS_KEY = "";
    public String ATTENDANCE_START_DATE_KEY = "", ATTENDANCE_END_DATE_KEY = "", ATTENDANCE_COURSE_CODE_KEY = "", MEDIA_FILES_KEY = "";
    public String TIMETABLE_KEY1 = "", ATTENDANCE_KEY1 = "", FILTER_BY_KEY = "";
    public String MY_REQUESTS_KEY = "", REQUEST_ID_KEY = "", SERVICE_REQUEST_STATUS_KEY = "", REQUEST_CATEGORY_KEY = "", REQUEST_TYPE_KEY = "", REQUEST_BY_KEY = "", REQUEST_ASSIGNED_TO_KEY ="", RAISE_REQUEST_KEY = "";
    public String MEDICAL_CONDITIONS_KEY = "", CONDITION_TYPE_KEY = "", CONSULTING_DOCTOR_KEY = "", CONTACT_NUMBER_KEY = "", SINCE_KEY = "",MEDICAL_CONDITION_KEY = "", PRECAUTION_MEDICATION_KEY = "";
    public String ACTION_TAKEN_BY_KEY = "", CLOSURE_REMARK_KEY = "",  ACTION_TYPE_KEY = "", APPROVAL_DETAILS_KEY = "", ASSIGNED_USER_KEY = "", CERTIFICATE_HANDOVER_DATE_KEY = "", CERTIFICATE_NAME_KEY = "", CLOSURE_REASON_KEY = "";
    public String EXECUTION_DETAILS_KEY = "", FOLLOW_UP_DETAILS_KEY = "",  MODE_OF_SUBMISSION_KEY = "", DOWNLOAD_CERTIFICATE_KEY = "", ENTERED_BY_KEY = "", REQUESTER_DETAILS_KEY = "", REQUESTER_NAME_KEY = "";
    public String DISCIPLINARY_ACTION_KEY = "", FEE_PAYER_DETAILS_KEY = "", TYPE_OF_INCIDENT_KEY = "", DATE_OF_INCIDENT_KEY = "", DATE_OF_ACTION_KEY = "", ACTION_TAKEN_KEY = "",DOCUMENT_UPLOADED_KEY = "";
    public String INCIDENT_DETAILS_KEY = "", REMARKS_KEY = "";

    public TranslationManager(Context context) {
        super(context);
        this.context = context;
        sharedPrefrenceManager = new SharedPrefrenceManager(context);

        translationData();
    }

    private void defaultKeys() {
        NOTIFICATION_KEY = context.getString(R.string.course_wise);
        NOTIFICATION_DETAILS_KEY = context.getString(R.string.notification_details);
        DASHBOARD_KEY = context.getString(R.string.dashboard);
        REFREASH_KEY = context.getString(R.string.refresh);
        OK_KEY = context.getString(R.string.ok);
        SHARE_APP_CONTENT_KEY = context.getString(R.string.share_app_content);
        COURSE_CODE_KEY = context.getString(R.string.course_code);
        USER_CODE_KEY = context.getString(R.string.user_code);
        FATHER_DETAILS_KEY = context.getString(R.string.father_detail);
        MOTHER_DETAILS_KEY = context.getString(R.string.mother_detail);
        LOCAL_GUARDIAN_DETAILS_KEY = context.getString(R.string.local_guardian_detail);
        GUARDIAN_DETAILS_KEY = context.getString(R.string.guardian_detail);

        //Dashboard
        ASSIGNMENT_KEY = context.getString(R.string.assignments);
        ATTENDANCE_KEY = context.getString(R.string.attendance);
        ATTENDANCE_KEY1 = context.getString(R.string.attendance1);
        FILTER_BY_KEY = context.getString(R.string.filter_by);
        MYCOURSE_KEY = context.getString(R.string.my_courses);
        TIMETABLE_KEY = context.getString(R.string.timetable);
        TIMETABLE_KEY1 = context.getString(R.string.timetable1);
        FEES_KEY = context.getString(R.string.fees);
        EXAMRESULT_KEY = context.getString(R.string.exam_result);
        CIRCULAR_KEY = context.getString(R.string.circular);
        EVENT_KEY = context.getString(R.string.events);
        ACADEMIA_DRIVE_KEY = context.getString(R.string.academia_drive);
        HOME_KEY = context.getString(R.string.home);
        LOGOUT_KEY = context.getString(R.string.logout);
        MYPROFILE_KEY = context.getString(R.string.my_profile);
        RESET_PASSWORD_KEY = context.getString(R.string.reset_password);
        SWITCH_STUDENT_KEY = context.getString(R.string.switch_student);
        RATE_REVIEW_KEY = context.getString(R.string.rate_n_review);
        FEEDBACK_KEY = context.getString(R.string.leave_a_feedback);
        DASHBOARD_TITLE_KEY = context.getString(R.string.app_name);

        //Attendance
        COURSE_WISE_KEY = context.getString(R.string.course_wise);
        PROGRAM_WISE_KEY = context.getString(R.string.program_wise);
        SESSION_WISE_KEY = context.getString(R.string.session_wise);
        PRESENT_KEY = context.getString(R.string.present);
        ABSENT_KEY = context.getString(R.string.absent);
        OTHER_KEY = context.getString(R.string.other);
        TOTAL_CLASSES_KEY = context.getString(R.string.total_classes);
        OVERALL_KEY = context.getString(R.string.overall);
        THISMONTH_KEY = context.getString(R.string.this_month);
        FACULTY_KEY = context.getString(R.string.faculty);
        ATTENDANCE_START_DATE_KEY = context.getString(R.string.start_date);
        ATTENDANCE_END_DATE_KEY = context.getString(R.string.end_date);
        ATTENDANCE_COURSE_CODE_KEY = context.getString(R.string.attendance_course_code_name);

        //Assignment
        HOMEWORKASSIGNMENT_KEY = context.getString(R.string.homework_assignment);
        COURSE_SESSIONDIARY_KEY = context.getString(R.string.session_diary);
        SESSION_NUMBER_KEY = context.getString(R.string.session_number);
        COURSE_VARIANT_KEY = context.getString(R.string.course_variant);
        SESSION_DATE_KEY = context.getString(R.string.session_date);
        TOPIC_KEY = context.getString(R.string.topics);
        ATTACHMENTS_KEY = context.getString(R.string.attachments);
        DUE_ON_KEY = context.getString(R.string.due_on);
        COURSE_STATUS_KEY = context.getString(R.string.status);
        ATTACHMENT_KEY = context.getString(R.string.attachment);
        PUBLISH_DATE_KEY = context.getString(R.string.published_date);

        //Circular
        CIRCULAR_DETAILS_KEY = context.getString(R.string.circular_details);

        //ResetPassword
        CONFIRM_PASSWORD_KEY = context.getString(R.string.confirm_password);
        NEW_PASSWORD_KEY = context.getString(R.string.new_password);
        OLD_PASSWORD_KEY = context.getString(R.string.old_password);

        //Event
        EVENT_DESCRIPTION_KEY = context.getString(R.string.event_description);
        EVENT_VENUE_KEY = context.getString(R.string.event_venue);
        EVENT_START_DATE_KEY = context.getString(R.string.start_date);
        EVENT_END_DATE_KEY = context.getString(R.string.end_date);
        EVENT_ADMIN_KEY = context.getString(R.string.event_admin);

        //MyProfile
        PROFILE_INFORMATION_KEY = context.getString(R.string.profile_information);
        PAST_HISTORY_KEY = context.getString(R.string.student_past_history);
        PERSONAL_DETAILS_KEY = context.getString(R.string.personal_details);
        PARENTS_DETAILS_KEY = context.getString(R.string.parents_details);
        DOCUMENTS_KEY = context.getString(R.string.documents);
        MEDIA_FILES_KEY = context.getString(R.string.media_file);
        STUDENT_NAME_KEY = context.getString(R.string.student_name);
        STUDENTID_KEY = context.getString(R.string.student_id);
        ADMISSIONID_KEY = context.getString(R.string.admission_id);
        EMAILID_KEY = context.getString(R.string.email_id);
        PHONE_NUMBER_KEY = context.getString(R.string.phone_number);
        MOBILE_NUMBER_KEY = context.getString(R.string.mobile_number);
        ADDRESS_KEY = context.getString(R.string.address);
        DATE_OF_BIRTH_KEY = context.getString(R.string.date_of_birth);
        DATE_OF_ADMISSION_KEY = context.getString(R.string.date_of_registration);
        GENDER_KEY = context.getString(R.string.gender);
        FATHER_NAME_KEY = context.getString(R.string.father_name);
        MOTHER_NAME_KEY = context.getString(R.string.mother_name);
        RELIGION_KEY = context.getString(R.string.religion);
        CASTE_KEY = context.getString(R.string.caste);
        BLOOD_GROUP_KEY = context.getString(R.string.blood_group);
        DOCUMENT_NAME_KEY = context.getString(R.string.document_name);
        SUBMISSION_DATE_KEY = context.getString(R.string.submission_date);
        INSPECTION_DATE_KEY = context.getString(R.string.inspection_date);
        STATUS_KEY = context.getString(R.string.status);
        PROGRAM_KEY = context.getString(R.string.program);
        SEAT_TYPE_KEY = context.getString(R.string.seat_type);
        REMARK_KEY = context.getString(R.string.remark);
        PREDEFINED_DOC_KEY = context.getString(R.string.predefined_documents);
        OTHERS_KEY = context.getString(R.string.others);
        CONDITION_TYPE_KEY = context.getString(R.string.condition_type);
        SINCE_KEY = context.getString(R.string.since);
        CONSULTING_DOCTOR_KEY = context.getString(R.string.consulting_doctor);
        CONTACT_NUMBER_KEY = context.getString(R.string.contact_number);
        MEDICAL_CONDITION_KEY = context.getString(R.string.medical_condition);
        MEDICAL_CONDITIONS_KEY = context.getString(R.string.medical_conditions);
        PRECAUTION_MEDICATION_KEY = context.getString(R.string.precuation_medication);
        DISCIPLINARY_ACTION_KEY = context.getString(R.string.disciplinary_actions);
        FEE_PAYER_DETAILS_KEY = context.getString(R.string.fee_payers);

        //Disciplinary Action
        TYPE_OF_INCIDENT_KEY = context.getString(R.string.type_of_incident);
        DATE_OF_INCIDENT_KEY = context.getString(R.string.date_of_incident);
        DATE_OF_ACTION_KEY = context.getString(R.string.date_of_action);
        ACTION_TAKEN_KEY = context.getString(R.string.action_taken);
        DOCUMENT_UPLOADED_KEY = context.getString(R.string.action_taken);
        INCIDENT_DETAILS_KEY = context.getString(R.string.incident_details);
        REMARKS_KEY = context.getString(R.string.remark);

        //Exam Result
        RESULT_REPORT_KEY = context.getString(R.string.result_report);
        EXAM_DOC_KEY = context.getString(R.string.exam_docs);
        MARKSHEET_KEY = context.getString(R.string.marksheet);
        HALLTICKET_KEY = context.getString(R.string.hall_ticket);
        BATCH_KEY = context.getString(R.string.batch);
        PERIOD_KEY = context.getString(R.string.period);
        SECTION_KEY = context.getString(R.string.section);
        OBTAINEDMARKS_KEY = context.getString(R.string.obtained);
        MAXMARKS_KEY = context.getString(R.string.max);
        WEIGHTAGE_KEY = context.getString(R.string.weightage);
        EFFECTIVEMARKS_KEY = context.getString(R.string.effective_marks_grades);
        MODERATIONMARKS_KEY = context.getString(R.string.moderation_marks);
        HALL_TICKETNO_KEY = context.getString(R.string.hall_ticket_number);
        ASSESSEMENT_GROUP_KEY = context.getString(R.string.assessement_group);
        TOTAL_OUTSTANDING_KEY = context.getString(R.string.total_outstanding);

        //Timetable
        COURSE_WISE_KEY1 = context.getString(R.string.course_wise1);
        DAY_WISE_KEY = context.getString(R.string.day_wise);
        COURSES_KEY = context.getString(R.string.courses);

        //Fees
        BILLED_KEY = context.getString(R.string.billed);
        RECEIPT_KEY = context.getString(R.string.receipt);
        RECEIPT_NUMBER_KEY = context.getString(R.string.receipt_number);
        RECEIPT_DATE_KEY = context.getString(R.string.receipt_date);
        DISCOUNT_AMOUNT_KEY = context.getString(R.string.discount_amount);
        AMOUNT_SETTLED_KEY = context.getString(R.string.amount_settled);
        TOTAL_AMOUNT_KEY = context.getString(R.string.total_amount);
        BALANCE_AMOUNT_KEY = context.getString(R.string.balance_amount);
        FULLY_PENDING_KEY = context.getString(R.string.fully_pending);
        PARTIALLY_PAID_KEY = context.getString(R.string.partially_paid);
        PAY_KEY = context.getString(R.string.pay);
        PAYNOW_KEY = context.getString(R.string.pay_now);
        ALL_KEY = context.getString(R.string.all);
        DUE_KEY = context.getString(R.string.due);
        PAID_KEY = context.getString(R.string.paid);
        CANCEL_KEY = context.getString(R.string.cancel);
        AMOUNTTOPAY_KEY = context.getString(R.string.amount_to_pay);
        BILL_DATE_KEY = context.getString(R.string.bill_date);
        DUE_DATE_KEY = context.getString(R.string.due_date);
        BILL_NUMBER_KEY = context.getString(R.string.bill_number);
        TOTAL_AMOUNT_KEY = context.getString(R.string.total_amount);
        PARTIALLY_SETTLED_KEY = context.getString(R.string.partially_settled);
        BILLNO_KEY = context.getString(R.string.bill_number);
        INVOICE_KEY = context.getString(R.string.invoice);

        //My Request
        MY_REQUESTS_KEY = context.getString(R.string.my_request);
        REQUEST_ID_KEY = context.getString(R.string.request_id);
        SERVICE_REQUEST_STATUS_KEY = context.getString(R.string.service_request_status);
        REQUEST_CATEGORY_KEY = context.getString(R.string.request_category);
        REQUEST_TYPE_KEY = context.getString(R.string.request_type);
        REQUEST_BY_KEY = context.getString(R.string.request_by);
        REQUEST_ASSIGNED_TO_KEY = context.getString(R.string.request_assigned_to);
        RAISE_REQUEST_KEY = context.getString(R.string.raise_request);
        ACTION_TAKEN_BY_KEY = context.getString(R.string.action_taken_by);
        CLOSURE_REMARK_KEY = context.getString(R.string.closure_remark);
        ACTION_TYPE_KEY = context.getString(R.string.action_type);
        APPROVAL_DETAILS_KEY = context.getString(R.string.approval_details);
        ASSIGNED_USER_KEY = context.getString(R.string.assigned_user);
        CERTIFICATE_HANDOVER_DATE_KEY = context.getString(R.string.certificate_handover_date);
        CERTIFICATE_NAME_KEY = context.getString(R.string.certificate_name);
        CLOSURE_REASON_KEY = context.getString(R.string.closure_reason);
        EXECUTION_DETAILS_KEY = context.getString(R.string.execution_details);
        FOLLOW_UP_DETAILS_KEY = context.getString(R.string.follow_up_details);
        MODE_OF_SUBMISSION_KEY = context.getString(R.string.mode_of_submission);
        DOWNLOAD_CERTIFICATE_KEY = context.getString(R.string.download_certificate);
        ENTERED_BY_KEY = context.getString(R.string.entered_by);
        REQUESTER_DETAILS_KEY = context.getString(R.string.requester_details);
        REQUESTER_NAME_KEY = context.getString(R.string.requester_name);

    }

    private void translationData() {

        defaultKeys();

        //Here get translation key and set on modules
        ArrayList<TranslationKeys> translationKeyList = sharedPrefrenceManager.getTranslationKeyList(Consts.TRANSLATION_KEYS);
        if(translationKeyList != null && translationKeyList.size() > 0){

            for(TranslationKeys translationKeys : translationKeyList){

                String keyName = translationKeys.getKeyName();

                if(keyName.equalsIgnoreCase("NOTIFICATIONS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        NOTIFICATION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        NOTIFICATION_KEY = defaultValue;
                    }else{
                        NOTIFICATION_KEY = context.getString(R.string.notifications);
                    }
                }

                if(keyName.equalsIgnoreCase("NOTIFICATION_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        NOTIFICATION_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        NOTIFICATION_DETAILS_KEY = defaultValue;
                    }else{
                        NOTIFICATION_DETAILS_KEY = context.getString(R.string.notification_details);
                    }
                }

                if(keyName.equalsIgnoreCase("DASHBOARD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DASHBOARD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DASHBOARD_KEY = defaultValue;
                    }else{
                        DASHBOARD_KEY = context.getString(R.string.dashboard);
                    }
                }

                if(keyName.equalsIgnoreCase("REFRESH")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REFREASH_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REFREASH_KEY = defaultValue;
                    }else{
                        REFREASH_KEY = context.getString(R.string.refresh);
                    }
                }

                if(keyName.equalsIgnoreCase("OK")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OK_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OK_KEY = defaultValue;
                    }else{
                        OK_KEY = context.getString(R.string.ok);
                    }
                }

                if(keyName.equalsIgnoreCase("SHARE_APP_CONTENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SHARE_APP_CONTENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SHARE_APP_CONTENT_KEY = defaultValue;
                    }else{
                        SHARE_APP_CONTENT_KEY = context.getString(R.string.share_app_content);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_CODE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_CODE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_CODE_KEY = defaultValue;
                    }else{
                        COURSE_CODE_KEY = context.getString(R.string.course_code);
                    }
                }

                if(keyName.equalsIgnoreCase("USER_CODE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        USER_CODE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        USER_CODE_KEY = defaultValue;
                    }else{
                        USER_CODE_KEY = context.getString(R.string.user_code);
                    }
                }

                if(keyName.equalsIgnoreCase("FATHER_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FATHER_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FATHER_DETAILS_KEY = defaultValue;
                    }else{
                        FATHER_DETAILS_KEY = context.getString(R.string.father_detail);
                    }
                }

                if(keyName.equalsIgnoreCase("MOTHER_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MOTHER_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MOTHER_DETAILS_KEY = defaultValue;
                    }else{
                        MOTHER_DETAILS_KEY = context.getString(R.string.mother_detail);
                    }
                }

                if(keyName.equalsIgnoreCase("LOCAL_GUARDIAN_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        LOCAL_GUARDIAN_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        LOCAL_GUARDIAN_DETAILS_KEY = defaultValue;
                    }else{
                        LOCAL_GUARDIAN_DETAILS_KEY = context.getString(R.string.local_guardian_detail);
                    }
                }

                if(keyName.equalsIgnoreCase("GUARDIAN_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        GUARDIAN_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        GUARDIAN_DETAILS_KEY = defaultValue;
                    }else{
                        GUARDIAN_DETAILS_KEY = context.getString(R.string.guardian_detail);
                    }
                }

                if(keyName.equalsIgnoreCase("ASSIGNMENTS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ASSIGNMENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ASSIGNMENT_KEY = defaultValue;
                    }else{
                        ASSIGNMENT_KEY = context.getString(R.string.assignments);
                    }
                }

                if(keyName.equalsIgnoreCase("MY_COURSES")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MYCOURSE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MYCOURSE_KEY = defaultValue;
                    }else{
                        MYCOURSE_KEY = context.getString(R.string.my_courses);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTENDANCE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTENDANCE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTENDANCE_KEY = defaultValue;
                    }else{
                        ATTENDANCE_KEY = context.getString(R.string.attendance);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTENDANCE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTENDANCE_KEY1 = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTENDANCE_KEY1 = defaultValue;
                    }else{
                        ATTENDANCE_KEY1 = context.getString(R.string.attendance1);
                    }
                }

                if(keyName.equalsIgnoreCase("FILTER_BY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FILTER_BY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FILTER_BY_KEY = defaultValue;
                    }else{
                        FILTER_BY_KEY = context.getString(R.string.filter_by);
                    }
                }

                if(keyName.equalsIgnoreCase("TIME_TABLE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TIMETABLE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TIMETABLE_KEY = defaultValue;
                    }else{
                        TIMETABLE_KEY = context.getString(R.string.timetable);
                    }
                }

                if(keyName.equalsIgnoreCase("TIME_TABLE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TIMETABLE_KEY1 = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TIMETABLE_KEY1 = defaultValue;
                    }else{
                        TIMETABLE_KEY1 = context.getString(R.string.timetable1);
                    }
                }

                if(keyName.equalsIgnoreCase("FEES")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FEES_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FEES_KEY = defaultValue;
                    }else{
                        FEES_KEY = context.getString(R.string.fees);
                    }
                }

                if(keyName.equalsIgnoreCase("EXAM_RESULT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EXAMRESULT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EXAMRESULT_KEY = defaultValue;
                    }else{
                        EXAMRESULT_KEY = context.getString(R.string.exam_result);
                    }
                }

                if(keyName.equalsIgnoreCase("CIRCULARS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CIRCULAR_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CIRCULAR_KEY = defaultValue;
                    }else{
                        CIRCULAR_KEY = context.getString(R.string.circular);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_KEY = defaultValue;
                    }else{
                        EVENT_KEY = context.getString(R.string.events);
                    }
                }

                if(keyName.equalsIgnoreCase("ACADEMIA_DRIVE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ACADEMIA_DRIVE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ACADEMIA_DRIVE_KEY = defaultValue;
                    }else{
                        ACADEMIA_DRIVE_KEY = context.getString(R.string.academia_drive);
                    }
                }

                if(keyName.equalsIgnoreCase("HOME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        HOME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        HOME_KEY = defaultValue;
                    }else{
                        HOME_KEY = context.getString(R.string.home);
                    }
                }

                if(keyName.equalsIgnoreCase("LOGOUT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        LOGOUT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        LOGOUT_KEY = defaultValue;
                    }else{
                        LOGOUT_KEY = context.getString(R.string.logout);
                    }
                }

                if(keyName.equalsIgnoreCase("MY_PROFILE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MYPROFILE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MYPROFILE_KEY = defaultValue;
                    }else{
                        MYPROFILE_KEY = context.getString(R.string.my_profile);
                    }
                }

                if(keyName.equalsIgnoreCase("RESET_PASSWORD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RESET_PASSWORD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RESET_PASSWORD_KEY = defaultValue;
                    }else{
                        RESET_PASSWORD_KEY = context.getString(R.string.reset_password);
                    }
                }

                if(keyName.equalsIgnoreCase("RATE_REVIEW_APP")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RATE_REVIEW_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RATE_REVIEW_KEY = defaultValue;
                    }else{
                        RATE_REVIEW_KEY = context.getString(R.string.rate_n_review);
                    }
                }

                if(keyName.equalsIgnoreCase("FEEDBACK")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FEEDBACK_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FEEDBACK_KEY = defaultValue;
                    }else{
                        FEEDBACK_KEY = context.getString(R.string.leave_a_feedback);
                    }
                }

                if(keyName.equalsIgnoreCase("SWITCH_STUDENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SWITCH_STUDENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SWITCH_STUDENT_KEY = defaultValue;
                    }else{
                        SWITCH_STUDENT_KEY = context.getString(R.string.switch_student);
                    }
                }

                if(keyName.equalsIgnoreCase("DASHBOARD_TITLE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DASHBOARD_TITLE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DASHBOARD_TITLE_KEY = defaultValue;
                    }else{
                        DASHBOARD_TITLE_KEY = context.getString(R.string.app_name);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_WISE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_WISE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_WISE_KEY = defaultValue;
                    }else{
                        COURSE_WISE_KEY = context.getString(R.string.course_wise);
                    }
                }

                if(keyName.equalsIgnoreCase("PROGRAM_WISE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PROGRAM_WISE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PROGRAM_WISE_KEY = defaultValue;
                    }else{
                        PROGRAM_WISE_KEY = context.getString(R.string.program_wise);
                    }
                }

                if(keyName.equalsIgnoreCase("SESSION_WISE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SESSION_WISE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SESSION_WISE_KEY = defaultValue;
                    }else{
                        SESSION_WISE_KEY = context.getString(R.string.session_wise);
                    }
                }

                if(keyName.equalsIgnoreCase("PRESENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PRESENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PRESENT_KEY = defaultValue;
                    }else{
                        PRESENT_KEY = context.getString(R.string.present);
                    }
                }

                if(keyName.equalsIgnoreCase("ABSENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ABSENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ABSENT_KEY = defaultValue;
                    }else{
                        ABSENT_KEY = context.getString(R.string.absent);
                    }
                }

                if(keyName.equalsIgnoreCase("OTHER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OTHER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OTHER_KEY = defaultValue;
                    }else{
                        OTHER_KEY = context.getString(R.string.other);
                    }
                }

                if(keyName.equalsIgnoreCase("CONDITION TYPE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CONDITION_TYPE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CONDITION_TYPE_KEY = defaultValue;
                    }else{
                        CONDITION_TYPE_KEY = context.getString(R.string.condition_type);
                    }
                }

                if(keyName.equalsIgnoreCase("SINCE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SINCE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SINCE_KEY = defaultValue;
                    }else{
                        SINCE_KEY = context.getString(R.string.since);
                    }
                }

                if(keyName.equalsIgnoreCase("CONSULTING DOCTOR")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CONSULTING_DOCTOR_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CONSULTING_DOCTOR_KEY = defaultValue;
                    }else{
                        CONSULTING_DOCTOR_KEY = context.getString(R.string.consulting_doctor);
                    }
                }

                if(keyName.equalsIgnoreCase("CONTACT NUMBER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CONTACT_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CONTACT_NUMBER_KEY = defaultValue;
                    }else{
                        CONTACT_NUMBER_KEY = context.getString(R.string.contact_number);
                    }
                }

                if(keyName.equalsIgnoreCase("MEDICAL CONDITION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MEDICAL_CONDITION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MEDICAL_CONDITION_KEY = defaultValue;
                    }else{
                        MEDICAL_CONDITION_KEY = context.getString(R.string.medical_condition);
                    }
                }

                if(keyName.equalsIgnoreCase("DISCIPLINARY_ACTIONS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DISCIPLINARY_ACTION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DISCIPLINARY_ACTION_KEY = defaultValue;
                    }else{
                        DISCIPLINARY_ACTION_KEY = context.getString(R.string.disciplinary_actions);
                    }
                }

                if(keyName.equalsIgnoreCase("FEE_PAYER_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FEE_PAYER_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FEE_PAYER_DETAILS_KEY = defaultValue;
                    }else{
                        FEE_PAYER_DETAILS_KEY = context.getString(R.string.fee_payers);
                    }
                }

                if(keyName.equalsIgnoreCase("TYPE_OF_INCIDENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TYPE_OF_INCIDENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TYPE_OF_INCIDENT_KEY = defaultValue;
                    }else{
                        TYPE_OF_INCIDENT_KEY = context.getString(R.string.type_of_incident);
                    }
                }

                if(keyName.equalsIgnoreCase("DATE_OF_INCIDENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DATE_OF_INCIDENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DATE_OF_INCIDENT_KEY = defaultValue;
                    }else{
                        DATE_OF_INCIDENT_KEY = context.getString(R.string.date_of_incident);
                    }
                }

                if(keyName.equalsIgnoreCase("DATE_OF_ACTION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DATE_OF_ACTION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DATE_OF_ACTION_KEY = defaultValue;
                    }else{
                        DATE_OF_ACTION_KEY = context.getString(R.string.date_of_action);
                    }
                }

                if(keyName.equalsIgnoreCase("ACTION_TAKEN")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ACTION_TAKEN_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ACTION_TAKEN_KEY = defaultValue;
                    }else{
                        ACTION_TAKEN_KEY = context.getString(R.string.action_taken);
                    }
                }

                if(keyName.equalsIgnoreCase("DOCUMENT_UPLOADED")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DOCUMENT_UPLOADED_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DOCUMENT_UPLOADED_KEY = defaultValue;
                    }else{
                        DOCUMENT_UPLOADED_KEY = context.getString(R.string.document_uploaded);
                    }
                }

                if(keyName.equalsIgnoreCase("INCIDENT_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        INCIDENT_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        INCIDENT_DETAILS_KEY = defaultValue;
                    }else{
                        INCIDENT_DETAILS_KEY = context.getString(R.string.incident_details);
                    }
                }

                if(keyName.equalsIgnoreCase("REMARKS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REMARKS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REMARKS_KEY = defaultValue;
                    }else{
                        REMARKS_KEY = context.getString(R.string.remark);
                    }
                }

                if(keyName.equalsIgnoreCase("MEDICAL CONDITIONS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MEDICAL_CONDITIONS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MEDICAL_CONDITIONS_KEY = defaultValue;
                    }else{
                        MEDICAL_CONDITIONS_KEY = context.getString(R.string.medical_conditions);
                    }
                }

                if(keyName.equalsIgnoreCase("PRECAUTION/MEDICATION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PRECAUTION_MEDICATION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PRECAUTION_MEDICATION_KEY = defaultValue;
                    }else{
                        PRECAUTION_MEDICATION_KEY = context.getString(R.string.precuation_medication);
                    }
                }

                if(keyName.equalsIgnoreCase("TOTAL_CLASSES")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TOTAL_CLASSES_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TOTAL_CLASSES_KEY = defaultValue;
                    }else{
                        TOTAL_CLASSES_KEY = context.getString(R.string.total_classes);
                    }
                }

                if(keyName.equalsIgnoreCase("OVERALL")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OVERALL_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OVERALL_KEY = defaultValue;
                    }else{
                        OVERALL_KEY = context.getString(R.string.overall);
                    }
                }

                if(keyName.equalsIgnoreCase("THIS_MONTH")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        THISMONTH_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        THISMONTH_KEY = defaultValue;
                    }else{
                        THISMONTH_KEY = context.getString(R.string.this_month);
                    }
                }

                if(keyName.equalsIgnoreCase("FACULTY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FACULTY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FACULTY_KEY = defaultValue;
                    }else{
                        FACULTY_KEY = context.getString(R.string.faculty);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTENDANCE_START_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTENDANCE_START_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTENDANCE_START_DATE_KEY = defaultValue;
                    }else{
                        ATTENDANCE_START_DATE_KEY = context.getString(R.string.start_date);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTENDANCE_END_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTENDANCE_END_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTENDANCE_END_DATE_KEY = defaultValue;
                    }else{
                        ATTENDANCE_END_DATE_KEY = context.getString(R.string.end_date);
                    }
                }

                if(keyName.equalsIgnoreCase("HOMEWORK_ASSIGNMENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        HOMEWORKASSIGNMENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        HOMEWORKASSIGNMENT_KEY = defaultValue;
                    }else{
                        HOMEWORKASSIGNMENT_KEY = context.getString(R.string.homework_assignment);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_CODE_NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTENDANCE_COURSE_CODE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTENDANCE_COURSE_CODE_KEY = defaultValue;
                    }else{
                        ATTENDANCE_COURSE_CODE_KEY = context.getString(R.string.course_code_name);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_SESSION_DAIRY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_SESSIONDIARY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_SESSIONDIARY_KEY = defaultValue;
                    }else{
                        COURSE_SESSIONDIARY_KEY = context.getString(R.string.session_diary);
                    }
                }

                if(keyName.equalsIgnoreCase("SESSION_NUMBER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SESSION_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SESSION_NUMBER_KEY = defaultValue;
                    }else{
                        SESSION_NUMBER_KEY = context.getString(R.string.session_number);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_VARIANT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_VARIANT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_VARIANT_KEY = defaultValue;
                    }else{
                        COURSE_VARIANT_KEY = context.getString(R.string.course_variant);
                    }
                }

                if(keyName.equalsIgnoreCase("SESSION_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SESSION_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SESSION_DATE_KEY = defaultValue;
                    }else{
                        SESSION_DATE_KEY = context.getString(R.string.session_date);
                    }
                }

                if(keyName.equalsIgnoreCase("TOPICS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TOPIC_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TOPIC_KEY = defaultValue;
                    }else{
                        TOPIC_KEY = context.getString(R.string.topics);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTACHMENTS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTACHMENTS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTACHMENTS_KEY = defaultValue;
                    }else{
                        ATTACHMENTS_KEY = context.getString(R.string.attachments);
                    }
                }

                if(keyName.equalsIgnoreCase("DUE_ON")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DUE_ON_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DUE_ON_KEY = defaultValue;
                    }else{
                        DUE_ON_KEY = context.getString(R.string.due_on);
                    }
                }

                if(keyName.equalsIgnoreCase("STATUS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_STATUS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_STATUS_KEY = defaultValue;
                    }else{
                        COURSE_STATUS_KEY = context.getString(R.string.status);
                    }
                }

                if(keyName.equalsIgnoreCase("ATTACHMENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ATTACHMENT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ATTACHMENT_KEY = defaultValue;
                    }else{
                        ATTACHMENT_KEY = context.getString(R.string.attachment);
                    }
                }

                if(keyName.equalsIgnoreCase("CIRCULAR_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CIRCULAR_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CIRCULAR_DETAILS_KEY = defaultValue;
                    }else{
                        CIRCULAR_DETAILS_KEY = context.getString(R.string.circular_details);
                    }
                }

                if(keyName.equalsIgnoreCase("CONFIRM_PASSWORD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CONFIRM_PASSWORD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CONFIRM_PASSWORD_KEY = defaultValue;
                    }else{
                        CONFIRM_PASSWORD_KEY = context.getString(R.string.confirm_password);
                    }
                }

                if(keyName.equalsIgnoreCase("NEW_PASSWORD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        NEW_PASSWORD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        NEW_PASSWORD_KEY = defaultValue;
                    }else{
                        NEW_PASSWORD_KEY = context.getString(R.string.new_password);
                    }
                }

                if(keyName.equalsIgnoreCase("OLD_PASSWORD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OLD_PASSWORD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OLD_PASSWORD_KEY = defaultValue;
                    }else{
                        OLD_PASSWORD_KEY = context.getString(R.string.old_password);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT_DESC")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_DESCRIPTION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_DESCRIPTION_KEY = defaultValue;
                    }else{
                        EVENT_DESCRIPTION_KEY = context.getString(R.string.event_description);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT_VENUE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_VENUE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_VENUE_KEY = defaultValue;
                    }else{
                        EVENT_VENUE_KEY = context.getString(R.string.event_venue);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT_START_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_START_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_START_DATE_KEY = defaultValue;
                    }else{
                        EVENT_START_DATE_KEY = context.getString(R.string.start_date);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT_END_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_END_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_END_DATE_KEY = defaultValue;
                    }else{
                        EVENT_END_DATE_KEY = context.getString(R.string.end_date);
                    }
                }

                if(keyName.equalsIgnoreCase("EVENT_ADMIN")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EVENT_ADMIN_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EVENT_ADMIN_KEY = defaultValue;
                    }else{
                        EVENT_ADMIN_KEY = context.getString(R.string.event_admin);
                    }
                }

                if(keyName.equalsIgnoreCase("PROFILE_INFORMATION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PROFILE_INFORMATION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PROFILE_INFORMATION_KEY = defaultValue;
                    }else{
                        PROFILE_INFORMATION_KEY = context.getString(R.string.profile_information);
                    }
                }

                if(keyName.equalsIgnoreCase("STUDENT_PAST_HISTORY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PAST_HISTORY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PAST_HISTORY_KEY = defaultValue;
                    }else{
                        PAST_HISTORY_KEY = context.getString(R.string.student_past_history);
                    }
                }

                if(keyName.equalsIgnoreCase("PERSONAL_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PERSONAL_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PERSONAL_DETAILS_KEY = defaultValue;
                    }else{
                        PERSONAL_DETAILS_KEY = context.getString(R.string.personal_details);
                    }
                }

                if(keyName.equalsIgnoreCase("PARENTS_DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PARENTS_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PARENTS_DETAILS_KEY = defaultValue;
                    }else{
                        PARENTS_DETAILS_KEY = context.getString(R.string.parents_details);
                    }
                }

                if(keyName.equalsIgnoreCase("DOCUMENTS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DOCUMENTS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DOCUMENTS_KEY = defaultValue;
                    }else{
                        DOCUMENTS_KEY = context.getString(R.string.documents);
                    }
                }

                if(keyName.equalsIgnoreCase("STUDENT_NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        STUDENT_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        STUDENT_NAME_KEY = defaultValue;
                    }else{
                        STUDENT_NAME_KEY = context.getString(R.string.student_name);
                    }
                }

                if(keyName.equalsIgnoreCase("STUDENT_ID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        STUDENTID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        STUDENTID_KEY = defaultValue;
                    }else{
                        STUDENTID_KEY = context.getString(R.string.student_id);
                    }
                }

                if(keyName.equalsIgnoreCase("ADMISSION_ID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ADMISSIONID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ADMISSIONID_KEY = defaultValue;
                    }else{
                        ADMISSIONID_KEY = context.getString(R.string.admission_id);
                    }
                }

                if(keyName.equalsIgnoreCase("EMAIL_ID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EMAILID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EMAILID_KEY = defaultValue;
                    }else{
                        EMAILID_KEY = context.getString(R.string.email_id);
                    }
                }

                if(keyName.equalsIgnoreCase("PHONE_NUMBER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PHONE_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PHONE_NUMBER_KEY = defaultValue;
                    }else{
                        PHONE_NUMBER_KEY = context.getString(R.string.phone_number);
                    }
                }

                if(keyName.equalsIgnoreCase("MOBILE_NUMBER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MOBILE_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MOBILE_NUMBER_KEY = defaultValue;
                    }else{
                        MOBILE_NUMBER_KEY = context.getString(R.string.mobile_number);
                    }
                }

                if(keyName.equalsIgnoreCase("ADDRESS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ADDRESS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ADDRESS_KEY = defaultValue;
                    }else{
                        ADDRESS_KEY = context.getString(R.string.address);
                    }
                }

                if(keyName.equalsIgnoreCase("DATE_OF_REGISTRATION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DATE_OF_ADMISSION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DATE_OF_ADMISSION_KEY = defaultValue;
                    }else{
                        DATE_OF_ADMISSION_KEY = context.getString(R.string.date_of_registration);
                    }
                }

                if(keyName.equalsIgnoreCase("DATE_OF_BIRTH")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DATE_OF_BIRTH_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DATE_OF_BIRTH_KEY = defaultValue;
                    }else{
                        DATE_OF_BIRTH_KEY = context.getString(R.string.date_of_birth);
                    }
                }

                if(keyName.equalsIgnoreCase("CATEGORY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CATEGORY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CATEGORY_KEY = defaultValue;
                    }else{
                        CATEGORY_KEY = context.getString(R.string.category);
                    }
                }

                if(keyName.equalsIgnoreCase("GENDER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        GENDER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        GENDER_KEY = defaultValue;
                    }else{
                        GENDER_KEY = context.getString(R.string.gender);
                    }
                }

                if(keyName.equalsIgnoreCase("FATHER_NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FATHER_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FATHER_NAME_KEY = defaultValue;
                    }else{
                        FATHER_NAME_KEY = context.getString(R.string.father_name);
                    }
                }

                if(keyName.equalsIgnoreCase("MOTHER_NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MOTHER_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MOTHER_NAME_KEY = defaultValue;
                    }else{
                        MOTHER_NAME_KEY = context.getString(R.string.mother_name);
                    }
                }

                if(keyName.equalsIgnoreCase("RELIGION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RELIGION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RELIGION_KEY = defaultValue;
                    }else{
                        RELIGION_KEY = context.getString(R.string.religion);
                    }
                }

                if(keyName.equalsIgnoreCase("CASTE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CASTE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CASTE_KEY = defaultValue;
                    }else{
                        CASTE_KEY = context.getString(R.string.caste);
                    }
                }

                if(keyName.equalsIgnoreCase("BLOOD_GROUP")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BLOOD_GROUP_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BLOOD_GROUP_KEY = defaultValue;
                    }else{
                        BLOOD_GROUP_KEY = context.getString(R.string.blood_group);
                    }
                }

                if(keyName.equalsIgnoreCase("DOCUMENT_NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DOCUMENT_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DOCUMENT_NAME_KEY = defaultValue;
                    }else{
                        DOCUMENT_NAME_KEY = context.getString(R.string.document_name);
                    }
                }

                if(keyName.equalsIgnoreCase("DOCUMENT_SUBMISSION_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SUBMISSION_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SUBMISSION_DATE_KEY = defaultValue;
                    }else{
                        SUBMISSION_DATE_KEY = context.getString(R.string.submission_date);
                    }
                }

                if(keyName.equalsIgnoreCase("STATUS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        STATUS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        STATUS_KEY = defaultValue;
                    }else{
                        STATUS_KEY = context.getString(R.string.status);
                    }
                }

                if(keyName.equalsIgnoreCase("PROGRAM")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PROGRAM_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PROGRAM_KEY = defaultValue;
                    }else{
                        PROGRAM_KEY = context.getString(R.string.program);
                    }
                }

                if(keyName.equalsIgnoreCase("SEAT_TYPE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SEAT_TYPE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SEAT_TYPE_KEY = defaultValue;
                    }else{
                        SEAT_TYPE_KEY = context.getString(R.string.seat_type);
                    }
                }

                if(keyName.equalsIgnoreCase("DOCUMENT_REMARKS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REMARK_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REMARK_KEY = defaultValue;
                    }else{
                        REMARK_KEY = context.getString(R.string.remark);
                    }
                }

                if(keyName.equalsIgnoreCase("PREDEFINED_DOCUMENTS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PREDEFINED_DOC_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PREDEFINED_DOC_KEY = defaultValue;
                    }else{
                        PREDEFINED_DOC_KEY = context.getString(R.string.predefined_documents);
                    }
                }

                if(keyName.equalsIgnoreCase("OTHER_DOCUMENT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OTHERS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OTHERS_KEY = defaultValue;
                    }else{
                        OTHERS_KEY = context.getString(R.string.others);
                    }
                }

                if(keyName.equalsIgnoreCase("RESULT_REPORT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RESULT_REPORT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RESULT_REPORT_KEY = defaultValue;
                    }else{
                        RESULT_REPORT_KEY = context.getString(R.string.result_report);
                    }
                }

                if(keyName.equalsIgnoreCase("EXAM_DOCS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EXAM_DOC_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EXAM_DOC_KEY = defaultValue;
                    }else{
                        EXAM_DOC_KEY = context.getString(R.string.exam_docs);
                    }
                }

                if(keyName.equalsIgnoreCase("MARKSHEET")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MARKSHEET_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MARKSHEET_KEY = defaultValue;
                    }else{
                        MARKSHEET_KEY = context.getString(R.string.marksheet);
                    }
                }

                if(keyName.equalsIgnoreCase("HALLTICKET")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        HALLTICKET_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        HALLTICKET_KEY = defaultValue;
                    }else{
                        HALLTICKET_KEY = context.getString(R.string.hall_ticket);
                    }
                }

                if(keyName.equalsIgnoreCase("BATCH")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BATCH_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BATCH_KEY = defaultValue;
                    }else{
                        BATCH_KEY = context.getString(R.string.batch);
                    }
                }

                if(keyName.equalsIgnoreCase("PERIOD")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PERIOD_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PERIOD_KEY = defaultValue;
                    }else{
                        PERIOD_KEY = context.getString(R.string.period);
                    }
                }

                if(keyName.equalsIgnoreCase("OBTAINED MARKS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        OBTAINEDMARKS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        OBTAINEDMARKS_KEY = defaultValue;
                    }else{
                        OBTAINEDMARKS_KEY = context.getString(R.string.obtained);
                    }
                }

                if(keyName.equalsIgnoreCase("MAX_MARKS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MAXMARKS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MAXMARKS_KEY = defaultValue;
                    }else{
                        MAXMARKS_KEY = context.getString(R.string.max);
                    }
                }

                if(keyName.equalsIgnoreCase("WEIGHTAGE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        WEIGHTAGE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        WEIGHTAGE_KEY = defaultValue;
                    }else{
                        WEIGHTAGE_KEY = context.getString(R.string.weightage);
                    }
                }

                if(keyName.equalsIgnoreCase("EFFECTIVE MARKS/GRADE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EFFECTIVEMARKS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EFFECTIVEMARKS_KEY = defaultValue;
                    }else{
                        EFFECTIVEMARKS_KEY = context.getString(R.string.effective_marks_grades);
                    }
                }

                if(keyName.equalsIgnoreCase("MODERATION MARKS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MODERATIONMARKS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MODERATIONMARKS_KEY = defaultValue;
                    }else{
                        MODERATIONMARKS_KEY = context.getString(R.string.moderation_marks);
                    }
                }

                if(keyName.equalsIgnoreCase("HALL_TICKET_NO")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        HALL_TICKETNO_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        HALL_TICKETNO_KEY = defaultValue;
                    }else{
                        HALL_TICKETNO_KEY = context.getString(R.string.hall_ticket_number);
                    }
                }

                if(keyName.equalsIgnoreCase("ASSESSMENT_GROUP")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ASSESSEMENT_GROUP_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ASSESSEMENT_GROUP_KEY = defaultValue;
                    }else{
                        ASSESSEMENT_GROUP_KEY = context.getString(R.string.assessement_group);
                    }
                }

                if(keyName.equalsIgnoreCase("TOTAL_OUTSTANDING")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TOTAL_OUTSTANDING_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TOTAL_OUTSTANDING_KEY = defaultValue;
                    }else{
                        TOTAL_OUTSTANDING_KEY = context.getString(R.string.total_outstanding);
                    }
                }

                if(keyName.equalsIgnoreCase("SECTION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SECTION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SECTION_KEY = defaultValue;
                    }else{
                        SECTION_KEY = context.getString(R.string.section);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSE_WISE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSE_WISE_KEY1 = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSE_WISE_KEY1 = defaultValue;
                    }else{
                        COURSE_WISE_KEY1 = context.getString(R.string.course_wise1);
                    }
                }

                if(keyName.equalsIgnoreCase("COURSES")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        COURSES_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        COURSES_KEY = defaultValue;
                    }else{
                        COURSES_KEY = context.getString(R.string.courses);
                    }
                }

                if(keyName.equalsIgnoreCase("DAY_WISE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DAY_WISE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DAY_WISE_KEY = defaultValue;
                    }else{
                        DAY_WISE_KEY = context.getString(R.string.day_wise);
                    }
                }

                if(keyName.equalsIgnoreCase("PUBLISH_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PUBLISH_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PUBLISH_DATE_KEY = defaultValue;
                    }else{
                        PUBLISH_DATE_KEY = context.getString(R.string.photos);
                    }
                }

                if(keyName.equalsIgnoreCase("BILLED")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BILLED_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BILLED_KEY = defaultValue;
                    }else{
                        BILLED_KEY = context.getString(R.string.billed);
                    }
                }

                if(keyName.equalsIgnoreCase("RECEIPT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RECEIPT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RECEIPT_KEY = defaultValue;
                    }else{
                        RECEIPT_KEY = context.getString(R.string.receipt);
                    }
                }

                if(keyName.equalsIgnoreCase("RECEIPT_NO")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RECEIPT_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RECEIPT_NUMBER_KEY = defaultValue;
                    }else{
                        RECEIPT_NUMBER_KEY = context.getString(R.string.receipt_number);
                    }
                }

                if(keyName.equalsIgnoreCase("RECEIPT_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RECEIPT_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RECEIPT_DATE_KEY = defaultValue;
                    }else{
                        RECEIPT_DATE_KEY = context.getString(R.string.receipt_date);
                    }
                }

                if(keyName.equalsIgnoreCase("DISCOUNT_AMOUNT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DISCOUNT_AMOUNT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DISCOUNT_AMOUNT_KEY = defaultValue;
                    }else{
                        DISCOUNT_AMOUNT_KEY = context.getString(R.string.discount_amount);
                    }
                }

                if(keyName.equalsIgnoreCase("AMOUNT_SETTLED")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        AMOUNT_SETTLED_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        AMOUNT_SETTLED_KEY = defaultValue;
                    }else{
                        AMOUNT_SETTLED_KEY = context.getString(R.string.amount_settled);
                    }
                }

                if(keyName.equalsIgnoreCase("TOTAL_AMOUNT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        TOTAL_AMOUNT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        TOTAL_AMOUNT_KEY = defaultValue;
                    }else{
                        TOTAL_AMOUNT_KEY = context.getString(R.string.total_amount);
                    }
                }

                if(keyName.equalsIgnoreCase("BALANCE_AMOUNT")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BALANCE_AMOUNT_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BALANCE_AMOUNT_KEY = defaultValue;
                    }else{
                        BALANCE_AMOUNT_KEY = context.getString(R.string.balance_amount);
                    }
                }

                if(keyName.equalsIgnoreCase("FULLY_PENDING")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FULLY_PENDING_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FULLY_PENDING_KEY = defaultValue;
                    }else{
                        FULLY_PENDING_KEY = context.getString(R.string.fully_pending);
                    }
                }

                if(keyName.equalsIgnoreCase("PARTIALLY_PAID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PARTIALLY_PAID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PARTIALLY_PAID_KEY = defaultValue;
                    }else{
                        PARTIALLY_PAID_KEY = context.getString(R.string.partially_paid);
                    }
                }

                if(keyName.equalsIgnoreCase("PAY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PAY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PAY_KEY = defaultValue;
                    }else{
                        PAY_KEY = context.getString(R.string.pay);
                    }
                }

                if(keyName.equalsIgnoreCase("PAY_NOW")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PAYNOW_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PAYNOW_KEY = defaultValue;
                    }else{
                        PAYNOW_KEY = context.getString(R.string.pay_now);
                    }
                }

                if(keyName.equalsIgnoreCase("ALL")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ALL_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ALL_KEY = defaultValue;
                    }else{
                        ALL_KEY = context.getString(R.string.all);
                    }
                }

                if(keyName.equalsIgnoreCase("DUE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DUE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DUE_KEY = defaultValue;
                    }else{
                        DUE_KEY = context.getString(R.string.due);
                    }
                }

                if(keyName.equalsIgnoreCase("PAID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PAID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PAID_KEY = defaultValue;
                    }else{
                        PAID_KEY = context.getString(R.string.paid);
                    }
                }

                if(keyName.equalsIgnoreCase("CANCEL")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CANCEL_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CANCEL_KEY = defaultValue;
                    }else{
                        CANCEL_KEY = context.getString(R.string.cancel);
                    }
                }

                if(keyName.equalsIgnoreCase("AMOUNT_TO_PAY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        AMOUNTTOPAY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        AMOUNTTOPAY_KEY = defaultValue;
                    }else{
                        AMOUNTTOPAY_KEY = context.getString(R.string.amount_to_pay);
                    }
                }

                if(keyName.equalsIgnoreCase("BILL_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BILL_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BILL_DATE_KEY = defaultValue;
                    }else{
                        BILL_DATE_KEY = context.getString(R.string.bill_date);
                    }
                }

                if(keyName.equalsIgnoreCase("DUE_DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DUE_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DUE_DATE_KEY = defaultValue;
                    }else{
                        DUE_DATE_KEY = context.getString(R.string.due_date);
                    }
                }

                if(keyName.equalsIgnoreCase("BILL_NO")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BILL_NUMBER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BILL_NUMBER_KEY = defaultValue;
                    }else{
                        BILL_NUMBER_KEY = context.getString(R.string.bill_number);
                    }
                }

                if(keyName.equalsIgnoreCase("PARTIALLY_SETTLED")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        PARTIALLY_SETTLED_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        PARTIALLY_SETTLED_KEY = defaultValue;
                    }else{
                        PARTIALLY_SETTLED_KEY = context.getString(R.string.partially_settled);
                    }
                }

                if(keyName.equalsIgnoreCase("BILL_NO")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        BILLNO_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        BILLNO_KEY = defaultValue;
                    }else{
                        BILLNO_KEY = context.getString(R.string.bill_number);
                    }
                }

                if(keyName.equalsIgnoreCase("INVOICE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        INVOICE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        INVOICE_KEY = defaultValue;
                    }else{
                        INVOICE_KEY = context.getString(R.string.invoice);
                    }
                }

                if(keyName.equalsIgnoreCase("MEDIA_FILES")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MEDIA_FILES_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MEDIA_FILES_KEY = defaultValue;
                    }else{
                        MEDIA_FILES_KEY = context.getString(R.string.media_file);
                    }
                }

                if(keyName.equalsIgnoreCase("MY_REQUESTS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MY_REQUESTS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MY_REQUESTS_KEY = defaultValue;
                    }else{
                        MY_REQUESTS_KEY = context.getString(R.string.my_request);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUEST_ID")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUEST_ID_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUEST_ID_KEY = defaultValue;
                    }else{
                        REQUEST_ID_KEY = context.getString(R.string.request_id);
                    }
                }

                if(keyName.equalsIgnoreCase("SERVICE_REQUEST_STATUS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        SERVICE_REQUEST_STATUS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        SERVICE_REQUEST_STATUS_KEY = defaultValue;
                    }else{
                        SERVICE_REQUEST_STATUS_KEY = context.getString(R.string.service_request_status);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUEST_CATEGORY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUEST_CATEGORY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUEST_CATEGORY_KEY = defaultValue;
                    }else{
                        REQUEST_CATEGORY_KEY = context.getString(R.string.request_category);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUEST_TYPE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUEST_TYPE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUEST_TYPE_KEY = defaultValue;
                    }else{
                        REQUEST_TYPE_KEY = context.getString(R.string.request_type);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUEST_BY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUEST_BY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUEST_BY_KEY = defaultValue;
                    }else{
                        REQUEST_BY_KEY = context.getString(R.string.request_by);
                    }
                }

                if(keyName.equalsIgnoreCase("RAISE_REQUEST")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        RAISE_REQUEST_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        RAISE_REQUEST_KEY = defaultValue;
                    }else{
                        RAISE_REQUEST_KEY = context.getString(R.string.raise_request);
                    }
                }

                if(keyName.equalsIgnoreCase("ACTION TAKEN BY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ACTION_TAKEN_BY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ACTION_TAKEN_BY_KEY = defaultValue;
                    }else{
                        ACTION_TAKEN_BY_KEY = context.getString(R.string.action_taken_by);
                    }
                }

                if(keyName.equalsIgnoreCase("CLOSURE REMARK")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CLOSURE_REMARK_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CLOSURE_REMARK_KEY = defaultValue;
                    }else{
                        CLOSURE_REMARK_KEY = context.getString(R.string.closure_remark);
                    }
                }

                if(keyName.equalsIgnoreCase("ACTION TYPE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ACTION_TYPE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ACTION_TYPE_KEY = defaultValue;
                    }else{
                        ACTION_TYPE_KEY = context.getString(R.string.action_type);
                    }
                }

                if(keyName.equalsIgnoreCase("APPROVAL DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        APPROVAL_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        APPROVAL_DETAILS_KEY = defaultValue;
                    }else{
                        APPROVAL_DETAILS_KEY = context.getString(R.string.approval_details);
                    }
                }

                if(keyName.equalsIgnoreCase("ASSIGNED USER")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ASSIGNED_USER_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ASSIGNED_USER_KEY = defaultValue;
                    }else{
                        ASSIGNED_USER_KEY = context.getString(R.string.assigned_user);
                    }
                }

                if(keyName.equalsIgnoreCase("CERTIFICATE HANDOVER DATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CERTIFICATE_HANDOVER_DATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CERTIFICATE_HANDOVER_DATE_KEY = defaultValue;
                    }else{
                        CERTIFICATE_HANDOVER_DATE_KEY = context.getString(R.string.certificate_handover_date);
                    }
                }

                if(keyName.equalsIgnoreCase("CERTIFICATE NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CERTIFICATE_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CERTIFICATE_NAME_KEY = defaultValue;
                    }else{
                        CERTIFICATE_NAME_KEY = context.getString(R.string.certificate_name);
                    }
                }

                if(keyName.equalsIgnoreCase("CLOSURE REASON")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        CLOSURE_REASON_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        CLOSURE_REASON_KEY = defaultValue;
                    }else{
                        CLOSURE_REASON_KEY = context.getString(R.string.closure_reason);
                    }
                }

                if(keyName.equalsIgnoreCase("EXECUTION DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        EXECUTION_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        EXECUTION_DETAILS_KEY = defaultValue;
                    }else{
                        EXECUTION_DETAILS_KEY = context.getString(R.string.execution_details);
                    }
                }

                if(keyName.equalsIgnoreCase("FOLLOW UP DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        FOLLOW_UP_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        FOLLOW_UP_DETAILS_KEY = defaultValue;
                    }else{
                        FOLLOW_UP_DETAILS_KEY = context.getString(R.string.follow_up_details);
                    }
                }

                if(keyName.equalsIgnoreCase("MODE OF SUBMISSION")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        MODE_OF_SUBMISSION_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        MODE_OF_SUBMISSION_KEY = defaultValue;
                    }else{
                        MODE_OF_SUBMISSION_KEY = context.getString(R.string.mode_of_submission);
                    }
                }

                if(keyName.equalsIgnoreCase("DOWNLOAD CERTIFICATE")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        DOWNLOAD_CERTIFICATE_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        DOWNLOAD_CERTIFICATE_KEY = defaultValue;
                    }else{
                        DOWNLOAD_CERTIFICATE_KEY = context.getString(R.string.download_certificate);
                    }
                }

                if(keyName.equalsIgnoreCase("ENTERED BY")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        ENTERED_BY_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        ENTERED_BY_KEY = defaultValue;
                    }else{
                        ENTERED_BY_KEY = context.getString(R.string.entered_by);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUESTER DETAILS")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUESTER_DETAILS_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUESTER_DETAILS_KEY = defaultValue;
                    }else{
                        REQUESTER_DETAILS_KEY = context.getString(R.string.requester_details);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUESTER NAME")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUESTER_NAME_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUESTER_NAME_KEY = defaultValue;
                    }else{
                        REQUESTER_NAME_KEY = context.getString(R.string.requester_name);
                    }
                }

                if(keyName.equalsIgnoreCase("REQUEST_ASSIGNED_TO")){

                    String defaultValue = ProjectUtils.getCorrectedString(translationKeys.getDefaultValue());
                    String customerDefineValue = ProjectUtils.getCorrectedString(translationKeys.getCustomerDefineValue());

                    if(!customerDefineValue.equalsIgnoreCase("")){
                        REQUEST_ASSIGNED_TO_KEY = customerDefineValue;
                    }else if(!defaultValue.equalsIgnoreCase("")){
                        REQUEST_ASSIGNED_TO_KEY = defaultValue;
                    }else{
                        REQUEST_ASSIGNED_TO_KEY = context.getString(R.string.request_assigned_to);
                    }
                }
            }
        }
    }
}

