package com.serosoft.academiassu.Modules.Assignments.Assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters.AssignmentCourseAdapter;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Abhishek on October 31 2019.
 */

public class AssignmentCourseActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AssignmentCourseAdapter assignmentCourseAdapter;

    ArrayList<Assignment_Dto> assignmentList;
    ArrayList<AssignmentClub_Dto> assignmentClubList = new ArrayList<>();

    HashMap<String,ArrayList<Assignment_Dto>> assignmentTempMap = new HashMap<>();

    private final String TAG = AssignmentCourseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_course_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AssignmentCourseActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor) {
            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray data = responseObject.optJSONArray("rows");

                    if(data != null && data.length() > 0) {

                        checkEmpty(false);
                        assignmentList = new ArrayList<>();

                        for(int i = 0 ; i< data.length() ; i++) {

                            JSONObject jsonObject = data.optJSONObject(i);

                            int id = jsonObject.optInt("ID");
                            int doc_id = jsonObject.optInt("DOC_ID");
                            int serial_no = jsonObject.optInt("SERIAL_NO");
                            long submission_last_date_long = jsonObject.optLong("SUBMISSION_LAST_DATE_LONG");
                            long publish_date_long = jsonObject.optLong("PUBLISH_DATE_LONG");
                            String course_name = jsonObject.optString("COURSE_NAME");
                            String course_code = jsonObject.optString("COURSE_CODE");
                            String assignment_name = jsonObject.optString("ASSIGNMENT_NAME");
                            String assignment_type = jsonObject.optString("ASSIGNMENT_TYPE");
                            String assign_section_type = jsonObject.optString("ASSIGN_SECTION_TYPE");
                            String hwonline_submissions = jsonObject.optString("HWONLINESUBMISSIONS");
                            String course_variant = jsonObject.optString("COURSE_VARIANT");
                            String submission_last_date = jsonObject.optString("SUBMISSION_LAST_DATE");
                            String publish_date = jsonObject.optString("PUBLISH_DATE");
                            String hw_extended_date = jsonObject.optString("HW_EXTENDED_DATE");
                            String faculty_name = jsonObject.optString("FACULTY_NAME");

                            Assignment_Dto assignment_dto = new Assignment_Dto(id,doc_id,serial_no,submission_last_date_long,publish_date_long,course_name,course_code,assignment_name,assignment_type,assign_section_type,hwonline_submissions,course_variant,submission_last_date,publish_date,hw_extended_date,faculty_name);
                            assignmentList.add(assignment_dto);
                        }

                        //Here sort data for club same course name data
                        Collections.sort(assignmentList, Assignment_Dto.sortByName);
                        generateMergedData(assignmentList);
                    }
                    else
                    {
                        checkEmpty(true);
                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    checkEmpty(true);
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
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

    private void generateMergedData(ArrayList<Assignment_Dto> list)
    {
        for(int i=0; i<list.size() ; i++)
        {
            ArrayList<Assignment_Dto> tempList;

            if(i==0) {

                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                assignmentTempMap.put(list.get(i).getCourseName(),tempList);
            }
            else if(list.get(i-1).getCourseName().equalsIgnoreCase(list.get(i).getCourseName())) {

                if(assignmentTempMap.containsKey(list.get(i).getCourseName()))
                {
                    tempList = assignmentTempMap.get(list.get(i).getCourseName());
                    tempList.add(list.get(i));
                    assignmentTempMap.put(list.get(i).getCourseName(),tempList);
                }
                else
                {
                    tempList = new ArrayList<>();
                    tempList.add(list.get(i));
                    assignmentTempMap.put(list.get(i).getCourseName(),tempList);
                }
            }
            else {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                assignmentTempMap.put(list.get(i).getCourseName(),tempList);
            }
        }

        //Here get iterate hashmap data and put in arraylist
        Iterator myVeryOwnIterator = assignmentTempMap.keySet().iterator();
        while(myVeryOwnIterator.hasNext())
        {
            String courseName =(String)myVeryOwnIterator.next();
            ArrayList<Assignment_Dto> mergedDataList = assignmentTempMap.get(courseName);

            assignmentClubList.add(new AssignmentClub_Dto(courseName,mergedDataList));
        }

        assignmentCourseAdapter = new AssignmentCourseAdapter(mContext,assignmentClubList);
        recyclerView.setAdapter(assignmentCourseAdapter);
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
        recyclerView = findViewById(R.id.recyclerView);

        toolbar.setTitle(translationManager.HOMEWORKASSIGNMENT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAssignment, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                getNotifications();
                Intent intent1 = new Intent(mContext, AssignmentCourseActivity.class);
                startActivity(intent1);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
