package com.serosoft.academiassu.Modules.Assignments.SessionDiary;

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
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Adapters.SessionDiaryCourseAdapter;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiaryClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDocuemnt_Dto;
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
 * Created by Abhishek on October 23 2019.
 */

public class SessionDiaryCourseActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SessionDiaryCourseAdapter sessionDiaryCourseAdapter;

    ArrayList<SessionDiary_Dto> sessionDiaryList;
    ArrayList<SessionDocuemnt_Dto> sessionDocumentList;
    ArrayList<SessionDiaryClub_Dto> sessionDiaryClubList = new ArrayList<>();

    HashMap<String,ArrayList<SessionDiary_Dto>> sessionDiaryTempMap = new HashMap<>();

    private final String TAG = SessionDiaryCourseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_diary_course_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = SessionDiaryCourseActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents()
    {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS).execute();
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
            case KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray data = responseObject.optJSONArray("rows");

                    if(data != null && data.length() > 0)
                    {
                        checkEmpty(false);

                        sessionDiaryList = new ArrayList<>();
                        for(int i = 0 ; i< data.length() ; i++)
                        {
                            JSONObject jsonObject = data.optJSONObject(i);

                            int id = jsonObject.optInt("id");
                            long date_long = jsonObject.optLong("date_long");
                            long fromSlot_long = jsonObject.optLong("fromSlot_long");
                            long toSlot_long = jsonObject.optLong("toSlot_long");
                            String date = jsonObject.optString("date");
                            String sessionNo = jsonObject.optString("sessionNo");
                            String topic = jsonObject.optString("topic");
                            String course = jsonObject.optString("course");
                            String facultyName = jsonObject.optString("facultyName");
                            String courseVariant = jsonObject.optString("courseVariant");

                            JSONArray docuementArray = jsonObject.optJSONArray("documents");

                            sessionDocumentList = new ArrayList<>();
                            for(int j = 0 ; j<docuementArray.length() ; j++)
                            {
                                JSONObject jsonObject1 = docuementArray.optJSONObject(j);

                                int id1 = jsonObject1.optInt("id");
                                String value = jsonObject1.optString("value");
                                String secondValue = jsonObject1.optString("secondValue");

                                sessionDocumentList.add(new SessionDocuemnt_Dto(id1,value,secondValue));
                            }

                            SessionDiary_Dto sessionDiary_dto = new SessionDiary_Dto(id,date_long,fromSlot_long,toSlot_long,date,sessionNo,topic,course,facultyName,courseVariant,sessionDocumentList);
                            sessionDiaryList.add(sessionDiary_dto);
                        }

                        Collections.sort(sessionDiaryList, SessionDiary_Dto.sortByName);
                        generateMergedData(sessionDiaryList);
                    }
                    else
                    {
                        checkEmpty(true);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                    checkEmpty(true);
                    ProjectUtils.showLog(TAG,""+e.getMessage());
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

    public void generateMergedData(ArrayList<SessionDiary_Dto> list)
    {
        for(int i=0; i<list.size() ; i++)
        {
            ArrayList<SessionDiary_Dto> tempList;

            if(i==0)
            {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
            }
            else if(list.get(i-1).getCourse().equalsIgnoreCase(list.get(i).getCourse()))
            {
                if(sessionDiaryTempMap.containsKey(list.get(i).getCourse()))
                {
                    tempList = sessionDiaryTempMap.get(list.get(i).getCourse());
                    tempList.add(list.get(i));
                    sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
                }
                else
                {
                    tempList = new ArrayList<>();
                    tempList.add(list.get(i));
                    sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
                }
            }
            else {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
            }
        }

        //Here get iterate hashmap data and put in arraylist
        Iterator myVeryOwnIterator = sessionDiaryTempMap.keySet().iterator();
        while(myVeryOwnIterator.hasNext())
        {
            String courseName =(String)myVeryOwnIterator.next();
            ArrayList<SessionDiary_Dto> mergedDataList = sessionDiaryTempMap.get(courseName);

            sessionDiaryClubList.add(new SessionDiaryClub_Dto(courseName,mergedDataList));
        }

        sessionDiaryCourseAdapter = new SessionDiaryCourseAdapter(mContext,sessionDiaryClubList);
        recyclerView.setAdapter(sessionDiaryCourseAdapter);
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

        toolbar.setTitle(translationManager.COURSE_SESSIONDIARY_KEY.toUpperCase());
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
