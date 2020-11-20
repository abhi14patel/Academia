package com.serosoft.academiassu.Modules.MyCourses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Exam.Models.Batch_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ExamResult_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Period_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.Program_Dto;
import com.serosoft.academiassu.Modules.MyCourses.Adapters.MyCoursesListAdapter;
import com.serosoft.academiassu.Modules.MyCourses.Models.MyCourse_Dto;
import com.serosoft.academiassu.Modules.MyCourses.Models.ProgramBatchPeriod_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on March 11 2020.
 */

public class MyCoursesListActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;
    private AppCompatImageView ivFilter;
    private TextView tvProgramName,tvBatch,tvPeriod;
    private TextView tvBatch1,tvPeriod1;
    private LinearLayout llPeriod;

    String academyType = "";
    private String isSchool = "";

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ExamResult_Dto examResult_dto;

    MyCoursesListAdapter myCoursesListAdapter;
    ArrayList<MyCourse_Dto> myCourseList;

    String periodId = "0",batchId = "0",programId = "0";
    int periodId1 = 0,batchId1 = 0,programId1 = 0;

    ProgramBatchPeriod_Dto programBatchPeriod_dto = new ProgramBatchPeriod_Dto();

    ArrayList<Program_Dto> programList;
    ArrayList<Batch_Dto> batchList;
    ArrayList<Period_Dto> periodList;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = MyCoursesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycourses_list);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = MyCoursesListActivity.this;

        Initialize();

        populateContent();

        examResult_dto = sharedPrefrenceManager.getExamResult_Dto(Consts.EXAM_RESULT);

        programId1 = Integer.parseInt(examResult_dto.getProgramId());
        periodId1 = Integer.parseInt(examResult_dto.getPeriodId());
        batchId1 = Integer.parseInt(examResult_dto.getBatchId());
    }

    private void populateContent() {
        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_MYCOURSE_PROGRAM).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        ivFilter = findViewById(R.id.ivFilter);

        tvProgramName = findViewById(R.id.tvProgramName);
        tvBatch = findViewById(R.id.tvBatch);
        tvPeriod = findViewById(R.id.tvPeriod);
        tvBatch1 = findViewById(R.id.tvBatch1);
        tvPeriod1 = findViewById(R.id.tvPeriod1);
        llPeriod = findViewById(R.id.llPeriod);

        tvBatch1.setText(translationManager.BATCH_KEY);
        tvPeriod1.setText(translationManager.PERIOD_KEY);

        toolbar.setTitle(translationManager.MYCOURSE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyCourse, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
            isSchool = "true";
        }else{
            llPeriod.setVisibility(View.VISIBLE);
            isSchool = "false";
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && ivFilter.getVisibility() == View.VISIBLE) {
                    ivFilter.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && ivFilter.getVisibility() != View.VISIBLE) {
                    ivFilter.setVisibility(View.VISIBLE);
                }
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.preventTwoClick(ivFilter);
                Intent intent = new Intent(mContext, CourseFilterDialog.class);
                intent.putExtra(Consts.MY_COURSES,programBatchPeriod_dto);
                startActivityForResult(intent,FILTER_DIALOG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_DIALOG && resultCode == RESULT_OK && data != null)
        {
            programBatchPeriod_dto = (ProgramBatchPeriod_Dto) data.getSerializableExtra(Consts.MY_COURSES);

            setData(programBatchPeriod_dto);
        }
    }

    private void setData(ProgramBatchPeriod_Dto programBatchPeriod_dto) {

        String programName = ProjectUtils.getCorrectedString(programBatchPeriod_dto.getProgramName());
        if(!programName.equalsIgnoreCase("")){
            tvProgramName.setText(programName);
        }

        String batch = ProjectUtils.getCorrectedString(programBatchPeriod_dto.getBatch());
        if(!batch.equalsIgnoreCase("")){
            tvBatch.setText(batch);
        }

        String academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);

            String period = ProjectUtils.getCorrectedString(programBatchPeriod_dto.getPeriod());
            if(!period.equalsIgnoreCase("")){
                tvPeriod.setText(period);
            }
        }

        String programId = programBatchPeriod_dto.getProgramId();
        String batchId = programBatchPeriod_dto.getBatchId();
        String periodId = programBatchPeriod_dto.getPeriodId();

        showProgressDialog(this);
        new OptimizedServerCallAsyncTask(this,
                this, KEYS.SWITCH_MY_COURSES_LIST).execute(programId,batchId,periodId,isSchool);
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
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_MYCOURSE_PROGRAM:
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {
                        programList = new ArrayList<Program_Dto>();

                        for(int i = 0 ; i< jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String programName = jsonObject1.optString("value");
                            int programId = jsonObject1.optInt("id");

                            Program_Dto program_dto = new Program_Dto(programName,"",programId);
                            programList.add(program_dto);
                        }

                        if(programList != null && programList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< programList.size() ; i++){
                                Program_Dto program_dto = programList.get(i);

                                if(program_dto.getProgramId() == programId1)
                                {
                                    tvProgramName.setText(program_dto.getProgramName());
                                    programId = String.valueOf(program_dto.getProgramId());

                                    programBatchPeriod_dto.setProgramName(tvProgramName.getText().toString().trim());
                                    programBatchPeriod_dto.setProgramId(programId);

                                    new OptimizedServerCallAsyncTask(this,
                                            this, KEYS.SWITCH_MYCOURSE_BATCH).execute(programId);
                                }
                            }
                        }
                    }else{
                        hideProgressDialog();
                        checkEmpty(true);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_MYCOURSE_BATCH:

                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {
                        batchList = new ArrayList<Batch_Dto>();

                        for(int i = 0 ; i< jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String batchName = jsonObject1.optString("value");
                            int batchId = jsonObject1.optInt("id");

                            Batch_Dto batch_dto = new Batch_Dto(batchName,batchId);
                            batchList.add(batch_dto);
                        }

                        if(batchList != null && batchList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< batchList.size() ; i++){
                                Batch_Dto batch_dto = batchList.get(i);

                                if(batch_dto.getBatchId() == batchId1)
                                {
                                    tvBatch.setText(batch_dto.getBatchName());
                                    batchId = String.valueOf(batch_dto.getBatchId());

                                    programBatchPeriod_dto.setBatch(tvBatch.getText().toString().trim());
                                    programBatchPeriod_dto.setBatchId(batchId);

                                    if(academyType.equalsIgnoreCase(Consts.SCHOOL)){

                                        programBatchPeriod_dto.setPeriodId("0");

                                        new OptimizedServerCallAsyncTask(this,
                                                this, KEYS.SWITCH_MY_COURSES_LIST).execute(programId,batchId,periodId,isSchool);

                                    }else{
                                        new OptimizedServerCallAsyncTask(this,
                                                this, KEYS.SWITCH_MYCOURSE_PERIOD).execute(batchId);
                                    }
                                }
                            }
                        }
                    }else{
                        hideProgressDialog();
                        checkEmpty(true);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_MYCOURSE_PERIOD:
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {
                        periodList = new ArrayList<Period_Dto>();

                        for(int i = 0 ; i< jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String periodName = jsonObject1.optString("value");
                            int periodId = jsonObject1.optInt("id");

                            Period_Dto period_dto = new Period_Dto(periodName,periodId);
                            periodList.add(period_dto);
                        }

                        if(periodList != null && periodList.size() > 0){

                            //Here set current program selected
                            for(int i = 0 ; i< periodList.size() ; i++){
                                Period_Dto period_dto = periodList.get(i);

                                if(period_dto.getPeriodId() == periodId1)
                                {
                                    tvPeriod.setText(period_dto.getPeriodName());
                                    periodId = String.valueOf(period_dto.getPeriodId());

                                    programBatchPeriod_dto.setPeriod(tvPeriod.getText().toString().trim());
                                    programBatchPeriod_dto.setPeriodId(periodId);

                                    new OptimizedServerCallAsyncTask(this,
                                            this, KEYS.SWITCH_MY_COURSES_LIST).execute(programId,batchId,periodId,isSchool);
                                }
                            }
                        }
                    }else{
                        hideProgressDialog();
                        checkEmpty(true);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_MY_COURSES_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("rows");

                    if(jsonArray != null && jsonArray.length() > 0){

                        checkEmpty(false);
                        myCourseList = new ArrayList<>();

                        for(int i = 0 ; i< jsonArray.length() ; i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String courseName = jsonObject1.optString("courseName");
                            String courseCode = jsonObject1.optString("courseCode");
                            String courseVariantCode = jsonObject1.optString("courseVariantCode");
                            String faculty = jsonObject1.optString("faculty");
                            int course_id = jsonObject1.optInt("courseId");
                            boolean withDrawl = jsonObject1.optBoolean("withDrawl");

                            if(!withDrawl){
                                MyCourse_Dto list = new MyCourse_Dto(courseName,courseCode,faculty,courseVariantCode,course_id);
                                myCourseList.add(list);
                            }
                        }

                        if(myCourseList != null && myCourseList.size() > 0){
                            myCoursesListAdapter = new MyCoursesListAdapter(mContext,myCourseList);
                            recyclerView.setAdapter(myCoursesListAdapter);
                            myCoursesListAdapter.notifyDataSetChanged();
                        }else{
                            checkEmpty(true);
                        }

                    }else
                    {
                        checkEmpty(true);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    checkEmpty(true);
                }
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
                populateContent();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
