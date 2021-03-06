package com.serosoft.academiassu.Modules.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.JSONComparator;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TimeTableCourseWiseDetailListActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Toolbar toolbar;
    private ListView listView;
    private List<JSONObject> timeTableList;
    private SuperStateView superStateView;
    private TimeTableCourseWiseDetailListAdapter timeTableCourseWiseDetailListAdapter;
    private TextView textViewForHeader;
    private String courseName;
    private String courseId;

    private final String TAG = TimeTableCourseWiseDetailListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_course_wise_list_2018);

        courseName = getIntent().getStringExtra("courseName");
        courseId = getIntent().getStringExtra("courseId");

        Initialize();

        populateListView();
    }

    private void populateListView() {

        if (ConnectionDetector.isConnectingToInternet(this))
        {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_CALENDAR_COURSEWISE).execute(courseId);
        } else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void setListView() {
        listView.setVisibility(View.VISIBLE);
        timeTableCourseWiseDetailListAdapter = new TimeTableCourseWiseDetailListAdapter(this, timeTableList);
        listView.setAdapter(timeTableCourseWiseDetailListAdapter);
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        listView = findViewById(R.id.timetableViewTypeListView);
        superStateView = findViewById(R.id.superStateView);
        textViewForHeader = findViewById(R.id.sub_info_tv_second);
        textViewForHeader.setText(courseName);

        toolbar.setTitle(translationManager.COURSE_WISE_KEY1.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorTimetable, toolbar, this); }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            listView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callFor) {
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
            case KEYS.SWITCH_STUDENT_CALENDAR_COURSEWISE:
                hideProgressDialog();
                timeTableList = new ArrayList<>();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    if (responseObject.has("whatever")) {

                        JSONArray arr = responseObject.getJSONArray("whatever");

                        if(arr != null && arr.length() > 0){

                            checkEmpty(false);
                            timeTableList = new ArrayList<>();

                            for (int i = 0 ; i < arr.length() ; ++i ) {
                                JSONObject obj = arr.getJSONObject(i);
                                timeTableList.add(obj);
                            }
                            Collections.sort(timeTableList, new JSONComparator());
                            setListView();

                        }else
                        {
                            checkEmpty(true);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    checkEmpty(true);
                    ProjectUtils.showLog(TAG,""+e.getMessage());
                }
                break;

        }
    }
}