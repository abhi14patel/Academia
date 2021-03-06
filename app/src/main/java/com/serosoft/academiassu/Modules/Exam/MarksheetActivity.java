package com.serosoft.academiassu.Modules.Exam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Exam.Adapters.MarksheetAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.Marksheet_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MarksheetActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String marksheetId = "",marksheetPath = "";

    private ArrayList<Marksheet_Dto> marksheetList;
    MarksheetAdapter marksheetAdapter;

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = MarksheetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_marksheet_list);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = MarksheetActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_MARKSHEETS).execute();
        } else {
            showAlertDialog(this, getString(R.string.network_error), getString(R.string.please_check_your_network_connection));
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
            return;
        }

        switch (callFor) {
            case KEYS.SWITCH_STUDENT_MARKSHEETS:
                hideProgressDialog();
                JSONArray arr = new JSONArray();
                try {
                    JSONObject responseObject = new JSONObject(responseResult.toString());
                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever");

                        if(arr != null && arr.length() > 0){

                            checkEmpty(false);
                            marksheetList = new ArrayList<>();

                            for(int i = 0 ; i< arr.length() ; i++) {

                                JSONObject jsonObject1 = arr.optJSONObject(i);

                                int id = jsonObject1.optInt("id");
                                String programName = jsonObject1.optString("programName");
                                String sectionName = jsonObject1.optString("sectionName");
                                String batchName = jsonObject1.optString("batchName");
                                String periodName = jsonObject1.optString("periodName");
                                String marksheetPath = jsonObject1.optString("marksheetPath");

                                Marksheet_Dto marksheet_dto = new Marksheet_Dto(id,programName,sectionName,batchName,periodName,marksheetPath);
                                marksheetList.add(marksheet_dto);
                            }

                            marksheetAdapter = new MarksheetAdapter(mContext,marksheetList);
                            recyclerView.setAdapter(marksheetAdapter);
                            marksheetAdapter.notifyDataSetChanged();

                            marksheetAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    //Handle double tap
                                    ProjectUtils.preventTwoClick(view);

                                    Marksheet_Dto list = marksheetList.get(position);
                                    downloadMarksheet(list);
                                }
                            });

                        }else{
                            checkEmpty(true);
                        }
                    }else{
                        checkEmpty(true);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String message = jsonObject.optString("message");

                    if (downloaded)
                    {
                        String path = jsonObject.optString("path");
                        Intent intent=new Intent(mContext, PDFActivity2.class);
                        intent.putExtra("filePath",path);
                        intent.putExtra("folderName",Consts.RESULT);
                        intent.putExtra("screenName",translationManager.MARKSHEET_KEY);
                        intent.putExtra("idForDocument", marksheetId);
                        intent.putExtra("headerColor",R.color.colorExamMark);
                        startActivity(intent);
                    }else{
                        ProjectUtils.showLong(mContext,message);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }

    public void downloadMarksheet(Marksheet_Dto marksheet_dto) {

        marksheetPath = marksheet_dto.getMarksheetPath();
        marksheetId = String.valueOf(marksheet_dto.getId());

        if (ProjectUtils.hasPermissionInManifest(MarksheetActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(MarksheetActivity.this, MarksheetActivity.this,
                    KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD).execute(marksheetPath, Consts.RESULT);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
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

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.permission_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (ProjectUtils.hasPermissionInManifest(MarksheetActivity.this,PERMISSION_CODE,permission))
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

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.dashboardMenu:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                populateContents();
                getNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar.setTitle(translationManager.MARKSHEET_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorExamMark, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
