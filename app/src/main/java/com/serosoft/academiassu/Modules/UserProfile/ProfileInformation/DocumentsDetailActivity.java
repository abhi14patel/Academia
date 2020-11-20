package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.Document_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class DocumentsDetailActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;

    Document_Dto document_dto;

    private LinearLayout llInspectionDate,llStatus,llProgram,llSeatType,llRemark;
    private TextView tvDocumentName1,tvSubmissionDate1,tvInspectionDate1,tvStatus1,tvProgram1,tvSeatType1,tvRemark1,tvAttachment1;
    private TextView tvDocumentName,tvSubmissionDate,tvInspectionDate,tvStatus,tvProgram,tvSeatType,tvRemark,tvAttachment;
    private AppCompatImageView ivAttachment;
    private LinearLayout llAttachment;

    String docFileName = "",documentId = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = DocumentsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents_detail_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = DocumentsDetailActivity.this;

        Initialize();

        Intent intent = getIntent();
        document_dto = (Document_Dto) intent.getSerializableExtra(Consts.DOCUMENT_LIST);

        setData(document_dto);
    }

    private void setData(Document_Dto document_dto) {

        boolean isPreDefined = document_dto.isPreDefined();

        //Here check isPredefined and get value for document name
        if(isPreDefined){
            String name = ProjectUtils.getCorrectedString(document_dto.getValue());
            if(!name.equalsIgnoreCase("")) {

                tvDocumentName.setText(name);
            }else{
                tvDocumentName.setText(" - ");
            }
        }else{
            String name = ProjectUtils.getCorrectedString(document_dto.getDocumentName());
            if(!name.equalsIgnoreCase("")) {

                tvDocumentName.setText(name);
            }else{
                tvDocumentName.setText(" - ");
            }
        }

        long submissionDate = document_dto.getSubmissionDate();
        String date = ProjectUtils.convertTimestampToDate(submissionDate,mContext);

        if(!date.equalsIgnoreCase(""))
        {
            tvSubmissionDate.setText(date);
        }else{
            tvSubmissionDate.setText(" - ");
        }

        long inspectionDate = document_dto.getInspectionDate();
        if(inspectionDate != 0){
            String rtnDate = ProjectUtils.convertTimestampToDate(inspectionDate,mContext);
            if(!rtnDate.equalsIgnoreCase(""))
            {
                llInspectionDate.setVisibility(View.VISIBLE);
                tvInspectionDate.setText(rtnDate);
            }
        }else{
            llInspectionDate.setVisibility(View.GONE);
            tvInspectionDate.setText(" - ");
        }

        String status = ProjectUtils.getCorrectedString(document_dto.getStatus());
        if(!status.equalsIgnoreCase("")){
            llStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(ProjectUtils.capitalize(status));
        }else{
            llStatus.setVisibility(View.GONE);
            tvStatus.setText(" - ");
        }

        String program = ProjectUtils.getCorrectedString(document_dto.getProgramName());
        if(!program.equalsIgnoreCase("")){

            llProgram.setVisibility(View.VISIBLE);
            tvProgram.setText(program);
        }else{
            llProgram.setVisibility(View.GONE);
            tvProgram.setText(" - ");
        }

        String seatType = ProjectUtils.getCorrectedString(document_dto.getSeatType());
        if(!seatType.equalsIgnoreCase("")){

            llSeatType.setVisibility(View.VISIBLE);
            tvSeatType.setText(seatType);
        }else{
            llSeatType.setVisibility(View.GONE);
            tvSeatType.setText(" - ");
        }

        String remark = ProjectUtils.getCorrectedString(document_dto.getRemark());
        if(!remark.equalsIgnoreCase("")){

            llRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(remark);
        }else{
            llRemark.setVisibility(View.GONE);
            tvRemark.setText(" - ");
        }

        String pathName = ProjectUtils.getCorrectedString(document_dto.getDocPath());
        if(!pathName.equalsIgnoreCase(""))
        {
            int index = pathName.lastIndexOf("/");
            docFileName = pathName.substring(index + 1);

            llAttachment.setVisibility(View.VISIBLE);
            tvAttachment.setText(docFileName);
            ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));

            llAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ProjectUtils.hasPermissionInManifest(DocumentsDetailActivity.this,PERMISSION_CODE,permission))
                    {
                        callAPIWithPermission();
                    }
                }
            });

        }else{
            llAttachment.setVisibility(View.GONE);
        }
    }

    private void callAPIWithPermission() {

        documentId = String.valueOf(document_dto.getDocumentId());

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(DocumentsDetailActivity.this, DocumentsDetailActivity.this,
                KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD).execute(documentId, docFileName, Consts.DOCUMENTS);
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

                            if (ProjectUtils.hasPermissionInManifest(DocumentsDetailActivity.this,PERMISSION_CODE,permission))
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

    private void Initialize()
    {
        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle("DOCUMENT DETAILS");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        llInspectionDate = findViewById(R.id.llInspectionDate);
        llStatus = findViewById(R.id.llStatus);
        llProgram = findViewById(R.id.llProgram);
        llSeatType = findViewById(R.id.llSeatType);
        llRemark = findViewById(R.id.llRemark);
        llAttachment = findViewById(R.id.llAttachment);

        tvDocumentName1 = findViewById(R.id.tvDocumentName1);
        tvSubmissionDate1 = findViewById(R.id.tvSubmissionDate1);
        tvInspectionDate1 = findViewById(R.id.tvInspectionDate1);
        tvStatus1 = findViewById(R.id.tvStatus1);
        tvProgram1 = findViewById(R.id.tvProgram1);
        tvSeatType1 = findViewById(R.id.tvSeatType1);
        tvRemark1 = findViewById(R.id.tvRemark1);
        tvAttachment1 = findViewById(R.id.tvAttachment1);

        ivAttachment = findViewById(R.id.ivAttachment);
        tvAttachment = findViewById(R.id.tvAttachment);
        tvDocumentName = findViewById(R.id.tvDocumentName);
        tvSubmissionDate = findViewById(R.id.tvSubmissionDate);
        tvInspectionDate = findViewById(R.id.tvInspectionDate);
        tvStatus = findViewById(R.id.tvStatus);
        tvProgram = findViewById(R.id.tvProgram);
        tvSeatType = findViewById(R.id.tvSeatType);
        tvRemark = findViewById(R.id.tvRemark);

        tvDocumentName1.setText(translationManager.DOCUMENT_NAME_KEY);
        tvSubmissionDate1.setText(translationManager.SUBMISSION_DATE_KEY);
        tvInspectionDate1.setText(translationManager.INSPECTION_DATE_KEY);
        tvStatus1.setText(translationManager.STATUS_KEY);
        tvProgram1.setText(translationManager.PROGRAM_KEY);
        tvSeatType1.setText(translationManager.SEAT_TYPE_KEY);
        tvRemark1.setText(translationManager.REMARK_KEY);
        tvAttachment1.setText(translationManager.ATTACHMENT_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                onBackPressed();
            }break;

            case R.id.dashboardMenu:
            {
                onBackPressed();
            }break;

            case R.id.refresh:
            {
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
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
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String path = jsonObject.optString("path");
                    String extension = FilenameUtils.getExtension(docFileName).toUpperCase();
                    String message = jsonObject.optString("message");

                    if (extension.equalsIgnoreCase("PDF"))
                    {
                        if (downloaded)
                        {
                            Intent intent = new Intent(mContext, PDFActivity2.class);
                            intent.putExtra("filePath", path);
                            intent.putExtra("folderName", Consts.DOCUMENTS);
                            intent.putExtra("screenName", Consts.DOCUMENTS);
                            intent.putExtra("idForDocument", documentId);
                            intent.putExtra("headerColor", R.color.colorPrimary);
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
