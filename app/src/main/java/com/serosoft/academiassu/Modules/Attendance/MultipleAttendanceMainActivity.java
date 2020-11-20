package com.serosoft.academiassu.Modules.Attendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.Widgets.CustomViewPager;
import com.serosoft.academiassu.Helpers.ViewPagerAdapter;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Modules.Attendance.Fragments.CourseWiseFragment;
import com.serosoft.academiassu.Modules.Attendance.Fragments.ProgramWiseFragment;
import com.serosoft.academiassu.Modules.Attendance.Fragments.SessionWiseFragment;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.HashMap;

public class MultipleAttendanceMainActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    private ProgramWiseFragment programWiseFragment = null;
    private CourseWiseFragment courseWiseFragment = null;
    private SessionWiseFragment sessionWiseFragment = null;

    private final String TAG = MultipleAttendanceMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_multiple_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = MultipleAttendanceMainActivity.this;

        Initialize();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.attendance), bundle);
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.ATTENDANCE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAttendance, toolbar, this); }

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);

        programWiseFragment = new ProgramWiseFragment();
        sessionWiseFragment = new SessionWiseFragment();
        courseWiseFragment = new CourseWiseFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(sharedPrefrenceManager.getAttendanceTypeCountFromKey() == 2){
            if(sharedPrefrenceManager.getIsCompleteDayStatusFromKey()){
                viewPagerAdapter.addFragment(programWiseFragment,translationManager.PROGRAM_WISE_KEY);
            }
            if(sharedPrefrenceManager.getIsMultipleSessionStatusFromKey()){
                viewPagerAdapter.addFragment(sessionWiseFragment,translationManager.SESSION_WISE_KEY);
            }
            if(sharedPrefrenceManager.getIsCourseLevelStatusFromKey()){
                viewPagerAdapter.addFragment(courseWiseFragment,translationManager.COURSE_WISE_KEY);
            }
        }else{
            viewPagerAdapter.addFragment(programWiseFragment,translationManager.PROGRAM_WISE_KEY);
            viewPagerAdapter.addFragment(courseWiseFragment,translationManager.COURSE_WISE_KEY);
            viewPager.setAdapter(viewPagerAdapter);
        }

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callForSwitch = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callForSwitch) {
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
