package com.serosoft.academiassu.Modules.Exam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Widgets.CustomViewPager;
import com.serosoft.academiassu.Helpers.ViewPagerAdapter;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Modules.Exam.Fragments.ExamDocFragment;
import com.serosoft.academiassu.Modules.Exam.Fragments.ResultReportFragment;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.HashMap;

/**
 * Created by Abhishek on February 05 2020.
 */

public class ExamMainActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    private ResultReportFragment resultReportFragment = null;
    private ExamDocFragment examDocFragment = null;

    private final String TAG = ExamMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_main_activity);

        ProjectUtils.showLog(TAG,"onCreate");

        mContext = ExamMainActivity.this;

        Initialize();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.results), bundle);
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);

        toolbar.setTitle(translationManager.EXAMRESULT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorExamMark, toolbar, this); }

        resultReportFragment = new ResultReportFragment();
        examDocFragment = new ExamDocFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(resultReportFragment,translationManager.RESULT_REPORT_KEY);
        viewPagerAdapter.addFragment(examDocFragment,translationManager.EXAM_DOC_KEY);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:{
                onBackPressed();
            }break;

            case R.id.dashboardMenu:{
                onBackPressed();
            }break;

            case R.id.refresh:{
                getNotifications();
                resultReportFragment = (ResultReportFragment) viewPagerAdapter.getCurrentFragment(0);
                resultReportFragment.setValue();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
