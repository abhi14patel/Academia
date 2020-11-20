package com.serosoft.academiassu.Modules.Assignments.SessionDiary;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Consts;

import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Adapters.DocumentAdapter;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDocuemnt_Dto;
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
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on October 31 2019.
 */

public class SessionDiaryDetailActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private ExpandedListView attachmentsListView;
    DocumentAdapter documentAdapter;

    private TextView tvCourseName,tvCourseCode,tvCourseVariant,tvSessionDate,tvTimeSlot;
    private TextView tvFacultyName,tvTopics,tvDescription;

    private LinearLayout llCourseCode,llCourseVariant,llSessionDate,llTimeSlot;
    private LinearLayout llFacultyName,llTopics,llDescription,llAttachment;

    String documentId="",documentName="";
    String fromTime ="",toTime ="";
    SessionDiary_Dto sessionDiary_dto;

    private TextView tvCourseVariant1,tvSessionDate1,tvFacultyName1,tvTopics1,tvAttachment1;

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = SessionDiaryDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_diary_detail_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = SessionDiaryDetailActivity.this;

        Initialize();

        Intent intent = getIntent();
        sessionDiary_dto = (SessionDiary_Dto) intent.getSerializableExtra(Consts.SESSION_DIARY_LIST);

        showCourseData(sessionDiary_dto);
    }

    private void showCourseData(SessionDiary_Dto sessionDiary_dto) {

        String courseName = ProjectUtils.getCorrectedString(sessionDiary_dto.getCourse());
        if(!courseName.equalsIgnoreCase("")) {

            tvCourseName.setText(courseName);
        }

        String courseVariant = ProjectUtils.getCorrectedString(sessionDiary_dto.getCourseVariant());
        if(!courseVariant.equalsIgnoreCase("")) {

            tvCourseVariant.setText(courseVariant);
        }

        long longDate = sessionDiary_dto.getDate_long();
        String date = ProjectUtils.convertTimestampToDate(longDate,mContext);
        String time = ProjectUtils.convertTimestampToTime(longDate,mContext);
        if(!date.equalsIgnoreCase("")) {

            tvSessionDate.setText(date);
        }

        String facultyName = ProjectUtils.getCorrectedString(sessionDiary_dto.getFacultyName());
        if(!facultyName.equalsIgnoreCase("")) {

            tvFacultyName.setText(facultyName);
        }

        String topics = ProjectUtils.getCorrectedString(sessionDiary_dto.getTopic());
        if(!topics.equalsIgnoreCase("")) {

            tvTopics.setText(topics);
        }

        long longFromTime = sessionDiary_dto.getFromSlot_long();
        if(longFromTime != 0) {

            fromTime = ProjectUtils.convertTimestampToTime(longFromTime,mContext);
        }

        long longToTime = sessionDiary_dto.getToSlot_long();
        if(longToTime != 0) {

            toTime = ProjectUtils.convertTimestampToTime(longToTime,mContext);
        }

        if(!fromTime.equalsIgnoreCase("") && !toTime.equalsIgnoreCase("")) {

            llTimeSlot.setVisibility(View.VISIBLE);
            tvTimeSlot.setText(fromTime+" - "+toTime);
        }
        else
        {
            llTimeSlot.setVisibility(View.GONE);
        }

        //Here call description api
        if (ConnectionDetector.isConnectingToInternet(this)){

            showProgressDialog(this);
            String courseId = String.valueOf(sessionDiary_dto.getId());
            new OptimizedServerCallAsyncTask(mContext, SessionDiaryDetailActivity.this,
                    KEYS.SWITCH_COURSE_DIARY_DESCRIPTION).execute(courseId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        //Here set attachment
        ArrayList<SessionDocuemnt_Dto> sessionDocuemntList = sessionDiary_dto.getDocuemnt_dtos();
        if(sessionDocuemntList != null && sessionDocuemntList.size()>0){

            llAttachment.setVisibility(View.VISIBLE);

            documentAdapter = new DocumentAdapter(mContext,sessionDocuemntList,this);
            attachmentsListView.setAdapter(documentAdapter);

            attachmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SessionDocuemnt_Dto sessionDocuemnt_dto = sessionDocuemntList.get(position);

                    downloadDocument(sessionDocuemnt_dto);
                }
            });
        }
        else
        {
            llAttachment.setVisibility(View.GONE);
        }
    }

    public void downloadDocument(SessionDocuemnt_Dto sessionDocuemnt_dto)
    {
        documentId = String.valueOf(sessionDocuemnt_dto.getId());

        String docName = ProjectUtils.getCorrectedString(sessionDocuemnt_dto.getSecondValue());
        if(!docName.equalsIgnoreCase(""))
        {
            int index = docName.lastIndexOf("/");
            documentName = docName.substring(index + 1);
        }

        if (ProjectUtils.hasPermissionInManifest(SessionDiaryDetailActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(SessionDiaryDetailActivity.this, SessionDiaryDetailActivity.this,
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

                            if (ProjectUtils.hasPermissionInManifest(SessionDiaryDetailActivity.this,PERMISSION_CODE,permission))
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
            case KEYS.SWITCH_COURSE_DIARY_DESCRIPTION:
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
                            intent.putExtra("folderName",Consts.SESSION_DIARY);
                            intent.putExtra("screenName",Consts.SESSION_DIARY);
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

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.COURSE_SESSIONDIARY_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAssignment, toolbar, this); }

        attachmentsListView = findViewById(R.id.attachmentsListView);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvCourseCode = findViewById(R.id.tvCourseCode);
        tvCourseVariant = findViewById(R.id.tvCourseVariant);
        tvSessionDate = findViewById(R.id.tvSessionDate);
        tvTimeSlot = findViewById(R.id.tvTimeSlot);
        tvFacultyName = findViewById(R.id.tvFacultyName);
        tvTopics = findViewById(R.id.tvTopics);
        tvDescription = findViewById(R.id.tvDescription);

        llCourseCode = findViewById(R.id.llCourseCode);
        llCourseVariant = findViewById(R.id.llCourseVariant);
        llSessionDate = findViewById(R.id.llSessionDate);
        llTimeSlot = findViewById(R.id.llTimeSlot);
        llFacultyName = findViewById(R.id.llFacultyName);
        llTopics = findViewById(R.id.llTopics);
        llDescription = findViewById(R.id.llDescription);
        llAttachment = findViewById(R.id.llAttachment);

        tvCourseVariant1 = findViewById(R.id.tvCourseVariant1);
        tvSessionDate1 = findViewById(R.id.tvSessionDate1);
        tvFacultyName1 = findViewById(R.id.tvFacultyName1);
        tvTopics1 = findViewById(R.id.tvTopics1);
        tvAttachment1 = findViewById(R.id.tvAttachment1);

        tvCourseVariant1.setText(translationManager.COURSE_VARIANT_KEY);
        tvSessionDate1.setText(translationManager.SESSION_DATE_KEY);
        tvFacultyName1.setText(translationManager.FACULTY_KEY);
        tvTopics1.setText(translationManager.TOPIC_KEY);
        tvAttachment1.setText(translationManager.ATTACHMENTS_KEY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
