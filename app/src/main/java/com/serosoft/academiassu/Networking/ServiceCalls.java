package com.serosoft.academiassu.Networking;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestFilter_Dto;
import com.serosoft.academiassu.Utils.AcademiaFileManager;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ammarali on 19/06/16.
 */
public class ServiceCalls {

    public JSONObject sendDataToServer(Context context, HashMap<String, String> myMap, String methodname) {
        JSONObject jsonObject = null;
        disableSSLCertificateChecking();

        switch (methodname) {

            /*Login*/
            case KEYS.SWITCH_USER_LOGIN:
                try {
                    disableSSLCertificateChecking();
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("username", myMap.get(KEYS.USERNAME))
                            .appendQueryParameter("password", myMap.get(KEYS.PASSWORD))
                            .appendQueryParameter("grant_type", myMap.get(KEYS.GRANT_TYPE))
                            .appendQueryParameter("client_id", myMap.get(KEYS.CLIENT_ID))
                            .appendQueryParameter("client_secret",  myMap.get(KEYS.CLIENT_SECRET));
                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithPost("POST", KEYS.TIME_OUT, KEYS.LOGIN_SERVICE_METHOD,
                            query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_GOOGLE_SIGNIN:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("username", myMap.get(KEYS.USERNAME))
                            .appendQueryParameter("password", myMap.get(KEYS.PASSWORD))
                            .appendQueryParameter("grant_type", myMap.get(KEYS.GRANT_TYPE))
                            .appendQueryParameter("client_id", myMap.get(KEYS.CLIENT_ID))
                            .appendQueryParameter("client_secret",  myMap.get(KEYS.CLIENT_SECRET))
                            .appendQueryParameter("portal_code",  myMap.get(KEYS.PORTAL_CODE))
                            .appendQueryParameter("googleAccessToken",  myMap.get(KEYS.GOOGLE_ACCESS_TOKEN));
                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithPost("POST", KEYS.TIME_OUT, KEYS.LOGIN_SERVICE_METHOD,
                            query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_GOOGLE_SIGNIN_SETUP:
                try {
                    String url = KEYS.GOOGLE_SIGNIN_METHOD
                            + "?socialId=" + myMap.get("socialId");

                    jsonObject = getResponseWithoutHeaderForUpdateWS("GET", KEYS.TIME_OUT, url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_LATEST_VERSION_NEW:
                try {
                    String url = KEYS.LATEST_VERSION_METHOD_NEW
                            + "?platform=" + myMap.get("platform");

                    jsonObject = getResponseWithoutHeaderForUpdateWS("GET", KEYS.TIME_OUT, url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_LATEST_VERSION_NEW2:
                try {
                    String url = KEYS.LATEST_VERSION_METHOD_NEW
                            + "?platform=" + myMap.get("platform");

                    jsonObject = getResponseWithoutHeaderForUpdateWS("GET", KEYS.TIME_OUT, url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FORGOT_PASSWORD:
                try {
                    disableSSLCertificateChecking();
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("code", myMap.get("code"));
                    String url = KEYS.FORGOT_PASSWORD_METHOD + "?code=" + myMap.get("code").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseForForgotPassword("POST", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEATURES:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String query = builder.build().getEncodedQuery();

                    String url = KEYS.FEATURES_SERVICE_METHOD
                            + "?isMobielApp="
                            + myMap.get("isMobielApp").toString();

                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url, query, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FIND_USERID_SOCIAL_LOGIN:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String query = builder.build().getEncodedQuery();

                    String url = KEYS.FIND_USERID_SOCIAL_LOGIN_METHOD
                            + "?socialLoginIntegrationId="
                            + myMap.get("socialLoginIntegrationId").toString()
                            + "&socialLoginUserId="
                            + myMap.get("socialLoginUserId").toString();

                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url, query, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_ACADEMIC_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ACADEMIC_DETAILS_SERVICE_METHOD + "?studentId=" + myMap.get("studentId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ASSOCIATED_STUDENTS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ASSOCIATED_STUDENTS_METHOD
                            + "?personId="
                            + myMap.get("personId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_PERSONAL_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_PERSONAL_DETAILS_SERVICE_METHOD + "/" + myMap.get("studentId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_PERSONAL_DETAILS2:
                try {
                    String url = KEYS.STUDENT_PERSONAL_DETAILS2_SERVICE_METHOD + "?page=" + myMap.get("page").toString() + "&limit=" + myMap.get("limit").toString() + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForPersonalDetails2Service(context), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_PROFILE_PICTURE:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.STUDENT_PROFILE_PICTURE_METHOD
                            + "?imagePath="
                            + myMap.get("imagePath").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_DETAILS_METHOD
                            + myMap.get("id").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_SALUTATIONS:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_SALUTATIONS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_MULTILANGUAGE:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_MULTILANGUAGE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_CONTACTLANGUAGE:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_MULTILANGUAGE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_DISABILITIES:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_FINDALL_METHOD
                            + "?type="
                            + myMap.get("type").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_CURRENT_EDUCATIONAL:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_FINDALL_METHOD
                            + "?type="
                            + myMap.get("type").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_GENDER:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_GENDER_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_NATIONALITY:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_NATIONALITY_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROFILE_DOMICILE:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PROFILE_DOMICILE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_TYPE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_TYPE_METHOD
                            + "?academyLocatonId="
                            + myMap.get("academyLocationId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MOBILE_TRANSLATION:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.MOBILE_TRANSLATION_METHOD;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,query,context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_DATE_TIME_FORMAT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.DATE_TIME_FORMAT_METHOD;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FCM_REGISTRATION:
                try {
                    String url = KEYS.FCM_REGISTERATION_METHOD;// + "?page=" + myMap.get("page").toString() + "&limit=" + myMap.get("limit").toString() + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForFCMRegistrationService(context), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Others*/
            case KEYS.SWITCH_MARK_NOTIFICATION_AS_READ:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("userId", myMap.get("userId"));
                    String url = KEYS.MARK_NOTIFICATION_AS_READ_METHOD
                            + "?messageId="
                            + myMap.get("messageId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("PUT", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Assignment*/
            case KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_COURSE_COVERAGE_PLAN_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&portalId="
                            + myMap.get("portalId").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&studentIds="
                            + myMap.get("studentIds").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COURSE_DIARY_DESCRIPTION:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_COURSE_SESSION_DIARY_DESCRIPTION_METHOD
                            + "?id="
                            + myMap.get("id").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COURSE_COVERAGE_PLAN_1_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_COURSE_COVERAGE_PLAN_METHOD
                            + "?whetherPBS="
                            + "false"
                            + "&portalId="
                            + myMap.get("portalId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&admissionIds="
                            + myMap.get("admissionId").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COURSE_COVERAGE_PLAN_2_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_COURSE_COVERAGE_PLAN_METHOD
                            + "?whetherPBS="
                            + "true"
                            + "&portalId="
                            + myMap.get("portalId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&admissionIds="
                            + myMap.get("admissionId").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS:
                try {

                    String url = KEYS.STUDENT_HOMEWORK_ASSIGNMENT_METHOD
                            + "?page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForHomeworkAssignmentsService(context), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.STUDENT_HW_ASSIGNMENT_DETAILS_BY_ASSIGNMENTID
                            + "?id=" + myMap.get("id").toString()
                            + "&studentId=" + myMap.get("studentId").toString();

                    String query = builder.build().getEncodedQuery();

                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_DOCUMENTS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_HOMEWORK_DOCUMENTS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&admissionId="
                            + myMap.get("admissionId").toString()
                            + "&courseHomeWorkAssignmentId="
                            + myMap.get("courseHomeWorkAssignmentId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_HOMEWORK_ASSIGNMENT_DESCRIPTION_METHOD
                            + "?id="
                            + myMap.get("id").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                try {
                    String url = KEYS.STUDENT_HOMEWORK_DOCUMENT_DOWNLOAD_METHOD
                            + myMap.get("id").toString();
                    String fileName = myMap.get("fileName");
                    String folderName = myMap.get("folderName");

                    jsonObject = downloadWithHeaderAndRawText2("POST", KEYS.TIME_OUT, url, context, fileName,folderName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.STUDENT_HW_SAVED_ASSIGNMENT_DOCUMENT
                            + "?id=" + myMap.get("id").toString()
                            + "&docType=" + myMap.get("docType").toString()
                            + "&userId=" + myMap.get("userId").toString()
                            + "&portalId=" + myMap.get("portalId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ASSIGNMENT_TYPE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ASSIGNMENT_TYPE_METHOD
                            + "?type="
                            + myMap.get("type").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Attendance*/
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.STUDENT_ATTENDANCE_SUMMARY_COMPLETE_DAY_METHOD
                            + "?onlyCurrentRecords="
                            + "false"
                            + "&includeAttendanceOfOnlyPlannedSession="
                            + "false"
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString()
                            + "&viewAttendanceType="
                            + myMap.get("viewAttendanceType").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.STUDENT_ATTENDANCE_SUMMARY_COURSE_LEVEL_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&courseId="
                            + myMap.get("courseId").toString()
                            + "&courseVariantId="
                            + myMap.get("courseVariantId").toString()
                            + "&attendancePercentageFrom="
                            + myMap.get("attendancePercentageFrom").toString()
                            + "&attendancePercentageTo="
                            + myMap.get("attendancePercentageTo").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&isCurrentPeriod="
                            + myMap.get("isCurrentPeriod").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.STUDENT_ATTENDANCE_SUMMARY_COMPLETE_DAY_METHOD
                            + "?onlyCurrentRecords="
                            + "false"
                            + "&includeAttendanceOfOnlyPlannedSession="
                            + "false"
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString()
                            + "&viewAttendanceType="
                            + myMap.get("viewAttendanceType").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_PROGRAMS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_PROGRAM_BATCH_PERIOD_METHOD
                            + "?academyLocationIds="
                            + myMap.get("academyLocationIds").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_BATCH:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_PROGRAM_BATCH_PERIOD_METHOD
                            + "?programId="
                            + myMap.get("programId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_PERIOD:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_PROGRAM_BATCH_PERIOD_METHOD
                            + "?batchId="
                            + myMap.get("batchId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_SECTION:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_PROGRAM_BATCH_PERIOD_METHOD
                            + "?periodId="
                            + myMap.get("periodId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COURSE_LIST:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_COURSE_LIST_METHOD
                            + "?programId="
                            + myMap.get("programId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&onlyCurrentRecords="
                            + myMap.get("onlyCurrentRecords").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COURSE_VARIANT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ATTENDANCE_COURSE_VARIANT_METHOD
                            + "?programId="
                            + myMap.get("programId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&courseId="
                            + myMap.get("courseId").toString()
                            + "&onlyCurrentRecords="
                            + myMap.get("onlyCurrentRecords").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COMPLETE_DAY_METHOD
                            + "?admissionId="
                            + myMap.get("admissionId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD
                            + "?admissionId="
                            + myMap.get("admissionId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&courseVariantId="
                            + myMap.get("courseVariantId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader2("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD
                            + "?admissionId="
                            + myMap.get("admissionId").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&courseId="
                            + myMap.get("courseId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader2("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_ATTENDANCE_DETAILS_COURSE_LEVEL_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&attendanceType="
                            + myMap.get("attendanceType").toString()
                            + "&sectionId="
                            + myMap.get("sectionId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader2("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Event*/
            case KEYS.SWITCH_STUDENT_EVENTS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_EVENTS_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String folderName = myMap.get("folderName");

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EVENT_ATTACHMENTS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.EVENT_ATTACHMENTS_METHOD
                            + "?resourceBookingId="
                            + myMap.get("resourceBookingId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EVENT_PICTURE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_EVENT_PICTURE_METHOD
                            + "?imagePath="
                            + myMap.get("imagePath").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                    jsonObject.put("index", myMap.get("index"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*TimeTable*/
            case KEYS.SWITCH_STUDENT_CALENDAR_COURSEWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_CALENDAR_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&courseId="
                            + myMap.get("courseId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_TIMETABLE_COURSES:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.TIMETABLE_COURSES_METHOD
                            + "?studentId=" + myMap.get("studentId");

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_HOLIDAYS_MONTHWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_HOLIDAYS_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&startDate="
                            + myMap.get("startDate").toString()
                            + "&endDate="
                            + myMap.get("endDate").toString()
                            + "&isweekDayOff="
                            + myMap.get("isweekDayOff").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_CALENDAR_MONTHWISE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_CALENDAR_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&end="
                            + myMap.get("end").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_CALENDAR_DURATION:
                try {
                    String url = KEYS.STUDENT_CALENDAR_DURATION_METHOD
                            + "?periodId="
                            + myMap.get("periodId").toString();

                    String query = "";
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    ProjectUtils.showLog("Exec",e.getMessage());
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_CALENDAR:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_CALENDAR_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Fees*/
            case KEYS.SWITCH_STUDENT_FEES:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_FEES_METHOD
                            + "?whetherDeleted="
                            + myMap.get("whetherDeleted").toString()
                            + "&billedUserType="
                            + myMap.get("billedUserType").toString()
                            + "&raiseBillCategory="
                            + myMap.get("raiseBillCategory").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&id="
                            + myMap.get("id").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_RECEIPTS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_RECEIPTS_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader2("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_RECEIPT_DOWNLOAD:
                try {

                    String receiptStatus = myMap.get("receiptStatus");
                    String billId = myMap.get("receiptId");
                    String viewOrDownload = myMap.get("viewOrDownload");

                    String url = KEYS.RECEIPT_DOWNLOAD_METHOD;

                    String payloadString = getJSONForReceiptDownloadService(context, billId, receiptStatus).toString();
                    payloadString = "exportDocumentData=" + payloadString;
                    SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
                    String fileName = sharedPrefrenceManager.getUserCodeFromKey() + "_" + billId + "_Receipt.pdf";

                    jsonObject = downloadWithHeaderAndEncodedFormData("POST", KEYS.TIME_OUT, url,
                            payloadString, context, fileName, viewOrDownload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_INVOICE:
                try {

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.STUDENT_INVOICE_METHOD
                            + "?page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString()
                            + "&id=" + myMap.get("id").toString()
                            + "&billedUserType=" + myMap.get("billedUserType").toString()
                            + "&raiseBillCategory=" + myMap.get("raiseBillCategory").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PAYMENT_GATEWAY:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.PAYMENT_GATEWAY_METHOD
                            + "?type=" + myMap.get("type").toString()
                            + "&academyLocationIds=" + myMap.get("academyLocationIds").toString()
                            + "&isActive=" + myMap.get("isActive").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PAYMENT_ID:
                try {
                    String url = KEYS.PAYMENT_ID_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForPaymentIDService(myMap.get("payableAmount"), myMap.get("payLoadString")), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PAYTM_GET_CHECKSUMHASH:
                try {

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.PAYTM_GET_CHECKSUMHASH_METHOD
                            + "?MID=" + myMap.get("MID").toString()
                            + "&ORDER_ID=" + myMap.get("ORDER_ID").toString()
                            + "&CUST_ID=" + myMap.get("CUST_ID").toString()
                            + "&INDUSTRY_TYPE_ID=" + myMap.get("INDUSTRY_TYPE_ID").toString()
                            + "&CHANNEL_ID=" + myMap.get("CHANNEL_ID").toString()
                            + "&TXN_AMOUNT=" + myMap.get("TXN_AMOUNT").toString()
                            + "&WEBSITE=" + myMap.get("WEBSITE").toString()
                            + "&CALLBACK_URL=" + myMap.get("CALLBACK_URL").toString()
                            + "&academyLocationIds=" + myMap.get("academyLocationIds").toString()
                            + "&env=" + myMap.get("env").toString()
                            + "&isActive=" + myMap.get("isActive").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeaderForPayTM("POST", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_TRANSACTION_ID_UPDATE:
                try {
                    String url = KEYS.TRANSACTION_ID_UPDATE_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForUpdateTransactionIDService(myMap.get("status"),myMap.get("id"),myMap.get("txnid")), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_TRANSACTION_UPDATE:
                try {

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.TRANSACTION_UPDATE_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForUpdateTransactionService(context, myMap.get("id").toString()), context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_BILL_HEADER:
                try {
                    String url = KEYS.STUDENT_HOMEWORK_ASSIGNMENT_METHOD
                            + "?page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForBillHeaders(context, myMap.get("billId")), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_INVOICE_DETAILS:
                try {
                    String url = KEYS.INVOICE_DETAILS_METHOD
                            + "?billId=" + myMap.get("billId").toString()
                            + "&portalId=" + myMap.get("portalId").toString()
                            + "&page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url,context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PAYMENT_ID_FEE_HEAD:
                try {
                    String url = KEYS.PAYMENT_ID_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForPaymentIDFeeHeadService(myMap.get("payableAmount"), myMap.get("payLoadString")), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PENDING_BILLS:
                try {

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));

                    String url = KEYS.STUDENT_PENDING_BILLS_METHOD
                            + "?page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString()
                            + "&id=" + myMap.get("id").toString()
                            + "&billedUserType=" + myMap.get("billedUserType").toString()
                            + "&isRequestFromMobile=" + myMap.get("isRequestFromMobile").toString()
                            + "&raiseBillCategory=" + myMap.get("raiseBillCategory").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PENDING_FEE_HEADS:
                try {

                    String url = KEYS.STUDENT_PENDING_FEE_HEADS_METHOD
                            + "?id=" + myMap.get("id").toString()
                            + "&billedUserType=" + myMap.get("billedUserType").toString()
                            + "&showAllSettlement=" + myMap.get("showAllSettlement").toString()
                            + "&adjustmentType=" + myMap.get("adjustmentType").toString()
                            + "&isRequestFromMobile=" + myMap.get("isRequestFromMobile").toString()
                            + "&page=" + myMap.get("page").toString()
                            + "&limit=" + myMap.get("limit").toString()
                            + "&start=" + myMap.get("start").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url,context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_INVOICE_DOWNLOAD:
                try {
                    String url = KEYS.INVOICE_DOWNLOAD_METHOD;
                    String billId = myMap.get("billId");
                    String viewOrDownload = myMap.get("viewOrDownload");
                    String payloadString = getJSONForInvoiceDownloadService(context, billId).toString();
                    payloadString = "exportDocumentData=" + payloadString;
                    SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
                    String fileName = sharedPrefrenceManager.getUserCodeFromKey() + "_" + billId + "_Invoice.pdf";

                    jsonObject = downloadWithHeaderAndEncodedFormData("POST", KEYS.TIME_OUT, url,
                            payloadString, context, fileName, viewOrDownload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Circular*/
            case KEYS.SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD:
                try {
                    String url = KEYS.STUDENT_NOTIFICATION_DOCUMENT_DOWNLOAD_METHOD
                            + myMap.get("id").toString();
                    String fileName = myMap.get("fileName");
                    String folderName = myMap.get("folderName");

                    jsonObject = downloadWithHeaderAndRawText2("POST", KEYS.TIME_OUT, url, context, fileName,folderName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Reset Password*/
            case KEYS.SWITCH_RESET_PASSWORD:
                try {

                    String url = KEYS.RESET_PASSWORD_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON("POST", KEYS.TIME_OUT, url,
                            getJSONForResetPasswordService(context, myMap.get("currentPassword").toString(), myMap.get("newPassword").toString()), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PASSWORD_POLICY:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.PASSWORD_POLICY_METHOD;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Dashboard*/
            case KEYS.SWITCH_FEATURE_PRIVILEGES:
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String url = KEYS.FEATURE_PRIVILEGES_METHOD
                            + "?portalId=" + myMap.get("portalId").toString()
                            + "&appPortal=" + myMap.get("appPortal").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ACADEMY_IMAGE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ACADEMY_IMAGE_METHOD
                            + "?academyLocationId="
                            + myMap.get("academyLocationId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_LATEST_VERSION:
                try {
                    String url = KEYS.LATEST_VERSION_METHOD
                            + "?platform=" + myMap.get("platform");

                    jsonObject = getResponseWithHeaderForUpdateWS("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ALL_CURRENCY:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ALL_CURRENY_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_LOGOUT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.LOGOUT_METHOD;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_CIRCULARS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("userId", myMap.get("userId"));
                    String url = KEYS.CIRCULARS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&userId="
                            + myMap.get("userId").toString()
                            + "&messageType="
                            + myMap.get("messageType").toString()
                            + "&whetherAdhoc="
                            + myMap.get("whetherAdhoc").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ASSOCIATED_SIBLINGS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ASSOCIATED_SIBLINGS_METHOD
                            + "?parentPersonId="
                            + myMap.get("parentPersonId").toString()
                            + "&parentUserId="
                            + myMap.get("parentUserId").toString()
                            + "&studentPersonId="
                            + myMap.get("studentPersonId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_NOTIFICATIONS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("userId", myMap.get("userId"));
                    String url = KEYS.NOTIFICATIONS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString()
                            + "&userId="
                            + myMap.get("userId").toString()
                            + "&messageType="
                            + myMap.get("messageType").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("userId", myMap.get("userId"));
                    String url = KEYS.NOTIFICATIONS_COUNT_METHOD + "?userId=" + myMap.get("userId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FCM_LOGOUT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.FCM_LOGOUT_METHOD
                            + "?id="
                            + myMap.get("id").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEEDBACK_EMAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.FEEDBACK_EMAILS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*UserProfile*/
            case KEYS.SWITCH_PARENT_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.PARENT_DETAILS_METHOD
                            + "?personId="
                            + myMap.get("personId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_DOCUMENT_LIST:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.DOCUMENT_LIST_METHOD
                            + "?admissionId="
                            + myMap.get("admissionId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_DOCUMENT_TYPE:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.DOCUMENT_TYPE_METHOD;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MEDICAL_CONDITION_LIST:
                try {

                    String url = KEYS.MEDICAL_CONDITION_LIST_METHOD
                            + "?personId="
                            + myMap.get("personId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MEDICAL_CONDITION_TYPES:
                try {

                    String url = KEYS.MEDICAL_CONDITION_TYPES_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE:
                try {

                    String url = KEYS.MEDICAL_CONDITION_COUNTRY_CODE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MEDICAL_CONDITION_CREATE:
                try {

                    String url = KEYS.MEDICAL_CONDITION_CREATE_METHOD;

                    jsonObject = getResponseWithHeaderAndJSON2("POST", KEYS.TIME_OUT, url,
                            getJSONForCreateMedicalConditionService(myMap),context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_DISCIPLINARY_ACTION_LIST:
                try {

                    String url = KEYS.DISCIPLINARY_ACTION_LIST_METHOD
                            + "?personId="
                            + myMap.get("personId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEE_PAYER_LIST:
                try {

                    String url = KEYS.FEE_PAYER_LIST_METHOD
                            + "?personId="
                            + myMap.get("personId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEE_PAYER_CURRENT_ADDRESS:
                try {

                    String url = KEYS.STUDENT_CURRENT_ADDRESS_METHOD
                            + "?personId="
                            + myMap.get("personId").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_COUNTRY_REGION_CODE:
                try {

                    String url = KEYS.COUNTRY_REGION_METHOD
                            + "?cid="
                            + myMap.get("cid").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEE_PAYER_CITY:
                try {

                    String url = KEYS.FEE_PAYER_CITY_METHOD
                            + "?countryId="
                            + myMap.get("countryId").toString()
                            + "&countryRegionId="
                            + myMap.get("countryRegionId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_DEBIT_ORDER_DATE:
                try {

                    String url = KEYS.FEE_PAYER_DEBIT_ORDER_DATE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEE_BANK_LIST:
                try {

                    String url = KEYS.FEE_PAYER_BANKDRAW_ONMASTER_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FEE_BANK_BRANCH_CODE:
                try {

                    String url = KEYS.FEE_PAYER_BANK_BRANCH_CODE_METHOD
                            + "?bankId="
                            + myMap.get("bankId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REMOVE_PROFILE:
                try {

                    String url = KEYS.REMOVE_PROFILE_METHOD
                            + "?personId="
                            + myMap.get("personId").toString();

                    jsonObject = getResponseWithPostMethod("POST", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Exam*/
            case KEYS.SWITCH_RESULT_REPORT:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.RESULT_REPORT_METHOD
                            + "?admissionId="
                            + myMap.get("admissionId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&sort="
                            + myMap.get("sort").toString()
                            + "&node="
                            + myMap.get("node").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_MARKSHEETS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_MARKSHEETS_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader2("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_HALLTICKET:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.STUDENT_HALLTICKET_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&reportEnum="
                            + myMap.get("reportEnum").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD:
                try {
                    String url = KEYS.STUDENT_MARKSHEETS_DOWNLOAD_METHOD;
                    String path = myMap.get("marksheetPath");
                    String folderName = myMap.get("folderName");
                    byte[] bytesEncoded = Base64.encode(path.getBytes(), 0);
                    String fileName = FilenameUtils.getName(path);

                    System.out.println("encoded value is " + new String(bytesEncoded));
                    String marksheetPath = "filePath=" + new String(bytesEncoded);
                    jsonObject = downloadWithHeaderAndRawText("POST", KEYS.TIME_OUT, url,
                            marksheetPath, context, fileName,folderName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PROGRAMS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.PROGRAMS_METHOD
                            + "?academyLocationIds="
                            + myMap.get("academyLocationIds").toString()
                            + "&programIds="
                            + myMap.get("programIds").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_BATCH:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.BATCH_PERIOD_METHOD
                            + "?filterType="
                            + myMap.get("filterType").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_PERIOD:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.BATCH_PERIOD_METHOD
                            + "?filterType="
                            + myMap.get("filterType").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&studentId="
                            + myMap.get("studentId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*My Courses*/
            case KEYS.SWITCH_MYCOURSE_PROGRAM:
                try {

                    String url = KEYS.PROGRAM_BATCH_PERIOD_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&onlyCurrentRecord="
                            + myMap.get("onlyCurrentRecord").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MYCOURSE_BATCH:
                try {

                    String url = KEYS.PROGRAM_BATCH_PERIOD_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&onlyCurrentRecord="
                            + myMap.get("onlyCurrentRecord").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MYCOURSE_PERIOD:
                try {

                    String url = KEYS.PROGRAM_BATCH_PERIOD_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&onlyCurrentRecord="
                            + myMap.get("onlyCurrentRecord").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MY_COURSES_LIST:
                try {

                    String url = KEYS.MY_COURSES_METHOD
                            + "?studentId="
                            + myMap.get("studentId").toString()
                            + "&programId="
                            + myMap.get("programId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&periodId="
                            + myMap.get("periodId").toString()
                            + "&wheatherSchool="
                            + myMap.get("wheatherSchool").toString()
                            + "&portalId="
                            + myMap.get("portalId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*Academia Drive*/
            case KEYS.SWITCH_ACADEMIA_DRIVE_FOLDER:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ACADEMIA_DRIVE_FOLDER_METHOD
                            + "?academyLocationIds="
                            + myMap.get("academyLocationIds").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ALBUM_DETAILS:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ALBUM_DETAILS_METHOD
                            + "?galleryId="
                            + myMap.get("galleryId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();;

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_ULTIMATE_GALLERY:
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("studentId", myMap.get("studentId"));
                    String url = KEYS.ULTIMATE_GALLERY_METHOD
                            + "?academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&batchId="
                            + myMap.get("batchId").toString()
                            + "&isFacultyPortal="
                            + myMap.get("isFacultyPortal").toString()
                            + "&isStudentPortal="
                            + myMap.get("isStudentPortal").toString();

                    String query = builder.build().getEncodedQuery();
                    jsonObject = getResponseWithHeader("GET", KEYS.TIME_OUT, url,
                            query, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            /*My Request*/
            case KEYS.SWITCH_RAISE_REQUEST_TYPE:
                try {
                    String url = KEYS.MY_REQUEST_RAISE_REQUEST_TYPE_METHOD
                            + "?serviceRequestGroup="
                            + myMap.get("serviceRequestGroup").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&portalId="
                            + myMap.get("portalId").toString()
                            + "&serviceRequestCategory="
                            + myMap.get("serviceRequestCategory").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url,context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REQUEST_STATUS:
                try {

                    String url = KEYS.MY_REQUEST_REQUEST_STATUS_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REQUEST_TYPE:
                try {

                    String url = KEYS.MY_REQUEST_REQUEST_TYPE_METHOD
                            + "?serviceRequestGroup="
                            + myMap.get("serviceRequestGroup").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&serviceRequestCategory="
                            + myMap.get("serviceRequestCategory").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REQUESTER_DETAILS:
                try {

                    String url = KEYS.MY_REQUEST_REQUESTER_DETAILS_METHOD
                            + "?id=" + myMap.get("id");

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REQUESTER_HOSTEL_BASIC_DETAILS:
                try {

                    String url = KEYS.MY_REQUEST_REQUESTER_HOSTEL_BASIC_DETAILS_METHOD
                            + "?requestId=" + myMap.get("requestId");

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_REQUESTER_BASIC_DETAILS:
                try {

                    String url = KEYS.MY_REQUEST_REQUESTER_BASIC_DETAILS_METHOD
                            + "?requestId=" + myMap.get("requestId");

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_MANDETORY_DOCUMENTS:
                try {

                    String url = KEYS.MY_REQUEST_MANDATORY_DOCUMENT_METHOD
                            + "?serviceRequestId="
                            + myMap.get("serviceRequestId")
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_FOLLOWUP_DETAILS:
                try {

                    String url = KEYS.MY_REQUEST_FOLLOWUP_DETAILS_METHOD
                            + "?serviceRequestId="
                            + myMap.get("serviceRequestId")
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_APPROVAL_DETAILS:
                try {

                    String url = KEYS.MY_REQUEST_APPROVAL_DETAILS_METHOD
                            + "?serviceRequestId="
                            + myMap.get("serviceRequestId")
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_WITHDRAW_REQUEST:
                try {
                    String url = KEYS.MY_REQUEST_WITHDRAWN_REQUEST_METHOD;

                    String serviceRequestid = "serviceRequestId=" + myMap.get("serviceRequestId");

                    jsonObject = getResponseWithPostMethodRawText("POST", KEYS.TIME_OUT, url, serviceRequestid, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EXECUTION_HANDOVER_MODE:
                try {

                    String url = KEYS.MY_REQUEST_EXECUTION_HANDOVER_MODE_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EXECUTION_CLOSURE_REASON:
                try {

                    String url = KEYS.MY_REQUEST_EXECUTION_CLOSURE_REASON_METHOD
                            + "?page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EXECUTION_PRINT_CERTIFICATE:
                try {

                    String url = KEYS.MY_REQUEST_EXECUTION_PRINT_CERTIFICATE_METHOD
                            + "?certificateCategory="
                            + myMap.get("certificateCategory").toString()
                            + "&academyLocationId="
                            + myMap.get("academyLocationId").toString()
                            + "&page="
                            + myMap.get("page").toString()
                            + "&start="
                            + myMap.get("start").toString()
                            + "&limit="
                            + myMap.get("limit").toString();

                    jsonObject = getResponseWithGetMethod("GET", KEYS.TIME_OUT, url, context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;

            case KEYS.SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE:
                try {
                    String url = KEYS.MY_REQUEST_EXECUTION_DOWNLOAD_CERTIFICATE_METHOD;

                    String serviceRequestid = "serviceRequestId="
                            + myMap.get("serviceRequestId")
                            + "&certificateId="
                            + myMap.get("certificateId").toString();

                    String fileName = myMap.get("certificateId").toString() + "_Certificate.pdf";

                    jsonObject = getResponseWithPostMethodRawText2("POST", KEYS.TIME_OUT, url, serviceRequestid, context, fileName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;
        }
        return jsonObject;
    }

    public JSONObject getResponseWithPost(String postMethod, int timeout, String url, String query) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;
        try {
            URL urlObject = new URL(BaseURL.BASE_URL + url);
            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod(postMethod);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(String.valueOf(query));
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 400:
                    JSONObject obj = new JSONObject();
                    obj.put("status", "Wrong Credentials");
                    jsonObject = obj;
                    return jsonObject;
                case 201:
                case 200:
                case 204:
                case 401:
                case 403:
                case 404:
                case 500:
                case 503:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        jsonObject = new JSONObject(sb.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public JSONObject getResponseWithGetMethod(String getMethod, int timeout, String url, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(getMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:
                case 204:
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithPostMethod(String postMethod, int timeout, String url, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:
                case 204:
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();

                        JSONObject obj = new JSONObject();
                        obj.put("response", str2);
                        jsonObject = obj;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithPostMethodRawText(String getMethod, int timeout, String url, String rawText, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(getMethod);

            OutputStream os = connection.getOutputStream();
            os.write(rawText.getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:
                case 204:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("message", "Success");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeader(String postMethod, int timeout, String url, String query, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            if(!accessToken.equalsIgnoreCase("")){
                connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestProperty("Authorization", str);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setDoInput(true);
                connection.setUseCaches(true);
                connection.setRequestMethod(postMethod);

                connection.connect();
            }

            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:
                case 204:
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseForForgotPassword(String postMethod, int timeout, String url, String query, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.contains("true"))
                        {
                            JSONObject obj = new JSONObject();
                            obj.put("message", "true");
                            jsonObject = obj;
                            return jsonObject;
                        }
                        else if (str2.startsWith("["))
                        {
                            JSONObject obj = new JSONObject();
                            obj.put("message", "false");
                            jsonObject = obj;
                            return jsonObject;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("message", "error");
                            jsonObject = obj;
                            return jsonObject;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            try {
                JSONObject obj = new JSONObject();
                obj.put("message", "false");
                jsonObject = obj;
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeaderForPayTM(String postMethod, int timeout, String url, String query, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:
                case 204:
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        if(line.contains("{") || line.contains("[")){
                            sb.append(line + "\n");
                        }
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithoutHeaderForUpdateWS(String getMethod, int timeout, String url) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(getMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeaderForUpdateWS(String getMethod, int timeout, String url, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Accept-Language", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(getMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeader2(String postMethod, int timeout, String url, String query, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeaderAndJSON(String postMethod, int timeout, String url, JSONObject requestJSONObject, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            OutputStream os = connection.getOutputStream();
            os.write(requestJSONObject.toString().getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webservice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("true"))
                        {
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", str2);
                            jsonObject = obj;
                        }
                        else if (Character.isDigit(str2.charAt(0)) == true) {
                            str2 = str2.replace("\n","");
                            JSONObject obj = new JSONObject();
                            obj.put("firebaseID", Long.valueOf(str2));
                            jsonObject = obj;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject getResponseWithHeaderAndJSON2(String postMethod, int timeout, String url, JSONObject requestJSONObject, Context context) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            OutputStream os = connection.getOutputStream();
            os.write(requestJSONObject.toString().getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("message", "Success!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webservice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("true"))
                        {
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", str2);
                            jsonObject = obj;
                        }
                        else if (Character.isDigit(str2.charAt(0)) == true) {
                            str2 = str2.replace("\n","");
                            JSONObject obj = new JSONObject();
                            obj.put("firebaseID", Long.valueOf(str2));
                            jsonObject = obj;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject downloadWithHeaderAndEncodedFormData(String postMethod, int timeout, String url, String rawText, Context context, String fileName, String viewOrDownload) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;
        String extension = FilenameUtils.getExtension(fileName).toUpperCase();
        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            OutputStream os = connection.getOutputStream();
            os.write(rawText.getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                    if (viewOrDownload.equals(KEYS.TRUE))
                    {
                        //Use Encryption
                        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia");
                        directory.mkdirs();
                        File directory2 = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia" + File.separator + "DB");
                        directory2.mkdirs();

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
                        String updatedFileName = "Blob" + ".dat";
                        String updatedPath = path + "/Academia/DB/" + updatedFileName;

                        InputStream inputStream = connection.getInputStream();
                        AcademiaFileManager.encryptFromStream(inputStream, updatedPath);

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message", "Document downloaded!");
                        obj2.put("downloaded", true);
                        obj2.put("path", updatedPath);
                        jsonObject = obj2;
                        break;
                    }
                    else
                    {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
                        File newFile = new File(path + fileName);

                        FileOutputStream fileOutput = new FileOutputStream(newFile);
                        InputStream inputStream = connection.getInputStream();

                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;

                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutput.write(buffer, 0, bufferLength);
                        }
                        fileOutput.close();

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message", "Document downloaded to Internal" + newFile);
                        obj2.put("downloaded", true);
                        obj2.put("path", path + "/" + fileName);
                        jsonObject = obj2;
                        break;
                    }
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    JSONObject obj = new JSONObject();
                    obj.put("message", "File not found on Server!");
                    obj.put("downloaded", false);
                    jsonObject = obj;
                    break;
            }
            return jsonObject;

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public JSONObject getResponseWithPostMethodRawText2(String postMethod, int timeout, String url, String rawText, Context context, String fileName) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod(postMethod);

            OutputStream os = connection.getOutputStream();
            os.write(rawText.getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 401:
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("error_message", "Session expired! Please login again!");
                        jsonObject = obj;
                        return jsonObject;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 201:
                case 200:

                    //Use Encryption
                    File directory2 = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia" + File.separator + "DB");
                    directory2.mkdirs();

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
                    String updatedFileName = "Blob" + ".dat";
                    String updatedPath = path + "/Academia/DB/" + updatedFileName;

                    InputStream inputStream = connection.getInputStream();
                    AcademiaFileManager.encryptFromStream(inputStream, updatedPath);

                    JSONObject obj2 = new JSONObject();
                    obj2.put("message", "Document downloaded!");
                    obj2.put("downloaded", true);
                    obj2.put("path", updatedPath);
                    jsonObject = obj2;
                    return jsonObject;

                case 204:
                case 400:
                case 403:
                case 500:
                    if (url.contains(KEYS.FCM_LOGOUT_METHOD)) {
                        if (status == 200 || status == 204) {
                            JSONObject obj = new JSONObject();
                            obj.put("response", true);
                            jsonObject = obj;
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("response", false);
                            jsonObject = obj;
                        }
                    }
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    try {
                        String str2 = sb.toString();
                        if (str2.startsWith("{"))
                        {
                            jsonObject = new JSONObject(sb.toString());
                        }
                        else if (str2.startsWith("["))
                        {
                            //Doing this cuz a stupid webervice is sending array instead of dictionary. Duh! :/
                            JSONArray arr = new JSONArray(sb.toString());
                            JSONObject obj = new JSONObject();
                            obj.put("whatever", arr);
                            jsonObject = obj;
                        }
                        else if (str2.contains("You Have Logged Out successfully.")) {
                            //Doing this cuz a stupid logout webervice is sending string instead of dictionary. Duh! :/
                            JSONObject obj = new JSONObject();
                            obj.put("response", str2);
                            jsonObject = obj;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }

    public JSONObject downloadWithHeaderAndRawText(String postMethod, int timeout, String url, String rawText, Context context, String fileName,String folderName) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;
        String extension = FilenameUtils.getExtension(fileName).toUpperCase();

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();


            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod("GET");

            OutputStream os = connection.getOutputStream();
            os.write(rawText.getBytes("UTF-8"));
            os.close();

            connection.connect();
            int status = connection.getResponseCode();
            BufferedReader br;
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 201:
                case 200:
                    if (extension.equals("PDF"))
                    {
                        //Use Encryption
                        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia");
                        directory.mkdirs();
                        File directory2 = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia" + File.separator + "DB");
                        directory2.mkdirs();

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
                        String updatedFileName = "Blob" + ".dat";
                        String updatedPath = path + "/Academia/DB/" + updatedFileName;

                        InputStream inputStream = connection.getInputStream();
                        AcademiaFileManager.encryptFromStream(inputStream, updatedPath);

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message", "Marksheet downloaded! Opening it...");
                        obj2.put("downloaded", true);
                        obj2.put("path", updatedPath);
                        jsonObject = obj2;
                        break;
                    }
                    else
                    {
                        String path = Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA + File.separator + Consts.MEDIA + File.separator + folderName;
                        File dir = new File(path);
                        // Create the storage directory if it does not exist
                        if (!dir.exists())
                        {
                            dir.mkdirs();
                        }

                        File newFile = new File(dir.getAbsoluteFile() + File.separator + fileName);

                        FileOutputStream fileOutput = new FileOutputStream(newFile);
                        InputStream inputStream = connection.getInputStream();

                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;

                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutput.write(buffer, 0, bufferLength);
                        }
                        fileOutput.close();

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message", "Document downloaded to Internal" + newFile);
                        obj2.put("downloaded", true);
                        obj2.put("path", path + "/" + fileName);
                        jsonObject = obj2;
                        break;
                    }

                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    JSONObject obj = new JSONObject();
                    obj.put("message", "Marksheet not found on Server!");
                    obj.put("downloaded", false);
                    jsonObject = obj;
                    break;
            }
            return jsonObject;

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public JSONObject downloadWithHeaderAndRawText2(String postMethod, int timeout, String url, Context context, String fileName,String folderName) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;
        String extension = FilenameUtils.getExtension(fileName).toUpperCase();

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            URL urlObject = new URL(BaseURL.BASE_URL + url);

            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", str);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod("GET");

            connection.connect();
            int status = connection.getResponseCode();
            switch (status) {
                case 201:
                case 200:
                    if (extension.equalsIgnoreCase("PDF"))
                    {
                        //Use Encryption
                        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia");
                        directory.mkdirs();
                        File directory2 = new File(Environment.getExternalStorageDirectory()+File.separator+"Academia" + File.separator + "DB");
                        directory2.mkdirs();

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + '/';
                        String updatedFileName = "Blob" + ".dat";
                        String updatedPath = path + "/Academia/DB/" + updatedFileName;

                        InputStream inputStream = connection.getInputStream();
                        AcademiaFileManager.encryptFromStream(inputStream, updatedPath);

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message", "Marksheet downloaded! Opening it...");
                        obj2.put("downloaded", true);
                        obj2.put("path", updatedPath);
                        jsonObject = obj2;
                        return jsonObject;
                    }
                    else
                    {
                        String path = Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA + File.separator + Consts.MEDIA + File.separator + folderName;
                        File dir = new File(path);
                        // Create the storage directory if it does not exist
                        if (!dir.exists())
                        {
                            dir.mkdirs();
                        }

                        File newFile = new File(dir.getAbsoluteFile() + File.separator + fileName);

                        FileOutputStream fileOutput = new FileOutputStream(newFile);
                        InputStream inputStream = connection.getInputStream();

                        byte[] buffer = new byte[1024];
                        int bufferLength = 0;

                        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                            fileOutput.write(buffer, 0, bufferLength);
                        }
                        fileOutput.close();

                        JSONObject obj2 = new JSONObject();
                        obj2.put("message","Document downloaded to Internal" + newFile);
                        obj2.put("downloaded", true);
                        obj2.put("path", path + "/" + fileName);
                        jsonObject = obj2;
                        return jsonObject;
                    }
                case 204:
                case 400:
                case 401:
                case 403:
                case 500:
                    JSONObject obj3 = new JSONObject();
                    obj3.put("message","Attachment not found at server!");
                    obj3.put("downloaded", false);
                    jsonObject = obj3;
                    return jsonObject;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    //-----Custom Function-----//
    public JSONObject getJSONForHomeworkAssignmentsService(Context context)
    {
        try {
            JSONObject obj = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            JSONObject obj4 = new JSONObject();
            JSONObject obj5 = new JSONObject();
            obj.put("configCode", "HOMEWORK_ASSIGNMENT_LISTING");
            obj2.put("paramCode", "BATCH_ID");
            obj3.put("paramCode", "ACADEMY_LOCATION_ID");
            obj4.put("paramCode", "ADMISSION_ID");
            obj5.put("paramCode", "PUBLISH_DATE_LESS_OR_EQUAL");

            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            Integer batchID = sharedPrefrenceManager.getBatchIDFromKey();
            Integer admissionID = sharedPrefrenceManager.getAdmissionIDFromKey();
            Integer academyLocationID = sharedPrefrenceManager.getAcademyLocationIDFromKey();

            JSONArray arr1 = new JSONArray();
            JSONArray arr2 = new JSONArray();
            JSONArray arr3 = new JSONArray();
            JSONArray arr4 = new JSONArray();

            arr1.put(batchID);
            arr2.put(admissionID);
            arr3.put(academyLocationID);

            Date todayDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String todayString = formatter.format(todayDate);

            arr4.put(todayString);

            obj2.put("paramValues", arr1);
            obj3.put("paramValues", arr3);
            obj4.put("paramValues", arr2);
            obj5.put("paramValues", arr4);

            JSONArray arr = new JSONArray();
            arr.put(obj2);
            arr.put(obj3);
            arr.put(obj4);
            arr.put(obj5);

            obj.put("searchCriterias", arr);
            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForBillHeaders(Context context, String invoiceID)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("configCode", "BILL_FEE_PLAN_PC_LISTING");

            JSONObject obj2 = new JSONObject();

            JSONArray arr1 = new JSONArray();
            arr1.put(invoiceID);

            obj2.put("paramValues", arr1);
            obj2.put("paramCode", "BILL_HEADER_ID");

            JSONArray arr = new JSONArray();
            arr.put(obj2);

            obj.put("searchCriterias", arr);
            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForPersonalDetails2Service(Context context)
    {
        try {
            JSONObject obj = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            JSONObject obj4 = new JSONObject();
            JSONObject obj5 = new JSONObject();
            obj.put("configCode", "STUDENT_LIST");
            obj2.put("paramCode", "STATUS");
            obj3.put("paramCode", "ADMISSION_STATUS");
            obj4.put("paramCode", "STUDENT_ID");
            obj5.put("paramCode", "ACADEMY_LOCATION_ID");

            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String studentCode = sharedPrefrenceManager.getUserCodeFromKey();
            Integer academyLocationID = sharedPrefrenceManager.getAcademyLocationIDFromKey();

            JSONArray arr1 = new JSONArray();
            JSONArray arr2 = new JSONArray();
            JSONArray arr3 = new JSONArray();
            JSONArray arr4 = new JSONArray();

            arr1.put(true);
            arr2.put(true);
            arr3.put(studentCode);
            arr4.put(academyLocationID);

            obj2.put("paramValues", arr1);
            obj3.put("paramValues", arr2);
            obj4.put("paramValues", arr3);
            obj5.put("paramValues", arr4);

            JSONArray arr = new JSONArray();
            arr.put(obj2);
            arr.put(obj3);
            arr.put(obj4);
            arr.put(obj5);

            obj.put("searchCriterias", arr);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForFCMRegistrationService(Context context)
    {
        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            String fcmToken = sharedPrefrenceManager.getFirebaseTokenFromKey();
            Integer userID = sharedPrefrenceManager.getUserIDFromKey();
            Long firebaseID = sharedPrefrenceManager.getFirebaseIDFromKey();

            JSONObject obj = new JSONObject();
            JSONObject obj2 = new JSONObject();
            obj2.put("id", userID);

            obj.put("devicePlatForm", "ANDROID");
            obj.put("deviceTokenId", fcmToken);
            obj.put("personId", obj2);

            if(firebaseID != -1) {
                obj.put("id", firebaseID);
            }

            return obj;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForPaymentIDService(String payableAmount, String payLoadString)
    {
        try {
            JSONObject obj = new JSONObject();

            obj.put("receiptAmount", Double.valueOf(payableAmount));

            JSONArray jsonArray = new JSONArray(payLoadString);
            JSONArray payloadArray = new JSONArray();

            for (int i = 0 ; i < jsonArray.length() ; ++i) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                JSONObject payloadObject = new JSONObject();

                payloadObject.put("billId", jsonObject.optInt("billId"));

                if(jsonObject.has("partAmount")){
                    payloadObject.put("adjustedAmount", jsonObject.optDouble("partAmount"));
                    payloadObject.put("balanceAmount", jsonObject.optDouble("remainBalance"));
                }else{

                    payloadObject.put("adjustedAmount", jsonObject.optDouble("balanceAmount"));
                    payloadObject.put("balanceAmount", 0);
                }

                payloadObject.put("billFeePlanRuleStageId", "");

                payloadArray.put(payloadObject);
            }

            obj.put("onlinePayments", payloadArray);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForPaymentIDFeeHeadService(String payableAmount, String payLoadString)
    {
        try {
            JSONObject obj = new JSONObject();

            obj.put("receiptAmount", Double.valueOf(payableAmount));

            JSONArray jsonArray = new JSONArray(payLoadString);
            JSONArray payloadArray = new JSONArray();

            for (int i = 0 ; i < jsonArray.length() ; ++i) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                JSONObject payloadObject = new JSONObject();

                payloadObject.put("billId", jsonObject.optInt("billHeaderId"));

                if(jsonObject.has("partAmount")){
                    payloadObject.put("adjustedAmount", jsonObject.optDouble("partAmount"));
                    payloadObject.put("balanceAmount", jsonObject.optDouble("remainBalance"));
                }else{

                    payloadObject.put("adjustedAmount", jsonObject.optDouble("totalBalanceAmount"));
                    payloadObject.put("balanceAmount", 0);
                }

                payloadObject.put("billFeePlanRuleStageId", "");

                payloadArray.put(payloadObject);
            }

            obj.put("onlinePayments", payloadArray);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForInvoiceDownloadService(Context context, String billId)
    {
        try {
            JSONObject obj = new JSONObject();
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);

//            obj.put("code", "MANAGE_BILLS_CODE");
            obj.put("code", "MANAGE_BILLS_CODE");
            obj.put("reportTitle", "Bill");
            obj.put("academyLocationId", sharedPrefrenceManager.getAcademyLocationIDFromKey());
            obj.put("userId", sharedPrefrenceManager.getUserCodeFromKey());

            JSONObject obj2 = new JSONObject();
            obj2.put("userId", sharedPrefrenceManager.getUserCodeFromKey());
            obj2.put("ids", billId);

            obj.put("reportParams", obj2);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForReceiptDownloadService(Context context, String receiptId, String receiptStatus)
    {
        try {
            JSONObject obj = new JSONObject();
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);

            if(receiptStatus.equalsIgnoreCase("PARTLY_SETTLED") || receiptStatus.equalsIgnoreCase("SETTLED")){

                obj.put("code", "MANAGE_RECEIPT_SETTLEMENT_STUDENT_CODE");
                obj.put("reportTitle", "Receipt Settlement");
                obj.put("academyLocationId", sharedPrefrenceManager.getAcademyLocationIDFromKey());
                obj.put("userId", sharedPrefrenceManager.getUserCodeFromKey());

                JSONObject obj2 = new JSONObject();
                obj2.put("userId", sharedPrefrenceManager.getUserCodeFromKey());
                obj2.put("ids", receiptId);
                obj2.put("SettledReceiptId", receiptId);

                obj.put("reportParams", obj2);

            }else{

                obj.put("code", "MANAGE_RECEIPT_STUDENT_CODE");
                obj.put("reportTitle", "Receipt");
                obj.put("academyLocationId", sharedPrefrenceManager.getAcademyLocationIDFromKey());
                obj.put("userId", sharedPrefrenceManager.getUserCodeFromKey());

                JSONObject obj2 = new JSONObject();
                obj2.put("userId", sharedPrefrenceManager.getUserCodeFromKey());
                obj2.put("ids", "");
                obj2.put("PendingReceiptId", receiptId);

                obj.put("reportParams", obj2);
            }

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONMyRequestService(Context context,ArrayList<RequestFilter_Dto> showList)
    {
        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
        int studentId = sharedPrefrenceManager.getUserIDFromKey();
        int academyLocationId = sharedPrefrenceManager.getAcademyLocationIDFromKey();

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("All_REQUESTS");

        ArrayList<Integer> arr1 = new ArrayList<Integer>();
        arr1.add(academyLocationId);

        ArrayList<Integer> arr2 = new ArrayList<Integer>();
        arr2.add(studentId);

        showList.add(new RequestFilter_Dto("requestFor",arr,1));
        showList.add(new RequestFilter_Dto("ACADEMY_LOCATION_ID",arr1,2));
        showList.add(new RequestFilter_Dto("STUDENT_USERID",arr2,2));

        try {
            JSONObject obj = new JSONObject();
            obj.put("configCode", "STUDENT_TRACK_REQUEST");

            JSONArray array = new JSONArray();
            for (int i=0 ; i<showList.size() ; i++) {

                JSONObject searchCri = new JSONObject();
                searchCri.put("paramCode", showList.get(i).getName());
                JSONArray valArray = new JSONArray();
                switch (showList.get(i).getType())
                {
                    case 1:
                        ArrayList<String> val = (ArrayList<String>) showList.get(i).getValues();
                        for(String str : val){
                            valArray.put(str);
                        }
                        searchCri.put("paramValues",valArray);
                        break;
                    case 2:
                        ArrayList<Integer> val1 = (ArrayList<Integer>)showList.get(i).getValues();
                        for(Integer in : val1){
                            valArray.put(in);
                        }
                        searchCri.put("paramValues",valArray);
                        break;
                }

                array.put(searchCri);
            }

            obj.put("searchCriterias", array);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForResetPasswordService(Context context, String currentPassword, String newPassword)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPassword", currentPassword);
            obj.put("newPassword", newPassword);
            obj.put("confirmedPassword", newPassword);

            return obj;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForUpdateTransactionService(Context context, String id)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForUpdateTransactionIDService(String status, String paymentID, String transactionID)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("status", status);
            obj.put("id", paymentID);
            obj.put("txnid", transactionID);

            return obj;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJSONForCreateMedicalConditionService(HashMap<String, String> myMap)
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("consultingDoctor", myMap.get("consultingDoctor").toString());
            obj.put("dateSince", myMap.get("dateSince").toString());
            obj.put("doctorTelephoneCountryCode", myMap.get("doctorTelephoneCountryCode").toString());
            obj.put("doctorTelephoneNo", myMap.get("doctorTelephoneNo").toString());
            obj.put("medicalCondition", myMap.get("medicalCondition").toString());
            obj.put("remarks", myMap.get("remarks").toString());

            JSONObject obj2 = new JSONObject();
            obj2.put("id",myMap.get("conditionType").toString());
            obj.put("conditionType",obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("id",myMap.get("personId").toString());
            obj.put("person",obj3);

            return obj;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
