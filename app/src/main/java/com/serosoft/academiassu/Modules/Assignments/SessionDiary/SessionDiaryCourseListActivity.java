package com.serosoft.academiassu.Modules.Assignments.SessionDiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Adapters.SessionDiaryCourseListAdapter;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiaryClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on October 23 2019.
 */

public class SessionDiaryCourseListActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private AppCompatImageView ivCourse;
    private TextView tvCourseName;
    private SuperStateView superStateView;

    SessionDiaryClub_Dto sessionDiaryClub_dto;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SessionDiaryCourseListAdapter sessionDiaryCourseListAdapter;

    private final String TAG = SessionDiaryCourseListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_diary_course_list_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = SessionDiaryCourseListActivity.this;

        Initialize();

        Intent intent = getIntent();
        sessionDiaryClub_dto = (SessionDiaryClub_Dto) intent.getSerializableExtra(Consts.SESSION_DIARY_LIST);

        String name = ProjectUtils.getCorrectedString(sessionDiaryClub_dto.getCourseName());
        if(!name.equalsIgnoreCase(""))
        {
            tvCourseName.setText(name);
        }

        ArrayList<SessionDiary_Dto> sessionDiaryList = sessionDiaryClub_dto.getSessionDiaryList();
        if(sessionDiaryList != null && sessionDiaryList.size() > 0) {

            checkEmpty(false);
            sessionDiaryCourseListAdapter = new SessionDiaryCourseListAdapter(mContext,sessionDiaryList);
            recyclerView.setAdapter(sessionDiaryCourseListAdapter);
        }
        else
        {
            checkEmpty(true);
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

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }

    private void checkEmpty(boolean is_empty) {

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
        tvCourseName = findViewById(R.id.tvCourseName);
        ivCourse = findViewById(R.id.ivCourse);
        ivCourse.setImageResource(R.drawable.session_diary_white);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

        return true;
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
