package com.serosoft.academiassu.Modules.Attendance.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Attendance.Adapters.MultipleAttendanceTypeAdapter;
import com.serosoft.academiassu.Modules.Attendance.Dialogs.AttendanceCourseFilterDialog;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceType_Dto;
import com.serosoft.academiassu.Modules.Attendance.Models.Attendance_Dto;
import com.serosoft.academiassu.Modules.Attendance.MultipleAttendanceActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseWiseFragment extends Fragment implements AsyncTaskCompleteListener {

    BaseActivity baseActivity;
    SharedPrefrenceManager sharedPrefrenceManager;
    private Context mContext;
    private RecyclerView recyclerView;
    private SuperStateView superStateView;
    private LinearLayoutManager linearLayoutManager;
    private AppCompatImageView ivFilter;

    String periodId = "0", programId = "0", batchId = "0";
    String startDate = "", endDate = "",percentageTo = "100",percentageFrom = "0", courseCodeId = "", courseVariantId = "";
    String fStartDate = "",fEndDate = "";

    ArrayList<AttendanceType_Dto> attendanceTypeList;
    MultipleAttendanceTypeAdapter multipleAttendanceTypeAdapter;

    String title = "";
    int totalRecords = 0;
    int presentRecords = 0;
    int absentRecords = 0;
    int otherTillDate = 0;
    int courseId = 0;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = CourseWiseFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.attendance_list_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView start");

        mContext = getActivity();
        baseActivity = new BaseActivity();
        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);

        Initialize(v);

        populateContents();

        return v;
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(mContext))
        {
            programId = String.valueOf(sharedPrefrenceManager.getProgramIDFromKey());
            batchId = String.valueOf(sharedPrefrenceManager.getBatchIDFromKey());
            periodId = String.valueOf(sharedPrefrenceManager.getPeriodIDFromKey());

            baseActivity.showProgressDialog(mContext);

            new OptimizedServerCallAsyncTask(mContext, this,
                    KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL).execute(programId,batchId,periodId,courseCodeId,courseVariantId,percentageFrom,percentageTo,startDate,endDate);

        }
        else
        {
            Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callFor)
        {
            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL:

                baseActivity.hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult.toString());

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

                        multipleAttendanceTypeAdapter = new MultipleAttendanceTypeAdapter(mContext,attendanceTypeList,KEYS.COURSE_LEVEL);
                        recyclerView.setAdapter(multipleAttendanceTypeAdapter);
                        multipleAttendanceTypeAdapter.notifyDataSetChanged();

                        multipleAttendanceTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                AttendanceType_Dto list = attendanceTypeList.get(position);
                                String facultyName = ProjectUtils.getCorrectedString(list.getFacultyName());

                                Intent intent = new Intent(mContext, MultipleAttendanceActivity.class);

                                String courseName = ProjectUtils.getCorrectedString(list.getCourseName());
                                totalRecords = list.getTotalRecords();
                                presentRecords = list.getPresentRecords();
                                absentRecords = list.getAbsentRecords();
                                courseId = list.getCourseId();

                                otherTillDate = totalRecords - presentRecords - absentRecords;
                                title = courseName;

                                fStartDate = ProjectUtils.getDateFormat3(startDate);
                                fEndDate = ProjectUtils.getDateFormat3(endDate);

                                intent.putExtra("title", title);
                                intent.putExtra("details", facultyName);
                                intent.putExtra("totalTillDate", totalRecords);
                                intent.putExtra("presentTillDate", presentRecords);
                                intent.putExtra("absentTillDate", absentRecords);
                                intent.putExtra("otherTillDate", otherTillDate);
                                intent.putExtra("attendanceType", KEYS.COURSE_LEVEL);
                                intent.putExtra("courseId", courseId);
                                intent.putExtra("sectionId", "");
                                intent.putExtra("periodId", periodId);
                                intent.putExtra("fStartDate", fStartDate);
                                intent.putExtra("fEndDate", fEndDate);
                                startActivity(intent);
                            }
                        });

                    }else{
                        checkEmpty(true);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,"Session = "+ex.getMessage());
                    checkEmpty(true);
                    //showAlertDialog(AttendanceMainActivity.this, "OOPS!", "API Error at " + this.getLocalClassName());
                }break;
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

    private void Initialize(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        superStateView = v.findViewById(R.id.superStateView);
        ivFilter = v.findViewById(R.id.ivFilter);

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

        if(requestCode == FILTER_DIALOG && resultCode == getActivity().RESULT_OK && data != null)
        {
            Attendance_Dto attendance_dto = (Attendance_Dto) data.getSerializableExtra(Consts.ATTENDANCE_RESULT);

            String formatDate = ProjectUtils.getTimestampToDate(mContext);

            programId = ProjectUtils.getCorrectedString(attendance_dto.getProgramId());
            batchId = ProjectUtils.getCorrectedString(attendance_dto.getBatchId());
            periodId = ProjectUtils.getCorrectedString(attendance_dto.getPeriodId());
            courseCodeId = ProjectUtils.getCorrectedString(attendance_dto.getCourseCodeId());
            courseVariantId = ProjectUtils.getCorrectedString(attendance_dto.getCourseVariantId());
            startDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(attendance_dto.getStartDate()));
            endDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(attendance_dto.getEndDate()));
            percentageFrom = ProjectUtils.getCorrectedString(attendance_dto.getPercentageFrom());
            percentageTo = ProjectUtils.getCorrectedString(attendance_dto.getPercentageTo());

            if (ConnectionDetector.isConnectingToInternet(mContext))
            {
                baseActivity.showProgressDialog(mContext);

                new OptimizedServerCallAsyncTask(mContext, this,
                        KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL).execute(programId,batchId,periodId,courseCodeId,courseVariantId,percentageFrom,percentageTo,startDate,endDate);
            }
            else
            {
                Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
