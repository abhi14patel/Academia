package com.serosoft.academiassu.Modules.MyRequest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.StatusClass;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.MandatoryDocumentsAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.VoluntaryDocumentsAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Models.ExecutionCertificate_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestDocuments_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequesterDetails_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Widgets.ExpandedListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaveRequestDetailsActivity extends BaseActivity implements AsyncTaskCompleteListener, View.OnClickListener {

    private Context mContext;
    private Toolbar toolbar;
    private RelativeLayout rlRequesterDetails;
    private TextView tvCount,tvRequestStatus,tvRequesterName,tvEnteredBy,tvRequestAssignedTo,tvRequestDate,tvRequestReason,tvFromDateTime,tvToDateTime,tvComment;
    private TextView tvRequesterDetails1,tvEnteredBy1,tvRequestAssignedTo1,tvRequestDate1,tvRequestReason1,tvFromDateTime1,tvToDateTime1;
    private TextView tvMandatory1,tvVoluntary1,tvComment1;
    private TextView tvWithdraw,tvFollowUp,tvApproval,tvExecution;
    private View divider1,divider2;
    private LinearLayout llExecution,llApproval,llFollowup;
    private ExpandedListView lvMandatory,lvVoluntary;
    private CardView cardVoluntary,cardMandatory,cardComment;

    String requestId = "";
    String requestCode = "";
    String status = "";
    String approverName = "";
    int requestStatusId = 0;
    ExecutionCertificate_Dto executionCertificate_dto;

    RequesterDetails_Dto requesterDetails_dto;
    ArrayList<RequestDocuments_Dto> documentList;
    VoluntaryDocumentsAdapter voluntaryDocumentsAdapter;
    MandatoryDocumentsAdapter mandatoryDocumentsAdapter;

    private final String TAG = LeaveRequestDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_request_details_activity);
        ProjectUtils.showLog(TAG,"onCreate start");

        mContext = LeaveRequestDetailsActivity.this;

        Intent intent = getIntent();
        requestId = intent.getStringExtra(Consts.REQUEST_ID);
        requestCode = intent.getStringExtra(Consts.REQUEST_CODE);
        status = intent.getStringExtra(Consts.STATUS);

        Initialize();

        populateContent();

        //Here hide counter
        if(status.equalsIgnoreCase(StatusClass.CLOSED) || status.equalsIgnoreCase(StatusClass.WITHDRAWN) || status.equalsIgnoreCase(StatusClass.REJECTED)){
            tvCount.setVisibility(View.INVISIBLE);
        }
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
        toolbar.setTitle(requestCode.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyRequest, toolbar, this); }

        rlRequesterDetails = findViewById(R.id.rlRequesterDetails);

        tvRequesterName = findViewById(R.id.tvRequesterName);

        tvCount = findViewById(R.id.tvCount);
        tvRequestStatus = findViewById(R.id.tvRequestStatus);
        tvEnteredBy = findViewById(R.id.tvEnteredBy);
        tvRequestAssignedTo = findViewById(R.id.tvRequestAssignedTo);
        tvRequestDate = findViewById(R.id.tvRequestDate);
        tvRequestReason = findViewById(R.id.tvRequestReason);
        tvFromDateTime = findViewById(R.id.tvFromDateTime);
        tvToDateTime = findViewById(R.id.tvToDateTime);
        tvComment = findViewById(R.id.tvComment);

        tvRequesterDetails1 = findViewById(R.id.tvRequesterDetails1);
        tvEnteredBy1 = findViewById(R.id.tvEnteredBy1);
        tvRequestAssignedTo1 = findViewById(R.id.tvRequestAssignedTo1);
        tvRequestDate1 = findViewById(R.id.tvRequestDate1);
        tvRequestReason1 = findViewById(R.id.tvRequestReason1);
        tvFromDateTime1 = findViewById(R.id.tvFromDateTime1);
        tvToDateTime1 = findViewById(R.id.tvToDateTime1);
        tvMandatory1 = findViewById(R.id.tvMandatory1);
        tvVoluntary1 = findViewById(R.id.tvVoluntary1);
        tvComment1 = findViewById(R.id.tvComment1);

        tvWithdraw = findViewById(R.id.tvWithdraw);
        tvFollowUp = findViewById(R.id.tvFollowUp);
        tvApproval = findViewById(R.id.tvApproval);
        tvExecution = findViewById(R.id.tvExecution);

        tvRequesterDetails1.setText(translationManager.REQUESTER_DETAILS_KEY);
        tvEnteredBy1.setText(translationManager.ENTERED_BY_KEY);
        tvRequestAssignedTo1.setText(translationManager.REQUEST_ASSIGNED_TO_KEY);
        tvFollowUp.setText(translationManager.FOLLOW_UP_DETAILS_KEY);
        tvApproval.setText(translationManager.APPROVAL_DETAILS_KEY);
        tvExecution.setText(translationManager.EXECUTION_DETAILS_KEY);

        lvMandatory = findViewById(R.id.lvMandatory);
        lvVoluntary = findViewById(R.id.lvVoluntary);

        cardVoluntary = findViewById(R.id.cardVoluntary);
        cardMandatory = findViewById(R.id.cardMandatory);
        cardComment = findViewById(R.id.cardComment);

        llFollowup = findViewById(R.id.llFollowup);
        llApproval = findViewById(R.id.llApproval);
        llExecution = findViewById(R.id.llExecution);

        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);

        rlRequesterDetails.setOnClickListener(this);
        llFollowup.setOnClickListener(this);
        llApproval.setOnClickListener(this);
        llExecution.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.rlRequesterDetails:
                showRequesterDetailsDialog();
                break;
            case R.id.llFollowup:
                Intent intent = new Intent(mContext,FollowupActivity.class);
                intent.putExtra(Consts.REQUEST_CODE,requestCode);
                intent.putExtra(Consts.REQUEST_ID,requestId);
                intent.putExtra(Consts.STATUS,requestStatusId);
                startActivity(intent);
                break;
            case R.id.llApproval:
                Intent intent1 = new Intent(mContext, ApprovalActivity.class);
                intent1.putExtra(Consts.REQUEST_ID,requestId);
                startActivity(intent1);
                break;
            case R.id.llExecution:
                Intent intent2 = new Intent(mContext, ExecutionActivity.class);
                intent2.putExtra(Consts.APPROVER_NAME,approverName);
                intent2.putExtra(Consts.STATUS,status);
                intent2.putExtra(Consts.LEAVE_TYPE,"LEAVE");
                intent2.putExtra(Consts.REQUEST_ID,requestId);
                intent2.putExtra(Consts.EXECUTION_CERTIFICATE_DTO,executionCertificate_dto);
                startActivity(intent2);
                break;
            case R.id.tvWithdraw:
                ProjectUtils.showDialog2(mContext, getString(R.string.withdraw), "Are you sure you want to withdraw your request?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        withdrawRequest();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                },false);
                break;
        }
    }

    private void withdrawRequest() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_WITHDRAW_REQUEST).execute(requestId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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
            case KEYS.SWITCH_REQUESTER_DETAILS:
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    requesterDetails_dto = new RequesterDetails_Dto();

                    String printName = jsonObject.optString("printName");
                    String mobileNumber = jsonObject.optString("mobileNumber");
                    String emailId = jsonObject.optString("emailId");

                    tvRequesterName.setText(ProjectUtils.getCorrectedString(printName));

                    requesterDetails_dto.setRequeserName(ProjectUtils.getCorrectedString(printName));
                    requesterDetails_dto.setMobileNumber(ProjectUtils.getCorrectedString(mobileNumber));
                    requesterDetails_dto.setEmailId(ProjectUtils.getCorrectedString(emailId));

                    JSONArray jsonArray1 = jsonObject.optJSONArray("admissionCodes");
                    if(jsonArray1 != null && jsonArray1.length() > 0){
                        for (int i = 0 ; i < jsonArray1.length() ; ++i) {
                            String admissionCode = (String) jsonArray1.get(0);
                            requesterDetails_dto.setRequeserName(ProjectUtils.getCorrectedString(printName+"("+admissionCode+")"));
                        }
                    }

                    JSONArray jsonArray2 = jsonObject.optJSONArray("programs");
                    if(jsonArray2 != null && jsonArray2.length() > 0){
                        for (int i = 0 ; i < jsonArray2.length() ; ++i) {
                            String program = (String) jsonArray2.get(0);
                            requesterDetails_dto.setProgram(ProjectUtils.getCorrectedString(program));
                        }
                    }

                    JSONArray jsonArray3 = jsonObject.optJSONArray("batches");
                    if(jsonArray3 != null && jsonArray3.length() > 0){
                        for (int i = 0 ; i < jsonArray3.length() ; ++i) {
                            String batch = (String) jsonArray3.get(0);
                            requesterDetails_dto.setBatch(ProjectUtils.getCorrectedString(batch));
                        }
                    }

                    if (ConnectionDetector.isConnectingToInternet(mContext)) {

                        new OptimizedServerCallAsyncTask(this,
                                this, KEYS.SWITCH_REQUESTER_BASIC_DETAILS).execute(requestId);
                    } else {
                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_REQUESTER_BASIC_DETAILS:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONObject jsonObject1 = jsonObject.optJSONObject("serviceRequestSetting");
                    JSONObject jsonObject2 = jsonObject1.optJSONObject("defaultAssignee");
                    String defaultAssignee = jsonObject2.optString("value");
                    tvRequestAssignedTo.setText(ProjectUtils.getCorrectedString(defaultAssignee));

                    String requesterType = ProjectUtils.capitalize(jsonObject.optString("requesterType"));
                    JSONObject jsonObject3 = jsonObject.optJSONObject("enteredBy");
                    String enteredBy = jsonObject3.optString("value");
                    tvEnteredBy.setText(ProjectUtils.getCorrectedString(enteredBy)+" ("+requesterType+")");

                    long requestDate = jsonObject.optLong("requestDate");
                    String date = ProjectUtils.convertTimestampToDate(requestDate,mContext);
                    tvRequestDate.setText(date);

                    //Here get current day
                    long currentLongDate = System.currentTimeMillis();
                    String currentDate = ProjectUtils.convertTimestampToDate(currentLongDate,mContext);

                    //Here get count between two dates
                    int diffDays = ProjectUtils.getCountOfDays(date,currentDate);
                    tvCount.setText("Pending since - "+diffDays+" day(s)");

                    String remarks = jsonObject.optString("remarks");
                    tvRequestReason.setText(ProjectUtils.getCorrectedString(remarks.trim()));

                    String comment = ProjectUtils.getCorrectedString(jsonObject.optString("comment"));
                    if(!comment.equalsIgnoreCase("")){
                        cardComment.setVisibility(View.VISIBLE);
                        tvComment.setText(comment.trim());
                    }else{
                        cardComment.setVisibility(View.GONE);
                    }

                    String fTime = "";
                    String tTime = "";

                    JSONObject jsonObject4 = jsonObject.optJSONObject("detail");
                    long fromDate = jsonObject4.optLong("fromDate");
                    long toDate = jsonObject4.optLong("toDate");
                    long fromTime = jsonObject4.optLong("fromTime");
                    long toTime = jsonObject4.optLong("toTime");

                    String fDate = ProjectUtils.convertTimestampToDate(fromDate,mContext);
                    if(fromTime != 0){
                        fTime = ProjectUtils.convertTimestampToTime(fromTime,mContext);
                    }
                    tvFromDateTime.setText(fDate+" "+fTime);

                    String tDate = ProjectUtils.convertTimestampToDate(toDate,mContext);
                    if(toTime != 0){
                        tTime = ProjectUtils.convertTimestampToTime(toTime,mContext);
                    }
                    tvToDateTime.setText(tDate+" "+tTime);

                    //Here set Request status
                    requestStatusId = jsonObject.optInt("requestStatusId");
                    ProjectUtils.getRequestStatus(mContext,tvRequestStatus,requestStatusId);

                    //Here show withdrawn button
                    boolean isWithdrawn = jsonObject.optBoolean("isWithdrawn");
                    int requestStatusId = jsonObject.optInt("requestStatusId");
                    if((requestStatusId == 2) && (requesterType.equalsIgnoreCase("Student") || requesterType.equalsIgnoreCase("Parents"))){
                        if(!isWithdrawn){
                            tvWithdraw.setVisibility(View.VISIBLE);
                        }else{
                            tvWithdraw.setVisibility(View.GONE);
                        }
                    }else{
                        tvWithdraw.setVisibility(View.GONE);
                    }

                    //Here set voluntary documents
                    JSONArray jsonArray = jsonObject.optJSONArray("voluntaryDocuments");
                    documentList = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject5 = jsonArray.getJSONObject(i);

                        int id = jsonObject5.optInt("id");
                        String name = jsonObject5.optString("name");

                        documentList.add(new RequestDocuments_Dto(id,name));
                    }

                    if(documentList != null && documentList.size() > 0){
                        cardVoluntary.setVisibility(View.VISIBLE);
                        voluntaryDocumentsAdapter = new VoluntaryDocumentsAdapter(mContext,documentList);
                        lvVoluntary.setAdapter(voluntaryDocumentsAdapter);
                    }else{
                        cardVoluntary.setVisibility(View.GONE);
                    }

                    JSONArray jsonFollowUpArray = jsonObject.optJSONArray("followupDetails");
                    if(jsonFollowUpArray != null && jsonFollowUpArray.length() > 0){
                        llFollowup.setVisibility(View.VISIBLE);
                    }

                    JSONArray jsonApprovalArray = jsonObject.optJSONArray("approvalDetails");
                    if(jsonApprovalArray != null && jsonApprovalArray.length() > 0){

                        JSONObject jsonObject5 = jsonApprovalArray.getJSONObject(0);

                        JSONObject jsonObject6 = jsonObject5.optJSONObject("user");
                        approverName = jsonObject6.optString("value");


                        llApproval.setVisibility(View.VISIBLE);

                        if(status.equalsIgnoreCase(StatusClass.REJECTED)){
                            llExecution.setVisibility(View.GONE);
                        }else{
                            llExecution.setVisibility(View.VISIBLE);
                        }
                    }

                    JSONObject jsonObjExecution = jsonObject.optJSONObject("executionDetail");
                    if(jsonObjExecution != null){
                        int modeId = 0;
                        int closureReasonId = jsonObjExecution.optInt("closureReasonId");
                        String closureRemark = jsonObjExecution.optString("remark");

                        JSONObject handoverMode = jsonObjExecution.optJSONObject("handoverMode");
                        if(handoverMode != null){
                            modeId = handoverMode.optInt("id");
                        }

                        long handoverDate = jsonObjExecution.optLong("handoverDate");

                        executionCertificate_dto = new ExecutionCertificate_Dto();
                        executionCertificate_dto.setClosureReasonId(closureReasonId);
                        executionCertificate_dto.setRemark(closureRemark);
                        executionCertificate_dto.setModeId(modeId);
                        executionCertificate_dto.setHandoverDate(handoverDate);
                    }

                    if((llFollowup.getVisibility() == View.VISIBLE) && (llApproval.getVisibility() == View.VISIBLE) && (llExecution.getVisibility() == View.VISIBLE)){
                        llFollowup.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                        llApproval.setVisibility(View.VISIBLE);
                        divider2.setVisibility(View.VISIBLE);
                        llExecution.setVisibility(View.VISIBLE);
                    }else if((llFollowup.getVisibility() == View.VISIBLE) && (llApproval.getVisibility() == View.VISIBLE)){
                        llFollowup.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                        llApproval.setVisibility(View.VISIBLE);
                    }else if((llApproval.getVisibility() == View.VISIBLE) && (llExecution.getVisibility() == View.VISIBLE)){
                        llApproval.setVisibility(View.VISIBLE);
                        divider2.setVisibility(View.VISIBLE);
                        llExecution.setVisibility(View.VISIBLE);
                    }else if((llFollowup.getVisibility() == View.VISIBLE) && (llExecution.getVisibility() == View.VISIBLE)){
                        llFollowup.setVisibility(View.VISIBLE);
                        divider1.setVisibility(View.VISIBLE);
                        llExecution.setVisibility(View.VISIBLE);
                    }

                    if (ConnectionDetector.isConnectingToInternet(mContext)) {

                        new OptimizedServerCallAsyncTask(this,
                                this, KEYS.SWITCH_MANDETORY_DOCUMENTS).execute(requestId);
                    } else {
                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_MANDETORY_DOCUMENTS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");
                    documentList = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String name = jsonObject1.optString("name");

                        documentList.add(new RequestDocuments_Dto(id,name));
                    }

                    if(documentList != null && documentList.size() > 0){
                        cardMandatory.setVisibility(View.VISIBLE);
                        mandatoryDocumentsAdapter = new MandatoryDocumentsAdapter(mContext,documentList);
                        lvMandatory.setAdapter(mandatoryDocumentsAdapter);
                    }else{
                        cardMandatory.setVisibility(View.GONE);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_WITHDRAW_REQUEST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    if(jsonObject.has("message")){
                        ProjectUtils.showLong(mContext,"Request Withdrawn Successfully");
                        AcademiaApp.IS_UPDATE = true;
                        onBackPressed();
                    }else{
                        showAlertDialog2(LeaveRequestDetailsActivity.this, getString(R.string.oops), getString(R.string.something_went_wrong));
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
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
                populateContent();
                getNotifications();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRequesterDetailsDialog() {

        Dialog alertDialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.leave_requester_details_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        AppCompatImageView ivClose = convertView.findViewById(R.id.ivClose);

        TextView tvRequesterName1 = convertView.findViewById(R.id.tvRequesterName1);
        TextView tvBatch1 = convertView.findViewById(R.id.tvBatch1);
        TextView tvProgram1 = convertView.findViewById(R.id.tvProgram1);
        TextView tvEmailId1 = convertView.findViewById(R.id.tvEmailId1);
        TextView tvMobileNumber1 = convertView.findViewById(R.id.tvMobileNumber1);

        titleTextView.setText(translationManager.REQUESTER_DETAILS_KEY.toUpperCase());
        tvRequesterName1.setText(translationManager.REQUESTER_NAME_KEY);
        tvBatch1.setText(translationManager.BATCH_KEY);
        tvProgram1.setText(translationManager.PROGRAM_KEY);
        tvEmailId1.setText(translationManager.EMAILID_KEY);
        tvMobileNumber1.setText(translationManager.MOBILE_NUMBER_KEY);

        TextView tvRequesterName = convertView.findViewById(R.id.tvRequesterName);
        TextView tvBatch = convertView.findViewById(R.id.tvBatch);
        TextView tvProgram = convertView.findViewById(R.id.tvProgram);
        TextView tvEmailId = convertView.findViewById(R.id.tvEmailId);
        TextView tvMobileNumber = convertView.findViewById(R.id.tvMobileNumber);

        if(requesterDetails_dto != null){
            String requesterName = ProjectUtils.getCorrectedString(requesterDetails_dto.getRequeserName());
            String batch = ProjectUtils.getCorrectedString(requesterDetails_dto.getBatch());
            String program = ProjectUtils.getCorrectedString(requesterDetails_dto.getProgram());
            String emailId = ProjectUtils.getCorrectedString(requesterDetails_dto.getEmailId());
            String mobileNumber = ProjectUtils.getCorrectedString(requesterDetails_dto.getMobileNumber());

            tvRequesterName.setText(requesterName);
            tvBatch.setText(batch);
            tvProgram.setText(program);
            tvEmailId.setText(emailId);
            tvMobileNumber.setText(mobileNumber);
        }

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
