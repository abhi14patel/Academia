package com.serosoft.academiassu.Modules.MyRequest.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;

import com.serosoft.academiassu.Modules.MyRequest.Adapters.RequestIdAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.RequestStatusAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.RequestTypeAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestFilter_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestStatus_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestType_Dto;
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

public class MyRequestFilterDialog extends Activity implements AsyncTaskCompleteListener, View.OnClickListener {

    LayoutInflater inflater;
    View view;
    BaseActivity baseActivity;

    private TextView tvRequestId1,tvRequestStatus1,tvRequestCategory1,tvRequestType1,tvFromDate1,tvToDate1;
    private AutoCompleteTextView atvRequestId;
    private Spinner spnRequestStatus,spnRequestCategory,spnRequestType;
    private TextView tvFromDate,tvToDate;

    private Button btnApply,btnReset;
    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private Context mContext;
    SharedPrefrenceManager sharedPrefrenceManager;

    Calendar calendarDefault = null;
    private int mYear, mMonth, mDay;
    private int mYear1, mMonth1, mDay1;

    String fromDate = "";
    String toDate = "";
    Long dateFrom, dateTo;

    ArrayList<String> requestIdList;
    RequestIdAdapter requestIdAdapter;

    ArrayList<RequestStatus_Dto> requestStatusList;
    RequestStatusAdapter requestStatusAdapter;

    ArrayList<RequestType_Dto> requestTypeList;
    RequestTypeAdapter requestTypeAdapter;

    TranslationManager translationManager;

    int requestStatusId = 0;
    int requestTypeId = 0;
    ArrayList<RequestFilter_Dto> requestFilterList;
    ArrayList<RequestFilter_Dto> newRequestFilterList;

    int requestStatusId1 = 0,requestStatusId2 = 0;
    int requestTypeId1 = 0,requestTypeId2 = 0;
    String filterDate = "";

    private static final String TAG = MyRequestFilterDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = MyRequestFilterDialog.this;
        baseActivity = new BaseActivity();

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        setFinishOnTouchOutside(false);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.myrequest_filter_layout,null,false);

        setContentView(view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        IntializeView(view);

        Intent intent = getIntent();
        newRequestFilterList = (ArrayList<RequestFilter_Dto>) intent.getSerializableExtra(Consts.REQUEST_FILTER_LIST);

        setupData();

        setFilterData(newRequestFilterList);
    }

    private void setFilterData(ArrayList<RequestFilter_Dto> newRequestFilterList) {

        if(newRequestFilterList != null && newRequestFilterList.size() > 0){

            for(RequestFilter_Dto filter : newRequestFilterList){

                String name = filter.getName();
                if(name.equalsIgnoreCase("REQUEST_ID")){

                    String value = String.valueOf(filter.getValues());
                    value = value.replaceAll("[\\[\\](){}]","");
                    atvRequestId.setText(value);
                }else if(name.equalsIgnoreCase("STATUS")){

                    String value = String.valueOf(filter.getValues());
                    value = value.replaceAll("[\\[\\](){}]","");
                    requestStatusId1 = Integer.parseInt(value);
                }else if(name.equalsIgnoreCase("REQUEST_TYPE_ID")){

                    String value = String.valueOf(filter.getValues());
                    value = value.replaceAll("[\\[\\](){}]","");
                    requestTypeId1 = Integer.parseInt(value);
                }else if(name.equalsIgnoreCase("REQUEST_DATE")){

                    String value = String.valueOf(filter.getValues());
                    filterDate = value.replaceAll("[\\[\\](){}]","");

                    getFilterDates(filterDate);
                }
            }
        }
    }

    private void getFilterDates(String filterDate) {

        String date[] = filterDate.split(",");

        String dateF = date[0].trim();
        String dateT = date[1].trim();

        if(!dateF.equalsIgnoreCase("")){

            String []fDate = dateF.split("-");
            String s1 = fDate[0];
            String s2 = fDate[1];
            String s3 = fDate[2];

            mYear = Integer.parseInt(s1);
            mMonth = (Integer.parseInt(s2)-1);
            mDay = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            dateFrom = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvFromDate.setText(toDate);
        }

        if(!dateT.equalsIgnoreCase("")){

            String []tDate = dateT.split("-");
            String s1 = tDate[0];
            String s2 = tDate[1];
            String s3 = tDate[2];

            mYear1 = Integer.parseInt(s1);
            mMonth1 = (Integer.parseInt(s2)-1);
            mDay1 = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            dateTo = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvToDate.setText(toDate);
        }
    }

    private void IntializeView(View view) {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationManager = new TranslationManager(this);

        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);

        tvRequestId1 = view.findViewById(R.id.tvRequestId1);
        tvRequestStatus1 = view.findViewById(R.id.tvRequestStatus1);
        tvRequestCategory1 = view.findViewById(R.id.tvRequestCategory1);
        tvRequestType1 = view.findViewById(R.id.tvRequestType1);
        tvFromDate1 = view.findViewById(R.id.tvFromDate1);
        tvToDate1 = view.findViewById(R.id.tvToDate1);

        atvRequestId = view.findViewById(R.id.atvRequestId);
        spnRequestStatus = view.findViewById(R.id.spnRequestStatus);
        spnRequestCategory = view.findViewById(R.id.spnRequestCategory);
        spnRequestType = view.findViewById(R.id.spnRequestType);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        tvToDate = view.findViewById(R.id.tvToDate);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);

        tvRequestId1.setText(translationManager.REQUEST_ID_KEY);
        tvRequestStatus1.setText(translationManager.SERVICE_REQUEST_STATUS_KEY);
        tvRequestCategory1.setText(translationManager.REQUEST_CATEGORY_KEY);
        tvRequestType1.setText(translationManager.REQUEST_TYPE_KEY);
        tvTitle.setText(translationManager.FILTER_BY_KEY);
    }

    private void setupData() {

        requestFilterList = new ArrayList<>();

        requestStatusId1 = -1;
        requestTypeId1 = -1;

        populateContents();

        atvRequestId.setText("");
        atvRequestId.setThreshold(3);

        //Here set current from and to date
        calendarDefault = Calendar.getInstance();
        mYear = calendarDefault.get(Calendar.YEAR);
        mMonth = calendarDefault.get(Calendar.MONTH);
        mDay = calendarDefault.get(Calendar.DAY_OF_MONTH);

        mYear1 = calendarDefault.get(Calendar.YEAR);
        mMonth1 = calendarDefault.get(Calendar.MONTH);
        mDay1 = calendarDefault.get(Calendar.DAY_OF_MONTH);

        dateFrom = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
        dateTo = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);

        tvFromDate.setText("");
        tvToDate.setText("");
    }

    private void populateContents() {

        requestIdList = sharedPrefrenceManager.getRequestIdList(Consts.REQUEST_ID_LIST);

        requestIdAdapter = new RequestIdAdapter(mContext,R.layout.document_type_item,requestIdList);
        atvRequestId.setAdapter(requestIdAdapter);
        requestIdAdapter.notifyDataSetChanged();

        atvRequestId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProjectUtils.hideKeyboard2(mContext,atvRequestId);
            }
        });

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_REQUEST_STATUS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.ivClose:{
                finish();
            }break;

            case R.id.btnApply:{

                if (atvRequestId!=null && atvRequestId.getText().length()>0) {
                    ArrayList<String> arr = new ArrayList<String>();
                    arr.add(atvRequestId.getText().toString());
                    requestFilterList.add(new RequestFilter_Dto("REQUEST_ID",arr,1));
                }

                if(requestStatusId != 0){
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    arr.add(requestStatusId);
                    requestFilterList.add(new RequestFilter_Dto("STATUS",arr,2));
                }

                if(requestTypeId != 0){
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    arr.add(requestTypeId);
                    requestFilterList.add(new RequestFilter_Dto("REQUEST_TYPE_ID",arr,2));
                }

                String fromDate = tvFromDate.getText().toString();
                String toDate = tvToDate.getText().toString();

                if(!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")){

                    fromDate = ProjectUtils.getDateFormat3(fromDate);
                    toDate = ProjectUtils.getDateFormat3(toDate);

                    ArrayList<String> arr = new ArrayList<String>();
                    arr.add(fromDate);
                    arr.add(toDate);
                    requestFilterList.add(new RequestFilter_Dto("REQUEST_DATE",arr,1));

                    Intent intent = new Intent();
                    intent.putExtra("requestFilter",requestFilterList);
                    setResult(RESULT_OK,intent);
                    finish();

                }else if(!fromDate.equalsIgnoreCase("")){

                    ProjectUtils.showLong(mContext,"Please enter till date");
                }else if(!toDate.equalsIgnoreCase("")){

                    ProjectUtils.showLong(mContext,"Please enter from date");
                }else if(fromDate.equalsIgnoreCase("") && toDate.equalsIgnoreCase("")){

                    Intent intent = new Intent();
                    intent.putExtra("requestFilter",requestFilterList);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }break;

            case R.id.btnReset:{
                setupData();
            }break;

            case R.id.tvFromDate:{

                ProjectUtils.hideKeyboard(MyRequestFilterDialog.this);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                dateFrom = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                fromDate = ProjectUtils.convertTimestampToDate(dateFrom,mContext);

                                tvFromDate.setText(fromDate);
                            }
                        }, mYear, mMonth, mDay);
                //datePickerDialog.getDatePicker().setMaxDate(dateTo);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;

            case R.id.tvToDate:{

                ProjectUtils.hideKeyboard(MyRequestFilterDialog.this);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear1 = year;
                                mMonth1 = monthOfYear;
                                mDay1 = dayOfMonth;

                                dateTo = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                toDate = ProjectUtils.convertTimestampToDate(dateTo,mContext);

                                tvToDate.setText(toDate);
                            }
                        }, mYear1, mMonth1, mDay1);
                //datePickerDialog.getDatePicker().setMinDate(dateFrom);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;
        }
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_REQUEST_STATUS:{

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        requestStatusList = new ArrayList<>();
                        requestStatusList.add(new RequestStatus_Dto(-1,"Select",""));

                        for(int i = 0 ; i< jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            int id = jsonObject1.optInt("id");
                            String code = jsonObject1.optString("code");
                            String value = jsonObject1.optString("value");

                            requestStatusList.add(new RequestStatus_Dto(id,value,code));
                        }

                        spinnerModified(requestStatusList.size(), spnRequestStatus);

                        if(requestStatusList != null && requestStatusList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< requestStatusList.size() ; i++){

                                RequestStatus_Dto requestStatus_dto = requestStatusList.get(i);

                                if(requestStatus_dto.getId() == requestStatusId1)
                                {
                                    requestStatusId2 = i;
                                }
                            }

                            requestStatusAdapter = new RequestStatusAdapter(mContext,requestStatusList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == requestStatusId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnRequestStatus.setAdapter(requestStatusAdapter);

                            spnRequestStatus.setSelection(requestStatusId2);
                        }

                        spnRequestStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                ProjectUtils.hideKeyboard2(mContext,spnRequestStatus);

                                RequestStatus_Dto requestStatus_dto = (RequestStatus_Dto) spnRequestStatus.getSelectedItem();

                                if(requestStatus_dto.getValue().equalsIgnoreCase("Select")){
                                    requestStatusId = 0;
                                }else{
                                    requestStatusId = requestStatus_dto.getId();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                    new OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_REQUEST_TYPE).execute(Consts.MY_REQUEST_TYPE_OTHER);
                } else {
                    Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                }

            }break;

            case KEYS.SWITCH_REQUEST_TYPE:{

                baseActivity.hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        requestTypeList = new ArrayList<>();
                        requestTypeList.add(new RequestType_Dto(-1,"Select"));

                        for(int i = 0 ; i< jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            int id = jsonObject1.optInt("id");
                            String value = jsonObject1.optString("value");

                            requestTypeList.add(new RequestType_Dto(id,value));
                        }

                        spinnerModified(requestTypeList.size(), spnRequestType);

                        if(requestTypeList != null && requestTypeList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< requestTypeList.size() ; i++){

                                RequestType_Dto requestType_dto = requestTypeList.get(i);

                                if(requestType_dto.getId() == requestTypeId1)
                                {
                                    requestTypeId2 = i;
                                }
                            }

                            requestTypeAdapter = new RequestTypeAdapter(mContext,requestTypeList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == requestTypeId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnRequestType.setAdapter(requestTypeAdapter);

                            spnRequestType.setSelection(requestTypeId2);
                        }

                        spnRequestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                ProjectUtils.hideKeyboard2(mContext,spnRequestType);

                                RequestType_Dto requestType_dto = (RequestType_Dto) spnRequestType.getSelectedItem();

                                if(requestType_dto.getValue().equalsIgnoreCase("Select")){
                                    requestTypeId = 0;
                                }else{
                                    requestTypeId = requestType_dto.getId();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }break;
        }
    }

    private void spinnerModified(int size, Spinner spnPeriod) {

        if (size > 1) {
            spnPeriod.setEnabled(true);
            spnPeriod.setBackgroundResource(R.drawable.spinner_bg);
        } else {
            spnPeriod.setEnabled(false);
            spnPeriod.setBackgroundResource(R.drawable.spinner_bg_normal);
        }
    }
}
