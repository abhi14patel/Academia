package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.serosoft.academiassu.Helpers.AcademiaApp;

import com.serosoft.academiassu.Helpers.FilePath;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.DocumentTypeAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.DocumentType_Dto;
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
import java.util.ArrayList;
import java.util.HashMap;

public class DocumentUploadActivity extends BaseActivity {

    private Context mContext;
    private TextView tvTitle;
    private AppCompatImageView ivClose,ivCancel,ivAttachment;
    private LinearLayout llAttachment,llSelectFile;

    private RadioGroup radio_group;
    private RadioButton radioPredefined,radioOthers;

    private TextView tvDocumentType1,tvSubmissionDate1,tvRemark1;
    private Spinner spnDocumentType;
    private TextView tvSubmissionDate,tvAttachment;
    private EditText etDocumentType,etRemark;
    private Button btnBrowse,btnSubmit;

    ArrayList<DocumentType_Dto> documentTypeList;
    DocumentTypeAdapter documentTypedapter;

    ArrayList<Integer> list = new ArrayList<>();
    int PREDEFINED_VIEW = 0, OTHERS_VIEW = 0;

    File filePath;
    Uri uri;
    String docFileName = "",path = "";
    private static final int PICKFILE_RESULT_CODE = 1;

    boolean docOption = true;
    int documentTypeId = 0;

    private final String TAG = DocumentUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_upload_activity);
        ProjectUtils.showLog(TAG,"onCreateView Start");

        mContext = DocumentUploadActivity.this;

        Initialize();

        viewEnableDisable(true);

        if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){

            radioPredefined.setVisibility(View.VISIBLE);
            radioOthers.setVisibility(View.VISIBLE);

        }else{
            setModuleWithPermission();
        }

        long longDate= System.currentTimeMillis();
        String date = ProjectUtils.convertTimestampToDate(longDate,mContext);
        tvSubmissionDate.setText(date);

        //Here call description api
        if (ConnectionDetector.isConnectingToInternet(this)){

            showProgressDialog(this);

            new OptimizedServerCallAsyncTask(mContext, DocumentUploadActivity.this,
                    KEYS.SWITCH_DOCUMENT_TYPE).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void setModuleWithPermission() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        PREDEFINED_VIEW = 0;
        OTHERS_VIEW = 0;

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_PROFILE_PREDEFINED_DOC_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_PREDEFINED_DOC_VIEW:
                    PREDEFINED_VIEW = 1;
                    break;
                case Permissions.PARENT_PERMISSION_PROFILE_OTHERDOCUMENTS_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_OTHERDOCUMENTS_VIEW:
                    OTHERS_VIEW = 1;
                    break;
            }
        }

        if(PREDEFINED_VIEW != 0 && OTHERS_VIEW != 0)
        {
            radioPredefined.setVisibility(View.VISIBLE);
            radioOthers.setVisibility(View.VISIBLE);
        }
        else if (PREDEFINED_VIEW != 0)
        {
            radioPredefined.setVisibility(View.VISIBLE);
            radioOthers.setVisibility(View.GONE);
        }
        else if (OTHERS_VIEW != 0)
        {
            radioPredefined.setVisibility(View.GONE);
            radioOthers.setVisibility(View.VISIBLE);
            radioOthers.setChecked(true);
        }else{
            radioOthers.setVisibility(View.GONE);
        }
    }

    private void Initialize() {

        radio_group = findViewById(R.id.radio_group);
        radioPredefined = findViewById(R.id.radioPredefined);
        radioOthers = findViewById(R.id.radioOthers);

        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);

        tvTitle.setText("ADD NEW DOCUMENT");

        tvDocumentType1 = findViewById(R.id.tvDocumentType1);
        tvSubmissionDate1 = findViewById(R.id.tvSubmissionDate1);
        tvRemark1 = findViewById(R.id.tvRemark1);

        radioPredefined.setText(translationManager.PREDEFINED_DOC_KEY);
        radioOthers.setText(translationManager.OTHERS_KEY);
        tvSubmissionDate1.setText(translationManager.SUBMISSION_DATE_KEY);
        tvRemark1.setText(translationManager.REMARK_KEY);

        spnDocumentType = findViewById(R.id.spnDocumentType);
        tvSubmissionDate = findViewById(R.id.tvSubmissionDate);
        tvAttachment = findViewById(R.id.tvAttachment);

        etDocumentType = findViewById(R.id.etDocumentType);
        etRemark = findViewById(R.id.etRemark);

        btnBrowse = findViewById(R.id.btnBrowse);
        btnSubmit = findViewById(R.id.btnSubmit);

        llSelectFile = findViewById(R.id.llSelectFile);
        llAttachment = findViewById(R.id.llAttachment);
        ivAttachment = findViewById(R.id.ivAttachment);
        ivCancel = findViewById(R.id.ivCancel);

        ivClose.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radioPredefined:{

                        viewEnableDisable(true);
                    }break;

                    case R.id.radioOthers:{

                        viewEnableDisable(false);
                    }break;
                }

            }
        });
    }

    private void viewEnableDisable(boolean b) {

        if(b){
            spnDocumentType.setVisibility(View.VISIBLE);
            etDocumentType.setVisibility(View.GONE);
            docOption = true;
        }else{
            spnDocumentType.setVisibility(View.GONE);
            etDocumentType.setVisibility(View.VISIBLE);
            docOption = false;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.ivClose:{
                finish();
            }break;

            case R.id.btnBrowse:{

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

            case R.id.btnSubmit:{

                ProjectUtils.preventTwoClick(btnSubmit);

                if(radioPredefined.isChecked() && spnDocumentType.getSelectedItem().toString().trim().equalsIgnoreCase("Select")){

                    ProjectUtils.showLong(mContext,"Please Select Document Type");
                }else  if(radioOthers.isChecked() && etDocumentType.getText().toString().equalsIgnoreCase("")){

                    ProjectUtils.showLong(mContext,"Please Enter Document Type");
                    etDocumentType.requestFocus();
                }else if(llAttachment.getVisibility() != View.VISIBLE){

                    ProjectUtils.showLong(mContext,"Please Select a Document");
                } else{

                    if(ConnectionDetector.isConnectingToInternet(this)){

                    showProgressDialog(this);

                    uploadTemp(filePath);

                    } else {
                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

            }break;

            case R.id.ivCancel: {
                llAttachment.setVisibility(View.GONE);
                llSelectFile.setVisibility(View.VISIBLE);
                tvAttachment.setText("");
                path = "";
                uri = null;
            }break;
        }
    }

    private void uploadTemp(File filePath) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.STUDENT_HW_ASSIGNMENT_UPLOAD_TEMP;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        int studentId = sharedPrefrenceManager.getUserIDFromKey();

        AndroidNetworking.upload(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addMultipartParameter("access_token",accessToken)
                .addMultipartParameter("id","")
                .addMultipartParameter("module", "student")
                .addMultipartParameter("entityId", String.valueOf(studentId))
                .addMultipartParameter("entityType","student")
                .addMultipartParameter("doctype","EXT_DOCUMENT")
                .addMultipartFile("file", filePath)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        // do anything with response
                        ProjectUtils.showLog("Upload Temp Response: ", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            path = jsonObject.optString("path");

                            uploadFileOnServer1(path);

                        }catch (Exception ex){
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());
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

    private void uploadFileOnServer1(String path) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.DOCUMENT_UPLOAD1_METHOD;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        String txt = tvSubmissionDate.getText().toString();
        String formattedDate = ProjectUtils.formateDateFromString("dd/MM/yyyy", "yyyy-MM-dd", txt);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "");
            jsonObject.put("isPreDefined", docOption);
            jsonObject.put("submissionDate", formattedDate);
            jsonObject.put("remark", etRemark.getText().toString().trim());
            jsonObject.put("isInspectionDone", false);
            jsonObject.put("status", "SUBMITTED");
            jsonObject.put("path", path);
            jsonObject.put("type", "EXT_DOCUMENT");
            jsonObject.put("name", etDocumentType.getText().toString().trim());
            jsonObject.put("modeOfSubmission", "ONLINE");
            jsonObject.put("isElectronic", true);

            if(docOption){
                JSONObject obj1=new JSONObject();
                obj1.put("id",documentTypeId);
                jsonObject.put("documentType",obj1);
            }

            JSONObject obj2=new JSONObject();
            obj2.put("id",sharedPrefrenceManager.getUserIDFromKey());
            obj2.put("value",sharedPrefrenceManager.getUserFirstNameFromKey()+" "+sharedPrefrenceManager.getUserLastNameFromKey());
            obj2.put("code",sharedPrefrenceManager.getUserCodeFromKey());
            jsonObject.put("createdBy",obj2);

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
                            ProjectUtils.showLog("Upload File OnServer1 : ", response);

                            uploadFileOnServer2(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                            hideProgressDialog();
                            ProjectUtils.showLog(TAG,""+anError.getMessage());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
            ProjectUtils.showLog(TAG,""+e.getMessage());
        }
    }

    private void uploadFileOnServer2(String response) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.DOCUMENT_UPLOAD2_METHOD;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "SUBMITTED");
            jsonObject.put("isElectronic", true);

            JSONObject obj1=new JSONObject();
            obj1.put("id",sharedPrefrenceManager.getUserIDFromKey());
            jsonObject.put("student",obj1);

            JSONObject obj2=new JSONObject();
            obj2.put("id",response);
            jsonObject.put("document",obj2);

            JSONObject obj3=new JSONObject();
            obj3.put("id",sharedPrefrenceManager.getAdmissionIDFromKey());
            jsonObject.put("admission",obj3);

            if(docOption){
                JSONObject obj4=new JSONObject();
                obj4.put("id",documentTypeId);
                jsonObject.put("documentType",obj4);
            }

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

                            if(!response.equalsIgnoreCase("0")){
                                ProjectUtils.showLong(mContext,"Document Uploaded Successfully");
                                AcademiaApp.IS_UPDATE = true;
                                onBackPressed();
                            }else{
                                ProjectUtils.showLong(mContext,"Document already exists for this document type!");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                            hideProgressDialog();
                            ProjectUtils.showLog(TAG,""+anError.getMessage());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
            ProjectUtils.showLog(TAG,""+e.getMessage());
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
                                filePath = new File(fileName);

                                int file_size = Integer.parseInt(String.valueOf(filePath.length()/1024));

                                if(file_size < 20000){

                                    llAttachment.setVisibility(View.VISIBLE);
                                    llSelectFile.setVisibility(View.GONE);
                                    int index = fileName.lastIndexOf("/");
                                    docFileName = fileName.substring(index + 1);

                                    tvAttachment.setText(docFileName);
                                    ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));

                                }else{
                                    llAttachment.setVisibility(View.GONE);
                                    llSelectFile.setVisibility(View.VISIBLE);
                                    Toast.makeText(this, "Can't upload document more than 20 MB", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                llAttachment.setVisibility(View.GONE);
                                llSelectFile.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception ex) {
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());

                            ProjectUtils.showLong(mContext,"Something went wrong, Please try again");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {
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

            case KEYS.SWITCH_DOCUMENT_TYPE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    documentTypeList = new ArrayList<>();

                    documentTypeList.add(new DocumentType_Dto(0,"Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        documentTypeList.add(new DocumentType_Dto(id, value));
                    }

                    documentTypedapter = new DocumentTypeAdapter(mContext,documentTypeList);
                    spnDocumentType.setAdapter(documentTypedapter);

                    spnDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            DocumentType_Dto documentTypeDto = (DocumentType_Dto) spnDocumentType.getSelectedItem();
                            documentTypeId = documentTypeDto.getId();
                            ProjectUtils.showLog(TAG,""+documentTypeId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
