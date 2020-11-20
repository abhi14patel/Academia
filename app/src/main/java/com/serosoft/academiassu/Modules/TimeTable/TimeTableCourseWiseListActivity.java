package com.serosoft.academiassu.Modules.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.serosoft.academiassu.Networking.KEYS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTableCourseWiseListActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Toolbar toolbar;
    private ListView listView;
    private List<JSONObject> courseList;
    private TimeTableCourseWiseListAdapter timeTableCourseWiseListAdapter;
    private TextView textViewForHeader;
    private SuperStateView superStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_course_wise_list_2018);

        Initialize();

        populateContents();
    }

    private void populateContents() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_TIMETABLE_COURSES).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void setListView2() {

        if(courseList != null && courseList.size() > 0){
            listView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.GONE);

            timeTableCourseWiseListAdapter = new TimeTableCourseWiseListAdapter(this, courseList);
            listView.setAdapter(timeTableCourseWiseListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent=new Intent(TimeTableCourseWiseListActivity.this, TimeTableCourseWiseDetailListActivity.class);
                    JSONObject obj = courseList.get(i);
                    int courseId = obj.optInt("courseId", -1);
                    String courseName = obj.optString("courseName");
                    intent.putExtra("courseId", String.valueOf(courseId));
                    intent.putExtra("courseName", courseName);
                    startActivity(intent);
                }
            });
        }else{
            listView.setVisibility(View.GONE);
            superStateView.setVisibility(View.VISIBLE);
        }

    }

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        listView = findViewById(R.id.timetableViewTypeListView);
        textViewForHeader = findViewById(R.id.sub_info_tv_second);
        textViewForHeader.setText("Course List");

        toolbar.setTitle(translationManager.COURSE_WISE_KEY1.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorTimetable, toolbar, this); }
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
                populateContents();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            case KEYS.SWITCH_TIMETABLE_COURSES:
                hideProgressDialog();
                courseList = new ArrayList<>();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    if (responseObject.has("whatever")) {
                        JSONArray arr = responseObject.getJSONArray("whatever");

                        courseList = new ArrayList<>();
                        for (int i = 0 ; i < arr.length() ; ++i ) {
                            JSONObject obj = arr.getJSONObject(i);
                            courseList.add(obj);
                        }
                        setListView2();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                    setListView2();
                }
                break;
        }
    }

}