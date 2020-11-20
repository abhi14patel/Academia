package com.serosoft.academiassu.Modules.Assignments.Assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.serosoft.academiassu.Helpers.FilePath;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class AssignmentUploadActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private Button btnBrowse;
    private AppCompatImageView ivAttachment,ivCancel;
    private TextView tvAttachment;
    private Button btnSaved,btnSubmit;
    private LinearLayout llAttachment;
    Uri uri;
    int groupAssignmentId = 0;
    int reSubmitStatus = 0;
    String docFileName = "",path = "",assignmentId = "";

    private static final int PICKFILE_RESULT_CODE = 1;

    private final String TAG = AssignmentUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_upload_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AssignmentUploadActivity.this;

        Initialize();

        Intent intent = getIntent();
        groupAssignmentId = intent.getIntExtra("groupAssignmentId",0);
        assignmentId = intent.getStringExtra("assignmentId");
        reSubmitStatus = intent.getIntExtra("reSubmitStatus",0);

        //Here call description api
        if (ConnectionDetector.isConnectingToInternet(this)){

            showProgressDialog(this);
            String studentId = String.valueOf(sharedPrefrenceManager.getUserIDFromKey());

            new OptimizedServerCallAsyncTask(mContext, AssignmentUploadActivity.this,
                    KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT).execute(assignmentId,studentId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        ivAttachment = findViewById(R.id.ivAttachment);
        ivCancel = findViewById(R.id.ivCancel);
        tvAttachment = findViewById(R.id.tvAttachment);
        llAttachment = findViewById(R.id.llAttachment);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnSaved = findViewById(R.id.btnSaved);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnBrowse.setOnClickListener(this);
        btnSaved.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        toolbar.setTitle(getResources().getString(R.string.submit_assignment).toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAssignment, toolbar, this); }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.btnBrowse:
            {
                if(Build.VERSION.SDK_INT>22)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else
                {
                    try
                    {
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE);

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(mContext, getString(R.string.please_give_permission), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }

            }break;

            case R.id.btnSaved:
            {
                if(llAttachment.getVisibility() == View.VISIBLE){

                    showProgressDialog(this);

                    getSavedFile(path);

                }else{
                    Toast.makeText(mContext,getString(R.string.please_upload_a_file_first),Toast.LENGTH_LONG).show();
                }
            }break;

            case R.id.btnSubmit:
            {
                if(llAttachment.getVisibility() == View.VISIBLE){

                    showProgressDialog(this);

                    getSubmitFile(path);

                }else{
                    Toast.makeText(mContext,getString(R.string.please_upload_a_file_first),Toast.LENGTH_LONG).show();
                }
            }break;

            case R.id.ivCancel:
            {
                llAttachment.setVisibility(View.GONE);
                tvAttachment.setText("");
                path = "";
                uri = null;
            }break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                if (!(grantResults.length > 0))
                {
                    Toast.makeText(mContext, getString(R.string.permission_msg), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE);

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(mContext, getString(R.string.please_give_permission), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void getSavedFile(String path) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.STUDENT_HW_ASSIGNMENT_SAVE_MULTIPLE_DOCUMENT;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", groupAssignmentId);
            jsonObject.put("docType", "SUBMITED_DOC");
            jsonObject.put("submissionStatus", "Drafted");
            jsonObject.put("submissionDate", "");
            jsonObject.put("uploadedBy", "student");

            JSONArray array=new JSONArray();
            JSONObject obj=new JSONObject();
            obj.put("name",docFileName);
            obj.put("path",path);
            obj.put("type","DOCUMENT");
            obj.put("submissionDate","");
            array.put(obj);

            jsonObject.put("documents",array);

            AndroidNetworking.post(url)
                    .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                    .addHeaders("Authorization",str)
                    .addHeaders("Content-Type","application/json")
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            // do anything with response
                            ProjectUtils.showLog("Saved File Response: ", response);
                            hideProgressDialog();
                            ProjectUtils.showLong(mContext,"Document Saved Successfully");
                        }

                        @Override
                        public void onError(ANError anError) {

                            hideProgressDialog();
                            ProjectUtils.showLog(TAG,""+anError.getMessage());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSubmitFile(String path) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.STUDENT_HW_ASSIGNMENT_SUBMIT_DOCUMENT;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        String submissionDate = ProjectUtils.getDateTime();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", groupAssignmentId);
            jsonObject.put("docType", "SUBMITED_DOC");
            if(reSubmitStatus == 1){
                jsonObject.put("submissionStatus", "ReSubmitted");
            }else{
                jsonObject.put("submissionStatus", "Submitted");
            }
            jsonObject.put("submissionDate", submissionDate);
            jsonObject.put("uploadedBy", "student");
            jsonObject.put("resubmissionDate", "");

            JSONArray array=new JSONArray();
            JSONObject obj=new JSONObject();
            obj.put("name",docFileName);
            obj.put("path",path);
            obj.put("type","DOCUMENT");
            obj.put("submissionDate",submissionDate);
            array.put(obj);

            jsonObject.put("documents",array);

            AndroidNetworking.post(url)
                    .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                    .addHeaders("Authorization",str)
                    .addHeaders("Content-Type","application/json")
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            // do anything with response
                            ProjectUtils.showLog("Submit File Response: ", response);
                            hideProgressDialog();
                            ProjectUtils.showLong(mContext,"Document Submitted Successfully");

                            onBackPressed();
                        }

                        @Override
                        public void onError(ANError anError) {

                            hideProgressDialog();
                            ProjectUtils.showLog(TAG,""+anError.getMessage());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case PICKFILE_RESULT_CODE:

                if(resultCode==RESULT_OK){

                    if (data != null)
                    {
                        uri = data.getData();
                    }

                    if(uri != null){

                        try {

                            String fileName = FilePath.getPath(mContext, uri);

                            if(fileName != null && !fileName.equalsIgnoreCase(""))
                            {
                                File file = new File(fileName);

                                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));

                                if(file_size < 20000){

                                    llAttachment.setVisibility(View.VISIBLE);
                                    int index = fileName.lastIndexOf("/");
                                    docFileName = fileName.substring(index + 1);

                                    tvAttachment.setText(docFileName);
                                    ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));

                                    //Here call description api
                                    if (ConnectionDetector.isConnectingToInternet(this)){

                                        showProgressDialog(this);

                                        uploadTemp(file);

                                    } else {
                                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    llAttachment.setVisibility(View.GONE);
                                    Toast.makeText(this, getString(R.string.can_not_upload_document), Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                llAttachment.setVisibility(View.GONE);
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());

                            ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                        }
                    }else{
                        ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                    }
                }
                break;
        }
    }

    private void uploadTemp(File file) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.STUDENT_HW_ASSIGNMENT_UPLOAD_TEMP;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        AndroidNetworking.upload(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addMultipartParameter("access_token",accessToken)
                .addMultipartParameter("id","")
                .addMultipartParameter("module", "homeworkSubmission")
                .addMultipartParameter("entityId", "")
                .addMultipartParameter("entityType","homeworkSubmission")
                .addMultipartParameter("name","ISDA")
                .addMultipartFile("file", file)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        // do anything with response
                        ProjectUtils.showLog("Upload Temp Response: ", response.toString());
                        hideProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            path = jsonObject.optString("path");

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        hideProgressDialog();
                        ProjectUtils.showLog(TAG,""+error.getMessage());
                    }
                });
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
            case KEYS.SWITCH_HOMEWORK_SAVED_DOCUMENT:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        JSONArray jsonArray1 = jsonObject1.optJSONArray("documents");

                        if(jsonArray1 != null && jsonArray1.length() > 0){

                            llAttachment.setVisibility(View.VISIBLE);

                            for(int j = 0 ; j<jsonArray1.length() ; j++){

                                JSONObject jsonObject2 = jsonArray1.optJSONObject(j);

                                docFileName = jsonObject2.optString("name");
                                path = jsonObject2.optString("path");

                                tvAttachment.setText(docFileName);
                                ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));
                            }
                        }
                        else{
                            llAttachment.setVisibility(View.GONE);
                        }

                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;
        }
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
