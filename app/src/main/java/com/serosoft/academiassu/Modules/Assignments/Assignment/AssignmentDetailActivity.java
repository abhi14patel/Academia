package com.serosoft.academiassu.Modules.Assignments.Assignment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters.DocumentAdapter;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentDocument_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Widgets.ExpandedListView;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on November 01 2019.
 */

public class AssignmentDetailActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private Button btnSubmit;
    private TextView tvSubmitMsg;

    private TextView tvCourseName,tvCourseStatus,tvAssignmentName,tvCourseVariant,tvPublishedDate,tvDueOn;
    private TextView tvFacultyName,tvDescription;

    private LinearLayout llAttachment,llDescription,llFacultyName;
    private LinearLayout llDueOn,llPublishedDate,llCourseVariant,llAssignmentName;

    Assignment_Dto assignment_dto;

    String documentId="",documentName="";
    ArrayList<AssignmentDocument_Dto> assignmentDocumentList;
    private ExpandedListView attachmentsListView;
    DocumentAdapter documentAdapter;

    int groupAssignmentId = 0;
    String submissionStatus ="";
    long publishDate, submissionDate, reSubmitDate;

    private TextView tvCourseVariant1,tvFacultyName1,tvCourseStatus1,tvPublishedDate1,tvDueOn1,tvAttachment1;

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = AssignmentDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_detail_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AssignmentDetailActivity.this;

        Initialize();

        Intent intent = getIntent();
        assignment_dto = (Assignment_Dto) intent.getSerializableExtra(Consts.ASSIGNMENT_LIST);

        showAssignmentData(assignment_dto);
    }

    private void showAssignmentData(Assignment_Dto assignment_dto) {

        String courseName = ProjectUtils.getCorrectedString(assignment_dto.getCourseName());
        if(!courseName.equalsIgnoreCase("")) {

            tvCourseName.setText(courseName);
        }

        String assignmentName = ProjectUtils.getCorrectedString(assignment_dto.getAssignmentName());
        if(!assignmentName.equalsIgnoreCase("")) {

            tvAssignmentName.setText(assignmentName);
        }

        String courseVariant = ProjectUtils.getCorrectedString(assignment_dto.getCourseVariant());
        if(!courseVariant.equalsIgnoreCase("")) {

            tvCourseVariant.setText(courseVariant);
        }

        String facultyName = ProjectUtils.getCorrectedString(assignment_dto.getFacultyName());
        if(!facultyName.equalsIgnoreCase("")) {

            tvFacultyName.setText(facultyName);
        }

        publishDate = assignment_dto.getPublish_date_long();
        String date = ProjectUtils.convertTimestampToDate(publishDate,mContext);
        String time = ProjectUtils.convertTimestampToTime(publishDate,mContext);

        if(!date.equalsIgnoreCase(""))
        {
            tvPublishedDate.setText(date+" "+time);
        }

        submissionDate = assignment_dto.getSubmission_last_date_long();
        String date1 = ProjectUtils.convertTimestampToDate(submissionDate,mContext);
        String time1 = ProjectUtils.convertTimestampToTime(submissionDate,mContext);

        if(!date1.equalsIgnoreCase(""))
        {
            tvDueOn.setText(date1+" "+time1);
        }

        //Here call attachment api
        if (ConnectionDetector.isConnectingToInternet(this)){

            showProgressDialog(this);
            String assignmentId = String.valueOf(assignment_dto.getId());
            new OptimizedServerCallAsyncTask(mContext, AssignmentDetailActivity.this,
                    KEYS.SWITCH_HOMEWORK_DOCUMENTS).execute(assignmentId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Here call saved document api
        if (ConnectionDetector.isConnectingToInternet(this)){

            String assignmentId = String.valueOf(assignment_dto.getId());
            String studentId = String.valueOf(sharedPrefrenceManager.getUserIDFromKey());

            new OptimizedServerCallAsyncTask(mContext, AssignmentDetailActivity.this,
                    KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT).execute(assignmentId,studentId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        //Here call description api
        if (ConnectionDetector.isConnectingToInternet(this)){

            //showProgressDialog(this);
            String assignmentId = String.valueOf(assignment_dto.getId());
            new OptimizedServerCallAsyncTask(mContext, AssignmentDetailActivity.this,
                    KEYS.SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION).execute(assignmentId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_DESCRIPTION:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    String description = jsonObject.optString("description");

                    description = ProjectUtils.getCorrectedString(description);

                    if(!description.equalsIgnoreCase("")) {

                        llDescription.setVisibility(View.VISIBLE);
                        tvDescription.setText(description);
                    }
                    else
                    {
                        llDescription.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    llDescription.setVisibility(View.GONE);
                    tvCourseStatus.setText(getString(R.string.pending));
                    tvCourseStatus.setTextColor(getResources().getColor(R.color.colorPending));
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
                            String hwOnlineSubmission = assignment_dto.getHwOnlineSubmissions();
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

                                            String assignmentId = String.valueOf(assignment_dto.getId());

                                            Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                            intent.putExtra("groupAssignmentId",groupAssignmentId);
                                            intent.putExtra("assignmentId",assignmentId);
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

                                                String assignmentId = String.valueOf(assignment_dto.getId());

                                                Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                                intent.putExtra("groupAssignmentId",groupAssignmentId);
                                                intent.putExtra("assignmentId",assignmentId);
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

                                            String assignmentId = String.valueOf(assignment_dto.getId());

                                            Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                            intent.putExtra("groupAssignmentId",groupAssignmentId);
                                            intent.putExtra("assignmentId",assignmentId);
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

                                                String assignmentId = String.valueOf(assignment_dto.getId());

                                                Intent intent = new Intent(mContext,AssignmentUploadActivity.class);
                                                intent.putExtra("groupAssignmentId",groupAssignmentId);
                                                intent.putExtra("assignmentId",assignmentId);
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

                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
                                    tvSubmitMsg.setVisibility(View.VISIBLE);
                                    tvSubmitMsg.setText(getString(R.string.you_have_already_submitted_this_assignment));
                                    tvSubmitMsg.setTextColor(getResources().getColor(R.color.colorCompleted));

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

            case KEYS.SWITCH_HOMEWORK_DOCUMENTS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    assignmentDocumentList = new ArrayList<>();
                    for(int i = 0 ; i<jsonArray.length() ; i++)
                    {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int documentId = jsonObject1.optInt("documentId");
                        String documentName = jsonObject1.optString("documentName");
                        String path = jsonObject1.optString("path");

                        assignmentDocumentList.add(new AssignmentDocument_Dto(documentId,documentName,path));
                        break;
                    }

                    if(assignmentDocumentList != null && assignmentDocumentList.size()>0) {

                        llAttachment.setVisibility(View.VISIBLE);

                        documentAdapter = new DocumentAdapter(mContext,assignmentDocumentList);
                        attachmentsListView.setAdapter(documentAdapter);

                        attachmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                AssignmentDocument_Dto assignmentDocument_dto = assignmentDocumentList.get(position);

                                downloadDocument(assignmentDocument_dto);
                            }
                        });
                    }
                    else
                    {
                        llAttachment.setVisibility(View.GONE);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    llAttachment.setVisibility(View.GONE);
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

    private void downloadDocument(AssignmentDocument_Dto assignmentDocument_dto) {

        documentId = String.valueOf(assignmentDocument_dto.getDocumentId());

        String docName = ProjectUtils.getCorrectedString(assignmentDocument_dto.getPath());
        if(!docName.equalsIgnoreCase(""))
        {
            int index = docName.lastIndexOf("/");
            documentName = docName.substring(index + 1);
        }

        if (ProjectUtils.hasPermissionInManifest(AssignmentDetailActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(AssignmentDetailActivity.this, AssignmentDetailActivity.this,
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

                            if (ProjectUtils.hasPermissionInManifest(AssignmentDetailActivity.this,PERMISSION_CODE,permission))
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
        attachmentsListView = findViewById(R.id.attachmentsListView);

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

        llAttachment = findViewById(R.id.llAttachment);
        llDescription = findViewById(R.id.llDescription);
        llFacultyName = findViewById(R.id.llFacultyName);
        llDueOn = findViewById(R.id.llDueOn);
        llPublishedDate = findViewById(R.id.llPublishedDate);
        llCourseVariant = findViewById(R.id.llCourseVariant);
        llAssignmentName = findViewById(R.id.llAssignmentName);

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
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

        return true;
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
