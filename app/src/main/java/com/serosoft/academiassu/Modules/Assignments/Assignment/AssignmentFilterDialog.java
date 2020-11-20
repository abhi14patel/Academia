package com.serosoft.academiassu.Modules.Assignments.Assignment;

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
import com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters.AssignmentNameAdapter;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters.AssignmentTypeAdapter;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentType_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
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

public class AssignmentFilterDialog extends Activity implements AsyncTaskCompleteListener, View.OnClickListener {

    BaseActivity baseActivity;
    LayoutInflater inflater;
    View view;

    TranslationManager translationManager;
    SharedPrefrenceManager sharedPrefrenceManager;
    private Spinner spnAssignmentType;
    private AutoCompleteTextView atvAssignmentName;
    private TextView tvFromDate,tvToDate;
    private Button btnApply,btnReset;
    private TextView tvAssignmentType1,tvAssignmentName1,tvFromDate1,tvToDate1;

    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private Context mContext;

    Calendar calendarDefault = null;
    private int mYear, mMonth, mDay;
    private int mYear1, mMonth1, mDay1;

    String assignmentType = "";
    int assignmentTypeId = 0,assignmentTypeId1 = 0;
    String assignmentName = "";
    String fromDate = "";
    String toDate = "";
    Long dateFrom, dateTo;

    private boolean isReset = false;

    ArrayList<AssignmentType_Dto> assignmentTypeList;
    AssignmentTypeAdapter assignmentTypeAdapter;

    ArrayList<Assignment_Dto> assignmentList;
    AssignmentNameAdapter assignmentNameAdapter;

    private static final String TAG = AssignmentFilterDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = AssignmentFilterDialog.this;

        baseActivity = new BaseActivity();

        setFinishOnTouchOutside(false);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.assignment_filter_layout,null,false);
        sharedPrefrenceManager = new SharedPrefrenceManager(this);

        setContentView(view);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        IntializeView(view);

        Intent intent = getIntent();
        assignmentList = (ArrayList<Assignment_Dto>) intent.getSerializableExtra(Consts.ASSIGNMENT_LIST);
        assignmentName =  intent.getStringExtra(Consts.ASSIGNMENT_NAME);
        assignmentTypeId =  intent.getIntExtra(Consts.ASSIGNMENT_TYPE_ID,0);
        fromDate =  intent.getStringExtra(Consts.FROM_DATE);
        toDate =  intent.getStringExtra(Consts.TO_DATE);

        setupData();

        if(!assignmentName.equalsIgnoreCase("")){
            atvAssignmentName.setText(assignmentName);
        }

        if(!fromDate.equalsIgnoreCase("")){

            String []fDate = fromDate.split("-");
            String s1 = fDate[0];
            String s2 = fDate[1];
            String s3 = fDate[2];

            mDay = Integer.parseInt(s1);
            mMonth = (Integer.parseInt(s2)-1);
            mYear = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            dateFrom = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvFromDate.setText(toDate);
        }

        if(!toDate.equalsIgnoreCase("")){

            String []tDate = toDate.split("-");
            String s1 = tDate[0];
            String s2 = tDate[1];
            String s3 = tDate[2];

            mDay1 = Integer.parseInt(s1);
            mMonth1 = (Integer.parseInt(s2)-1);
            mYear1 = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            dateTo = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvToDate.setText(toDate);
        }
    }

    private void setupData() {

        populateContents();

        //Here set assignment name drop down
        atvAssignmentName.setText("");
        atvAssignmentName.setThreshold(1);

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

        if (ConnectionDetector.isConnectingToInternet(this)) {

            baseActivity.showProgressDialog(mContext);

            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_ASSIGNMENT_TYPE).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }


    private void IntializeView(View view) {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationManager = new TranslationManager(this);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(translationManager.FILTER_BY_KEY);
        ivClose = view.findViewById(R.id.ivClose);

        spnAssignmentType = view.findViewById(R.id.spnAssignmentType);
        atvAssignmentName = view.findViewById(R.id.atvAssignmentName);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        tvToDate = view.findViewById(R.id.tvToDate);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        tvAssignmentType1 = view.findViewById(R.id.tvAssignmentType1);
        tvAssignmentName1 = view.findViewById(R.id.tvAssignmentName1);
        tvFromDate1 = view.findViewById(R.id.tvFromDate1);
        tvToDate1 = view.findViewById(R.id.tvToDate1);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_ASSIGNMENT_TYPE:

                baseActivity.hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        assignmentTypeList = new ArrayList<>();
                        assignmentTypeList.add(new AssignmentType_Dto(-1,"Select"));

                        for(int i = 0 ; i< jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String value = jsonObject1.optString("value");
                            int id = jsonObject1.optInt("id");

                            AssignmentType_Dto list = new AssignmentType_Dto(id,value);
                            assignmentTypeList.add(list);
                        }

                        if(assignmentTypeList != null && assignmentTypeList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< assignmentTypeList.size() ; i++){

                                AssignmentType_Dto assignmentType_dto = assignmentTypeList.get(i);

                                if(assignmentType_dto.getId() == assignmentTypeId)
                                {
                                    assignmentTypeId1 = i;
                                }
                            }

                            assignmentTypeAdapter = new AssignmentTypeAdapter(mContext,assignmentTypeList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == assignmentTypeId1) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnAssignmentType.setAdapter(assignmentTypeAdapter);

                            spnAssignmentType.setSelection(assignmentTypeId1);
                        }

                        spnAssignmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                AssignmentType_Dto assignmentType_dto = (AssignmentType_Dto) spnAssignmentType.getSelectedItem();

                                if(assignmentType_dto.getValue().equalsIgnoreCase("Select")){

                                    assignmentType = "";
                                    assignmentTypeId = -1;

                                    ArrayList<Assignment_Dto> temp = new ArrayList<>();
                                    for(Assignment_Dto assignment_dto : assignmentList)
                                    {
                                        temp.add(assignment_dto);
                                    }
                                    if (temp != null && temp.size()>0)
                                    {
                                        assignmentNameAdapter = new AssignmentNameAdapter(mContext,R.layout.document_type_item,temp);
                                        atvAssignmentName.setAdapter(assignmentNameAdapter);
                                        assignmentNameAdapter.notifyDataSetChanged();

                                        atvAssignmentName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                ProjectUtils.hideKeyboard2(mContext,atvAssignmentName);
                                            }
                                        });
                                    }

                                }else{

                                    assignmentType = assignmentType_dto.getValue();
                                    assignmentTypeId = assignmentType_dto.getId();

                                    if(isReset){
                                        atvAssignmentName.setText("");
                                    }
                                    isReset = true;

                                    ArrayList<Assignment_Dto> temp = new ArrayList<>();
                                    for(Assignment_Dto assignment_dto : assignmentList)
                                    {
                                        if((assignment_dto.getAssignment_type().contains(assignmentType)))
                                        {
                                            temp.add(assignment_dto);
                                        }
                                    }
                                    if (temp != null && temp.size()>0)
                                    {
                                        assignmentNameAdapter = new AssignmentNameAdapter(mContext,R.layout.document_type_item,temp);
                                        atvAssignmentName.setAdapter(assignmentNameAdapter);
                                        assignmentNameAdapter.notifyDataSetChanged();

                                        atvAssignmentName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                ProjectUtils.hideKeyboard2(mContext,atvAssignmentName);
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                    else{
                        spnAssignmentType.setEnabled(false);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
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

                Intent intent = new Intent();
                intent.putExtra("AssignmentTypeId",assignmentTypeId);
                intent.putExtra("AssignmentType",assignmentType);
                intent.putExtra("AssignmentName",atvAssignmentName.getText().toString().trim());
                intent.putExtra("FromDate",fromDate);
                intent.putExtra("ToDate",toDate);
                setResult(RESULT_OK,intent);
                finish();
            }break;

            case R.id.btnReset:{
                assignmentTypeId = 0;
                assignmentTypeId1 = 0;
                assignmentName = "";
                fromDate = "";
                toDate = "";
                setupData();
            }break;

            case R.id.tvFromDate:{

                ProjectUtils.hideKeyboard(AssignmentFilterDialog.this);

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
                datePickerDialog.getDatePicker().setMaxDate(dateTo);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;

            case R.id.tvToDate:{

                ProjectUtils.hideKeyboard(AssignmentFilterDialog.this);

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
                datePickerDialog.getDatePicker().setMinDate(dateFrom);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;
        }
    }
}
