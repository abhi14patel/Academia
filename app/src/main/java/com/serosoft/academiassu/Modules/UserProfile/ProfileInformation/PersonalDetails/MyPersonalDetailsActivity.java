package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.serosoft.academiassu.Helpers.FilePath;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters.CorrespondenceLanguageAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters.CurrentEducationAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters.DisabilitiesAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters.SalutationAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.CurrentEducation_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Disabilities_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Profile_Dto;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.MyPersonalDetailsActivityBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MyPersonalDetailsActivity extends BaseActivity {

    private Context mContext;
    MyPersonalDetailsActivityBinding binding;

    int salutationId = -1,salutationId1;
    int correspondencelanguageId = -1,correspondencelanguageId1;
    int contactlanguageId = -1,contactlanguageId1;

    JSONObject getJsonData;
    int selectedSalutationId = 0;
    int selectedCorrespondenceLangId = 0;
    int selectedContactLangId = 0;

    File filePath;
    Uri uri;
    String docFileName = "";
    private static final int PICKFILE_RESULT_CODE = 1;

    ArrayList<Profile_Dto> salutationList;
    SalutationAdapter salutationAdapter;

    ArrayList<Profile_Dto> correspondenceLanguageList;
    CorrespondenceLanguageAdapter correspondenceLanguageAdapter;

    ArrayList<Disabilities_Dto> disabilitiesList;
    ArrayList<Disabilities_Dto> disabilitiesSelectedList;
    ArrayList<Integer> disabilitieIdsList;
    DisabilitiesAdapter disabilitiesAdapter;

    ArrayList<CurrentEducation_Dto> currentEducationList;
    ArrayList<CurrentEducation_Dto> currentEducationSelectedList;
    ArrayList<Integer> currentEducationIdsList;
    CurrentEducationAdapter currentEducationAdapter;

    private final String TAG = MyPersonalDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyPersonalDetailsActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreateView Start");

        mContext = MyPersonalDetailsActivity.this;

        Initialize();

        populateContent();
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_DETAILS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        binding.header.tvTitle.setText("EDIT "+getString(R.string.personal_details).toUpperCase());

        binding.header.ivClose.setOnClickListener(this);
        binding.btnBrowse.setOnClickListener(this);
        binding.ivCancel.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
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

            case R.id.ivCancel: {
                binding.llAttachment.setVisibility(View.GONE);
                binding.llSelectFile.setVisibility(View.VISIBLE);
                binding.tvAttachment.setText("");
                uri = null;
            }break;

            case R.id.btnSubmit: {

                showProgressDialog(mContext);
                submitPersonalDetails(getJsonData);
            }break;
        }
    }

    private void submitPersonalDetails(JSONObject getJsonData) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", getJsonData.optString("id"));
            jsonObject.put("version", getJsonData.optString("version"));
            jsonObject.put("firstName", getJsonData.optString("firstName"));

            JSONArray jsonArray1 = new JSONArray();
            jsonObject.put("studentAttendanceGroup",jsonArray1);

            jsonObject.put("code", getJsonData.optString("code"));
            jsonObject.put("lastName", getJsonData.optString("lastName"));
            jsonObject.put("shortName", getJsonData.optString("shortName"));
            jsonObject.put("isMidtermJoinee", getJsonData.optBoolean("isMidtermJoinee"));
            jsonObject.put("accountingKey", "");
            jsonObject.put("middleName", ProjectUtils.getCorrectedString(getJsonData.optString("middleName")));

            JSONObject getperson = getJsonData.optJSONObject("person");
            JSONObject genderGet = getperson.optJSONObject("genderCSM");
            JSONObject countryGet = getperson.optJSONObject("country");
            JSONObject religionGet = getperson.optJSONObject("religion");
            JSONObject castCategoryGet = getperson.optJSONObject("castCategory");
            JSONObject nationalityGet = getperson.optJSONObject("nationality");
            JSONObject studentStatusGet = getperson.optJSONObject("studentStatus");
            JSONObject homeLanguageGet = getperson.optJSONObject("homeLanguage");
            JSONObject secondLanguageGet = getperson.optJSONObject("secondLanguage");
            JSONObject bankDetailGet = getperson.optJSONObject("bankDetail");
            JSONObject physicalCharacteristicsGet = getperson.optJSONObject("physicalCharacteristics");

            JSONArray customDataArray = getperson.optJSONArray("customData");

            JSONObject putPerson = new JSONObject();
            putPerson.put("id",getperson.optString("id"));
            putPerson.put("version",getperson.optString("version"));

            JSONObject salutationPut = new JSONObject();
            if(selectedSalutationId != 0){
                salutationPut.put("id",selectedSalutationId);
            }
            putPerson.put("salutation",salutationPut);

            putPerson.put("firstName",ProjectUtils.getCorrectedString(getperson.optString("firstName")));
            putPerson.put("middleName",ProjectUtils.getCorrectedString(getperson.optString("middleName")));
            putPerson.put("lastName",ProjectUtils.getCorrectedString(getperson.optString("lastName")));
            putPerson.put("shortName",ProjectUtils.getCorrectedString(getperson.optString("shortName")));
            putPerson.put("printName",ProjectUtils.getCorrectedString(getperson.optString("printName")));

            JSONObject genderPut = new JSONObject();
            genderPut.put("id",genderGet.optInt("id"));
            putPerson.put("genderCSM",genderPut);

            String dateofbirth = "";
            long dob = getperson.optLong("birthDate");
            if(dob != 0){
                dateofbirth = ProjectUtils.convertTimestampToDate2(dob,mContext);
            }
            putPerson.put("birthDate",dateofbirth);

            putPerson.put("birthPlace",binding.etPOB.getText().toString().trim());
            putPerson.put("birthPlaceOtherLanguage",binding.etPOBOL.getText().toString().trim());

            JSONObject countryPut = new JSONObject();
            countryPut.put("id",countryGet.optInt("id"));
            putPerson.put("country",countryPut);

            JSONObject nationalityPut = new JSONObject();
            nationalityPut.put("id",nationalityGet.optInt("id"));
            putPerson.put("nationality",nationalityPut);

            putPerson.put("secondaryCitizenship",getperson.get("secondaryCitizenship"));

            String admissionDate = getJsonData.optString("admissionDate");
            String newadmissionDate = ProjectUtils.getDateFormat("dd/MM/yyyy",admissionDate);
            putPerson.put("admissionDate",newadmissionDate);

            putPerson.put("domicile",getperson.get("domicile"));
            putPerson.put("idtype",getperson.get("idtype"));
            putPerson.put("nationalID",binding.etAadhaarNumber.getText().toString().trim());
            putPerson.put("identityExpiryDate",ProjectUtils.getCorrectedString(getperson.optString("identityExpiryDate")));

            JSONObject studentStatusPut = new JSONObject();
            if(studentStatusGet != null){
                int id = studentStatusGet.optInt("id");
                studentStatusPut.put("id",id);
                putPerson.put("studentStatus",studentStatusPut);
            }else{
                putPerson.put("studentStatus",null);
            }

            putPerson.put("specifyOther",ProjectUtils.getCorrectedString(getperson.optString("specifyOther")));
            putPerson.put("isLearnerSACitizen",getperson.optBoolean("isLearnerSACitizen"));

            JSONObject homeLanguagePut = new JSONObject();
            if(homeLanguageGet != null){
                int id = homeLanguageGet.optInt("id");
                homeLanguagePut.put("id",id);
                putPerson.put("homeLanguage",homeLanguagePut);
            }

            JSONObject secondLanguagePut = new JSONObject();
            if(secondLanguageGet != null){
                int id = secondLanguageGet.optInt("id");
                secondLanguagePut.put("id",id);
                putPerson.put("secondLanguage",secondLanguagePut);
            }

            JSONObject correspondenceLanguagePut = new JSONObject();
            if(selectedCorrespondenceLangId != 0){
                correspondenceLanguagePut.put("id",selectedCorrespondenceLangId);
                putPerson.put("correspondenceLanguage",correspondenceLanguagePut);
            }

            JSONObject contactLanguagePut = new JSONObject();
            if(selectedContactLangId != 0){
                contactLanguagePut.put("id",selectedContactLangId);
                putPerson.put("contactLanguage",contactLanguagePut);
            }

            putPerson.put("isWheelChairRequired",getperson.optBoolean("isWheelChairRequired"));
            putPerson.put("isTertiaryInstitution",getperson.optBoolean("isTertiaryInstitution"));
            putPerson.put("phoneCountryCode",getperson.get("phoneCountryCode"));
            putPerson.put("phoneAreaCode",getperson.get("phoneAreaCode"));
            putPerson.put("phoneNo",ProjectUtils.getCorrectedString(getperson.optString("phoneNo")));
            putPerson.put("mobileCountryCode",ProjectUtils.getCorrectedString(getperson.optString("mobileCountryCode")));
            putPerson.put("mobileNumber",ProjectUtils.getCorrectedString(getperson.optString("mobileNumber")));
            putPerson.put("alternateMobileCountryCode",ProjectUtils.getCorrectedString(getperson.optString("alternateMobileCountryCode")));
            putPerson.put("alternateMobileNo",ProjectUtils.getCorrectedString(getperson.optString("alternateMobileNo")));
            putPerson.put("emailId",ProjectUtils.getCorrectedString(getperson.optString("emailId")));
            putPerson.put("alternateEmailId",ProjectUtils.getCorrectedString(getperson.optString("alternateEmailId")));
            putPerson.put("externalSysRefOne",ProjectUtils.getCorrectedString(getperson.optString("externalSysRefOne")));
            putPerson.put("externalSysRefTwo",ProjectUtils.getCorrectedString(getperson.optString("externalSysRefTwo")));
            putPerson.put("externalSysRefThree",ProjectUtils.getCorrectedString(getperson.optString("externalSysRefThree")));
            putPerson.put("boardNumber",ProjectUtils.getCorrectedString(getperson.optString("boardNumber")));
            putPerson.put("bloodGroup",ProjectUtils.getCorrectedString(getperson.optString("bloodGroup")));
            putPerson.put("maritalStatus",getperson.get("maritalStatus"));

            JSONObject religionPut = new JSONObject();
            religionPut.put("id",religionGet.optInt("id"));
            putPerson.put("religion",religionPut);

            JSONObject castCategoryPut = new JSONObject();
            if(castCategoryGet != null){
                int id = castCategoryGet.optInt("id");
                castCategoryPut.put("id",id);
                putPerson.put("castCategory",castCategoryPut);
            }

            putPerson.put("category",getperson.get("category"));
            putPerson.put("personalIncome",ProjectUtils.getCorrectedString(getperson.optString("personalIncome")));
            putPerson.put("validTill",ProjectUtils.getCorrectedString(getperson.optString("validTill")));
            putPerson.put("isRegistrationContract",getperson.optBoolean("isRegistrationContract"));

            JSONArray customDataPutArray = new JSONArray();
            for (int j=0 ; j<customDataArray.length() ; j++){

                JSONObject customDataGet = customDataArray.getJSONObject(j);

                JSONObject customDataPut = new JSONObject();

                customDataPut.put("cf16",customDataGet.optString("cf16"));
                customDataPut.put("cf11",customDataGet.optInt("cf11"));
                customDataPut.put("cf12",ProjectUtils.getCorrectedString(customDataGet.optString("cf12")));
                customDataPut.put("cf2",ProjectUtils.getCorrectedString(customDataGet.optString("cf2")));
                customDataPut.put("cf14",ProjectUtils.getCorrectedString(customDataGet.optString("cf14")));
                customDataPut.put("cf3",ProjectUtils.getCorrectedString(customDataGet.optString("cf3")));
                customDataPut.put("cf1",ProjectUtils.getCorrectedString(customDataGet.optString("cf1")));
                customDataPut.put("cf4",ProjectUtils.getCorrectedString(customDataGet.optString("cf4")));
                customDataPut.put("cf18",ProjectUtils.getCorrectedString(customDataGet.optString("cf18")));
                customDataPut.put("cf8",ProjectUtils.getCorrectedString(customDataGet.optString("cf8")));
                customDataPut.put("cf9",ProjectUtils.getCorrectedString(customDataGet.optString("cf9")));
                customDataPut.put("cf6",ProjectUtils.getCorrectedString(customDataGet.optString("cf6")));
                customDataPut.put("cf5",ProjectUtils.getCorrectedString(customDataGet.optString("cf5")));
                customDataPut.put("cf7",ProjectUtils.getCorrectedString(customDataGet.optString("cf7")));
                customDataPut.put("id",ProjectUtils.getCorrectedString(customDataGet.optString("id")));
                customDataPut.put("version",ProjectUtils.getCorrectedString(customDataGet.optString("version")));
                customDataPutArray.put(customDataPut);
            }
            putPerson.put("customData",customDataPutArray);

            JSONArray personDisabilityArray = new JSONArray();
            for (int i=0 ; i<disabilitiesSelectedList.size() ; i++){

                JSONObject jsonObject1 = new JSONObject();

                JSONObject haveDisabilities = new JSONObject();
                haveDisabilities.put("id",disabilitiesSelectedList.get(i).getId());
                jsonObject1.put("haveDisabilities",haveDisabilities);

                JSONObject studentPerson = new JSONObject();
                studentPerson.put("id",getperson.optInt("id"));
                jsonObject1.put("studentPerson",studentPerson);

                personDisabilityArray.put(jsonObject1);
            }
            putPerson.put("personDisability",personDisabilityArray);

            JSONArray personEducationInterventionsArray = new JSONArray();
            for (int i=0 ; i<currentEducationSelectedList.size() ; i++){

                JSONObject jsonObject1 = new JSONObject();

                JSONObject educationInterventions = new JSONObject();
                educationInterventions.put("id",currentEducationSelectedList.get(i).getId());
                jsonObject1.put("educationInterventions",educationInterventions);

                JSONObject studentPerson = new JSONObject();
                studentPerson.put("id",getperson.optInt("id"));
                jsonObject1.put("studentPerson",studentPerson);

                personEducationInterventionsArray.put(jsonObject1);
            }
            putPerson.put("personEducationInterventions",personEducationInterventionsArray);

            JSONObject bankDetailPut = new JSONObject();
            bankDetailPut.put("id",bankDetailGet.optString("id"));
            bankDetailPut.put("version",bankDetailGet.optString("version"));
            bankDetailPut.put("bankName",bankDetailGet.get("bankName"));
            bankDetailPut.put("accountNumber",bankDetailGet.get("accountNumber"));
            bankDetailPut.put("branchName",bankDetailGet.get("branchName"));
            bankDetailPut.put("ifsCode",bankDetailGet.get("ifsCode"));
            bankDetailPut.put("beneficiaryName",bankDetailGet.get("beneficiaryName"));
            bankDetailPut.put("accountTypeId",bankDetailGet.get("accountTypeId"));
            putPerson.put("bankDetail",bankDetailPut);

            JSONObject physicalCharacteristicsPut = new JSONObject();
            if(physicalCharacteristicsGet != null){
                int id = physicalCharacteristicsGet.optInt("id");
                int version = physicalCharacteristicsGet.optInt("version");
                physicalCharacteristicsPut.put("id",id);
                physicalCharacteristicsPut.put("version",version);
                putPerson.put("physicalCharacteristics",physicalCharacteristicsPut);
            }

            jsonObject.put("person",putPerson);

            jsonObject.put("printName", getJsonData.optString("printName"));

            //Here do api calling for login
            String url = BaseURL.BASE_URL + KEYS.PROFILE_SUBMIT_METHOD;

            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

            AndroidNetworking.put(url)
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
                            ProjectUtils.showLog("Submit Response: ", response);

                            if(binding.llAttachment.getVisibility() == View.VISIBLE){
                                uploadDocument(filePath);
                            }else{
                                ProjectUtils.showLong(mContext,"Student personal information submitted successfully!");
                                onBackPressed();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                            hideProgressDialog();
                            ProjectUtils.showLog(TAG,""+anError.getMessage());

                            ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
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

                                    binding.llAttachment.setVisibility(View.VISIBLE);
                                    binding.llSelectFile.setVisibility(View.GONE);
                                    int index = fileName.lastIndexOf("/");
                                    docFileName = fileName.substring(index + 1);

                                    binding.tvAttachment.setText(docFileName);
                                    binding.ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));

                                }else{
                                    binding.llAttachment.setVisibility(View.GONE);
                                    binding.llSelectFile.setVisibility(View.VISIBLE);
                                    Toast.makeText(this, "Can't upload document more than 20 MB", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                binding.llAttachment.setVisibility(View.GONE);
                                binding.llSelectFile.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception ex) {
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());

                            ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                        }
                    }
                }
                break;
        }
    }

    private void uploadDocument(File filePath) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.PROFILE_DOCUMENT_UPLOAD_METHOD;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        String personId = String.valueOf(sharedPrefrenceManager.getPersonIDFromKey());

        AndroidNetworking.upload(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addMultipartParameter("access_token",accessToken)
                .addMultipartParameter("personId", personId)
                .addMultipartFile("file", filePath)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        ProjectUtils.showLong(mContext,"Student personal information submitted successfully!");
                        onBackPressed();
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        hideProgressDialog();
                        ProjectUtils.showLog(TAG,""+error.getMessage());

                        ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                    }
                });
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
            case KEYS.SWITCH_PROFILE_DETAILS:

                try {
                    getJsonData = new JSONObject(responseResult.toString());

                    JSONObject person = getJsonData.optJSONObject("person");

                    JSONObject salutation = person.optJSONObject("salutation");
                    salutationId = salutation.optInt("id");

                    JSONObject correspondenceLanguage = person.optJSONObject("correspondenceLanguage");
                    if(correspondenceLanguage != null){
                        correspondencelanguageId = correspondenceLanguage.optInt("id");
                    }

                    JSONObject contactLanguage = person.optJSONObject("contactLanguage");
                    if(contactLanguage != null){
                        contactlanguageId = contactLanguage.optInt("id");
                    }

                    JSONArray personDisability = person.optJSONArray("personDisability");
                    disabilitieIdsList = new ArrayList<>();
                    if(personDisability != null && personDisability.length() > 0){
                        for(int i = 0 ; i< personDisability.length() ; i++){
                            JSONObject jsonObject1 = personDisability.optJSONObject(i);

                            JSONObject haveDisabilities = jsonObject1.optJSONObject("haveDisabilities");
                            int id = haveDisabilities.optInt("id");
                            disabilitieIdsList.add(id);
                        }
                    }

                    JSONArray personEducationInterventions = person.optJSONArray("personEducationInterventions");
                    currentEducationIdsList = new ArrayList<>();
                    if(personEducationInterventions != null && personEducationInterventions.length() > 0){
                        for(int i = 0 ; i< personEducationInterventions.length() ; i++){
                            JSONObject jsonObject2 = personEducationInterventions.optJSONObject(i);

                            JSONObject educationInterventions = jsonObject2.optJSONObject("educationInterventions");
                            int id = educationInterventions.optInt("id");
                            currentEducationIdsList.add(id);
                        }
                    }

                    String firstName = ProjectUtils.getCorrectedString(person.optString("firstName"));
                    String lastName = ProjectUtils.getCorrectedString(person.optString("lastName"));
                    String birthPlace = ProjectUtils.getCorrectedString(person.optString("birthPlace"));
                    String birthPlaceOtherLanguage = ProjectUtils.getCorrectedString(person.optString("birthPlaceOtherLanguage"));
                    String nationalID = ProjectUtils.getCorrectedString(person.optString("nationalID"));
                    String supportDocument = ProjectUtils.getCorrectedString(person.optString("supportDocument"));

                    binding.tvFirstName.setText(firstName+" "+lastName);
                    binding.etAadhaarNumber.setText(nationalID);

                    binding.etPOB.setText(birthPlace);
                    binding.etPOB.setSelection(binding.etPOB.getText().length());

                    binding.etPOBOL.setText(birthPlaceOtherLanguage);
                    binding.etPOBOL.setSelection(binding.etPOBOL.getText().length());

                    binding.etAadhaarNumber.setText(nationalID);
                    binding.etAadhaarNumber.setSelection(binding.etAadhaarNumber.getText().length());

                    if(!supportDocument.equalsIgnoreCase("")){
                        binding.tvSupportDocument.setVisibility(View.VISIBLE);
                        binding.tvSupportDocument.setText("("+supportDocument+")");
                    }else{
                        binding.tvSupportDocument.setVisibility(View.GONE);
                    }

                    if (ConnectionDetector.isConnectingToInternet(this)) {

                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_SALUTATIONS).execute();
                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_MULTILANGUAGE).execute();
                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_CONTACTLANGUAGE).execute();
                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_DISABILITIES).execute();
                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_CURRENT_EDUCATIONAL).execute();
                    } else {
                        Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                }
                break;

            case KEYS.SWITCH_PROFILE_SALUTATIONS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    salutationList = new ArrayList<>();

                    salutationList.add(new Profile_Dto(-1,"Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("salutationName");

                        salutationList.add(new Profile_Dto(id, value));
                    }

                    if(salutationList != null && salutationList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< salutationList.size() ; i++){

                            Profile_Dto profile_dto = salutationList.get(i);

                            if(profile_dto.getId() == salutationId)
                            {
                                salutationId1 = i;
                            }
                        }

                        salutationAdapter = new SalutationAdapter(mContext,salutationList){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position
                                if (position == salutationId1) {
                                    v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                }
                                else {
                                    // for other views
                                    v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                }
                                return v;
                            }
                        };
                        binding.spnSalutation.setAdapter(salutationAdapter);
                        binding.spnSalutation.setSelection(salutationId1);

                        binding.spnSalutation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Profile_Dto profile_dto = (Profile_Dto) binding.spnSalutation.getSelectedItem();

                                selectedSalutationId = profile_dto.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.disableSpinner(binding.spnSalutation);
                }
                break;

            case KEYS.SWITCH_PROFILE_MULTILANGUAGE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    correspondenceLanguageList = new ArrayList<>();

                    correspondenceLanguageList.add(new Profile_Dto(0,"Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("itemValue");

                        correspondenceLanguageList.add(new Profile_Dto(id, value));
                    }

                    if(correspondenceLanguageList != null && correspondenceLanguageList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< correspondenceLanguageList.size() ; i++){

                            Profile_Dto profile_dto = correspondenceLanguageList.get(i);

                            if(profile_dto.getId() == correspondencelanguageId)
                            {
                                correspondencelanguageId1 = i;
                            }
                        }

                        correspondenceLanguageAdapter = new CorrespondenceLanguageAdapter(mContext,correspondenceLanguageList){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position
                                if (position == correspondencelanguageId1) {
                                    v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                }
                                else {
                                    // for other views
                                    v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                }
                                return v;
                            }
                        };
                        binding.spnCorrespondenceLanguage.setAdapter(correspondenceLanguageAdapter);
                        binding.spnCorrespondenceLanguage.setSelection(correspondencelanguageId1);

                        binding.spnCorrespondenceLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Profile_Dto profile_dto = (Profile_Dto) binding.spnCorrespondenceLanguage.getSelectedItem();

                                selectedCorrespondenceLangId = profile_dto.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.disableSpinner(binding.spnCorrespondenceLanguage);
                }
                break;

            case KEYS.SWITCH_PROFILE_CONTACTLANGUAGE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    correspondenceLanguageList = new ArrayList<>();

                    correspondenceLanguageList.add(new Profile_Dto(0,"Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("itemValue");

                        correspondenceLanguageList.add(new Profile_Dto(id, value));
                    }

                    if(correspondenceLanguageList != null && correspondenceLanguageList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< correspondenceLanguageList.size() ; i++){

                            Profile_Dto profile_dto = correspondenceLanguageList.get(i);

                            if(profile_dto.getId() == contactlanguageId)
                            {
                                contactlanguageId1 = i;
                            }
                        }

                        correspondenceLanguageAdapter = new CorrespondenceLanguageAdapter(mContext,correspondenceLanguageList){
                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position
                                if (position == contactlanguageId1) {
                                    v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                }
                                else {
                                    // for other views
                                    v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                }
                                return v;
                            }
                        };
                        binding.spnContactLanguage.setAdapter(correspondenceLanguageAdapter);
                        binding.spnContactLanguage.setSelection(contactlanguageId1);

                        binding.spnContactLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Profile_Dto profile_dto = (Profile_Dto) binding.spnContactLanguage.getSelectedItem();

                                selectedContactLangId = profile_dto.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.disableSpinner(binding.spnContactLanguage);
                }
                break;

            case KEYS.SWITCH_PROFILE_DISABILITIES:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    disabilitiesList = new ArrayList<>();
                    disabilitiesSelectedList = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        Disabilities_Dto disabilities_dto = new Disabilities_Dto();
                        disabilities_dto.setId(id);
                        disabilities_dto.setValue(value);
                        disabilities_dto.setSelected(false);
                        disabilitiesList.add(disabilities_dto);
                    }

                    //Here sort both list
                    Collections.sort(disabilitiesList, Disabilities_Dto.sortById);
                    Collections.sort(disabilitieIdsList);

                    for(int k = 0 ; k < disabilitieIdsList.size() ; k++){

                        int id = disabilitieIdsList.get(k);

                        for(int l = 0 ; l < disabilitiesList.size() ; l++){

                            Disabilities_Dto disabilities_dto = disabilitiesList.get(l);

                            if(disabilities_dto.getId() == id)
                            {
                                disabilities_dto.setSelected(true);
                                disabilitiesSelectedList.add(disabilities_dto);
                            }
                            disabilitiesList.set(l,disabilities_dto);
                        }
                    }

                    // Here set data (,) seprated value
                    if(disabilitiesSelectedList != null && disabilitiesSelectedList.size() > 0){
                        String csv = disabilitiesSelectedList.toString().replace("[", "").replace("]", "").replace(", ", ",");
                        binding.tvDisabilities.setText(csv);
                    }else{
                        binding.tvDisabilities.setText("");
                    }

                    binding.tvDisabilities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(disabilitiesList != null && disabilitiesList.size() > 0){

                                Dialog alertDialog = new Dialog(mContext);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.disabilities_dialog, null);
                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog.setContentView(convertView);
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                ListView lvDisabilities = (ListView) convertView.findViewById(R.id.lvDisabilities);
                                TextView tvCancel = convertView.findViewById(R.id.tvCancel);
                                TextView tvOk = convertView.findViewById(R.id.tvOk);

                                disabilitiesAdapter = new DisabilitiesAdapter(mContext,disabilitiesList,disabilitiesSelectedList);
                                lvDisabilities.setAdapter(disabilitiesAdapter);

                                tvCancel.setOnClickListener(new View.OnClickListener()
                                {
                                    public void onClick(View v)
                                    {
                                        alertDialog.dismiss();
                                    }
                                });

                                tvOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();
                                        disabilitiesSelectedList = disabilitiesAdapter.getSelectedIds();

                                        // CSV format
                                        String csv = disabilitiesSelectedList.toString().replace("[", "").replace("]", "").replace(", ", ",");
                                        binding.tvDisabilities.setText(csv);
                                    }
                                });

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alertDialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                alertDialog.show();
                                alertDialog.getWindow().setAttributes(lp);
                            }else{
                                ProjectUtils.showLong(mContext,"No records found");
                            }
                        }
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_PROFILE_CURRENT_EDUCATIONAL:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    currentEducationList = new ArrayList<>();
                    currentEducationSelectedList = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        CurrentEducation_Dto currentEducation_dto = new CurrentEducation_Dto();
                        currentEducation_dto.setId(id);
                        currentEducation_dto.setValue(value);
                        currentEducation_dto.setSelected(false);
                        currentEducationList.add(currentEducation_dto);
                    }

                    //Here sort both list
                    Collections.sort(currentEducationList, CurrentEducation_Dto.sortById);
                    Collections.sort(currentEducationIdsList);

                    for(int k = 0 ; k < currentEducationIdsList.size() ; k++){

                        int id = currentEducationIdsList.get(k);

                        for(int l = 0 ; l < currentEducationList.size() ; l++){

                            CurrentEducation_Dto currentEducation_dto = currentEducationList.get(l);

                            if(currentEducation_dto.getId() == id)
                            {
                                currentEducation_dto.setSelected(true);
                                currentEducationSelectedList.add(currentEducation_dto);
                            }
                            currentEducationList.set(l,currentEducation_dto);
                        }
                    }

                    // CSV format
                    if(currentEducationSelectedList != null && currentEducationSelectedList.size() > 0){
                        String csv = currentEducationSelectedList.toString().replace("[", "").replace("]", "").replace(", ", ",");
                        binding.tvCurrentEducation.setText(csv);
                    }else{
                        binding.tvCurrentEducation.setText("");
                    }

                    binding.tvCurrentEducation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(currentEducationList != null && currentEducationList.size() > 0){

                                Dialog alertDialog = new Dialog(mContext);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.disabilities_dialog, null);
                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog.setContentView(convertView);
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                ListView lvDisabilities = (ListView) convertView.findViewById(R.id.lvDisabilities);
                                TextView tvCancel = convertView.findViewById(R.id.tvCancel);
                                TextView tvOk = convertView.findViewById(R.id.tvOk);

                                currentEducationAdapter = new CurrentEducationAdapter(mContext,currentEducationList,currentEducationSelectedList);
                                lvDisabilities.setAdapter(currentEducationAdapter);

                                tvCancel.setOnClickListener(new View.OnClickListener()
                                {
                                    public void onClick(View v)
                                    {
                                        alertDialog.dismiss();
                                    }
                                });

                                tvOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();
                                        currentEducationSelectedList = currentEducationAdapter.getSelectedIds();

                                        // CSV format
                                        String csv = currentEducationSelectedList.toString().replace("[", "").replace("]", "").replace(", ", ",");
                                        binding.tvCurrentEducation.setText(csv);
                                    }
                                });

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(alertDialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                alertDialog.show();
                                alertDialog.getWindow().setAttributes(lp);
                            }else{
                                ProjectUtils.showLong(mContext,"No records found");
                            }
                        }
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
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