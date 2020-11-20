package com.serosoft.academiassu.Modules.Exam.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.serosoft.academiassu.Modules.Exam.Adapters.BatchAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.PeriodAdapter;
import com.serosoft.academiassu.Modules.Exam.Adapters.ProgramAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.Batch_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ExamResult_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Period_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Program_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamFilterDialog extends Activity implements AsyncTaskCompleteListener {

    LayoutInflater inflater;
    View view;

    private LinearLayout llPeriod,llBatch,llProgram;
    private TextView tvProgram1,tvBatch1,tvPeriod1;
    private Spinner spnProgram,spnBatch,spnPeriod;
    private Button btnApply,btnReset;
    private TextView tvTitle;
    private AppCompatImageView ivClose;
    private Context mContext;
    SharedPrefrenceManager sharedPrefrenceManager;

    ExamResult_Dto examResult_dto = new ExamResult_Dto();

    TranslationManager translationManager;
    String academyType = "";
    String periodId = "",batchId = "",programId = "";
    int periodId1 = 0,batchId1 = 0,programId1 = 0;
    int periodId2 = 0,batchId2 = 0,programId2 = 0;

    ArrayList<Program_Dto> programList;
    ProgramAdapter programAdapter;

    ArrayList<Batch_Dto> batchList;
    BatchAdapter batchAdapter;

    ArrayList<Period_Dto> periodList;
    PeriodAdapter periodAdapter;

    private static final String TAG = ExamFilterDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = ExamFilterDialog.this;

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        setFinishOnTouchOutside(false);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.exam_filter_layout,null,false);

        setContentView(view);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        IntializeView(view);

        populateContent();

        Intent intent = getIntent();
        examResult_dto = (ExamResult_Dto) intent.getSerializableExtra(Consts.EXAM_RESULT);

        programId1 = Integer.parseInt(examResult_dto.getProgramId());
        periodId1 = Integer.parseInt(examResult_dto.getPeriodId());
        batchId1 = Integer.parseInt(examResult_dto.getBatchId());
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            String programIds = sharedPrefrenceManager.getProgramIdsFromKey();
            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_PROGRAMS).execute(programIds);
        } else {
            Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void IntializeView(View view) {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationManager = new TranslationManager(this);

        spnProgram = view.findViewById(R.id.spnProgram);
        spnBatch = view.findViewById(R.id.spnBatch);
        spnPeriod = view.findViewById(R.id.spnPeriod);
        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        llPeriod = view.findViewById(R.id.llPeriod);
        llBatch = view.findViewById(R.id.llBatch);
        llProgram = view.findViewById(R.id.llProgram);

        tvProgram1 = view.findViewById(R.id.tvProgram1);
        tvBatch1 = view.findViewById(R.id.tvBatch1);
        tvPeriod1 = view.findViewById(R.id.tvPeriod1);

        academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra(Consts.EXAM_RESULT,examResult_dto);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                populateContent();

                Intent intent = getIntent();
                examResult_dto = (ExamResult_Dto) intent.getSerializableExtra(Consts.EXAM_RESULT);

                programId1 = sharedPrefrenceManager.getProgramIDFromKey();
                batchId1 = sharedPrefrenceManager.getBatchIDFromKey();
                periodId1 = sharedPrefrenceManager.getPeriodIDFromKey();
            }
        });

        tvBatch1.setText(translationManager.BATCH_KEY);
        tvProgram1.setText(translationManager.PROGRAM_KEY);
        tvPeriod1.setText(translationManager.PERIOD_KEY);
        tvTitle.setText(translationManager.FILTER_BY_KEY);
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_PROGRAMS:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        programList = new ArrayList<>();
                        for(int i = 0 ; i< jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String programName = jsonObject1.optString("programName");
                            String academyLocationName = jsonObject1.optString("academyLocationName");
                            int academyLocationId = jsonObject1.optInt("academyLocationId");
                            int programId = jsonObject1.optInt("programId");

                            Program_Dto program_dto = new Program_Dto(programName,"",programId);
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

                                examResult_dto.setProgramId(programId);
                                examResult_dto.setProgramName(programName);

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    new OptimizedServerCallAsyncTask(mContext, ExamFilterDialog.this, KEYS.SWITCH_BATCH).execute("BATCH",programId);
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

            case KEYS.SWITCH_BATCH:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        batchList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            int batchId = jsonObject1.optInt("id");
                            String batchName = jsonObject1.optString("code");

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

                                examResult_dto.setBatchId(batchId);
                                examResult_dto.setBatch(batch);
                                examResult_dto.setPeriodId("0");

                                if (ConnectionDetector.isConnectingToInternet(mContext)) {

                                    new OptimizedServerCallAsyncTask(mContext, ExamFilterDialog.this, KEYS.SWITCH_PERIOD).execute("PERIOD",batchId);
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

            case KEYS.SWITCH_PERIOD:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0){

                        periodList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            int periodId = jsonObject1.optInt("id");
                            String periodName = jsonObject1.optString("code");

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

                            examResult_dto.setPeriodId(periodId);
                            examResult_dto.setPeriod(period);
                        }else{
                            spnPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    Period_Dto period_dto = (Period_Dto) spnPeriod.getSelectedItem();
                                    periodId = String.valueOf(period_dto.getPeriodId());
                                    String period = period_dto.getPeriodName();

                                    examResult_dto.setPeriodId(periodId);
                                    examResult_dto.setPeriod(period);
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
