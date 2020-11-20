package com.serosoft.academiassu.Modules.Assignments.Assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters.AssignmentCourseListAdapter;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Abhishek on October 31 2019.
 */

public class AssignmentCourseListActivity extends BaseActivity
{
    private Context mContext;
    public Toolbar toolbar;
    private AppCompatImageView ivCourse;
    private TextView tvCourseName;
    private SuperStateView superStateView;
    private AppCompatImageView ivFilter;

    AssignmentClub_Dto assignmentClub_dto;

    ArrayList<Assignment_Dto> assignmentList;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AssignmentCourseListAdapter assignmentCourseListAdapter;

    String assignmentType = "";
    int assignmentTypeId = 0;
    String assignmentName = "";
    String fromDate = "";
    String toDate = "";

    boolean i1 = false,i2 = false,i3 = false,i4 = false;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = AssignmentCourseListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_course_list_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = AssignmentCourseListActivity.this;

        Initialize();

        Intent intent = getIntent();
        assignmentClub_dto = (AssignmentClub_Dto) intent.getSerializableExtra(Consts.ASSIGNMENT_LIST);

        String name = ProjectUtils.getCorrectedString(assignmentClub_dto.getCourseName());
        if(!name.equalsIgnoreCase(""))
        {
            tvCourseName.setText(name);
        }

        assignmentList = assignmentClub_dto.getAssignmentList();
        if(assignmentList != null && assignmentList.size() > 0) {

            checkEmpty(false);
            assignmentCourseListAdapter = new AssignmentCourseListAdapter(mContext,assignmentList);
            recyclerView.setAdapter(assignmentCourseListAdapter);
        }
        else
        {
            checkEmpty(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_DIALOG && resultCode == RESULT_OK && data != null)
        {
            String formatDate = ProjectUtils.getTimestampToDate(mContext);

            assignmentTypeId = data.getIntExtra("AssignmentTypeId",0);
            assignmentType = data.getStringExtra("AssignmentType");
            assignmentName = data.getStringExtra("AssignmentName");
            fromDate = data.getStringExtra("FromDate");
            toDate = data.getStringExtra("ToDate");

            filter(assignmentType,assignmentName,fromDate,toDate);

            fromDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(fromDate));
            toDate = ProjectUtils.getDateFormat(formatDate,ProjectUtils.getCorrectedString(toDate));
        }
    }

    public void filter(String assignmentType,String assignmentName,String fromDate,String toDate)
    {
        ArrayList<Assignment_Dto> temp = new ArrayList<>();
        try
        {
            for(Assignment_Dto assignment_dto : assignmentList)
            {
                String publishDate = ProjectUtils.convertTimestampToDate(assignment_dto.getPublish_date_long(),mContext);
                String dueDate = ProjectUtils.convertTimestampToDate(assignment_dto.getSubmission_last_date_long(),mContext);

                String format = ProjectUtils.getTimestampToDate(mContext);

                Date d1 = stringToDate(publishDate,format);     //Here is publish date
                Date d2 = stringToDate(dueDate,format);         //Here is due date

                Date d11 = stringToDate(fromDate,format);       //Here is from date
                Date d22 = stringToDate(toDate,format);         //Here to date

                if(d11 != null){
                    i1 = d1.after(d11);
                    i2 = d1.equals(d11);
                } else{
                    i1 = true;
                    i2 = true;
                }

                if(d22 != null){
                    i3 = d1.equals(d22);
                    i4 = d1.before(d22);
                } else{
                    i3 = true;
                    i4 = true;
                }

                if((assignment_dto.getAssignment_type().contains(assignmentType))
                        && assignment_dto.getAssignmentName().contains(assignmentName)
                        && ((i1 || i2) && (i3 || i4)))
                {
                    temp.add(assignment_dto);
                }
            }

            if (temp.size()>0)
            {
                checkEmpty(false);
                assignmentCourseListAdapter.updateResults(temp);
            }
            else
            {
                checkEmpty(true);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private Date stringToDate(String aDate,String format) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
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
        tvCourseName = findViewById(R.id.tvCourseName);
        ivCourse = findViewById(R.id.ivCourse);
        ivCourse.setImageResource(R.drawable.assignment_icon_white);
        ivFilter = findViewById(R.id.ivFilter);

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

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fromDate.equalsIgnoreCase("") || !toDate.equalsIgnoreCase("")){
                    fromDate = ProjectUtils.getDateFormat2(fromDate);
                    toDate = ProjectUtils.getDateFormat2(toDate);
                }

                ProjectUtils.preventTwoClick(ivFilter);
                Intent intent = new Intent(mContext, AssignmentFilterDialog.class);
                intent.putExtra(Consts.ASSIGNMENT_LIST,assignmentList);
                intent.putExtra(Consts.ASSIGNMENT_TYPE_ID,assignmentTypeId);
                intent.putExtra(Consts.ASSIGNMENT_NAME,assignmentName);
                intent.putExtra(Consts.FROM_DATE,fromDate);
                intent.putExtra(Consts.TO_DATE,toDate);
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
