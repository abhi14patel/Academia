package com.serosoft.academiassu.Modules.Assignments.Assignment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;

import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Abhishek on November 01 2019.
 */

public class AssignmentDetailTempActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private Button btnSubmit;
    private TextView tvSubmitMsg;

    private TextView tvCourseName,tvCourseStatus,tvAssignmentName,tvCourseVariant,tvPublishedDate,tvDueOn;
    private TextView tvFacultyName,tvDescription,tvAttachment;
    private AppCompatImageView ivAttachment;

    private TextView tvCourseVariant1,tvFacultyName1,tvCourseStatus1,tvPublishedDate1,tvDueOn1,tvAttachment1;

    private LinearLayout llAttachment,llDescription;
    int assignmentId = 0;
    int groupAssignmentId = 0;
    String id = "";
    long publishDate, submissionDate, reSubmitDate;
    String documentId="",documentName="",submissionStatus ="",hwOnlineSubmission="";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = AssignmentDetailTempActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_detail_temp_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AssignmentDetailTempActivity.this;

        Initialize();

        Intent intent = getIntent();
        assignmentId = intent.getIntExtra(Consts.ASSIGNMENT_ID,0);
        ProjectUtils.showLog(TAG,""+assignmentId);

        //Here call description api
        if (ConnectionDetector.isConnectingToInternet(this)){

            String assignmentid = String.valueOf(assignmentId);
            new OptimizedServerCallAsyncTask(mContext, AssignmentDetailTempActivity.this,
                    KEYS.SWITCH_HOMEWORK_DOCUMENTS).execute(assignmentid);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        showAssignmentData(assignmentId);
    }

    private void showAssignmentData(int assignmentId) {

        //Here call description new api
        if (ConnectionDetector.isConnectingToInternet(this)){

            showProgressDialog(this);

            String assignmentid = String.valueOf(assignmentId);
            String studentId = String.valueOf(sharedPrefrenceManager.getUserIDFromKey());

            new OptimizedServerCallAsyncTask(mContext, AssignmentDetailTempActivity.this,
                    KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2).execute(assignmentid,studentId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {

            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor) {

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS2:
                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult);

                    id = String.valueOf(jsonObject.optInt("id"));
                    documentId = String.valueOf(jsonObject.optInt("documentId"));
                    long dateGiven = jsonObject.optLong("dateGiven");
                    submissionDate = jsonObject.optLong("dateDueOn");
                    publishDate = jsonObject.optLong("dateAssignment");
                    String assignmentName = jsonObject.optString("assignmentName");
                    String facultyName = jsonObject.optString("facultyName");
                    String description = jsonObject.optString("description");
                    String courseCode = jsonObject.optString("courseCode");
                    String courseVariantCode = jsonObject.optString("courseVariantCode");
                    documentName = jsonObject.optString("documentName");
                    String path = jsonObject.optString("path");
                    submissionStatus = jsonObject.optString("status");
                    String courseName = jsonObject.optString("courseName");
                    hwOnlineSubmission = jsonObject.optString("assignmentStatus");

                    courseName = ProjectUtils.getCorrectedString(courseName);
                    if(!courseName.equalsIgnoreCase("")) {

                        tvCourseName.setText(courseName);
                    }

                    assignmentName = ProjectUtils.getCorrectedString(assignmentName);
                    if(!assignmentName.equalsIgnoreCase("")) {

                        tvAssignmentName.setText(assignmentName);
                    }

                    courseVariantCode = ProjectUtils.getCorrectedString(courseVariantCode);
                    if(!courseVariantCode.equalsIgnoreCase("")) {

                        tvCourseVariant.setText(courseVariantCode);
                    }

                    facultyName = ProjectUtils.getCorrectedString(facultyName);
                    if(!facultyName.equalsIgnoreCase("")) {

                        tvFacultyName.setText(facultyName);
                    }

                    String date = ProjectUtils.convertTimestampToDate(publishDate,mContext);
                    String time = ProjectUtils.convertTimestampToTime(publishDate,mContext);
                    if(!date.equalsIgnoreCase(""))
                    {
                        tvPublishedDate.setText(date+" "+time);
                    }

                    String date1 = ProjectUtils.convertTimestampToDate(submissionDate,mContext);
                    String time1 = ProjectUtils.convertTimestampToTime(submissionDate,mContext);
                    if(!date1.equalsIgnoreCase(""))
                    {
                        tvDueOn.setText(date1+" "+time1);
                    }

                    description = ProjectUtils.getCorrectedString(description);
                    if(!description.equalsIgnoreCase("")){

                        llDescription.setVisibility(View.VISIBLE);
                        tvDescription.setText(description);
                    }
                    else
                    {
                        llDescription.setVisibility(View.GONE);
                    }

                    //Here manage status
                    tvCourseStatus.setText(submissionStatus);

                    if(submissionStatus.equalsIgnoreCase(getString(R.string.pending))){
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
                    }else if(submissionStatus.equalsIgnoreCase(getString(R.string.saved))){
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSaved));
                    }else if(submissionStatus.equalsIgnoreCase(getString(R.string.completed))){
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorCompleted));
                    }else if(submissionStatus.equalsIgnoreCase(getString(R.string.submitted))){
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSubmitted));
                    }else if(submissionStatus.equalsIgnoreCase(getString(R.string.reSubmitted))){
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSubmitted));
                    }else{
                        tvCourseStatus.setText(getString(R.string.pending));
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
                    }

                    String docPath = ProjectUtils.getCorrectedString(path);
                    //Here manage document attachment
                    if(!docPath.equalsIgnoreCase("")){

                        llAttachment.setVisibility(View.VISIBLE);
                        int index = docPath.lastIndexOf("/");
                        String docFileName = docPath.substring(index + 1);

                        tvAttachment.setText(docFileName);
                        ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));
                        tvAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (ProjectUtils.hasPermissionInManifest(AssignmentDetailTempActivity.this,PERMISSION_CODE,permission))
                                {
                                    callAPIWithPermission();
                                }
                            }
                        });
                    }else{
                        llAttachment.setVisibility(View.GONE);
                    }

                    //Here call saved document api
                    if (ConnectionDetector.isConnectingToInternet(this)){

                        String assignmentid = String.valueOf(assignmentId);
                        String studentId = String.valueOf(sharedPrefrenceManager.getUserIDFromKey());

                        new OptimizedServerCallAsyncTask(mContext, AssignmentDetailTempActivity.this,
                                KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT).execute(assignmentid,studentId);
                    } else {
                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                }
                break;

            case KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        for(int i = 0 ; i<jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            groupAssignmentId = jsonObject1.optInt("id");
                            submissionStatus = jsonObject1.optString("submissionStatus");
                            reSubmitDate = jsonObject1.optLong("resubmissionDueDate");
                            tvCourseStatus.setText(submissionStatus);

                            if(submissionStatus.equalsIgnoreCase(getString(R.string.pending))){
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
                            }else if(submissionStatus.equalsIgnoreCase(getString(R.string.saved))){
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSaved));
                            }else if(submissionStatus.equalsIgnoreCase(getString(R.string.completed))){
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorCompleted));
                            }else if(submissionStatus.equalsIgnoreCase(getString(R.string.submitted))){
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSubmitted));
                            }else if(submissionStatus.equalsIgnoreCase(getString(R.string.reSubmitted))){
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorSubmitted));
                            }else{
                                tvCourseStatus.setText(getString(R.string.pending));
                                tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
                            }

                            //Here set button and message according to status
                            int dueStatus = getDueStatus(submissionDate);
                            int publishStatus = getPublishStatus(publishDate);
                            int reSubmitStatus = getReSubmitStatus(reSubmitDate);

                            if(hwOnlineSubmission.equalsIgnoreCase("Yes")){

                                btnSubmit.setVisibility(View.VISIBLE);

                                if(dueStatus == 1 && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.pending)))){

                                    btnSubmit.setEnabled(true);
                                    tvSubmitMsg.setVisibility(View.GONE);
                                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));

                                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                            intent.putExtra("groupAssignmentId",groupAssignmentId);
                                            intent.putExtra("assignmentId",id);
                                            intent.putExtra("reSubmitStatus",reSubmitStatus);
                                            startActivity(intent);
                                        }
                                    });

                                }else if(dueStatus == 0 && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.pending)))){

                                    if(reSubmitStatus == 1){
                                        btnSubmit.setEnabled(true);
                                        tvSubmitMsg.setVisibility(View.GONE);
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));

                                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                                intent.putExtra("groupAssignmentId",groupAssignmentId);
                                                intent.putExtra("assignmentId",id);
                                                intent.putExtra("reSubmitStatus",reSubmitStatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        btnSubmit.setEnabled(false);
                                        tvSubmitMsg.setVisibility(View.VISIBLE);
                                        tvSubmitMsg.setText(getString(R.string.you_cannot_submit_this_assignment));
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                    }

                                }else if(dueStatus == 1 && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.saved)))){

                                    btnSubmit.setEnabled(true);
                                    tvSubmitMsg.setVisibility(View.GONE);
                                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));

                                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                            intent.putExtra("groupAssignmentId",groupAssignmentId);
                                            intent.putExtra("assignmentId",id);
                                            intent.putExtra("reSubmitStatus",reSubmitStatus);
                                            startActivity(intent);
                                        }
                                    });

                                }else if((dueStatus == 1 || dueStatus == 0) && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.submitted)))){

                                    if(reSubmitStatus == 1){
                                        btnSubmit.setEnabled(true);
                                        tvSubmitMsg.setVisibility(View.GONE);
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));

                                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                                intent.putExtra("groupAssignmentId",groupAssignmentId);
                                                intent.putExtra("assignmentId",id);
                                                intent.putExtra("reSubmitStatus",reSubmitStatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        btnSubmit.setEnabled(false);
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                        tvSubmitMsg.setVisibility(View.VISIBLE);
                                        tvSubmitMsg.setText(getString(R.string.you_have_already_submitted_this_assignment));
                                        tvSubmitMsg.setTextColor(getResources().getColor(R.color.colorCompleted));
                                    }

                                }else if((dueStatus == 1 || dueStatus == 0) && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.completed)))){

                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                    tvSubmitMsg.setVisibility(View.VISIBLE);
                                    tvSubmitMsg.setText(getString(R.string.you_have_already_submitted_this_assignment));
                                    tvSubmitMsg.setTextColor(getResources().getColor(R.color.colorCompleted));

                                }else if((dueStatus == 1 || dueStatus == 0) && publishStatus == 1 && (submissionStatus.equalsIgnoreCase(getString(R.string.reSubmitted)))){

                                    if(reSubmitStatus == 1){
                                        btnSubmit.setEnabled(true);
                                        tvSubmitMsg.setVisibility(View.GONE);
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));

                                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                                intent.putExtra("groupAssignmentId",groupAssignmentId);
                                                intent.putExtra("assignmentId",id);
                                                intent.putExtra("reSubmitStatus",reSubmitStatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{

                                        btnSubmit.setEnabled(false);
                                        tvSubmitMsg.setVisibility(View.VISIBLE);
                                        tvSubmitMsg.setText(getString(R.string.you_cannot_submit_this_assignment));
                                        btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                    }

                                }else{
                                    tvSubmitMsg.setVisibility(View.VISIBLE);
                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                }
                            }else{
                                btnSubmit.setVisibility(View.GONE);
                            }
                        }
                    }else{
                        tvCourseStatus.setText(getString(R.string.pending));
                        tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;

            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String path = jsonObject.optString("path");
                    String extension = FilenameUtils.getExtension(documentName).toUpperCase();
                    String message = jsonObject.optString("message");

                    if (extension.equalsIgnoreCase("PDF"))
                    {
                        if (downloaded)
                        {
                            Intent intent=new Intent(mContext, PDFActivity2.class);
                            intent.putExtra("filePath",path);
                            intent.putExtra("folderName",Consts.ASSIGNMENT);
                            intent.putExtra("screenName",Consts.ASSIGNMENT);
                            intent.putExtra("idForDocument",documentId);
                            intent.putExtra("headerColor",R.color.colorAssignment);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        if (downloaded)
                        {
                            openFile(new File(path), this);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    //Here get event status according to start and end date
    private int getDueStatus(long due_date) {

        long currentDate = System.currentTimeMillis();

        if(currentDate < due_date) {
            return 1;
        }
        else {
            return 0;
        }
    }

    //Here get event status according to start and end date
    private int getPublishStatus(long publishDate) {

        long currentDate = System.currentTimeMillis();

        if(currentDate > publishDate) {
            return 1;
        }
        else {
            return 0;
        }
    }

    //Here get event status according to start and end date
    private int getReSubmitStatus(long reSubmitDate) {

        long currentDate = System.currentTimeMillis();

        if(currentDate < reSubmitDate) {
            return 1;
        }
        else {
            return 0;
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(AssignmentDetailTempActivity.this, AssignmentDetailTempActivity.this,
                KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD).execute(documentId, documentName, Consts.ASSIGNMENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        boolean hasAllPermissions = false;

        switch (requestCode)
        {
            case PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasAllPermissions = true;
                }
                else
                {
                    hasAllPermissions = false;

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.allow_all_permissions), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (ProjectUtils.hasPermissionInManifest(AssignmentDetailTempActivity.this,PERMISSION_CODE,permission))
                            {
                                callAPIWithPermission();
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },false);
                }

                if(hasAllPermissions) callAPIWithPermission();

                break;
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSubmitMsg = findViewById(R.id.tvSubmitMsg);

        toolbar.setTitle(translationManager.HOMEWORKASSIGNMENT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAssignment, toolbar, this); }

        tvCourseName = findViewById(R.id.tvCourseName);
        tvCourseStatus = findViewById(R.id.tvCourseStatus);
        tvAssignmentName = findViewById(R.id.tvAssignmentName);
        tvCourseVariant = findViewById(R.id.tvCourseVariant);
        tvPublishedDate = findViewById(R.id.tvPublishedDate);
        tvDueOn = findViewById(R.id.tvDueOn);
        tvFacultyName = findViewById(R.id.tvFacultyName);
        tvDescription = findViewById(R.id.tvDescription);
        tvAttachment = findViewById(R.id.tvAttachment);
        ivAttachment = findViewById(R.id.ivAttachment);

        llAttachment = findViewById(R.id.llAttachment);
        llDescription = findViewById(R.id.llDescription);

        tvCourseVariant1 = findViewById(R.id.tvCourseVariant1);
        tvFacultyName1 = findViewById(R.id.tvFacultyName1);
        tvCourseStatus1 = findViewById(R.id.tvCourseStatus1);
        tvAttachment1 = findViewById(R.id.tvAttachment1);
        tvPublishedDate1 = findViewById(R.id.tvPublishedDate1);
        tvDueOn1 = findViewById(R.id.tvDueOn1);

        tvCourseVariant1.setText(translationManager.COURSE_VARIANT_KEY);
        tvFacultyName1.setText(translationManager.FACULTY_KEY);
        tvCourseStatus1.setText(translationManager.COURSE_STATUS_KEY+" - ");
        tvAttachment1.setText(translationManager.ATTACHMENT_KEY);
        tvPublishedDate1.setText(translationManager.PUBLISH_DATE_KEY);
        tvDueOn1.setText(translationManager.DUE_ON_KEY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(mContext, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                getNotifications();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
