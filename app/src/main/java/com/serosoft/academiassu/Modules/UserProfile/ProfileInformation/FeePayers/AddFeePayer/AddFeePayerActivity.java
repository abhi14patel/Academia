package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers.AddFeePayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.serosoft.academiassu.CommonClass.ParentAdapter;
import com.serosoft.academiassu.CommonClass.Parent_Dto;
import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Helpers.FilePath;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.CountryCodeAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.CountryCode_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters.SalutationAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Profile_Dto;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.AddFeePayerActivityBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AddFeePayerActivity extends BaseActivity {

    AddFeePayerActivityBinding binding;
    private Context mContext;

    ArrayList<Profile_Dto> salutationList;
    SalutationAdapter salutationAdapter;

    ArrayList<Country_Dto> countryList;
    CountryAdapter countryAdapter;

    private int isWeatherDefault = 0;
    ArrayList<CountryCode_Dto> countryCodeList;
    CountryCodeAdapter countryCodeAdapter;

    String currentCountry = "";
    int currentCountryId = 0;
    String currentCountryRegion = "";
    int currentCountryRegionId = 0;
    String currentCity = "";
    int currentCityId = 0;

    ArrayList<Parent_Dto> countryRegionList;
    ParentAdapter countryRegionAdapter;

    ArrayList<Parent_Dto> cityList;
    ParentAdapter cityAdapter;

    ArrayList<Parent_Dto> debitOrderDateList;
    ParentAdapter debitOrderDateAdapter;

    ArrayList<Parent_Dto> bankNameList;
    ParentAdapter bankNameAdapter;

    ArrayList<Parent_Dto> branchCodeList;
    ParentAdapter branchCodeAdapter;

    int titleId = 0;
    int debitOrderId = 0;
    String cId = "";
    String cRegionId = "";
    String cityId = "";
    String bankId = "";
    String branchCode = "";

    int lengthMaxTH = 15;
    int lengthMiniTH = 7;

    int lengthMaxTW = 15;
    int lengthMiniTW = 7;

    int lengthMaxMN = 15;
    int lengthMiniMN = 7;

    File filePath1,filePath2;
    Uri uri1,uri2;
    String docFileName1 = "",docFileName2 = "";
    private static final int PICKFILE_RESULT_CODE1 = 1;
    private static final int PICKFILE_RESULT_CODE2 = 2;

    private final String TAG = AddFeePayerActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddFeePayerActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AddFeePayerActivity.this;

        Initialize();

        populateCountryContent();
        populateContent();
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(this))
        {
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PROFILE_SALUTATIONS).execute();
        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCountryContent() {

        if (ConnectionDetector.isConnectingToInternet(this))
        {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE).execute();
        }
        else
        {
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
            case KEYS.SWITCH_PROFILE_SALUTATIONS:
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

                    salutationAdapter = new SalutationAdapter(mContext,salutationList);
                    binding.spnTitle.setAdapter(salutationAdapter);

                    binding.spnTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Profile_Dto profile_dto = (Profile_Dto) binding.spnTitle.getSelectedItem();
                            titleId = profile_dto.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.disableSpinner(binding.spnTitle);
                }
                break;

            case KEYS.SWITCH_MEDICAL_CONDITION_COUNTRY_CODE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    currentCityId = 0;
                    countryList = new ArrayList<>();
                    countryCodeList = new ArrayList<>();

                    countryList.add(new Country_Dto(0,"","","","Select",false));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String isdCode = jsonObject1.optString("isdCode");
                        boolean whetherDefault = jsonObject1.optBoolean("whetherDefault");
                        String maximumDigit = jsonObject1.optString("maximumDigit");
                        String minimumDigit = jsonObject1.optString("minimumDigit");
                        String countryName = jsonObject1.optString("countryName");

                        countryList.add(new Country_Dto(id,isdCode,maximumDigit,minimumDigit,countryName,whetherDefault));
                        countryCodeList.add(new CountryCode_Dto(id,isdCode,maximumDigit,minimumDigit,countryName,whetherDefault));
                    }

                    if(countryList != null && countryList.size() > 0){

                        //Here set current country selected
                        for(int i = 0 ; i< countryList.size() ; i++){

                            Country_Dto country_dto = countryList.get(i);

                            if(country_dto.getCountryName().equalsIgnoreCase(currentCountry))
                            {
                                currentCountryId = i;
                                break;
                            }
                        }

                        countryAdapter = new CountryAdapter(mContext,countryList);
                        binding.spnCountry.setAdapter(countryAdapter);
                        binding.spnCountry.setSelection(currentCountryId);
                        binding.spnCountry.setTitle("Select Country");
                    }

                    binding.spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Country_Dto country_dto = (Country_Dto) binding.spnCountry.getSelectedItem();

                            if(!country_dto.getCountryName().equalsIgnoreCase("Select")){
                                cId = String.valueOf(country_dto.getId());
                                populateCountryRegion(cId);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    if(countryCodeList != null && countryCodeList.size() > 0){

                        //Here set current program selected
                        for(int i = 0 ; i< countryCodeList.size() ; i++){

                            CountryCode_Dto countryCode_dto = countryCodeList.get(i);

                            if(countryCode_dto.isWhetherDefault())
                            {
                                isWeatherDefault = i;
                                break;
                            }
                        }

                        countryCodeAdapter = new CountryCodeAdapter(mContext,countryCodeList);
                        binding.spnHTCountryCode.setAdapter(countryCodeAdapter);
                        binding.spnHTCountryCode.setSelection(isWeatherDefault);

                        binding.spnHTCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                binding.etHomeTelephone.setText("");

                                String maxLength = countryCodeList.get(position).getMaximumDigit();
                                String miniLength = countryCodeList.get(position).getMinimumDigit();

                                if(!maxLength.equalsIgnoreCase("")){

                                    lengthMaxTH = 0;
                                    lengthMiniTH = 0;

                                    lengthMaxTH = Integer.parseInt(maxLength);
                                    lengthMiniTH = Integer.parseInt(miniLength);
                                    binding.etHomeTelephone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxTH)});
                                }else{
                                    lengthMaxTH = 15;
                                    lengthMiniTH = 7;
                                    binding.etHomeTelephone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxTH)});
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                        countryCodeAdapter = new CountryCodeAdapter(mContext,countryCodeList);
                        binding.spnWTCountryCode.setAdapter(countryCodeAdapter);
                        binding.spnWTCountryCode.setSelection(isWeatherDefault);

                        binding.spnWTCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                binding.etWorkTelephone.setText("");

                                String maxLength = countryCodeList.get(position).getMaximumDigit();
                                String miniLength = countryCodeList.get(position).getMinimumDigit();

                                if(!maxLength.equalsIgnoreCase("")){

                                    lengthMaxTW = 0;
                                    lengthMiniTW = 0;

                                    lengthMaxTW = Integer.parseInt(maxLength);
                                    lengthMiniTW = Integer.parseInt(miniLength);
                                    binding.etWorkTelephone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxTW)});
                                }else{
                                    lengthMaxTW = 15;
                                    lengthMiniTW = 7;
                                    binding.etWorkTelephone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxTW)});
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                        countryCodeAdapter = new CountryCodeAdapter(mContext,countryCodeList);
                        binding.spnMNCountryCode.setAdapter(countryCodeAdapter);
                        binding.spnMNCountryCode.setSelection(isWeatherDefault);

                        binding.spnMNCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                binding.etMobileNumber.setText("");

                                String maxLength = countryCodeList.get(position).getMaximumDigit();
                                String miniLength = countryCodeList.get(position).getMinimumDigit();

                                if(!maxLength.equalsIgnoreCase("")){

                                    lengthMaxMN = 0;
                                    lengthMiniMN = 0;

                                    lengthMaxMN = Integer.parseInt(maxLength);
                                    lengthMiniMN = Integer.parseInt(miniLength);
                                    binding.etMobileNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxMN)});
                                }else{
                                    lengthMaxMN = 15;
                                    lengthMiniMN = 7;
                                    binding.etMobileNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthMaxMN)});
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.disableSpinner(binding.spnCountry);
                    ProjectUtils.disableSpinner(binding.spnHTCountryCode);
                    ProjectUtils.disableSpinner(binding.spnWTCountryCode);
                    ProjectUtils.disableSpinner(binding.spnMNCountryCode);
                }
                break;

            case KEYS.SWITCH_FEE_PAYER_CURRENT_ADDRESS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONObject jsonObject1 = jsonObject.optJSONObject("address");
                    String addressText = jsonObject1.optString("addressText");
                    binding.etAddress.setText(addressText);

                    JSONObject jsonObjectCountry = jsonObject1.optJSONObject("country");
                    currentCountry = jsonObjectCountry.optString("value");

                    JSONObject jsonObjectCountryRegion = jsonObject1.optJSONObject("parentCountryRegion");
                    currentCountryRegion = jsonObjectCountryRegion.optString("value");

                    JSONObject jsonObjectcity = jsonObject1.optJSONObject("city");
                    currentCity = jsonObjectcity.optString("value");

                    //here again call api for showing selected data
                    populateCountryContent();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;

            case KEYS.SWITCH_COUNTRY_REGION_CODE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    currentCountryRegionId = 0;
                    countryRegionList = new ArrayList<>();
                    countryRegionList.add(new Parent_Dto(0, "Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("countryRegionName");

                        countryRegionList.add(new Parent_Dto(id, value));
                    }

                    if(countryRegionList != null && countryRegionList.size() > 0){

                        //Here set current country selected
                        for(int i = 0 ; i< countryRegionList.size() ; i++){

                            Parent_Dto parent_dto = countryRegionList.get(i);

                            if(parent_dto.getValue().equalsIgnoreCase(currentCountryRegion))
                            {
                                currentCountryRegionId = i;
                                break;
                            }
                        }

                        countryRegionAdapter = new ParentAdapter(mContext,countryRegionList);
                        binding.spnRegion.setAdapter(countryRegionAdapter);
                        binding.spnRegion.setSelection(currentCountryRegionId);
                        binding.spnRegion.setTitle("Select Country Region");
                    }

                    binding.spnRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Parent_Dto parent_dto = (Parent_Dto) binding.spnRegion.getSelectedItem();

                            if(!parent_dto.getValue().equalsIgnoreCase("Select")){
                                cRegionId = String.valueOf(parent_dto.getId());
                                populateCountryRegionCity(cId,cRegionId);
                            }else{
                                currentCity = "";
                                currentCityId = 0;
                                populateCountryRegionCity(cId,"");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.disableSpinner(binding.spnRegion);
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_FEE_PAYER_CITY:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    currentCityId = 0;
                    cityList = new ArrayList<>();
                    cityList.add(new Parent_Dto(0, "Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        cityList.add(new Parent_Dto(id, value));
                    }

                    if(cityList != null && cityList.size() > 0){

                        //Here set current country selected
                        for(int i = 0 ; i< cityList.size() ; i++){

                            Parent_Dto parent_dto = cityList.get(i);

                            if(parent_dto.getValue().equalsIgnoreCase(currentCity))
                            {
                                currentCityId = i;
                                break;
                            }
                        }

                        cityAdapter = new ParentAdapter(mContext,cityList);
                        binding.spnCity.setAdapter(cityAdapter);
                        binding.spnCity.setSelection(currentCityId);
                        binding.spnCity.setTitle("Select City");

                        binding.spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Parent_Dto parent_dto = (Parent_Dto) binding.spnCity.getSelectedItem();

                                cityId = String.valueOf(parent_dto.getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.disableSpinner(binding.spnCity);
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_DEBIT_ORDER_DATE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    debitOrderDateList = new ArrayList<>();

                    debitOrderDateList.add(new Parent_Dto(0, "Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        debitOrderDateList.add(new Parent_Dto(id, value));
                    }

                    debitOrderDateAdapter = new ParentAdapter(mContext,debitOrderDateList);
                    binding.spnDebitOrderDate.setAdapter(debitOrderDateAdapter);

                    binding.spnDebitOrderDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Parent_Dto parent_dto = (Parent_Dto) binding.spnDebitOrderDate.getSelectedItem();

                            debitOrderId = parent_dto.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.disableSpinner(binding.spnDebitOrderDate);
                }
                break;

            case KEYS.SWITCH_FEE_BANK_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    bankNameList = new ArrayList<>();

                    bankNameList.add(new Parent_Dto(0, "Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        bankNameList.add(new Parent_Dto(id, value));
                    }

                    bankNameAdapter = new ParentAdapter(mContext,bankNameList);
                    binding.spnBankName.setAdapter(bankNameAdapter);

                    binding.spnBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Parent_Dto parent_dto = (Parent_Dto) binding.spnBankName.getSelectedItem();

                            bankId = String.valueOf(parent_dto.getId());
                            populateBranchCodeContent(bankId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.disableSpinner(binding.spnBankName);
                }
                break;

            case KEYS.SWITCH_FEE_BANK_BRANCH_CODE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    branchCodeList = new ArrayList<>();

                    branchCodeList.add(new Parent_Dto(0, "Select"));

                    for(int i = 0 ; i<jsonArray.length() ; i++){

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        int id = jsonObject1.optInt("id");
                        String value = jsonObject1.optString("value");

                        branchCodeList.add(new Parent_Dto(id, value));
                    }

                    branchCodeAdapter = new ParentAdapter(mContext,branchCodeList);
                    binding.spnBranchCode.setAdapter(branchCodeAdapter);

                    binding.spnBranchCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Parent_Dto parent_dto = (Parent_Dto) binding.spnBranchCode.getSelectedItem();

                            branchCode = String.valueOf(parent_dto.getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.disableSpinner(binding.spnBranchCode);
                }
                break;
        }
    }

    private void populateCountryRegion(String cId) {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_COUNTRY_REGION_CODE).execute(cId);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEE_PAYER_CITY).execute(cId,"");
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCountryRegionCity(String cId, String cRegionId) {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEE_PAYER_CITY).execute(cId,cRegionId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateBankDetailsContent() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_DEBIT_ORDER_DATE).execute();
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEE_BANK_LIST).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateBranchCodeContent(String bankId) {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEE_BANK_BRANCH_CODE).execute(bankId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCurrentAddressContent() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEE_PAYER_CURRENT_ADDRESS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        binding.header.tvTitle.setText("Add Fee Payer Details");

        binding.header.ivClose.setOnClickListener(this);
        binding.btnBrowse1.setOnClickListener(this);
        binding.ivCancel1.setOnClickListener(this);
        binding.btnBrowse2.setOnClickListener(this);
        binding.ivCancel2.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        binding.spnPayerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String type = binding.spnPayerType.getItemAtPosition(position).toString();
                if(type.equalsIgnoreCase("Person")){
                    uiUpdate(true);
                }else{
                    uiUpdate(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.switchSameCurrentAddress.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if(isOn){
                    populateCurrentAddressContent();
                }else{
                    binding.etAddress.setText("");

                    currentCountry = "";
                    currentCountryRegion = "";
                    currentCity = "";
                    binding.spnCountry.setSelection(0);
                    binding.spnRegion.setSelection(0);
                    binding.spnCity.setSelection(0);
                }
            }
        });

        binding.switchEmployeeOrganization.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if(isOn){
                    binding.llEmployeeId.setVisibility(View.VISIBLE);
                }else{
                    binding.llEmployeeId.setVisibility(View.GONE);
                }
            }
        });

        binding.spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String type = binding.spnPaymentMode.getItemAtPosition(position).toString();
                if(type.equalsIgnoreCase("EFT")){
                    binding.llDebit.setVisibility(View.GONE);
                }else{
                    binding.llDebit.setVisibility(View.VISIBLE);
                    populateBankDetailsContent();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.etHomeTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equalsIgnoreCase("")){

                    if(!ProjectUtils.isPhoneNumberValid(lengthMiniTH,lengthMaxTH,binding.etHomeTelephone.getText().toString())){

                        binding.etHomeTelephone.setError("Please enter Telephone (Home) between "+lengthMiniTH+" to "+lengthMaxTH+"");
                        binding.etHomeTelephone.requestFocus();

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.etWorkTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equalsIgnoreCase("")){

                    if(!ProjectUtils.isPhoneNumberValid(lengthMiniTH,lengthMaxTH,binding.etWorkTelephone.getText().toString())){

                        binding.etWorkTelephone.setError("Please enter Telephone (Work) between "+lengthMiniTW+" to "+lengthMaxTW+"");
                        binding.etWorkTelephone.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equalsIgnoreCase("")){

                    if(!ProjectUtils.isPhoneNumberValid(lengthMiniTH,lengthMaxTH,binding.etMobileNumber.getText().toString())){

                        binding.etMobileNumber.setError("Please enter Mobile Number between "+lengthMiniMN+" to "+lengthMaxMN+"");
                        binding.etMobileNumber.requestFocus();
                    }
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

            case R.id.btnBrowse1:{

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
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE1);

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

            case R.id.ivCancel1: {
                binding.llAttachment1.setVisibility(View.GONE);
                binding.llSelectFile1.setVisibility(View.VISIBLE);
                binding.tvAttachment1.setText("");
                filePath1 = null;
                uri1 = null;
            }break;

            case R.id.btnBrowse2:{

                if(Build.VERSION.SDK_INT>22)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                }
                else
                {
                    try
                    {
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE2);

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

            case R.id.ivCancel2: {
                binding.llAttachment2.setVisibility(View.GONE);
                binding.llSelectFile2.setVisibility(View.VISIBLE);
                binding.tvAttachment2.setText("");
                filePath2 = null;
                uri2 = null;
            }break;

            case R.id.btnSubmit:{

                if(binding.spnPayerType.getSelectedItem().toString().equalsIgnoreCase("Person")){

                    if(binding.spnTitle.getSelectedItemPosition() == 0) {

                        ProjectUtils.showLong(mContext,"Please select title");
                    }
                    else if(binding.etFname.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter first name");
                        binding.etFname.requestFocus();
                    }
                    else if(binding.etLname.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter last name");
                        binding.etLname.requestFocus();
                    }
                    else if(binding.etHomeTelephone.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter telephone (Home)");
                        binding.etHomeTelephone.requestFocus();
                    }
                    else if(!ProjectUtils.isPhoneNumberValid(lengthMiniTH,lengthMaxTH,binding.etHomeTelephone.getText().toString())){

                        ProjectUtils.showLong(mContext,"Please enter telephone (Home) between "+lengthMiniTH+" to "+lengthMaxTH+"");
                        binding.etHomeTelephone.requestFocus();
                    }
                    else if(binding.etWorkTelephone.getText().toString().length() != 0 && !ProjectUtils.isPhoneNumberValid(lengthMiniTW,lengthMaxTW,binding.etWorkTelephone.getText().toString())){

                        if(!ProjectUtils.isPhoneNumberValid(lengthMiniTW,lengthMaxTW,binding.etWorkTelephone.getText().toString())){

                            ProjectUtils.showLong(mContext,"Please enter telephone (Work) between "+lengthMiniTW+" to "+lengthMaxTW+"");
                            binding.etWorkTelephone.requestFocus();
                        }
                    }
                    else if(binding.etMobileNumber.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter mobile number");
                        binding.etMobileNumber.requestFocus();
                    }
                    else if(!ProjectUtils.isPhoneNumberValid(lengthMiniMN,lengthMaxMN,binding.etMobileNumber.getText().toString())){

                        ProjectUtils.showLong(mContext,"Please enter mobile number between "+lengthMiniMN+" to "+lengthMaxMN+"");
                        binding.etMobileNumber.requestFocus();
                    }
                    else if(binding.etHomeEmail.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter email (Home)");
                        binding.etHomeEmail.requestFocus();
                    }
                    else if(!ProjectUtils.isEmailValid(binding.etHomeEmail.getText().toString())){

                        ProjectUtils.showLong(mContext,"Please enter valid home email");
                        binding.etHomeEmail.requestFocus();
                    }
                    else if(binding.etWorkEmail.getText().toString().length() != 0 && !ProjectUtils.isEmailValid(binding.etWorkEmail.getText().toString())){

                        if(!ProjectUtils.isEmailValid(binding.etWorkEmail.getText().toString())){

                            ProjectUtils.showLong(mContext,"Please enter valid work email");
                            binding.etWorkEmail.requestFocus();
                        }
                    }
                    else if(filePath1 == null){

                        ProjectUtils.showLong(mContext,"Please choose fee payer id");
                    }else{
                        showProgressDialog(mContext);
                        feePayerDocumentUpload(filePath1,docFileName1,filePath2,docFileName2);
                    }
                }else{

                    if(binding.etCompanyName.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter company name");
                        binding.etCompanyName.requestFocus();
                    }
                    else if(binding.etCPFirstName.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter first name");
                        binding.etCPFirstName.requestFocus();
                    }
                    else if(binding.etCPLastName.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter last name");
                        binding.etCPLastName.requestFocus();
                    }
                    else if(binding.etWorkTelephone.getText().toString().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter telephone (Work)");
                        binding.etWorkTelephone.requestFocus();
                    }
                    else if(!ProjectUtils.isPhoneNumberValid(lengthMiniTW,lengthMaxTW,binding.etWorkTelephone.getText().toString())){

                        ProjectUtils.showLong(mContext,"Please enter telephone (Work) between "+lengthMiniTW+" to "+lengthMaxTW+"");
                        binding.etWorkTelephone.requestFocus();
                    }
                    else if(binding.etMobileNumber.getText().toString().trim().length() != 0 && !ProjectUtils.isPhoneNumberValid(lengthMiniMN,lengthMaxMN,binding.etMobileNumber.getText().toString())){

                        if(!ProjectUtils.isPhoneNumberValid(lengthMiniMN,lengthMaxMN,binding.etMobileNumber.getText().toString())){

                            ProjectUtils.showLong(mContext,"Please enter mobile number between "+lengthMiniMN+" to "+lengthMaxMN+"");
                            binding.etMobileNumber.requestFocus();
                        }
                    }
                    else if(binding.etWorkEmail.getText().toString().trim().length() == 0){

                        ProjectUtils.showLong(mContext,"Please enter work email");
                        binding.etWorkEmail.requestFocus();
                    }
                    else if(!ProjectUtils.isEmailValid(binding.etWorkEmail.getText().toString())){

                        ProjectUtils.showLong(mContext,"Please enter valid work email");
                        binding.etWorkEmail.requestFocus();
                    }
                    else{
                        showProgressDialog(mContext);
                        feePayerDocumentUpload(filePath1,docFileName1,filePath2,docFileName2);
                    }
                }

            }break;
        }
    }

    private void feePayerDocumentUpload(File fileFeePayerId, String feePayerName,File fileFeePayerLatestPayslip, String feePayerLatestPayslipName) {

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.FEE_PAYER_DOCUMENT_UPLOAD_METHOD;

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        ANRequest.MultiPartBuilder multipart = AndroidNetworking.upload(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addMultipartParameter("module", "FeePayerDetail")
                .addMultipartParameter("entityId", "")
                .addMultipartParameter("entityType","")
                .addMultipartFile("feePayerID", fileFeePayerId)
                .addMultipartParameter("feePayerID", feePayerName);

        if(fileFeePayerLatestPayslip != null){
            multipart.addMultipartFile("feePayerLatestPayslip", fileFeePayerLatestPayslip);
            multipart.addMultipartParameter("feePayerLatestPayslip", feePayerLatestPayslipName);
        }

        multipart.setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        String path = ProjectUtils.getCorrectedString(response.optString("path"));
                        String pathForSecondFile = ProjectUtils.getCorrectedString(response.optString("pathForSecondFile"));

                        submitForm(path,pathForSecondFile);
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

    private void submitForm(String file1, String file2){

        JSONObject objdebitOrderDate = null;
        JSONObject objFeePayerLatestPayslip = null;
        JSONObject objSalutation = null;
        JSONObject objFeePayerId = null;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", "");
            jsonObject.put("documentId", "");

            if(binding.spnPayerType.getSelectedItem().toString().equalsIgnoreCase("Person")){
                jsonObject.put("payerType", "PERSON");

                objFeePayerId = new JSONObject();

                if(!file1.equalsIgnoreCase("")){

                    int index1 = file1.lastIndexOf("/");
                    docFileName1 = file1.substring(index1 + 1);

                    objFeePayerId.put("path", file1);
                    objFeePayerId.put("name", docFileName1);
                    objFeePayerId.put("type", "DOCUMENT");
                    JSONObject objCreatedBy = new JSONObject();
                    objCreatedBy.put("id", sharedPrefrenceManager.getUserIDFromKey());
                    objFeePayerId.put("createdBy", objCreatedBy);
                }
                jsonObject.put("feePayerId",objFeePayerId);

                objFeePayerLatestPayslip = new JSONObject();
                if(!file2.equalsIgnoreCase("")){

                    int index2 = file2.lastIndexOf("/");
                    docFileName2 = file2.substring(index2 + 1);

                    objFeePayerLatestPayslip.put("path", file2);
                    objFeePayerLatestPayslip.put("name", docFileName2);
                    objFeePayerLatestPayslip.put("type", "DOCUMENT");
                    JSONObject objCreatedBy = new JSONObject();
                    objCreatedBy.put("id", sharedPrefrenceManager.getUserIDFromKey());
                    objFeePayerLatestPayslip.put("createdBy", objCreatedBy);
                    jsonObject.put("feePayerLatestPayslip",objFeePayerLatestPayslip);
                }else{
                    jsonObject.put("feePayerLatestPayslip",JSONObject.NULL);
                }

                objSalutation = new JSONObject();
                objSalutation.put("id", titleId);
                jsonObject.put("salutation",objSalutation);

            }else{
                jsonObject.put("payerType", "CORPORATE");
                jsonObject.put("feePayerId",JSONObject.NULL);
                jsonObject.put("feePayerLatestPayslip",JSONObject.NULL);
                jsonObject.put("salutation",JSONObject.NULL);
            }

            jsonObject.put("employeeOfOrganization", binding.switchEmployeeOrganization.isOn());
            jsonObject.put("employeeId", binding.etEmployeeId.getText().toString().trim());
            jsonObject.put("firstName", binding.etFname.getText().toString().trim());
            jsonObject.put("middleName", binding.etMname.getText().toString().trim());
            jsonObject.put("lastName", binding.etLname.getText().toString().trim());
            jsonObject.put("companyName", binding.etCompanyName.getText().toString().trim());
            jsonObject.put("contactPerson", binding.etCPFirstName.getText().toString().trim());
            jsonObject.put("surname", binding.etCPLastName.getText().toString().trim());
            jsonObject.put("doesFeePayerConsentToCreditCheck", binding.switchConsentToCredit.isOn());
            jsonObject.put("isSameAsCurrentAddress", binding.switchSameCurrentAddress.isOn());
            jsonObject.put("addressTextArea", binding.etAddress.getText().toString().trim());
            jsonObject.put("countryId", Integer.parseInt(cId));
            jsonObject.put("parentCountryRegionId", Integer.parseInt(cRegionId));
            jsonObject.put("cityId", Integer.parseInt(cityId));
            jsonObject.put("pincode", binding.etPincode.getText().toString().trim());
            jsonObject.put("homeTelephoneCountryCode", binding.spnHTCountryCode.getSelectedItem().toString().trim());
            jsonObject.put("homeTelephone", binding.etHomeTelephone.getText().toString().trim());
            jsonObject.put("workTelephoneCountryCode", binding.spnWTCountryCode.getSelectedItem().toString().trim());
            jsonObject.put("workTelephone", binding.etWorkTelephone.getText().toString().trim());
            jsonObject.put("countryCode", binding.spnMNCountryCode.getSelectedItem().toString().trim());
            jsonObject.put("mobileNumber", binding.etMobileNumber.getText().toString().trim());
            jsonObject.put("nationalAlternateId", binding.etNationalId.getText().toString().trim());
            jsonObject.put("emailHome", binding.etHomeEmail.getText().toString().trim());
            jsonObject.put("emailHomeUseForCommunication", binding.switchUseHomeEmail.isOn());
            jsonObject.put("emailWork", binding.etWorkEmail.getText().toString().trim());
            jsonObject.put("emailWorkUseForCommunication", binding.switchUseWorkEmail.isOn());
            jsonObject.put("companyAddress", binding.etCompanyAddress.getText().toString().trim());

            String paymentMode = binding.spnPaymentMode.getSelectedItem().toString().trim();
            if(!paymentMode.equalsIgnoreCase("EFT")){
                jsonObject.put("paymentMode", "DEBIT_ORDER");
                jsonObject.put("debitOrderDate", debitOrderId);
                jsonObject.put("accountType", binding.spnAccountType.getSelectedItem().toString().trim().toUpperCase());
                jsonObject.put("accountNumber", binding.etAccountNumber.getText().toString().trim());
                jsonObject.put("accountName", binding.etAccountName.getText().toString().trim());

                JSONObject objPerson = new JSONObject();
                objPerson.put("id", Integer.parseInt(bankId) );
                jsonObject.put("bank",objPerson);

                jsonObject.put("branchCode", Integer.parseInt(branchCode));
            }else{
                jsonObject.put("paymentMode", "EFT");
            }

            JSONArray jsonArray = new JSONArray();
            jsonObject.put("customData", jsonArray);

            JSONObject objPerson = new JSONObject();
            objPerson.put("id", sharedPrefrenceManager.getPersonIDFromKey());
            jsonObject.put("person",objPerson);

            if(debitOrderId != 0){
                objdebitOrderDate = new JSONObject();
                objdebitOrderDate.put("id", debitOrderId);
                jsonObject.put("debitOrderDateCSM",objdebitOrderDate);
            }else{
                jsonObject.put("debitOrderDateCSM",JSONObject.NULL);
            }

            ProjectUtils.showLog(TAG,""+jsonObject);

            //Here do api calling for login
            String url = BaseURL.BASE_URL + KEYS.FEE_PAYER_CREATE_METHOD;

            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
            String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
            String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

            String str = tokenType + " " + accessToken;

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
                            ProjectUtils.showLog("Submit Response: ", response);

                            hideProgressDialog();
                            ProjectUtils.showLong(mContext,"Fee Payer added successfully!");
                            AcademiaApp.IS_UPDATE = true;
                            onBackPressed();
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

            case PICKFILE_RESULT_CODE1:

                if(resultCode==RESULT_OK){

                    if (data != null)
                    {
                        uri1 = data.getData();
                    }

                    if(uri1 != null){

                        try {
                            String fileName = FilePath.getPath(mContext, uri1);

                            if(fileName != null && !fileName.equalsIgnoreCase(""))
                            {
                                filePath1 = new File(fileName);

                                int file_size = Integer.parseInt(String.valueOf(filePath1.length()/1024));

                                if(file_size < 20000){

                                    binding.llAttachment1.setVisibility(View.VISIBLE);
                                    binding.llSelectFile1.setVisibility(View.GONE);
                                    int index = fileName.lastIndexOf("/");
                                    docFileName1 = fileName.substring(index + 1);

                                    binding.tvAttachment1.setText(docFileName1);
                                    binding.ivAttachment1.setImageResource(ProjectUtils.showDocIcon(docFileName1));

                                }else{
                                    binding.llAttachment1.setVisibility(View.GONE);
                                    binding.llSelectFile1.setVisibility(View.VISIBLE);
                                    Toast.makeText(this, "Can't upload document more than 20 MB", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                binding.llAttachment1.setVisibility(View.GONE);
                                binding.llSelectFile1.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception ex) {
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());

                            ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                        }
                    }
                }
                break;

            case PICKFILE_RESULT_CODE2:

                if(resultCode==RESULT_OK){

                    if (data != null)
                    {
                        uri2 = data.getData();
                    }

                    if(uri2 != null){

                        try {
                            String fileName = FilePath.getPath(mContext, uri2);

                            if(fileName != null && !fileName.equalsIgnoreCase(""))
                            {
                                filePath2 = new File(fileName);

                                int file_size = Integer.parseInt(String.valueOf(filePath2.length()/1024));

                                if(file_size < 20000){

                                    binding.llAttachment2.setVisibility(View.VISIBLE);
                                    binding.llSelectFile2.setVisibility(View.GONE);
                                    int index = fileName.lastIndexOf("/");
                                    docFileName2 = fileName.substring(index + 1);

                                    binding.tvAttachment2.setText(docFileName2);
                                    binding.ivAttachment2.setImageResource(ProjectUtils.showDocIcon(docFileName2));

                                }else{
                                    binding.llAttachment2.setVisibility(View.GONE);
                                    binding.llSelectFile2.setVisibility(View.VISIBLE);
                                    Toast.makeText(this, "Can't upload document more than 20 MB", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                binding.llAttachment2.setVisibility(View.GONE);
                                binding.llSelectFile2.setVisibility(View.VISIBLE);
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

    private void uiUpdate(boolean isPerson){

        if(isPerson){
            binding.llPersonName.setVisibility(View.VISIBLE);
            binding.llPincode.setVisibility(View.VISIBLE);
            binding.llEmailPerson.setVisibility(View.VISIBLE);
            binding.llDocuments.setVisibility(View.VISIBLE);
            binding.llCompanyName.setVisibility(View.GONE);
            binding.llCompanyAddress.setVisibility(View.GONE);
        }else{
            binding.llCompanyName.setVisibility(View.VISIBLE);
            binding.llCompanyAddress.setVisibility(View.VISIBLE);
            binding.llPersonName.setVisibility(View.GONE);
            binding.llPincode.setVisibility(View.GONE);
            binding.llEmailPerson.setVisibility(View.GONE);
            binding.llDocuments.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
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
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE1);

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

            case 2:
            {
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
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICKFILE_RESULT_CODE2);

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
}