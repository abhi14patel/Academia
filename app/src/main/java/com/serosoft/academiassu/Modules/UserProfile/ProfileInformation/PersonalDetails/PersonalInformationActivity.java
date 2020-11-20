package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Utils.RSAEncryption;

import java.util.HashMap;

public class PersonalInformationActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;

    private String userName="";
    private String password="";

    private TextView tvStudentName1,tvStudentId1,tvAdmissionId1,tvGender1,tvDOB1,tvEmailId1,tvPhoneNumber1,tvMobileNumber1,tvAddress1,tvAdmissionDate1,tvFatherName1,tvMotherName1,tvReligion1,tvCasteCategory1,tvBloodGroup1;
    private TextView tvStudentName,tvStudentId,tvAdmissionId,tvGender,tvDOB,tvEmailId,tvPhoneNumber,tvMobileNumber,tvAddress,tvAdmissionDate,tvFatherName,tvMotherName,tvReligion,tvCasteCategory,tvBloodGroup;
    private LinearLayout llGender,llDOB,llEmailId,llContact1,llContact2,llAddress,llAdmissionDate,llFatherName,llMotherName,llReligion,llCasteCategory,llBloodGroup,llContact,llGeneral;

    private boolean fabExpanded = false;
    private FloatingActionButton fabEdit;
    private LinearLayout llSubMenu,llBankDetails,llOtherInformation,llContactDetails,llPersonalDetails;
    private TextView tvPersonalDetails,tvBankDetails,tvOtherInformation,tvContactDetails;

    private final String TAG = PersonalInformationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = PersonalInformationActivity.this;

        Initialize();

        setValues();
    }

    private void setValues() {

        tvStudentName.setText(sharedPrefrenceManager.getUserFirstNameFromKey() + " " + sharedPrefrenceManager.getUserLastNameFromKey());
        tvStudentId.setText(sharedPrefrenceManager.getUserCodeFromKey());
        tvAdmissionId.setText(sharedPrefrenceManager.getAdmissionCodeInSP());

        String gender = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentGenderFromKey());
        if(!gender.equalsIgnoreCase("")){
            llGender.setVisibility(View.VISIBLE);
            tvGender.setText(gender);
        }else{
            llGender.setVisibility(View.GONE);
        }

        String dob = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getDateOfBirthFromKey());
        if(!dob.equalsIgnoreCase("")){
            llDOB.setVisibility(View.VISIBLE);
            tvDOB.setText(dob);
        }else{
            llDOB.setVisibility(View.GONE);
        }

        String emailId = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getUserEmailFromKey());
        if(!emailId.equalsIgnoreCase("")){
            llEmailId.setVisibility(View.VISIBLE);
            tvEmailId.setText(emailId);
        }else{
            llEmailId.setVisibility(View.GONE);
        }

        String contact1 = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getContact2FromKey());
        if(!contact1.equalsIgnoreCase("")){
            llContact1.setVisibility(View.VISIBLE);
            tvPhoneNumber.setText(contact1);
        }else{
            llContact1.setVisibility(View.GONE);
        }

        String contact2 = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getContact1FromKey());
        if(!contact2.equalsIgnoreCase("")){
            llContact2.setVisibility(View.VISIBLE);
            tvMobileNumber.setText(contact2);
        }else{
            llContact2.setVisibility(View.GONE);
        }

        //If both contact1 and contact2 empty
        if(contact1.equalsIgnoreCase("") && contact2.equalsIgnoreCase("")){
            llContact.setVisibility(View.GONE);
        }else{
            llContact.setVisibility(View.VISIBLE);
        }

        String address = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getAddressFromKey());
        if(!address.equalsIgnoreCase("")){
            llAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(address);
        }else{
            llAddress.setVisibility(View.GONE);
        }

        String admissionDate = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getDateOfRegistrationFromKey());
        if(!admissionDate.equalsIgnoreCase("")){
            llAdmissionDate.setVisibility(View.VISIBLE);
            tvAdmissionDate.setText(admissionDate);
        }else{
            llAdmissionDate.setVisibility(View.GONE);
        }

        String fatherName = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentFathersNameFromKey());
        if(!fatherName.equalsIgnoreCase("")){
            llFatherName.setVisibility(View.VISIBLE);
            tvFatherName.setText(fatherName);
        }else{
            llFatherName.setVisibility(View.GONE);
        }

        String motherName = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentMothersNameFromKey());
        if(!motherName.equalsIgnoreCase("")){
            llMotherName.setVisibility(View.VISIBLE);
            tvMotherName.setText(motherName);
        }else{
            llMotherName.setVisibility(View.GONE);
        }

        String religion = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentReligionFromKey());
        if(!admissionDate.equalsIgnoreCase("")){
            llReligion.setVisibility(View.VISIBLE);
            tvReligion.setText(religion);
        }else{
            llReligion.setVisibility(View.GONE);
        }

        String caste = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentCasteFromKey());
        if(!admissionDate.equalsIgnoreCase("")){
            llCasteCategory.setVisibility(View.VISIBLE);
            tvCasteCategory.setText(caste);
        }else{
            llCasteCategory.setVisibility(View.GONE);
        }

        if(!religion.equalsIgnoreCase("") && !caste.equalsIgnoreCase("")){
            llGeneral.setVisibility(View.VISIBLE);
        }else{
            llGeneral.setVisibility(View.GONE);
        }

        String bloodGroup = ProjectUtils.getCorrectedString2(sharedPrefrenceManager.getStudentBloodGroupFromKey());
        if(!bloodGroup.equalsIgnoreCase("")){
            llBloodGroup.setVisibility(View.VISIBLE);
            tvBloodGroup.setText(bloodGroup);
        }else{
            llBloodGroup.setVisibility(View.GONE);
        }
    }

    private void Initialize()
    {
        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.PERSONAL_DETAILS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        llGender = findViewById(R.id.llGender);
        llDOB = findViewById(R.id.llDOB);
        llEmailId = findViewById(R.id.llEmailId);
        llContact1 = findViewById(R.id.llContact1);
        llContact2 = findViewById(R.id.llContact2);
        llAddress = findViewById(R.id.llAddress);
        llAdmissionDate = findViewById(R.id.llAdmissionDate);
        llFatherName = findViewById(R.id.llFatherName);
        llMotherName = findViewById(R.id.llMotherName);
        llReligion = findViewById(R.id.llReligion);
        llCasteCategory = findViewById(R.id.llCasteCategory);
        llBloodGroup = findViewById(R.id.llBloodGroup);
        llContact = findViewById(R.id.llContact);
        llGeneral = findViewById(R.id.llGeneral);

        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvAdmissionId = findViewById(R.id.tvAdmissionId);
        tvGender = findViewById(R.id.tvGender);
        tvDOB = findViewById(R.id.tvDOB);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvAdmissionDate = findViewById(R.id.tvAdmissionDate);
        tvFatherName = findViewById(R.id.tvFatherName);
        tvMotherName = findViewById(R.id.tvMotherName);
        tvReligion = findViewById(R.id.tvReligion);
        tvCasteCategory = findViewById(R.id.tvCasteCategory);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);

        tvStudentName1 = findViewById(R.id.tvStudentName1);
        tvStudentId1 = findViewById(R.id.tvStudentId1);
        tvAdmissionId1 = findViewById(R.id.tvAdmissionId1);
        tvGender1 = findViewById(R.id.tvGender1);
        tvDOB1 = findViewById(R.id.tvDOB1);
        tvEmailId1 = findViewById(R.id.tvEmailId1);
        tvPhoneNumber1 = findViewById(R.id.tvPhoneNumber1);
        tvMobileNumber1 = findViewById(R.id.tvMobileNumber1);
        tvAddress1 = findViewById(R.id.tvAddress1);
        tvAdmissionDate1 = findViewById(R.id.tvAdmissionDate1);
        tvFatherName1 = findViewById(R.id.tvFatherName1);
        tvMotherName1 = findViewById(R.id.tvMotherName1);
        tvReligion1 = findViewById(R.id.tvReligion1);
        tvCasteCategory1 = findViewById(R.id.tvCasteCategory1);
        tvBloodGroup1 = findViewById(R.id.tvBloodGroup1);

        fabEdit = findViewById(R.id.fabEdit);
        llSubMenu = findViewById(R.id.llSubMenu);
        llBankDetails = findViewById(R.id.llBankDetails);
        llOtherInformation = findViewById(R.id.llOtherInformation);
        llContactDetails = findViewById(R.id.llContactDetails);
        llPersonalDetails = findViewById(R.id.llPersonalDetails);

        tvPersonalDetails = findViewById(R.id.tvPersonalDetails);
        tvBankDetails = findViewById(R.id.tvBankDetails);
        tvOtherInformation = findViewById(R.id.tvOtherInformation);
        tvContactDetails = findViewById(R.id.tvContactDetails);

        fabEdit.setOnClickListener(this);
        llBankDetails.setOnClickListener(this);
        llOtherInformation.setOnClickListener(this);
        llContactDetails.setOnClickListener(this);
        llPersonalDetails.setOnClickListener(this);

        tvStudentName1.setText(translationManager.STUDENT_NAME_KEY);
        tvStudentId1.setText(translationManager.STUDENTID_KEY);
        tvAdmissionId1.setText(translationManager.ADMISSIONID_KEY);
        tvEmailId1.setText(translationManager.EMAILID_KEY);
        tvPhoneNumber1.setText(translationManager.PHONE_NUMBER_KEY);
        tvMobileNumber1.setText(translationManager.MOBILE_NUMBER_KEY);
        tvAddress1.setText(translationManager.ADDRESS_KEY);
        tvDOB1.setText(translationManager.DATE_OF_BIRTH_KEY);
        tvAdmissionDate1.setText(translationManager.DATE_OF_ADMISSION_KEY);
        tvGender1.setText(translationManager.GENDER_KEY);
        tvFatherName1.setText(translationManager.FATHER_NAME_KEY);
        tvMotherName1.setText(translationManager.MOTHER_NAME_KEY);
        tvReligion1.setText(translationManager.RELIGION_KEY);
        tvCasteCategory1.setText(translationManager.CASTE_KEY);
        tvBloodGroup1.setText(translationManager.BLOOD_GROUP_KEY);

        closeSubMenusFab();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id)
        {
            case R.id.fabEdit:
                if (fabExpanded){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
                break;

            case R.id.llBankDetails:
                /*startActivity(new Intent(mContext,MyBankDetailsActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                closeSubMenusFab();*/
                break;

            case R.id.llOtherInformation:
                /*startActivity(new Intent(mContext,MyOtherInformationActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                closeSubMenusFab();*/
                break;

            case R.id.llContactDetails:
                /*startActivity(new Intent(mContext,MyContactDetailsActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                closeSubMenusFab();*/
                break;

            case R.id.llPersonalDetails:
                startActivity(new Intent(mContext,MyPersonalDetailsActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                closeSubMenusFab();
                break;
        }
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
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }break;

            case R.id.refresh:
            {
                getNotifications();
                populateContents();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateContents() {
        if (sharedPrefrenceManager.getIsParentStatusFromKey()) {
            if (ConnectionDetector.isConnectingToInternet(mContext)) {

                showProgressDialog(mContext);
                new OptimizedServerCallAsyncTask(mContext, PersonalInformationActivity.this,
                        KEYS.SWITCH_SWITCH_STUDENT).execute();
            }
            else
                showAlertDialog(this, getString(R.string.network_error), getString(R.string.please_check_your_network_connection));

        } else {
            userName = sharedPrefrenceManager.getUserNameFromKey();
            password = sharedPrefrenceManager.getPasswordFromKey();

            if (((null != userName) && (!userName.equals(""))) && ((null != password) && (!password.equals("")))) {
                showProgressDialog(this);
                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_USER_LOGIN).
                        execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
            }
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        String returnResult = response.get(KEYS.RETURN_RESULT);

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
            return;
        }

        switch (callFor)
        {
            case KEYS.SWITCH_USER_LOGIN:
                if (returnResult == KEYS.TRUE) {
                    hideProgressDialog();
                    setValues();
                } else{
                    userName = sharedPrefrenceManager.getUserNameFromKey();
                    String password1 = sharedPrefrenceManager.getPasswordFromKey();
                    password = RSAEncryption.encryptData(password1);

                    new OptimizedServerCallAsyncTask(PersonalInformationActivity.this, PersonalInformationActivity.this, KEYS.SWITCH_USER_LOGIN_INCREPTION).
                            execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                }
                break;

            case KEYS.SWITCH_USER_LOGIN_INCREPTION:
                hideProgressDialog();
                if (returnResult == KEYS.TRUE) {
                    setValues();
                } else{
                    showAlertDialog(this, getString(R.string.info), getString(R.string.unexpected_error));
                }
                break;

            case KEYS.SWITCH_SWITCH_STUDENT:
                hideProgressDialog();
                if (returnResult == KEYS.TRUE) {
                    setValues();
                    getNotifications();
                } else{
                    showAlertDialog(this, getString(R.string.info), getString(R.string.unexpected_error));
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
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        llSubMenu.setVisibility(View.INVISIBLE);
        fabEdit.setImageResource(R.drawable.ic_edit);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        llSubMenu.setVisibility(View.VISIBLE);
        fabEdit.setImageResource(R.drawable.ic_edit);
        fabExpanded = true;
    }
}
