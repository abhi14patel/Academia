package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import android.app.DatePickerDialog;
import android.content.Context;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.CountryCodeAdapter;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.MedConditionTypeAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.CountryCode_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.DocumentType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MedicalConditionUploadActivity extends BaseActivity {

    private Context mContext;
    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private TextView tvConditionType1,tvMedicalCondition1,tvSince1,tvConsultingDoctor1,tvContactNumber1,tvPrecuation1;
    private EditText etMedicalCondition,etConsultingDoctor,etContact,etPrecuation;
    private TextView tvSince;
    private Button btnSubmit;
    private Spinner spnConditionType,spnContactCode;

    Calendar calendarDefault = null;
    private int mYear, mMonth, mDay;
    Long date1,date2;
    String startDate = "";

    ArrayList<DocumentType_Dto> medConditionTypeList;
    MedConditionTypeAdapter medConditionTypeAdapter;
    String medConditionTypeId = "";

    ArrayList<CountryCode_Dto> countryCodeList;
    CountryCodeAdapter countryCodeAdapter;
    int lengthMax = 15;
    int lengthMini = 7;

    private final String TAG = MedicalConditionUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_condition_upload_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = MedicalConditionUploadActivity.this;

        Initialize();

        populateContent();
    }


    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_MEDICAL_CONDITION_TYPES).execute();
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE).execute();
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

        switch (callFor){

            case KEYS.SWITCH_MEDICAL_CONDITION_TYPES:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    medConditionTypeList = new ArrayList<>();

                    medConditionTypeList.add(new DocumentType_Dto(0,"Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        medConditionTypeList.add(new DocumentType_Dto(id, value));
                    }

                    medConditionTypeAdapter = new MedConditionTypeAdapter(mContext,medConditionTypeList);
                    spnConditionType.setAdapter(medConditionTypeAdapter);

                    spnConditionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            DocumentType_Dto documentTypeDto = (DocumentType_Dto) spnConditionType.getSelectedItem();
                            medConditionTypeId = String.valueOf(documentTypeDto.getId());
                            ProjectUtils.showLog(TAG,""+medConditionTypeId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });


                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    countryCodeList = new ArrayList<>();

                    countryCodeList.add(new CountryCode_Dto(-1,"","","","",false));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String isdCode = jsonObject1.optString("isdCode");
                        boolean whetherDefault = jsonObject1.optBoolean("whetherDefault");
                        String maximumDigit = jsonObject1.optString("maximumDigit");
                        String minimumDigit = jsonObject1.optString("minimumDigit");
                        String countryName = jsonObject1.optString("countryName");

                        countryCodeList.add(new CountryCode_Dto(id,isdCode,maximumDigit,minimumDigit,countryName,whetherDefault));
                    }

                    countryCodeAdapter = new CountryCodeAdapter(mContext,countryCodeList);
                    spnContactCode.setAdapter(countryCodeAdapter);
                    countryCodeAdapter.notifyDataSetChanged();

                    spnContactCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            etContact.setText("");

                            String maxLength = countryCodeList.get(position).getMaximumDigit();
                            String miniLength = countryCodeList.get(position).getMinimumDigit();

                            if(!maxLength.equalsIgnoreCase("")){

                                lengthMax = 0;
                                lengthMini = 0;

                                lengthMax = Integer.parseInt(maxLength);
                                lengthMini = Integer.parseInt(miniLength);
                                etContact.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMax)});
                            }else{
                                lengthMax = 15;
                                lengthMini = 7;
                                etContact.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMax)});
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_MEDICAL_CONDITION_CREATE:
                hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    if(jsonObject.has("message")){
                        ProjectUtils.showLong(mContext,"Medical Condition added successfully!");
                        AcademiaApp.IS_UPDATE = true;
                        onBackPressed();
                    }else{
                        showAlertDialog2(MedicalConditionUploadActivity.this, getString(R.string.oops), getString(R.string.something_went_wrong));
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    showAlertDialog2(MedicalConditionUploadActivity.this, getString(R.string.oops), getString(R.string.something_went_wrong));
                }
                break;
        }
    }


    private void Initialize() {

        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);

        tvTitle.setText("ADD");

        tvConditionType1 = findViewById(R.id.tvConditionType1);
        tvMedicalCondition1 = findViewById(R.id.tvMedicalCondition1);
        tvSince1 = findViewById(R.id.tvSince1);
        tvConsultingDoctor1 = findViewById(R.id.tvConsultingDoctor1);
        tvContactNumber1 = findViewById(R.id.tvContactNumber1);
        tvPrecuation1 = findViewById(R.id.tvPrecuation1);

        etMedicalCondition = findViewById(R.id.etMedicalCondition);
        etConsultingDoctor = findViewById(R.id.etConsultingDoctor);
        etContact = findViewById(R.id.etContact);
        etPrecuation = findViewById(R.id.etPrecuation);
        tvSince = findViewById(R.id.tvSince);
        btnSubmit = findViewById(R.id.btnSubmit);

        spnConditionType = findViewById(R.id.spnConditionType);
        spnContactCode = findViewById(R.id.spnContactCode);

        tvConditionType1.setText(translationManager.CONDITION_TYPE_KEY);
        tvSince1.setText(translationManager.SINCE_KEY);
        tvConsultingDoctor1.setText(translationManager.CONSULTING_DOCTOR_KEY);
        tvContactNumber1.setText(translationManager.CONTACT_NUMBER_KEY);
        tvMedicalCondition1.setText(translationManager.MEDICAL_CONDITION_KEY);
        tvPrecuation1.setText(translationManager.PRECAUTION_MEDICATION_KEY);

        ivClose.setOnClickListener(this);
        tvSince.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        //Here set current from and to date
        calendarDefault = Calendar.getInstance();
        mYear = calendarDefault.get(Calendar.YEAR);
        mMonth = calendarDefault.get(Calendar.MONTH);
        mDay = calendarDefault.get(Calendar.DAY_OF_MONTH);

        date2 = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);

        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equalsIgnoreCase("")){

                    if(!ProjectUtils.isPhoneNumberValid(lengthMini,lengthMax,etContact.getText().toString())){

                        etContact.setError("Please enter "+translationManager.CONTACT_NUMBER_KEY+" between "+lengthMini+" to "+lengthMax+"");
                        etContact.requestFocus();
                        btnSubmit.setEnabled(false);
                    }else{
                        btnSubmit.setEnabled(true);
                    }
                }else{
                    btnSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id){
            case R.id.ivClose:{
                finish();
            }break;

            case R.id.tvSince:{

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                date1 = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                startDate = ProjectUtils.convertTimestampToDate(date1,mContext);

                                tvSince.setText(startDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(date2);
                datePickerDialog.setTitle("");
                datePickerDialog.show();

            }break;

            case R.id.btnSubmit:{

                if(spnConditionType.getSelectedItemPosition() == 0) {

                    ProjectUtils.showLong(mContext,"Please select "+translationManager.CONDITION_TYPE_KEY);
                }
                else if(etMedicalCondition.getText().toString().trim().length() == 0){

                    ProjectUtils.showLong(mContext,"Please enter "+translationManager.MEDICAL_CONDITION_KEY);
                }
                else{

                    ProjectUtils.hideKeyboard(MedicalConditionUploadActivity.this);
                    ProjectUtils.preventTwoClick(btnSubmit);

                    String formatDate = ProjectUtils.getTimestampToDate(mContext);
                    String consultingDoctor = etConsultingDoctor.getText().toString().trim();
                    String dateSince = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(tvSince.getText().toString().trim()));
                    String countryCode = spnContactCode.getSelectedItem().toString().trim();
                    String contact = etContact.getText().toString().trim();
                    String medicalCondition = etMedicalCondition.getText().toString().trim();
                    String remark = etPrecuation.getText().toString().trim();

                    showProgressDialog(this);
                    new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_MEDICAL_CONDITION_CREATE)
                            .execute(medConditionTypeId,consultingDoctor,dateSince,countryCode,contact,medicalCondition,remark);
                    }
            }break;
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