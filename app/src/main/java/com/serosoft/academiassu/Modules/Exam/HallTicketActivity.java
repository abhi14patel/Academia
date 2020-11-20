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
import com.serosoft.academiassu.Modules.Exam.Adapters.HallTicketAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.HallTicket_Dto;
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

public class HallTicketActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    String hallTicketId = "",hallTicketPath = "";

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private ArrayList<HallTicket_Dto> hallTicketList;
    HallTicketAdapter hallTicketAdapter;

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = HallTicketActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_hall_ticket_list);

        ProjectUtils.showLog(TAG,"onCreate");
        mContext = HallTicketActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_HALLTICKET).execute();
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
            case KEYS.SWITCH_STUDENT_HALLTICKET:
                hideProgressDialog();
                JSONArray arr = new JSONArray();

                try {
                    JSONObject responseObject = new JSONObject(responseResult.toString());

                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever");

                        if (arr != null && arr.length() > 0) {

                            checkEmpty(false);
                            hallTicketList = new ArrayList<>();

                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject jsonObject1 = arr.optJSONObject(i);

                                int totalOutstanding = jsonObject1.optInt("totalOutstanding");
                                String programName = jsonObject1.optString("programName");
                                String batchName = jsonObject1.optString("batchName");
                                String periodName = jsonObject1.optString("periodName");
                                String evaluationGroupCode = jsonObject1.optString("evaluationGroupCode");
                                String hallticket = jsonObject1.optString("hallticket");
                                String hallTicketPath = jsonObject1.optString("hallTicketPath");
                                String currencyCode = jsonObject1.optString("currencyCode");
                                boolean showAmount = jsonObject1.optBoolean("showAmount");

                                HallTicket_Dto hallTicket_dto = new HallTicket_Dto(totalOutstanding,showAmount,programName,batchName,periodName,evaluationGroupCode,hallticket,hallTicketPath,currencyCode);
                                hallTicketList.add(hallTicket_dto);
                            }

                            hallTicketAdapter = new HallTicketAdapter(mContext, hallTicketList);
                            recyclerView.setAdapter(hallTicketAdapter);
                            hallTicketAdapter.notifyDataSetChanged();

                            hallTicketAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    //Handle double tap
                                    ProjectUtils.preventTwoClick(view);

                                    HallTicket_Dto list = hallTicketList.get(position);
                                    downloadHallTicket(list);
                                }
                            });

                        } else {
                            checkEmpty(true);
                        }
                    }else {
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
                        intent.putExtra("screenName",translationManager.HALLTICKET_KEY);
                        intent.putExtra("idForDocument", hallTicketId);
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

    public void downloadHallTicket(HallTicket_Dto hallTicket_dto) {

        hallTicketPath = hallTicket_dto.getHallTicketPath();
        hallTicketId = hallTicket_dto.getHallticket();

        if (ProjectUtils.hasPermissionInManifest(HallTicketActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext, HallTicketActivity.this,
                    KEYS.SWITCH_STUDENT_MARKSHEETS_DOWNLOAD).execute(hallTicketPath, Consts.RESULT);
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

                            if (ProjectUtils.hasPermissionInManifest(HallTicketActivity.this,PERMISSION_CODE,permission))
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

        toolbar.setTitle(translationManager.HALLTICKET_KEY.toUpperCase());
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
}
