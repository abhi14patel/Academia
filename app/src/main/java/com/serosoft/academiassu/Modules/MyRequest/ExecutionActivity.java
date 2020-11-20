package com.serosoft.academiassu.Modules.MyRequest;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.StatusClass;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.ExecutionAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Models.ExecutionCertificate_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.Execution_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ExecutionActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private Spinner spnModeSubmission;
    private Spinner spnClosureReason;
    private Spinner spnCertificateName;
    private TextView tvDate,tvUserName;
    private EditText etRemark;
    private Button btnPrint;
    private TextView tvModeSubmission1,tvDate1,tvClosureReason1,tvClosureRemark1;

    String status = "";
    String approverName = "";
    String leaveType = "";
    String requestId = "";
    String certificateId = "";

    ArrayList<Execution_Dto> executionList;
    ExecutionAdapter executionAdapter;

    Calendar calendarDefault = null;
    private int mYear, mMonth, mDay;
    Long date1,date2;
    String endDate = "";

    ExecutionCertificate_Dto executionCertificate_dto;
    int modeId = 0,closureId = 0;
    int modeId1 = 0,closureId1 = 0;

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = ExecutionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_execution_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = ExecutionActivity.this;

        Intent intent = getIntent();
        if(intent.hasExtra(Consts.STATUS)){
            status = intent.getStringExtra(Consts.STATUS);
        }
        if(intent.hasExtra(Consts.APPROVER_NAME)){
            approverName = intent.getStringExtra(Consts.APPROVER_NAME);
        }
        if(intent.hasExtra(Consts.LEAVE_TYPE)){
            leaveType = intent.getStringExtra(Consts.LEAVE_TYPE);
        }
        if(intent.hasExtra(Consts.REQUEST_ID)){
            requestId = intent.getStringExtra(Consts.REQUEST_ID);
        }
        if(intent.hasExtra(Consts.EXECUTION_CERTIFICATE_DTO)){
            executionCertificate_dto = (ExecutionCertificate_Dto) intent.getSerializableExtra(Consts.EXECUTION_CERTIFICATE_DTO);
        }

        Initialize();

        populateContent();

        disableViews();
    }

    private void disableViews() {
        if(status.equalsIgnoreCase(StatusClass.CLOSED)){
            ProjectUtils.disableSpinner(spnModeSubmission);
            ProjectUtils.disableSpinner(spnClosureReason);
            tvDate.setEnabled(false);
            etRemark.setEnabled(false);
        }
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_EXECUTION_HANDOVER_MODE).execute();

        }else{
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateContentClosure() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_EXECUTION_CLOSURE_REASON).execute();

        }else{
            hideProgressDialog();
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCertificateName() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_EXECUTION_PRINT_CERTIFICATE).execute(leaveType);

        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateDownloadCertificate(){

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE).execute(requestId,certificateId);

        }else{
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.tvDate:
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                date1 = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                endDate = ProjectUtils.convertTimestampToDate(date1,mContext);

                                tvDate.setText(endDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(date2);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;

            case R.id.btnPrint:
            {
                if(spnModeSubmission.getSelectedItemPosition() == 0) {

                    ProjectUtils.showLong(mContext,"Please select "+translationManager.MODE_OF_SUBMISSION_KEY);
                }
                else if(tvDate.getText().toString().trim().length() == 0){

                    ProjectUtils.showLong(mContext,"Please select "+translationManager.CERTIFICATE_HANDOVER_DATE_KEY);
                }
                else if(spnClosureReason.getSelectedItemPosition() == 0) {

                    ProjectUtils.showLong(mContext,"Please select "+translationManager.CLOSURE_REASON_KEY);
                }
                else if(etRemark.getText().toString().trim().length() == 0){

                    ProjectUtils.showLong(mContext,"Please enter "+translationManager.CLOSURE_REMARK_KEY);
                }
                else{
                    showCertificateDialog();
                }
            }break;
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        spnModeSubmission = findViewById(R.id.spnModeSubmission);
        spnClosureReason = findViewById(R.id.spnClosureReason);
        tvDate = findViewById(R.id.tvDate);
        tvUserName = findViewById(R.id.tvUserName);
        etRemark = findViewById(R.id.etRemark);
        btnPrint = findViewById(R.id.btnPrint);

        tvModeSubmission1 = findViewById(R.id.tvModeSubmission1);
        tvDate1 = findViewById(R.id.tvDate1);
        tvClosureReason1 = findViewById(R.id.tvClosureReason1);
        tvClosureRemark1 = findViewById(R.id.tvClosureRemark1);

        tvModeSubmission1.setText(translationManager.MODE_OF_SUBMISSION_KEY);
        tvDate1.setText(translationManager.CERTIFICATE_HANDOVER_DATE_KEY);
        tvClosureReason1.setText(translationManager.CLOSURE_REASON_KEY);
        tvClosureRemark1.setText(translationManager.CLOSURE_REMARK_KEY);
        btnPrint.setText(translationManager.DOWNLOAD_CERTIFICATE_KEY);

        tvDate.setOnClickListener(this);
        btnPrint.setOnClickListener(this);

        toolbar.setTitle(translationManager.EXECUTION_DETAILS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyRequest, toolbar, this); }

        //Here set current from and to date
        calendarDefault = Calendar.getInstance();
        mYear = calendarDefault.get(Calendar.YEAR);
        mMonth = calendarDefault.get(Calendar.MONTH);
        mDay = calendarDefault.get(Calendar.DAY_OF_MONTH);

        date2 = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);

        tvUserName.setText(mContext.getString(R.string.user_name)+" : "+approverName);

        if(executionCertificate_dto != null){
            etRemark.setText(executionCertificate_dto.getRemark());
            String endDate = ProjectUtils.convertTimestampToDate(executionCertificate_dto.getHandoverDate(),mContext);
            tvDate.setText(endDate);

            modeId = executionCertificate_dto.getModeId();
            closureId = executionCertificate_dto.getClosureReasonId();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor)
        {
            case KEYS.SWITCH_EXECUTION_HANDOVER_MODE:

                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray jsonArray = responseObject.optJSONArray("whatever");

                    executionList = new ArrayList<>();
                    executionList.add(new Execution_Dto(0,"Select"));
                    for(int i = 0 ; i< jsonArray.length() ;i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        int id = jsonObject.optInt("id");
                        String value = jsonObject.optString("value");
                        executionList.add(new Execution_Dto(id,value));
                    }

                    if(executionList != null && executionList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< executionList.size() ; i++){

                            Execution_Dto execution_dto = executionList.get(i);

                            if(execution_dto.getId() == modeId)
                            {
                                modeId1 = i;
                            }
                        }

                        executionAdapter = new ExecutionAdapter(mContext,executionList){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position
                                if (position == modeId1) {
                                    v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                }
                                else {
                                    // for other views
                                    v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                }
                                return v;
                            }
                        };

                        spnModeSubmission.setAdapter(executionAdapter);
                        spnModeSubmission.setSelection(modeId1);
                    }

                    disableViews();

                    populateContentClosure();

                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.disableSpinner(spnModeSubmission);
                }
                break;

            case KEYS.SWITCH_EXECUTION_CLOSURE_REASON:

                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray jsonArray = responseObject.optJSONArray("whatever");

                    executionList = new ArrayList<>();
                    executionList.add(new Execution_Dto(0,"Select"));
                    for(int i = 0 ; i< jsonArray.length() ;i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        int id = jsonObject.optInt("id");
                        String value = jsonObject.optString("value");
                        executionList.add(new Execution_Dto(id,value));
                    }

                    if(executionList != null && executionList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< executionList.size() ; i++){

                            Execution_Dto execution_dto = executionList.get(i);

                            if(execution_dto.getId() == closureId)
                            {
                                closureId1 = i;
                            }
                        }

                        executionAdapter = new ExecutionAdapter(mContext,executionList){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position
                                if (position == closureId1) {
                                    v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                }
                                else {
                                    // for other views
                                    v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                }
                                return v;
                            }
                        };

                        spnClosureReason.setAdapter(executionAdapter);
                        spnClosureReason.setSelection(closureId1);
                    }

                    disableViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                    ProjectUtils.disableSpinner(spnClosureReason);
                }
                break;

            case KEYS.SWITCH_EXECUTION_PRINT_CERTIFICATE:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray jsonArray = responseObject.optJSONArray("whatever");

                    executionList = new ArrayList<>();
                    executionList.add(new Execution_Dto(-1,"Select"));

                    for(int i = 0 ; i< jsonArray.length() ;i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        int id = jsonObject.optInt("id");
                        String value = jsonObject.optString("value");
                        executionList.add(new Execution_Dto(id,value));
                    }

                    executionAdapter = new ExecutionAdapter(this,executionList);
                    spnCertificateName.setAdapter(executionAdapter);
                    spnCertificateName.setEnabled(true);

                }catch (Exception ex){
                    ex.printStackTrace();
                    spnCertificateName.setEnabled(false);
                }
                break;

            case KEYS.SWITCH_EXECUTION_DOWNLOAD_CERTIFICATE:
                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String message = jsonObject.optString("message");
                    if (downloaded)
                    {
                        String path = jsonObject.optString("path");
                        Intent intent=new Intent(this, PDFActivity2.class);
                        intent.putExtra("filePath",path);
                        intent.putExtra("screenName","Certificate");;
                        intent.putExtra("folderName", Consts.MY_REQUEST);
                        intent.putExtra("idForDocument",requestId);
                        intent.putExtra("headerColor",R.color.colorMyRequest);
                        startActivity(intent);
                    }else{
                        ProjectUtils.showLong(ExecutionActivity.this,message);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    showAlertDialog(mContext,getString(R.string.oops),getString(R.string.something_went_wrong));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showCertificateDialog() {

        Dialog alertDialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.leave_execution_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        populateCertificateName();

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        AppCompatImageView ivClose = convertView.findViewById(R.id.ivClose);

        TextView tvCertificateName1 = convertView.findViewById(R.id.tvCertificateName1);
        spnCertificateName = convertView.findViewById(R.id.spnCertificateName);
        Button btnDownload = convertView.findViewById(R.id.btnDownload);

        titleTextView.setText(translationManager.DOWNLOAD_CERTIFICATE_KEY);
        tvCertificateName1.setText(translationManager.CERTIFICATE_NAME_KEY);

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        spnCertificateName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Execution_Dto execution_dto = executionList.get(position);
                certificateId = String.valueOf(execution_dto.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spnCertificateName.getSelectedItemPosition() == 0) {

                    ProjectUtils.showLong(mContext,"Please select "+translationManager.CERTIFICATE_NAME_KEY);
                }
                else{
                    alertDialog.dismiss();

                    if (ProjectUtils.hasPermissionInManifest(ExecutionActivity.this,PERMISSION_CODE,permission))
                    {
                        populateDownloadCertificate();
                    }
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
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

                            if (ProjectUtils.hasPermissionInManifest(ExecutionActivity.this,PERMISSION_CODE,permission))
                            {
                                populateDownloadCertificate();
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },false);
                }

                if(hasAllPermissions) populateDownloadCertificate();

                break;
        }
    }
}
