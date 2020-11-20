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
import android.view.MotionEvent;
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

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Attendance.Adapters.AttendanceCourseAdapter;
import com.serosoft.academiassu.Modules.Attendance.Adapters.AttendanceCourseVariantAdapter;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceCourseVariant_Dto;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceCourse_Dto;
import com.serosoft.academiassu.Modules.Attendance.Models.Attendance_Dto;
import com.serosoft.academiassu.Modules.Exam.Adapters.BatchAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.PeriodAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.ProgramAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.Batch_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Period_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Program_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Widgets.CustomCompliteTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AttendanceCourseFilterDialog extends Activity implements AsyncTaskCompleteListener, View.OnClickListener {

    LayoutInflater inflater;
    View view;

    BaseActivity baseActivity;
    SharedPrefrenceManager sharedPrefrenceManager;

    private LinearLayout llPeriod,llBatch,llProgram;
    private TextView tvProgram1,tvBatch1,tvPeriod1,tvCourseCodeName1,tvCourseVariant1,tvAttendancePercentage1;
    private CustomCompliteTextView atvCourseCodeName;
    private Spinner spnProgram,spnBatch,spnPeriod,spnCourseVariant;
    private TextView tvStartDate,tvEndDate;
    private Button btnApply,btnReset;
    private TextView tvStartDate1,tvEndDate1;
    private RangeSeekBar rangeSeekBar;
    private TextView tvPercentageFrom,tvPercentageTo;

    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private Context mContext;

    String academyType = "";
    TranslationManager translationManager;

    String cid = "", cvid = "";
    String startDate = "", endDate = "";
    float percentageFrom = 0f,percentageTo = 100f;
    String periodId = "",batchId = "",programId = "",courseId = "",courseVariantId = "";
    int periodId1 = 0,batchId1 = 0,programId1 = 0,courseId1 = 0,courseVariantId1 = 0;
    int periodId2 = 0,batchId2 = 0,programId2 = 0,courseVariantId2 = 0;

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

    ArrayList<AttendanceCourse_Dto> attendanceCourseList;
    AttendanceCourseAdapter attendanceCourseAdapter;

    ArrayList<AttendanceCourseVariant_Dto> attendanceCourseVariantList;
    AttendanceCourseVariantAdapter attendanceCourseVariantAdapter;

    private static final String TAG = AttendanceCourseFilterDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = AttendanceCourseFilterDialog.this;

        setFinishOnTouchOutside(false);
        baseActivity = new BaseActivity();
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.attendance_course_filter_layout,null,false);
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
        cid = intent.getStringExtra(Consts.COURSE_CODE_ID);
        cvid = intent.getStringExtra(Consts.COURSE_VARIANT_ID);
        startDate = intent.getStringExtra(Consts.FROM_DATE);
        endDate = intent.getStringExtra(Consts.TO_DATE);
        percentageFrom = Float.parseFloat(intent.getStringExtra(Consts.PERCENTAGE_FROM));
        percentageTo= Float.parseFloat(intent.getStringExtra(Consts.PERCENTAGE_TO));

        rangeSeekBar.setProgress(percentageFrom,percentageTo);

        if(cid.equalsIgnoreCase("")){
            courseId1 = 0;
        }else{
            courseId1 = Integer.parseInt(cid);
        }

        if(cvid.equalsIgnoreCase("")){
            courseVariantId1 = 0;
        }else{
            courseVariantId1 = Integer.parseInt(cvid);
        }

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
        atvCourseCodeName.setText("");
        atvCourseCodeName.setSearchEnabled(false);

        atvCourseCodeName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                atvCourseCodeName.setThreshold(0);
                atvCourseCodeName.setSearchEnabled(true);
                atvCourseCodeName.showDropDown();
                atvCourseCodeName.requestFocus();
                return false;
            }
        });
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
        atvCourseCodeName = view.findViewById(R.id.atvCourseCodeName);
        spnCourseVariant = view.findViewById(R.id.spnCourseVariant);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        tvPercentageFrom = view.findViewById(R.id.tvPercentageFrom);
        tvPercentageTo = view.findViewById(R.id.tvPercentageTo);

        tvProgram1 = view.findViewById(R.id.tvProgram1);
        tvBatch1 = view.findViewById(R.id.tvBatch1);
        tvPeriod1 = view.findViewById(R.id.tvPeriod1);
        tvCourseCodeName1 = view.findViewById(R.id.tvCourseCodeName1);
        tvCourseVariant1 = view.findViewById(R.id.tvCourseVariant1);
        tvAttendancePercentage1 = view.findViewById(R.id.tvAttendancePercentage1);

        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        tvStartDate1 = view.findViewById(R.id.tvStartDate1);
        tvEndDate1 = view.findViewById(R.id.tvEndDate1);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                int left = (int) leftValue;
                int right = (int) rightValue;
                tvPercentageFrom.setText(left+" %");
                tvPercentageTo.setText(right+" %");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);
        }

        tvBatch1.setText(translationManager.BATCH_KEY);
        tvProgram1.setText(translationManager.PROGRAM_KEY);
        tvPeriod1.setText(translationManager.PERIOD_KEY);
        tvCourseCodeName1.setText(translationManager.ATTENDANCE_COURSE_CODE_KEY);
        tvCourseVariant1.setText(translationManager.COURSE_VARIANT_KEY);
        tvStartDate1.setText(translationManager.ATTENDANCE_START_DATE_KEY);
        tvEndDate1.setText(translationManager.ATTENDANCE_END_DATE_KEY);
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
                                            AttendanceCourseFilterDialog.this, KEYS.SWITCH_ATTENDANCE_BATCH).execute(programId);
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
                    ProjectUtils.showLog(TAG,"Program = "+ex.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
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
                                attendance_dto.setCourseCodeId("");
                                attendance_dto.setCourseVariantId("");

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    new OptimizedServerCallAsyncTask(mContext,
                                            AttendanceCourseFilterDialog.this, KEYS.SWITCH_ATTENDANCE_PERIOD).execute(batchId);
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
                    ProjectUtils.showLog(TAG,"Batch = "+ex.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
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
                                        AttendanceCourseFilterDialog.this, KEYS.SWITCH_COURSE_LIST).execute(programId,batchId,periodId);
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
                                                AttendanceCourseFilterDialog.this, KEYS.SWITCH_COURSE_LIST).execute(programId,batchId,periodId);
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
                    ProjectUtils.showLog(TAG,"Period = "+ex.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_COURSE_LIST:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    attendanceCourseList = new ArrayList<>();

                    if(jsonArray != null && jsonArray.length() > 0){

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String courseVariantCode = jsonObject1.optString("courseVariantCode");
                            String courseCodeName = jsonObject1.optString("courseCodeName");
                            int courseId = jsonObject1.optInt("courseId");

                            AttendanceCourse_Dto attendanceCourse_dto = new AttendanceCourse_Dto(courseVariantCode,courseCodeName,courseId);
                            attendanceCourseList.add(attendanceCourse_dto);
                        }

                        //Here set pre selected value
                        if(attendanceCourseList != null && attendanceCourseList.size() > 0){

                            //Here set current period selected
                            for(int i = 0 ; i< attendanceCourseList.size() ; i++){

                                AttendanceCourse_Dto attendanceCourse_dto = attendanceCourseList.get(i);

                                if(attendanceCourse_dto.getCourseId() == courseId1)
                                {
                                    atvCourseCodeName.setText(attendanceCourse_dto.getCourseCodeName());
                                }
                            }
                        }

                        attendanceCourseAdapter = new AttendanceCourseAdapter(mContext,R.layout.document_type_item,attendanceCourseList);
                        atvCourseCodeName.setAdapter(attendanceCourseAdapter);
                        attendanceCourseAdapter.notifyDataSetChanged();

                        atvCourseCodeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                ProjectUtils.hideKeyboard2(mContext,atvCourseCodeName);
                                AttendanceCourse_Dto attendanceCourse_dto = (AttendanceCourse_Dto) attendanceCourseList.get(position);
                                courseId = String.valueOf(attendanceCourse_dto.getCourseId());
                                String courseCode = attendanceCourse_dto.getCourseCodeName();

                                attendance_dto.setCourseCode(courseCode);
                                attendance_dto.setCourseCodeId(courseId);

                                sharedPrefrenceManager.setAttendanceCourse_Dto(Consts.ATTENDANCE_COURSE_CODE,attendanceCourse_dto);

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    baseActivity.showProgressDialog(mContext);

                                    new OptimizedServerCallAsyncTask(mContext,
                                            AttendanceCourseFilterDialog.this, KEYS.SWITCH_COURSE_VARIANT).execute(programId,batchId,periodId,courseId);
                                } else {
                                    Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        //Here set prefilled
                        if(courseId1 != 0){

                            AttendanceCourse_Dto attendanceCourse_dto =  sharedPrefrenceManager.getAttendanceCourse_Dto(Consts.ATTENDANCE_COURSE_CODE);

                            courseId = String.valueOf(attendanceCourse_dto.getCourseId());
                            String courseCode = attendanceCourse_dto.getCourseCodeName();

                            attendance_dto.setCourseCode(courseCode);
                            attendance_dto.setCourseCodeId(courseId);

                            if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                new OptimizedServerCallAsyncTask(mContext,
                                        AttendanceCourseFilterDialog.this, KEYS.SWITCH_COURSE_VARIANT).execute(programId,batchId,periodId,courseId);
                            } else {
                                Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,"CourseList = "+ex.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_COURSE_VARIANT:

                baseActivity.hideProgressDialog();

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    attendanceCourseVariantList = new ArrayList<>();

                    if(jsonArray != null && jsonArray.length() > 0){

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String courseVariantCode = jsonObject1.optString("courseVariantCode");
                            int id = jsonObject1.optInt("id");

                            AttendanceCourseVariant_Dto attendanceCourseVariant_dto = new AttendanceCourseVariant_Dto(courseVariantCode,id);
                            attendanceCourseVariantList.add(attendanceCourseVariant_dto);
                        }

                        spinnerModified(attendanceCourseVariantList.size(), spnCourseVariant);

                        if(attendanceCourseVariantList != null && attendanceCourseVariantList.size() > 0){

                            //Here set current courevariant selected
                            for(int i = 0 ; i< attendanceCourseVariantList.size() ; i++){

                                AttendanceCourseVariant_Dto attendanceCourseVariant_dto = attendanceCourseVariantList.get(i);

                                if(attendanceCourseVariant_dto.getId() == courseVariantId1)
                                {
                                    courseVariantId2 = i;
                                }
                            }

                            attendanceCourseVariantAdapter = new AttendanceCourseVariantAdapter(mContext,attendanceCourseVariantList){
                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    if (position == courseVariantId2) {
                                        v.setBackgroundColor(getResources().getColor(R.color.colorGreylight));
                                    }
                                    else {
                                        // for other views
                                        v.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }
                                    return v;
                                }
                            };
                            spnCourseVariant.setAdapter(attendanceCourseVariantAdapter);

                            spnCourseVariant.setSelection(courseVariantId2);
                        }

                        spnCourseVariant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                AttendanceCourseVariant_Dto attendanceCourseVariant_dto = (AttendanceCourseVariant_Dto) spnCourseVariant.getSelectedItem();
                                courseVariantId = String.valueOf(attendanceCourseVariant_dto.getId());
                                String courseVariantCode = attendanceCourseVariant_dto.getCourseVariantCode();

                                attendance_dto.setCourseVariant(courseVariantCode);
                                attendance_dto.setCourseVariantId(courseVariantId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }
                    else{
                        spnCourseVariant.setEnabled(false);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,"CourseVariant = "+ex.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
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

                String perTo = tvPercentageTo.getText().toString().trim();
                String perFrom = tvPercentageFrom.getText().toString().trim();

                String[] separated1 = perTo.split(" ");
                String[] separated2 = perFrom.split(" ");
                String percentageTo = separated1[0];
                String percentageFrom = separated2[0];

                startDate = tvStartDate.getText().toString();
                endDate = tvEndDate.getText().toString();

                attendance_dto.setStartDate(startDate);
                attendance_dto.setEndDate(endDate);
                attendance_dto.setPercentageTo(percentageTo);
                attendance_dto.setPercentageFrom(percentageFrom);

                Intent intent = new Intent();
                intent.putExtra(Consts.ATTENDANCE_RESULT,attendance_dto);
                setResult(RESULT_OK,intent);
                finish();
            }break;

            case R.id.btnReset:{

                programId1 = sharedPrefrenceManager.getProgramIDFromKey();
                batchId1 = sharedPrefrenceManager.getBatchIDFromKey();
                periodId1 = sharedPrefrenceManager.getPeriodIDFromKey();
                courseId1 = 0;
                if(attendanceCourseVariantList != null && attendanceCourseVariantList.size() > 0){
                    attendanceCourseVariantList.clear();
                    attendanceCourseVariantAdapter.notifyDataSetChanged();
                    sharedPrefrenceManager.clearPreferences(Consts.ATTENDANCE_COURSE_CODE);
                }
                rangeSeekBar.setProgress(0f,100f);

                setupData();
            }break;

            case R.id.tvStartDate:{

                ProjectUtils.hideKeyboard(AttendanceCourseFilterDialog.this);

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

                ProjectUtils.hideKeyboard(AttendanceCourseFilterDialog.this);

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
