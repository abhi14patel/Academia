package com.serosoft.academiassu.Modules.TimeTable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;

import java.util.HashMap;

public class TimeTableViewTypeActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Toolbar toolbar;
    private LinearLayout llCourseWise,llDayWise;
    private TextView tvCourseWise,tvDayWise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_view_type_activity);

        Initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.TIMETABLE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorTimetable, toolbar, this); }

        llCourseWise = findViewById(R.id.llCourseWise);
        llDayWise = findViewById(R.id.llDayWise);
        tvCourseWise = findViewById(R.id.tvCourseWise);
        tvDayWise = findViewById(R.id.tvDayWise);

        llCourseWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TimeTableViewTypeActivity.this, TimeTableCourseWiseListActivity.class);
                startActivity(intent);
            }
        });

        llDayWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(TimeTableViewTypeActivity.this, TimeTableDayWiseListActivityK.class);
                startActivity(intent);
            }
        });

        tvCourseWise.setText(translationManager.COURSE_WISE_KEY1);
        tvDayWise.setText(translationManager.DAY_WISE_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.dashboardMenu:
                finish();
                return true;
            case R.id.refresh:
                getNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, "OOPS!", "Unexpected Error at " + this.getLocalClassName());
            return;
        }
        switch (callFor) {
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }
}
