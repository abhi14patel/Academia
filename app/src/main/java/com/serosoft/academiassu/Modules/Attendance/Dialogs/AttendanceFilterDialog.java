package com.serosoft.academiassu.Modules.Attendance.Dialogs;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.serosoft.academiassu.Modules.Attendance.Models.Attendance_Dto;
import com.serosoft.academiassu.Modules.Exam.Adapters.BatchAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.PeriodAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.ProgramAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.SectionAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.Batch_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Period_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Program_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Section_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AttendanceFilterDialog extends Activity implements AsyncTaskCompleteListener, View.OnClickListener {

    LayoutInflater inflater;
    View view;

    SharedPrefrenceManager sharedPrefrenceManager;
    private LinearLayout llPeriod,llBatch,llProgram;
    private TextView tvProgram1,tvBatch1,tvPeriod1,tvSection1;
    private Spinner spnProgram,spnBatch,spnPeriod,spnSection;
    private TextView tvStartDate,tvEndDate;
    private Button btnApply,btnReset;
    private TextView tvStartDate1,tvEndDate1;

    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private Context mContext;

    String startDate = "", endDate = "";
    TranslationManager translationManager;
    String academyType = "";
    String periodId = "",batchId = "",programId = "", sectionId = "";
    int periodId1 = 0,batchId1 = 0,programId1 = 0,sectionId1 = 0;
    int periodId2 = 0,batchId2 = 0,programId2 = 0,sectionId2 = 0;

    Calendar calendarDefault = null;
    private int mYear, mMonth, mDay;
    private int mYear1, mMonth1, mDay1;
    Long dateStart, dateEnd;

    Attendance_Dto attendance_dto = new Attendance_Dto();

    ArrayList<Program_Dto> programList;
    ProgramAdapter programAdapter;

    ArrayList<Batch_Dto> batchList;
    BatchAdapter batchAdapter;

    ArrayList<Period_Dto> periodList;
    PeriodAdapter periodAdapter;

    ArrayList<Section_Dto> sectionList;
    SectionAdapter sectionAdapter;

    private static final String TAG = AttendanceFilterDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = AttendanceFilterDialog.this;

        setFinishOnTouchOutside(false);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.attendance_filter_layout,null,false);
        sharedPrefrenceManager = new SharedPrefrenceManager(this);

        setContentView(view);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        IntializeView(view);

        setupData();

        Intent intent = getIntent();
        programId1 = Integer.parseInt(intent.getStringExtra(Consts.PROGRAM_ID));
        batchId1 = Integer.parseInt(intent.getStringExtra(Consts.BATCH_ID));
        periodId1 = Integer.parseInt(intent.getStringExtra(Consts.PERIOD_ID));
        sectionId1 = Integer.parseInt(intent.getStringExtra(Consts.SECTION_ID));
        startDate = intent.getStringExtra(Consts.FROM_DATE);
        endDate = intent.getStringExtra(Consts.TO_DATE);

        if(!startDate.equalsIgnoreCase("")){

            String []fDate = startDate.split("-");
            String s1 = fDate[0];
            String s2 = fDate[1];
            String s3 = fDate[2];

            mDay = Integer.parseInt(s1);
            mMonth = (Integer.parseInt(s2)-1);
            mYear = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            dateStart = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvStartDate.setText(toDate);
        }

        if(!endDate.equalsIgnoreCase("")){

            String []tDate = endDate.split("-");
            String s1 = tDate[0];
            String s2 = tDate[1];
            String s3 = tDate[2];

            mDay1 = Integer.parseInt(s1);
            mMonth1 = (Integer.parseInt(s2)-1);
            mYear1 = Integer.parseInt(s3);

            long d1 = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            dateEnd = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);
            String toDate = ProjectUtils.convertTimestampToDate(d1,mContext);
            tvEndDate.setText(toDate);
        }
    }

    private void setupData() {

        populateContents();

        //Here set current from and to date
        calendarDefault = Calendar.getInstance();
        mYear = calendarDefault.get(Calendar.YEAR);
        mMonth = calendarDefault.get(Calendar.MONTH);
        mDay = calendarDefault.get(Calendar.DAY_OF_MONTH);

        mYear1 = calendarDefault.get(Calendar.YEAR);
        mMonth1 = calendarDefault.get(Calendar.MONTH);
        mDay1 = calendarDefault.get(Calendar.DAY_OF_MONTH);

        dateStart = ProjectUtils.getLongDateFormat(mYear, mMonth, mDay);
        dateEnd = ProjectUtils.getLongDateFormat(mYear1, mMonth1, mDay1);

        tvStartDate.setText("");
        tvEndDate.setText("");
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_ATTENDANCE_PROGRAMS).execute();
        } else {
            Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void IntializeView(View view) {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationManager = new TranslationManager(this);

        tvTitle = view.findViewById(R.id.tvTitle);
        ivClose = view.findViewById(R.id.ivClose);

        llPeriod = view.findViewById(R.id.llPeriod);
        llBatch = view.findViewById(R.id.llBatch);
        llProgram = view.findViewById(R.id.llProgram);

        spnProgram = view.findViewById(R.id.spnProgram);
        spnBatch = view.findViewById(R.id.spnBatch);
        spnPeriod = view.findViewById(R.id.spnPeriod);
        spnSection = view.findViewById(R.id.spnSection);

        tvProgram1 = view.findViewById(R.id.tvProgram1);
        tvBatch1 = view.findViewById(R.id.tvBatch1);
        tvPeriod1 = view.findViewById(R.id.tvPeriod1);
        tvSection1 = view.findViewById(R.id.tvSection1);

        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        tvStartDate1 = view.findViewById(R.id.tvStartDate1);
        tvEndDate1 = view.findViewById(R.id.tvEndDate1);

        btnApply.setOnClickListener(this);
        btnApply.setEnabled(false);
        btnReset.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);
        }

        tvBatch1.setText(translationManager.BATCH_KEY);
        tvProgram1.setText(translationManager.PROGRAM_KEY);
        tvPeriod1.setText(translationManager.PERIOD_KEY);
        tvStartDate1.setText(translationManager.ATTENDANCE_START_DATE_KEY);
        tvEndDate1.setText(translationManager.ATTENDANCE_END_DATE_KEY);
        tvSection1.setText(translationManager.SECTION_KEY);
        tvTitle.setText(translationManager.FILTER_BY_KEY);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor){

            case KEYS.SWITCH_ATTENDANCE_PROGRAMS:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        programList = new ArrayList<>();
                        for(int i = 0 ; i< jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String value = jsonObject1.optString("value");
                            String code = jsonObject1.optString("code");
                            int id = jsonObject1.optInt("id");

                            Program_Dto program_dto = new Program_Dto(value,code,id);
                            programList.add(program_dto);
                        }

                        spinnerModified(programList.size(), spnProgram);

                        if(programList != null && programList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< programList.size() ; i++){

                                Program_Dto program_dto = programList.get(i);

                                if(program_dto.getProgramId() == programId1)
                                {
                                    programId2 = i;
                                }
                            }

                            programAdapter = new ProgramAdapter(mContext,programList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == programId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnProgram.setAdapter(programAdapter);

                            spnProgram.setSelection(programId2);
                        }

                        spnProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Program_Dto program_dto = (Program_Dto) spnProgram.getSelectedItem();
                                programId = String.valueOf(program_dto.getProgramId());
                                String programName = program_dto.getProgramName();

                                attendance_dto.setProgramId(programId);
                                attendance_dto.setProgramName(programName);

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    new OptimizedServerCallAsyncTask(mContext,
                                            AttendanceFilterDialog.this, KEYS.SWITCH_ATTENDANCE_BATCH).execute(programId);
                                } else {
                                    Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    }else{
                        spnProgram.setEnabled(false);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_BATCH:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        batchList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String batchName = jsonObject1.optString("value");
                            int batchId = jsonObject1.optInt("id");

                            Batch_Dto batch_dto = new Batch_Dto(batchName,batchId);
                            batchList.add(batch_dto);
                        }

                        spinnerModified(batchList.size(), spnBatch);

                        if(batchList != null && batchList.size() > 0){

                            //Here set current batch selected
                            for(int i = 0 ; i< batchList.size() ; i++){

                                Batch_Dto batch_dto = batchList.get(i);

                                if(batch_dto.getBatchId() == batchId1)
                                {
                                    batchId2 = i;
                                    break;
                                }else{
                                    batchId2 = batchList.size() - 1;
                                }
                            }

                            batchAdapter = new BatchAdapter(mContext,batchList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == batchId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnBatch.setAdapter(batchAdapter);

                            spnBatch.setSelection(batchId2);
                        }

                        spnBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Batch_Dto batch_dto = (Batch_Dto) spnBatch.getSelectedItem();
                                batchId = String.valueOf(batch_dto.getBatchId());
                                String batch = batch_dto.getBatchName();

                                attendance_dto.setBatchId(batchId);
                                attendance_dto.setBatch(batch);
                                attendance_dto.setPeriodId("0");

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    new OptimizedServerCallAsyncTask(mContext,
                                            AttendanceFilterDialog.this, KEYS.SWITCH_ATTENDANCE_PERIOD).execute(batchId);
                                } else {
                                    Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }else{
                        spnBatch.setEnabled(false);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_PERIOD:
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        periodList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String periodName = jsonObject1.optString("value");
                            int periodId = jsonObject1.optInt("id");

                            Period_Dto period_dto = new Period_Dto(periodName,periodId);
                            periodList.add(period_dto);
                        }

                        spinnerModified(periodList.size(), spnPeriod);

                        if(periodList != null && periodList.size() > 0){

                            //Here set current period selected
                            for(int i = 0 ; i< periodList.size() ; i++){

                                Period_Dto period_dto = periodList.get(i);

                                if(period_dto.getPeriodId() == periodId1)
                                {
                                    periodId2 = i;
                                    break;
                                }else{
                                    periodId2 = periodList.size() - 1;
                                }
                            }

                            //Here change selected item background in drop down view
                            periodAdapter = new PeriodAdapter(mContext,periodList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == periodId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };

                            spnPeriod.setAdapter(periodAdapter);

                            spnPeriod.setSelection(periodId2);
                        }

                        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
                            Period_Dto period_dto = (Period_Dto) spnPeriod.getSelectedItem();
                            periodId = String.valueOf(period_dto.getPeriodId());
                            String period = period_dto.getPeriodName();

                            attendance_dto.setPeriodId(periodId);
                            attendance_dto.setPeriod(period);

                            if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                new OptimizedServerCallAsyncTask(mContext,
                                        AttendanceFilterDialog.this, KEYS.SWITCH_ATTENDANCE_SECTION).execute(periodId);
                            } else {
                                Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            spnPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Period_Dto period_dto = (Period_Dto) spnPeriod.getSelectedItem();
                                    periodId = String.valueOf(period_dto.getPeriodId());
                                    String period = period_dto.getPeriodName();

                                    attendance_dto.setPeriodId(periodId);
                                    attendance_dto.setPeriod(period);

                                    if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                        new OptimizedServerCallAsyncTask(mContext,
                                                AttendanceFilterDialog.this, KEYS.SWITCH_ATTENDANCE_SECTION).execute(periodId);
                                    } else {
                                        Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });
                        }

                    }else{
                        spnPeriod.setEnabled(false);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_SECTION:
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        sectionList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String sectionName = jsonObject1.optString("value");
                            int sectionId = jsonObject1.optInt("id");

                            Section_Dto section_dto = new Section_Dto(sectionName,sectionId);
                            sectionList.add(section_dto);
                        }

                        spinnerModified(sectionList.size(), spnSection);

                        if(sectionList != null && sectionList.size() > 0){

                            //Here set current period selected
                            for(int i = 0 ; i< sectionList.size() ; i++){

                                Section_Dto section_dto = sectionList.get(i);

                                if(section_dto.getSectionId() == sectionId1)
                                {
                                    sectionId2 = i;
                                    break;
                                }else{
                                    sectionId2 = sectionList.size() - 1;
                                }
                            }

                            //Here change selected item background in drop down view
                            sectionAdapter = new SectionAdapter(mContext,sectionList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == sectionId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };

                            spnSection.setAdapter(sectionAdapter);

                            spnSection.setSelection(sectionId2);
                            btnApply.setEnabled(true);
                        }

                        spnSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Section_Dto section_dto = (Section_Dto) spnSection.getSelectedItem();
                                sectionId = String.valueOf(section_dto.getSectionId());
                                String section = section_dto.getSectionName();

                                attendance_dto.setSectionId(sectionId);
                                attendance_dto.setSection(section);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    }else{
                        spnSection.setEnabled(false);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
        }
    }

    private void spinnerModified(int size, Spinner spinner) {

        if (size > 1) {

            spinner.setEnabled(true);
            spinner.setBackgroundResource(R.drawable.spinner_bg);
        } else {
            spinner.setEnabled(false);
            spinner.setBackgroundResource(R.drawable.spinner_bg_normal);
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

                startDate = tvStartDate.getText().toString();
                endDate = tvEndDate.getText().toString();

                attendance_dto.setStartDate(startDate);
                attendance_dto.setEndDate(endDate);

                Intent intent = new Intent();
                intent.putExtra(Consts.ATTENDANCE_RESULT,attendance_dto);
                setResult(RESULT_OK,intent);
                finish();
            }break;

            case R.id.btnReset:{

                programId1 = sharedPrefrenceManager.getProgramIDFromKey();
                batchId1 = sharedPrefrenceManager.getBatchIDFromKey();
                periodId1 = sharedPrefrenceManager.getPeriodIDFromKey();
                sectionId1 = sharedPrefrenceManager.getSectionIDFromKey();

                setupData();
            }break;

            case R.id.tvStartDate:{

                ProjectUtils.hideKeyboard(AttendanceFilterDialog.this);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                dateStart = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                startDate = ProjectUtils.convertTimestampToDate(dateStart,mContext);

                                tvStartDate.setText(startDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(dateEnd);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;

            case R.id.tvEndDate:{

                ProjectUtils.hideKeyboard(AttendanceFilterDialog.this);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear1 = year;
                                mMonth1 = monthOfYear;
                                mDay1 = dayOfMonth;

                                dateEnd = ProjectUtils.getLongDateFormat(year, monthOfYear, dayOfMonth);

                                endDate = ProjectUtils.convertTimestampToDate(dateEnd,mContext);

                                tvEndDate.setText(endDate);
                            }
                        }, mYear1, mMonth1, mDay1);
                datePickerDialog.getDatePicker().setMinDate(dateStart);
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }break;
        }
    }
}
