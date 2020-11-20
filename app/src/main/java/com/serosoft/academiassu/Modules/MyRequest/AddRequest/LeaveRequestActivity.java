package com.serosoft.academiassu.Modules.MyRequest.AddRequest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LeaveRequestActivity extends BaseActivity implements AsyncTaskCompleteListener, View.OnClickListener {

    private Context mContext;
    private Toolbar toolbar;

    private TextView tvRequesterName1,tvBatch1,tvProgram1,tvEmailId1,tvMobileNumber1,tvRequesterDetails1;
    private TextView tvRequesterName,tvBatch,tvProgram,tvEmailId,tvMobileNumber;
    private TextView tvBasicDetails1,tvEnteredBy1,tvRequestAssignedTo1,tvRequestBy1,tvRequestDate1,tvRequestReason1,tvFromDate1,tvFromTime1,tvToDate1,tvToTime1;
    private TextView tvEnteredBy,tvRequestAssignedTo,tvRequestBy,tvRequestDate,tvFromDate,tvFromTime,tvToDate,tvToTime;
    private TextView tvAddVolDoc,tvAddManDoc;
    private EditText etRemark,etDocumentName,etDocumentPath;
    private ImageView ivUpload;
    private Button btnSubmit;

    private final String TAG = LeaveRequestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_request_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = LeaveRequestActivity.this;
        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);

        Initialize();

        populateContent();
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_REQUESTER_DETAILS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle("LEAVE REQUEST");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyRequest, toolbar, this); }

        tvRequesterDetails1 = findViewById(R.id.tvRequesterDetails1);
        tvRequesterName1 = findViewById(R.id.tvRequesterName1);
        tvProgram1 = findViewById(R.id.tvProgram1);
        tvEmailId1 = findViewById(R.id.tvEmailId1);
        tvBatch1 = findViewById(R.id.tvBatch1);
        tvMobileNumber1 = findViewById(R.id.tvMobileNumber1);
        tvRequesterName = findViewById(R.id.tvRequesterName);
        tvProgram = findViewById(R.id.tvProgram);
        tvBatch = findViewById(R.id.tvBatch);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);

        tvBasicDetails1 = findViewById(R.id.tvBasicDetails1);
        tvEnteredBy1 = findViewById(R.id.tvEnteredBy1);
        tvRequestAssignedTo1 = findViewById(R.id.tvRequestAssignedTo1);
        tvRequestBy1 = findViewById(R.id.tvRequestBy1);
        tvRequestDate1 = findViewById(R.id.tvRequestDate1);
        tvRequestReason1 = findViewById(R.id.tvRequestReason1);
        tvFromDate1 = findViewById(R.id.tvFromDate1);
        tvFromTime1 = findViewById(R.id.tvFromTime1);
        tvToDate1 = findViewById(R.id.tvToDate1);
        tvToTime1 = findViewById(R.id.tvToTime1);

        tvEnteredBy = findViewById(R.id.tvEnteredBy);
        tvRequestAssignedTo = findViewById(R.id.tvRequestAssignedTo);
        tvRequestBy = findViewById(R.id.tvRequestBy);
        tvRequestDate = findViewById(R.id.tvRequestDate);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvFromTime = findViewById(R.id.tvFromTime);
        tvToDate = findViewById(R.id.tvToDate);
        tvToTime = findViewById(R.id.tvToTime);

        tvAddVolDoc = findViewById(R.id.tvAddVolDoc);
        tvAddManDoc = findViewById(R.id.tvAddManDoc);
        ivUpload = findViewById(R.id.ivUpload);
        etDocumentName = findViewById(R.id.etDocumentName);
        etRemark = findViewById(R.id.etRemark);
        etDocumentPath = findViewById(R.id.etDocumentPath);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id)
        {
            case R.id.tvFromDate:
                break;

            case R.id.tvToDate:
                break;

            case R.id.tvFromTime:
                break;

            case R.id.tvToTime:
                break;

            case R.id.tvAddManDoc:
                break;

            case R.id.tvAddVolDoc:
                break;

            case R.id.ivUpload:
                break;

            case R.id.btnSubmit:
                break;
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
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_REQUESTER_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    String printName = jsonObject.optString("printName");
                    String mobileNumber = jsonObject.optString("mobileNumber");
                    String emailId = jsonObject.optString("emailId");

                    tvMobileNumber.setText(ProjectUtils.getCorrectedString(mobileNumber));
                    tvEmailId.setText(ProjectUtils.getCorrectedString(emailId));
                    tvRequesterName.setText(ProjectUtils.getCorrectedString(printName));

                    JSONArray jsonArray1 = jsonObject.optJSONArray("admissionCodes");
                    if(jsonArray1 != null && jsonArray1.length() > 0){
                        for (int i = 0 ; i < jsonArray1.length() ; ++i) {
                            String admissionCode = (String) jsonArray1.get(0);
                            tvRequesterName.setText(ProjectUtils.getCorrectedString(printName+"("+admissionCode+")"));
                        }
                    }

                    JSONArray jsonArray2 = jsonObject.optJSONArray("programs");
                    if(jsonArray2 != null && jsonArray2.length() > 0){
                        for (int i = 0 ; i < jsonArray2.length() ; ++i) {
                            String program = (String) jsonArray2.get(0);
                            tvProgram.setText(ProjectUtils.getCorrectedString(program));
                        }
                    }

                    JSONArray jsonArray3 = jsonObject.optJSONArray("batches");
                    if(jsonArray3 != null && jsonArray3.length() > 0){
                        for (int i = 0 ; i < jsonArray3.length() ; ++i) {
                            String batch = (String) jsonArray3.get(0);
                            tvBatch.setText(ProjectUtils.getCorrectedString(batch));
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
}
