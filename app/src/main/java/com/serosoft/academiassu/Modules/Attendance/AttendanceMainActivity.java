package com.serosoft.academiassu.Modules.Attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Attendance.Adapters.AttendanceTypeAdapter;
import com.serosoft.academiassu.Modules.Attendance.Dialogs.AttendanceCourseFilterDialog;
import com.serosoft.academiassu.Modules.Attendance.Dialogs.AttendanceFilterDialog;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceType_Dto;
import com.serosoft.academiassu.Modules.Attendance.Models.Attendance_Dto;
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

public class AttendanceMainActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private RecyclerView recyclerView;
    private SuperStateView superStateView;
    private LinearLayoutManager linearLayoutManager;
    private AppCompatImageView ivFilter;

    String periodId = "0", programId = "0", batchId = "0",sectionId = "0";
    String startDate = "", endDate = "",percentageTo = "100",percentageFrom = "0", courseCodeId = "", courseVariantId = "";
    String fStartDate = "",fEndDate = "";

    ArrayList<AttendanceType_Dto> attendanceTypeList;
    AttendanceTypeAdapter attendanceTypeAdapter;

    String attendanceType = "";
    String title = "";
    int totalRecords = 0;
    int presentRecords = 0;
    int absentRecords = 0;
    int otherTillDate = 0;
    int courseId = 0;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = AttendanceMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_main_list);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = AttendanceMainActivity.this;

        Initialize();

        attendanceType = sharedPrefrenceManager.getAttendanceTypeFromKey();

        populateContents();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.attendance), bundle);
    }

    private void populateContents()
    {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            programId = String.valueOf(sharedPrefrenceManager.getProgramIDFromKey());
            batchId = String.valueOf(sharedPrefrenceManager.getBatchIDFromKey());
            periodId = String.valueOf(sharedPrefrenceManager.getPeriodIDFromKey());
            sectionId = String.valueOf(sharedPrefrenceManager.getSectionIDFromKey());

            showProgressDialog(this);

            if (attendanceType.equalsIgnoreCase(KEYS.COMPLETE_DAY) )
            {
                new OptimizedServerCallAsyncTask(this,
                        this, KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY).execute(periodId,sectionId,startDate,endDate);
            }
            else if ( attendanceType.equalsIgnoreCase(KEYS.MULTIPLE_SESSION)) {
                new OptimizedServerCallAsyncTask(this,
                        this, KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION).execute(periodId,sectionId,startDate,endDate);
            }
            else
            {
                new OptimizedServerCallAsyncTask(this,
                        this, KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL).execute(programId,batchId,periodId,courseCodeId,courseVariantId,percentageFrom,percentageTo,startDate,endDate);
            }
        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
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

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        ivFilter = findViewById(R.id.ivFilter);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar.setTitle(translationManager.ATTENDANCE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAttendance, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!startDate.equalsIgnoreCase("") || !endDate.equalsIgnoreCase("")){
                    startDate = ProjectUtils.getDateFormat2(startDate);
                    endDate = ProjectUtils.getDateFormat2(endDate);
                }

                ProjectUtils.preventTwoClick(ivFilter);
                if(attendanceType.equalsIgnoreCase(KEYS.COMPLETE_DAY) || attendanceType.equalsIgnoreCase(KEYS.MULTIPLE_SESSION)){

                    Intent intent = new Intent(mContext, AttendanceFilterDialog.class);
                    intent.putExtra(Consts.PROGRAM_ID,programId);
                    intent.putExtra(Consts.BATCH_ID,batchId);
                    intent.putExtra(Consts.PERIOD_ID,periodId);
                    intent.putExtra(Consts.SECTION_ID,sectionId);
                    intent.putExtra(Consts.FROM_DATE,startDate);
                    intent.putExtra(Consts.TO_DATE,endDate);
                    startActivityForResult(intent,FILTER_DIALOG);
                }else{

                    Intent intent = new Intent(mContext, AttendanceCourseFilterDialog.class);
                    intent.putExtra(Consts.PROGRAM_ID,programId);
                    intent.putExtra(Consts.BATCH_ID,batchId);
                    intent.putExtra(Consts.PERIOD_ID,periodId);
                    intent.putExtra(Consts.COURSE_CODE_ID,courseCodeId);
                    intent.putExtra(Consts.COURSE_VARIANT_ID,courseVariantId);
                    intent.putExtra(Consts.FROM_DATE,startDate);
                    intent.putExtra(Consts.TO_DATE,endDate);
                    intent.putExtra(Consts.PERCENTAGE_TO,percentageTo);
                    intent.putExtra(Consts.PERCENTAGE_FROM,percentageFrom);
                    startActivityForResult(intent,FILTER_DIALOG);
                }
            }
        });

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_DIALOG && resultCode == RESULT_OK && data != null)
        {
            Attendance_Dto attendance_dto = (Attendance_Dto) data.getSerializableExtra(Consts.ATTENDANCE_RESULT);

            String formatDate = ProjectUtils.getTimestampToDate(mContext);

            programId = ProjectUtils.getCorrectedString(attendance_dto.getProgramId());
            batchId = ProjectUtils.getCorrectedString(attendance_dto.getBatchId());
            periodId = ProjectUtils.getCorrectedString(attendance_dto.getPeriodId());
            sectionId = ProjectUtils.getCorrectedString(attendance_dto.getSectionId());
            courseCodeId = ProjectUtils.getCorrectedString(attendance_dto.getCourseCodeId());
            courseVariantId = ProjectUtils.getCorrectedString(attendance_dto.getCourseVariantId());
            startDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(attendance_dto.getStartDate()));
            endDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(attendance_dto.getEndDate()));
            percentageFrom = ProjectUtils.getCorrectedString(attendance_dto.getPercentageFrom());
            percentageTo = ProjectUtils.getCorrectedString(attendance_dto.getPercentageTo());

            if (ConnectionDetector.isConnectingToInternet(this))
            {
                showProgressDialog(this);
                if (attendanceType.equalsIgnoreCase(KEYS.COMPLETE_DAY) )
                {
                    new OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY).execute(periodId,sectionId,startDate,endDate);
                }
                else if ( attendanceType.equalsIgnoreCase(KEYS.MULTIPLE_SESSION)) {

                    new OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION).execute(periodId,sectionId,startDate,endDate);
                }
                else
                {
                    new OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL).execute(programId,batchId,periodId,courseCodeId,courseVariantId,percentageFrom,percentageTo,startDate,endDate);
                }
            }
            else
            {
                Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                onBackPressed();
                return true;
            case R.id.refresh:
                populateContents();
                getNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, "OOPS!", "Unexpected Error at " + this.getLocalClassName());
            return;
        }
        switch (callFor)
        {
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_MULTIPLE_SESSION:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    attendanceTypeList = new ArrayList<>();

                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.optJSONArray("whatever");
                    } else {
                        arr = responseObject.optJSONArray("rows");
                    }
                    if (arr != null && arr.length() > 0)
                    {
                        checkEmpty(false);

                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);

                            String programName = obj.optString("programName");
                            String sectionCode = obj.optString("sectionCode");
                            String facultyName = obj.optString("facultyName");
                            String courseName = obj.optString("courseName");
                            int sectionId = obj.optInt("sectionId");
                            int totalRecords = obj.optInt("totalRecords");
                            int presentRecords = obj.optInt("presentRecords");
                            int absentRecords = obj.optInt("absentRecords");
                            int courseVariantId = obj.optInt("courseVariantId");
                            int courseId = obj.optInt("courseId");

                            AttendanceType_Dto attendanceType_dto = new AttendanceType_Dto(programName,sectionCode,facultyName,courseName,sectionId,totalRecords,presentRecords,absentRecords,courseVariantId,courseId);
                            attendanceTypeList.add(attendanceType_dto);
                        }

                        attendanceTypeAdapter = new AttendanceTypeAdapter(mContext,attendanceTypeList);
                        recyclerView.setAdapter(attendanceTypeAdapter);
                        attendanceTypeAdapter.notifyDataSetChanged();

                        attendanceTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                gotoCalender(position);
                            }
                        });

                    }else{
                        checkEmpty(true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,"Session = "+e.getMessage());
                    checkEmpty(true);
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "API Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COMPLETE_DAY:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    attendanceTypeList = new ArrayList<>();

                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.optJSONArray("whatever");
                    } else {
                        arr = responseObject.optJSONArray("rows");
                    }
                    if (arr != null && arr.length() > 0)
                    {
                        checkEmpty(false);

                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);

                            String programName = obj.optString("programName");
                            String sectionCode = obj.optString("sectionCode");
                            String facultyName = obj.optString("facultyName");
                            String courseName = obj.optString("courseName");
                            int sectionId = obj.optInt("sectionId");
                            int totalRecords = obj.optInt("totalRecords");
                            int presentRecords = obj.optInt("presentRecords");
                            int absentRecords = obj.optInt("absentRecords");
                            int courseVariantId = obj.optInt("courseVariantId");
                            int courseId = obj.optInt("courseId");

                            AttendanceType_Dto attendanceType_dto = new AttendanceType_Dto(programName,sectionCode,facultyName,courseName,sectionId,totalRecords,presentRecords,absentRecords,courseVariantId,courseId);
                            attendanceTypeList.add(attendanceType_dto);
                        }

                        attendanceTypeAdapter = new AttendanceTypeAdapter(mContext,attendanceTypeList);
                        recyclerView.setAdapter(attendanceTypeAdapter);
                        attendanceTypeAdapter.notifyDataSetChanged();

                        attendanceTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                gotoCalender(position);
                            }
                        });

                    }else{
                        checkEmpty(true);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,"Program = "+e.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    attendanceTypeList = new ArrayList<>();

                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.optJSONArray("whatever");
                    } else {
                        arr = responseObject.optJSONArray("rows");
                    }
                    if (arr != null && arr.length() > 0)
                    {
                        checkEmpty(false);

                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);

                            String programName = obj.optString("programName");
                            String sectionCode = obj.optString("sectionCode");
                            String facultyName = obj.optString("facultyName");
                            String courseName = obj.optString("courseName");
                            int sectionId = obj.optInt("sectionId");
                            int totalRecords = obj.optInt("totalRecords");
                            int presentRecords = obj.optInt("presentRecords");
                            int absentRecords = obj.optInt("absentRecords");
                            int courseVariantId = obj.optInt("courseVariantId");
                            int courseId = obj.optInt("courseId");

                            AttendanceType_Dto attendanceType_dto = new AttendanceType_Dto(programName,sectionCode,facultyName,courseName,sectionId,totalRecords,presentRecords,absentRecords,courseVariantId,courseId);
                            attendanceTypeList.add(attendanceType_dto);
                        }

                        attendanceTypeAdapter = new AttendanceTypeAdapter(mContext,attendanceTypeList);
                        recyclerView.setAdapter(attendanceTypeAdapter);
                        attendanceTypeAdapter.notifyDataSetChanged();

                        attendanceTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                gotoCalender(position);
                            }
                        });

                    }else{
                        checkEmpty(true);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,"Course = "+e.getMessage());
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
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

    private void gotoCalender(int position) {

        AttendanceType_Dto list = attendanceTypeList.get(position);
        String facultyName = ProjectUtils.getCorrectedString(list.getFacultyName());

        Intent intent = new Intent(mContext, MultipleAttendanceActivity.class);

        if (attendanceType.equalsIgnoreCase(KEYS.COMPLETE_DAY) || attendanceType.equalsIgnoreCase(KEYS.MULTIPLE_SESSION)) {

            String programName = ProjectUtils.getCorrectedString(list.getProgramName());
            String sectionCode = ProjectUtils.getCorrectedString(list.getSectionCode());
            totalRecords = list.getTotalRecords();
            presentRecords = list.getPresentRecords();
            absentRecords = list.getAbsentRecords();

            otherTillDate = totalRecords - presentRecords - absentRecords;
            title = programName + " - " + sectionCode;
        } else {

            String courseName = ProjectUtils.getCorrectedString(list.getCourseName());
            totalRecords = list.getTotalRecords();
            presentRecords = list.getPresentRecords();
            absentRecords = list.getAbsentRecords();
            courseId = list.getCourseId();

            otherTillDate = totalRecords - presentRecords - absentRecords;
            title = courseName;
        }

        fStartDate = ProjectUtils.getDateFormat3(startDate);
        fEndDate = ProjectUtils.getDateFormat3(endDate);

        intent.putExtra("title", title);
        intent.putExtra("details", facultyName);
        intent.putExtra("totalTillDate", totalRecords);
        intent.putExtra("presentTillDate", presentRecords);
        intent.putExtra("absentTillDate", absentRecords);
        intent.putExtra("otherTillDate", otherTillDate);
        intent.putExtra("attendanceType", attendanceType);
        intent.putExtra("courseId", courseId);
        intent.putExtra("sectionId", sectionId);
        intent.putExtra("periodId", periodId);
        intent.putExtra("fStartDate", fStartDate);
        intent.putExtra("fEndDate", fEndDate);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
