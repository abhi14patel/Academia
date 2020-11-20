package com.serosoft.academiassu.Modules.Assignments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentCourseActivity;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.SessionDiaryCourseActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on October 22 2019.
 */

public class AssignmentMainActivity extends BaseActivity implements AsyncTaskCompleteListener
{
    private Context mContext;
    public Toolbar toolbar;
    private LinearLayout llSessionDiary,llAssignment;
    private SuperStateView superStateView;
    private TextView tvSessionDiary,tvAssignment;

    ArrayList<Integer> list = new ArrayList<>();

    private final String TAG = AssignmentMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_main_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = AssignmentMainActivity.this;
        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);

        Initialize();

        permissionSetup();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.assignments), bundle);
    }

    private void permissionSetup()
    {
        //Here set module permission without api response
        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        llAssignment.setVisibility(View.GONE);
        llSessionDiary.setVisibility(View.GONE);
        superStateView.setVisibility(View.VISIBLE);

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                    llAssignment.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                    llSessionDiary.setVisibility(View.VISIBLE);
                    superStateView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void Initialize()
    {
        llSessionDiary = findViewById(R.id.llSessionDiary);
        llAssignment = findViewById(R.id.llAssignment);
        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);

        tvSessionDiary = findViewById(R.id.tvSessionDiary);
        tvAssignment = findViewById(R.id.tvAssignment);

        toolbar.setTitle(translationManager.ASSIGNMENT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAssignment, toolbar, this); }

        tvSessionDiary.setText(translationManager.COURSE_SESSIONDIARY_KEY);
        tvAssignment.setText(translationManager.HOMEWORKASSIGNMENT_KEY);

        llAssignment.setOnClickListener(this);
        llSessionDiary.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
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
                onBackPressed();
            }break;

            case R.id.refresh:
            {
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        int id = v.getId();

        switch (id)
        {
            case R.id.llAssignment:
            {
                Intent intent = new Intent(mContext, AssignmentCourseActivity.class);
                startActivity(intent);
            }break;

            case R.id.llSessionDiary:
            {
                Intent intent = new Intent(mContext, SessionDiaryCourseActivity.class);
                startActivity(intent);
            }break;
        }
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
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
